# Lab #5: Random Pet 🐶🐱
## Submitted by: Zurab Chkhartishvili
Time spent: 1.5 hours spent in total
## Overview

In this lab, you will build an app that displays random pet images from an online API whenever the user taps a button.  
The goal is to understand how to make network requests, parse JSON data, and dynamically update your UI.

---

## 🎯 Goals

By the end of this lab, you will be able to:
- [x] Request data from a public REST API
- [x] Parse JSON responses in Kotlin
- [x] Display images loaded from URLs
- [x] Combine networking and UI logic together in one app

---

## Resources

- [Dog API](https://dog.ceo/dog-api/)
- [Cat API](https://thecatapi.com/)
- [CodePath AsyncHTTPClient Library](https://github.com/codepath/AsyncHttpClient)
- [Glide Image Loading Library](https://github.com/bumptech/glide)

---

## Lab Instructions

### Step 0: Project Setup

1. Create a new Android Studio project using **Empty Views Activity**
2. Name your app **Random Pet**
3. Delete the default “Hello World” TextView
4. Add an `ImageView` centered horizontally (id: `petImage`)
5. Add a `Button` below it (id: `petButton`) with text: **"Get a random pet!"**

🎯 **Checkpoint:**  
Run your app — you should see the default image and the button underneath.

---

### Step 1: Permissions & Dependencies

1. In **AndroidManifest.xml**, add:
    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```
2. In **build.gradle.kts (:app)**, add:
    ```kotlin
    implementation("com.codepath.libraries:asynchttpclient:2.2.0")
    ```
3. Click **Sync Now**

🎯 **Checkpoint:**  
The app still looks the same — ready for networking next!

---

### Step 2: Fetching Dog Images from the API

In **MainActivity.kt**, add the following:

```kotlin
private fun fetchDogImage() {
    val client = AsyncHttpClient()
    client["https://dog.ceo/api/breeds/image/random", object : JsonHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
            Log.d("Dog", "response successful")
        }

        override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
            Log.d("Dog Error", errorResponse)
        }
    }]
}
```

Then call `fetchDogImage()` inside `onCreate()`.

🎯 **Checkpoint:**  
Run the app and check **Logcat** — you should see `D/Dog: response successful`.

---

### Step 3: Extracting the Image URL

The Dog API returns a JSON response like this:

```json
{
  "message": "https://images.dog.ceo/breeds/shihtzu/n02086240_2113.jpg",
  "status": "success"
}
```

Update your success block:

```kotlin
val petImageURL = json.jsonObject.getString("message")
Log.d("petImageURL", "pet image URL set: $petImageURL")
```

🎯 **Checkpoint:**  
Look in **Logcat** → you should see  
`D/petImageURL: pet image URL set: https://images.dog.ceo/...`

---

### Step 4: Displaying the Image with Glide

1. In **build.gradle.kts (:app)**, add:
    ```kotlin
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    ```
2. Sync Gradle.

3. Create a function to set up the button:
    ```kotlin
    private fun setupButton(button: Button) {
        button.setOnClickListener { fetchDogImage() }
    }
    ```

4. In `onSuccess`, load the image:
    ```kotlin
    val imageView = findViewById<ImageView>(R.id.petImage)
    Glide.with(this@MainActivity)
        .load(petImageURL)
        .fitCenter()
        .into(imageView)
    ```

5. In `onCreate`, call `setupButton(petButton)`.

🎯 **Checkpoint:**  
Tap the button — a random dog image should appear!

---

## 🌟 Stretch Features

### Step 5: Fetch Random Cat Images

1. Create a new function:
    ```kotlin
    private fun fetchCatImage() {
        val client = AsyncHttpClient()
        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val resultsJSON = json.jsonArray.getJSONObject(0)
                val petImageURL = resultsJSON.getString("url")
                val imageView = findViewById<ImageView>(R.id.petImage)
                Glide.with(this@MainActivity)
                    .load(petImageURL)
                    .fitCenter()
                    .into(imageView)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                Log.d("Cat Error", errorResponse)
            }
        }]
    }
    ```

2. Test it by temporarily calling `fetchCatImage()` instead of `fetchDogImage()` in your button.

🎯 **Checkpoint:**  
Run the app — random **cat** images should appear.

---

### Step 6: Random Dog or Cat 🐾

Add a random selector in your `setupButton()`:

```kotlin
private fun setupButton(button: Button) {
    button.setOnClickListener {
        val randomChoice = (0..1).random()
        if (randomChoice == 0) {
            fetchDogImage()
        } else {
            fetchCatImage()
        }
    }
}
```

🎯 **Checkpoint:**  
Run the app — each tap should randomly show a dog or cat image!

---

## 🎉 Congratulations!

You’ve created an app that loads **random dog and cat images** from APIs when the user taps a button.  
You now know how to:
- Make HTTP requests  
- Parse JSON  
- Load images dynamically  
- Combine multiple APIs into one fun project 🚀
