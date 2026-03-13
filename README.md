# Phishing Email Detector (Android)
## Faculty project

Android app built with **Kotlin** and **Jetpack Compose** that detects whether an email message is **phishing or legitimate** using an on-device machine learning model.

## How it works
1. User pastes email text into the input field.
2. The app loads a machine learning model.
3. The text is analyzed by `PhishingDetector`.
4. The app displays whether the email is **phishing or legitimate** with a confidence score.

I will paste images and videos here from the app when I do the frontend.

## Tech Stack
- Kotlin
- Android
- Jetpack Compose
- Kotlin Coroutines
- TensorFlow Lite (TFLite)

## Model
I used a Kaggle set and trained it.After training, the model was converted to **TensorFlow Lite (TFLite)** so it can run directly on the Android device.
https://colab.research.google.com/drive/10J9sHoMHolXv__JPh0oja_b-6ipsF1h5