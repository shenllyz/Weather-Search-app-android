# Weather Search Android App 🌤️

This is an Android Weather Application developed using **Kotlin** and **XML layouts**. The app allows users to search for cities, view current weather and forecasts, manage favorite cities, and share weather updates. The backend is built using **Node.js**, with data stored in **MongoDB Atlas** and deployed on **Google Cloud Platform (GCP)**.

---

## 🚀 **Features**

The application includes the following features:

1. **Home Screen Weather View**:  
   - Fetches current location using **IPinfo API**.
   - Displays real-time weather data from **Tomorrow.io API**.

2. **City Search**:  
   - Provides address lookup and autocomplete functionality using the **Google Geocoding & Places API**.
   - Fetches weather details for the searched city.

3. **Detailed Weather Information**:  
   - **Today**: Displays detailed weather for the current day.
   - **Weekly**: Line charts showing weekly high/low temperatures.
   - **Weather Data**: Additional graphs for cloud cover, precipitation, and humidity.

4. **Favorites Management**:  
   - Add or remove cities to/from favorites.
   - Favorites persist across app restarts using **MongoDB Atlas**.

5. **Progress Indicator**:  
   - Progress bars indicate data is being fetched asynchronously.

6. **Social Integration**:  
   - Share weather updates via Twitter with pre-formatted messages.

---

## ⚙️ **Tech Stack**

### **Frontend**:
- **Language**: Kotlin
- **Layouts**: XML
- **IDE**: Android Studio

### **Backend**:
- **Technology**: Node.js
- **Database**: MongoDB Atlas (NoSQL)
- **Deployment**: Google Cloud Platform (GCP)

### **APIs**:
- **Weather Data**: Tomorrow.io API
- **Current Location**: IPinfo API
- **Address Lookup & Autocomplete**: Google Geocoding & Places API
- **HTTP Requests**: Volley (for asynchronous calls)

### **Third-Party Libraries**:
- [Volley](https://developer.android.com/training/volley) - HTTP requests.
- [HighCharts Android](https://www.highcharts.com) - For graphing weather data.
- [Google Play Services](https://developers.google.com/android/guides/setup) - For location services.

---

## 🛠️ **Setup Instructions**

Follow these steps to run the Weather Search Android App:

### **1. Clone the Repository**:
```bash
git clone https://github.com/shenllyz/Weather-Search-app-android.git
cd Weather-Search-app-android
```
###  **2. Install Android Studio**:
Download and install [Android Studio](https://developer.android.com/studio).
###  **3. Run the App**:
- Open the project in **Android Studio**.
- Select a virtual device/emulator (e.g., **Pixel 5, API 30**).
- Build and run the app.

## 📱 Screenshots

| ![Home Screen](https://github.com/user-attachments/assets/a1faec9e-6932-4a17-973c-02c09d278471) | ![Search AutoComplete](https://github.com/user-attachments/assets/a233e697-5060-46fe-b35c-6511d0f5ca01) | ![Search Result](https://github.com/user-attachments/assets/8ef0dbc9-fa46-405b-94f4-38d125aa096c) |
|:------------------------:|:------------------------:|:-------------------------:|
| **Home Screen**         | **Search Autocomplete**  | **Search Result**         |

| ![Detail Today](https://github.com/user-attachments/assets/fe1dbd29-bb21-4653-92c9-de7ffe48281a) | ![Detail Weekly](https://github.com/user-attachments/assets/1a56dc3e-d58c-4b4a-b9d1-b0f0f4232c6a) | ![Detail Weather Data](https://github.com/user-attachments/assets/a685656d-82c2-4a25-ac4d-2f2b087873d9) |
|:-------------------------:|:------------------------:|:--------------------------:|
| **Detail - Today**       | **Detail - Weekly**      | **Detail - Weather Data**  |


## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.
