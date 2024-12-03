package com.example.weatherapp.repository
import android.content.Context
import com.example.weatherapp.utils.ApiConstants
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class WeatherRepository(context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun fetchIpInfo(
        onSuccess: (latitude: String, longitude: String, city: String, state: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/get_IPlocation"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val ipToken = response.getString("token")
                    val ipInfoUrl = "https://ipinfo.io/?token=$ipToken"

                    val ipInfoRequest = JsonObjectRequest(
                        Request.Method.GET, ipInfoUrl, null,
                        { ipInfoResponse ->
                            val loc = ipInfoResponse.getString("loc").split(",")
                            val latitude = loc[0]
                            val longitude = loc[1]
                            val city = ipInfoResponse.getString("city")
                            val state = ipInfoResponse.getString("region")
                            onSuccess(latitude, longitude, city, state)
                        },
                        { error ->
                            onError(error.message ?: "Error fetching IP Info data")
                        }
                    )
                    requestQueue.add(ipInfoRequest)

                } catch (e: Exception) {
                    onError("Error parsing IP token: ${e.message}")
                }
            },
            { error ->
                onError(error.message ?: "Error fetching IP token")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun fetchGeocodingData(
        address: String,
        onSuccess: (latitude: Double, longitude: Double, formattedAddress: String) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/get_geocoding?address=$address"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val status = response.getString("status")
                    if (status == "OK") {
                        val results = response.getJSONArray("results")
                        val location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                        val latitude = location.getDouble("lat")
                        val longitude = location.getDouble("lng")
                        val formattedAddress = results.getJSONObject(0).getString("formatted_address")
                        onSuccess(latitude, longitude, formattedAddress)
                    } else {
                        onError("Geocoding API error: $status")
                    }
                } catch (e: Exception) {
                    onError("Error parsing Geocoding data: ${e.message}")
                }
            },
             { error ->
                onError(error.message ?: "Error fetching Geocoding data")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }


    fun fetchWeatherData(
        lat: Double,
        lon: Double,
        onSuccess: (JSONObject) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/get_weather?latitude=$lat&longitude=$lon"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    if (response.has("data")) {
                        onSuccess(response)
                    } else {
                        onError("Weather API error: ${response.optString("error")}")
                    }
                } catch (e: Exception) {
                    onError("Error parsing Weather data: ${e.message}")
                }
            },
            { error ->
                onError(error.message ?: "Error fetching Weather data")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

}
