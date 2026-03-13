package com.example.phishing_detector

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt

class PhishingDetector(private val context: Context) {
    private var interpreter: Interpreter? = null
    private var vocabulary: Map<String, Int> = emptyMap()
    private var idfValues: Map<String, Double> = emptyMap()
    private var vocabSize = 0

    fun loadModel() {
        // Load TFLite model
        val modelFile = loadModelFile("phishing_model.tflite")
        interpreter = Interpreter(modelFile)

        // Load vocabulary
        val vocabJson = context.assets.open("vocabulary.json").bufferedReader().use { it.readText() }
        vocabulary = Gson().fromJson(vocabJson, object : TypeToken<Map<String, Int>>() {}.type)
        vocabSize = vocabulary.size

        // Load IDF values
        val idfJson = context.assets.open("idf_values.json").bufferedReader().use { it.readText() }
        idfValues = Gson().fromJson(idfJson, object : TypeToken<Map<String, Double>>() {}.type)
    }

    private fun loadModelFile(filename: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(emailText: String): Pair<Boolean, Float> {
        // 1. Create TF-IDF vector
        val tfidfVector = createTfidfVector(emailText)

        // 2. Prepare input tensor
        val inputArray = Array(1) { FloatArray(vocabSize) }
        for (i in 0 until vocabSize) {
            inputArray[0][i] = tfidfVector[i].toFloat()
        }

        // 3. Run inference - model outputs [1, 1] with sigmoid output
        val outputArray = Array(1) { FloatArray(1) }
        interpreter?.run(inputArray, outputArray)

        // 4. Get results
        // The output is a single probability (phishing probability)
        val phishingProb = outputArray[0][0]
        val legitimateProb = 1 - phishingProb

        val isPhishing = phishingProb > 0.5
        val confidence = if (isPhishing) phishingProb else legitimateProb

        return Pair(isPhishing, confidence)
    }

    private fun createTfidfVector(text: String): DoubleArray {
        val vector = DoubleArray(vocabSize)
        val tokens = text.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }

        // Count term frequencies
        val termFreq = mutableMapOf<String, Int>()
        tokens.forEach { token ->
            termFreq[token] = termFreq.getOrDefault(token, 0) + 1
        }

        // Also count bigrams (2-word combinations)
        for (i in 0 until tokens.size - 1) {
            val bigram = "${tokens[i]} ${tokens[i + 1]}"
            termFreq[bigram] = termFreq.getOrDefault(bigram, 0) + 1
        }

        // Calculate TF-IDF
        val totalTerms = tokens.size.toDouble()
        termFreq.forEach { (term, count) ->
            vocabulary[term]?.let { index ->
                val tf = count / totalTerms
                val idf = idfValues[term] ?: 0.0
                vector[index] = tf * idf
            }
        }

        // L2 normalization
        val norm = sqrt(vector.sumOf { it * it })
        if (norm > 0) {
            for (i in vector.indices) {
                vector[i] /= norm
            }
        }

        return vector
    }

    fun close() {
        interpreter?.close()
    }
}