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
- **Language**: [Kotlin](https://kotlinlang.org/)
- **Layouts**: [XML](https://developer.android.com/guide/topics/ui/declaring-layout)
- **IDE**: [Android Studio](https://developer.android.com/studio)

### **Backend**:
- **Technology**: [Node.js](https://nodejs.org/)
- **Database**: [MongoDB Atlas (NoSQL)](https://www.mongodb.com/cloud/atlas)
- **Deployment**: [Google Cloud Platform (GCP)](https://cloud.google.com/)

### **APIs**:
- **Weather Data**: [Tomorrow.io API](https://www.tomorrow.io/)
- **Current Location**: [IPinfo API](https://ipinfo.io/)
- **Address Lookup & Autocomplete**: [Google Geocoding & Places API](https://developers.google.com/maps/documentation/geocoding/start)
- **HTTP Requests**: [Volley](https://developer.android.com/training/volley) (for asynchronous calls)
- [HighCharts Android](https://www.highcharts.com) - For graphing weather data.


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

| ![Splash Screen](https://github.com/user-attachments/assets/bc6f2010-1fd3-4db4-8175-320b94241cce) | ![Loading Page](https://github.com/user-attachments/assets/5eef62f4-fe45-4c30-a8a7-35cb77a8c663) | ![Home Screen](https://github.com/user-attachments/assets/be80f5e1-af03-47f6-b2f2-645159adc747) |
|:------------------------:|:------------------------:|:-------------------------:|
| **Splash Screen**         | **Loading Page**  | **Home Screen**          |

| ![Detail Today](https://github.com/user-attachments/assets/fe1dbd29-bb21-4653-92c9-de7ffe48281a) | ![Detail Weekly](https://github.com/user-attachments/assets/1a56dc3e-d58c-4b4a-b9d1-b0f0f4232c6a) | ![Detail Weather Data](https://github.com/user-attachments/assets/a685656d-82c2-4a25-ac4d-2f2b087873d9) |
|:-------------------------:|:------------------------:|:--------------------------:|
| **Detail - Today**       | **Detail - Weekly**      | **Detail - Weather Data**  |

| ![Search AutoComplete](https://github.com/user-attachments/assets/66fe1bb2-49aa-4f71-8be4-823afb3eefc9) | ![Search Result](https://github.com/user-attachments/assets/2ca6c5b7-d8a4-40d6-aecf-4fb5c61ef5a8)  | ![image](https://github.com/user-attachments/assets/088219a8-8053-4af0-8ac0-d4b0fc1b3e6e) |
|:-------------------------:|:------------------------:|:--------------------------:|
| **Search**         | **Search Result**      | **Add To Favorite**  |

| ![Favorite List](https://github.com/user-attachments/assets/9a1c1dfc-64d9-431b-b5be-6eb3731c5952) | ![Remove From Favorite](https://github.com/user-attachments/assets/884ceb76-bb6a-4113-a67a-befa7ea914fb) | ![Twitter](https://github.com/user-attachments/assets/26158a04-47c6-46a2-93bd-771d9f403380) |
|:-------------------------:|:------------------------:|:--------------------------:|
| **Favorite List**       | **Remove From Favorite**      | **Twitter**  |
 
## 🎥 Demo Video

Watch the video demo of the app in action: [Weather Search Android App Demo](https://drive.google.com/file/d/1t6r0_KLbPTEZO6YOdpQjdLBJTO9VVb9T/view?usp=drive_link)
## 📄 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.
