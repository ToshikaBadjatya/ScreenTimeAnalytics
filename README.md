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

This configuration-first approach ensures ScreenTimeAnalytics can seamlessly adapt to different application architectures and analytics strategies.
