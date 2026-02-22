# ScreenTimeAnalytics

## **About ScreenTimeAnalytics**

**ScreenTimeAnalytics** is a lightweight Android analytics library written in **Kotlin** that tracks the amount of time users spend across activities and functional modules. It enables teams to understand user interaction patterns through **passive behavior tracking** and supports **data-driven decision-making** for future product and feature improvements.

---

## **Why Use ScreenTimeAnalytics?**

### **Efficient Tracking**
- Aggregates cumulative time spent across activities and application flows  
- Provides relative time distribution in **percentage terms**  

### **High Customization**
- Allows developers to define **trackable activities and modules**  
- Supports **customizable caching mechanisms**  
- Enables multiple **data logging formats** to fit different analytics needs

### **Fully Kotlin & Developer-Friendly**
- Built entirely in **Kotlin**, leveraging its concise and expressive syntax  

### **Reliable Analytics Caching**
- Caches analytics data in **developer-chosen persistent storage**  
- Uses **WorkManager** for efficient background syncing  

### **Easy Sync Integration**
- Simple configuration for defining **sync actions**  
- Supports syncing data to **Firebase**, **custom backends**, or other servers 
---

## **Installation**

To integrate ScreenTimeAnalytics library into your Android project, follow these simple steps:

Update your `settings.gradle` file with the following dependency:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } // this one
    }
}
```

Update your module level `build.gradle` file with the following dependencies:

```gradle
dependencies {
    implementation 'com.github.ToshikaBadjatya:ScreenTimeAnalytics:1.0.0'
    implementation 'com.github.ToshikaBadjatya:annotations:1.0.0'
    kapt 'com.github.ToshikaBadjatya:annotation-processor:1.0.0'
}
```

Don't forget to apply the kapt plugin in your `build.gradle`:

```gradle
plugins {
    id 'kotlin-kapt'
}
```


---
## **Usage**

Initialize the library in MainApplication
```kotlin
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val analyticsClient= FirebaseAnalyticsClient()
        val config =
            ScreenTimeConfig.Builder()
                .analyticsClient(analyticsClient)
                .build()
        ScreenTimeAnalytics.init(this, config)

    }
}
```

Mark the activities that need to be tracked

```kotlin
@TrackActivity
class MainActivity : ScreenTimeActivity() {
}
```

------

## **Customization in ScreenTimeAnalytics**

ScreenTimeAnalytics provides a flexible configuration mechanism that allows developers to control how analytics data is captured, stored, and reported.

### **Configuration Example**

```kotlin
ScreenTimeConfig.Builder()
    .showTimes(true)
    .showPercentage(true)
    .timeUnit(TimeUnit.SECONDS)
    .analyticsClient(analyticsClient)
    .setLocale(Locale.getDefault())
    .setStorage(PersistentStorageType.DATABASE)
    .build()
```

### **Available Customization Options**

- **Time Spent Visibility**
  - Configure whether to display **time spent**, **percentage distribution**, or **both** across tracked activities and flows.

- **Locale Configuration**
  - Specify the **locale** to control timestamp formatting and regional time representation for usage data.

- **Storage Type**
  - Choose how analytics data is persisted:
    - **Database** for structured and query-friendly storage
    - **File System** for lightweight or custom storage requirements

- **Time Unit**
  - Define the unit of time measurement (e.g., **seconds**, **milliseconds**, **minutes**) based on reporting needs.

- **Analytics Client**

  - Plug in a custom analytics client to control how and where data is processed or synced.
  - Sample client for eg
```kotlin
   class FirebaseAnalyticsClient: AnalyticsClient {
    private val database: FirebaseDatabase = Firebase.database
    private val endpoint = "analytics"

    override suspend fun sendEvent(response: ScreenTimeResponse) {
    database.reference.child(endpoint).push().setValue(response)
    }
    }
```

This configuration-first approach ensures ScreenTimeAnalytics can seamlessly adapt to different application architectures and analytics strategies.
