package com.example.weatherapp.repository
import android.content.Context
import com.example.weatherapp.utils.ApiConstants
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.model.FavoriteLocation
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

    fun fetchCityAutocomplete(
        context: Context,
        query: String,
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/autocomplete?input=$query"


        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val results = mutableListOf<Pair<String, String>>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val city = jsonObject.getString("city")
                        val state = jsonObject.getString("state")
                        results.add(Pair(city, state))
                    }
                    onSuccess(results)
                } catch (e: Exception) {
                    onError("Failed to parse response: ${e.message}")
                }
            },
            { error ->
                onError("Request failed: ${error.message}")
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    fun fetchFavorites(
        context: Context,
        onSuccess: (List<FavoriteLocation>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/get_all_favorites_locations"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                try {
                    val favorites = mutableListOf<FavoriteLocation>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getString("_id")
                        val city = jsonObject.getString("city")
                        val state = jsonObject.getString("state")
                        val lat = jsonObject.getDouble("lat")
                        val lng = jsonObject.getDouble("lng")
                        favorites.add(FavoriteLocation(city, state, lat, lng,id))
                    }
                    onSuccess(favorites)
                } catch (e: Exception) {
                    onError("Failed to parse response: ${e.message}")
                }
            },
            { error ->
                onError("Request failed: ${error.message}")
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    fun addFavorite(
        city: String,
        state: String,
        lat: Double,
        lng: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/add_favorite_location"
        val params = JSONObject().apply {
            put("city", city)
            put("state", state)
            put("lat", lat)
            put("lng", lng)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                try {
                        onSuccess()
                } catch (e: Exception) {
                    onError("Error parsing add favorite response: ${e.message}")
                }
            },
            { error ->
                onError(error.message ?: "Error adding favorite")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    fun deleteFavorite(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "${ApiConstants.BASE_URL}/delete_favorite_location?id=$id"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                onSuccess()
            },
            { error ->
                onError("Request failed: ${error.message}")
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

}
