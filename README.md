<div align="center">

# 🎣 Phishing Email Detector

**An Android app that detects phishing emails on-device using machine learning.**

*Faculty Project · Kotlin · Jetpack Compose · TensorFlow Lite*

[![Demo Videos](https://img.shields.io/badge/▶_Demo_Videos-Watch_Here-EA4335?style=for-the-badge)](https://github.com/bube-ristovska/email-phishing-detector/tree/main/demo-videos)
[![Colab Notebook](https://img.shields.io/badge/Colab-Training_Notebook-F9AB00?style=for-the-badge&logo=googlecolab&logoColor=white)](https://colab.research.google.com/drive/10J9sHoMHolXv__JPh0oja_b-6ipsF1h5)

</div>

---

## What it does

Paste any email text into the app and it instantly tells you whether the email is **phishing or legitimate**, along with a confidence score — all processed directly on the device, no internet required.

---

## How it works

| Step | Description |
|------|-------------|
| 1️⃣ | User pastes email text into the input field |
| 2️⃣ | Text is tokenized and converted into a **TF-IDF vector** |
| 3️⃣ | Vector is fed into the on-device **TFLite model** |
| 4️⃣ | App displays **Phishing ⚠️ or Legitimate ✅** with a confidence score |

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Async | Kotlin Coroutines |
| ML Runtime | TensorFlow Lite |
| Platform | Android |

---

## Model

The model was trained on a Kaggle phishing email dataset using **TF-IDF vectorization** and exported to **TensorFlow Lite** format so it runs fully on-device without any server calls.

- 📓 [View the training notebook on Google Colab](https://colab.research.google.com/drive/10J9sHoMHolXv__JPh0oja_b-6ipsF1h5)
- The model files (`phishing_model.tflite`, `vocabulary.json`, `idf_values.json`) are bundled in the app's `assets/` folder

---

## Project Structure

```
app/src/main/java/com/example/phishing_detector/
├── MainActivity.kt          # Entry point
├── PhishingDetector.kt      # ML model + TF-IDF logic
├── ui/
│   ├── Theme.kt             # Colors & Material theme
│   ├── AppNavigation.kt     # Drawer & screen routing
│   ├── InboxScreen.kt       # Demo inbox + email detail
│   ├── CheckerScreen.kt     # Paste & scan screen
│   └── SharedComponents.kt  # Reusable composables
└── data/
    └── SampleEmails.kt      # Demo email data
```

---

<div align="center">
<sub>Built with ❤️ as a faculty project</sub>
</div>
