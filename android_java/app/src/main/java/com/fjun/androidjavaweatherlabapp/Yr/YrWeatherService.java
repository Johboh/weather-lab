package com.fjun.androidjavaweatherlabapp.Yr;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit definition for YR weather endpoints.
 * API: https://api.met.no
 */
public interface YrWeatherService {

    String BASE_URL = "https://api.met.no/weatherapi/";
    String IMAGE_URL = BASE_URL + "weathericon/1.1/?symbol=%d&content_type=image/png";

    @GET("locationforecast/1.9")
    Call<Weatherdata> forecast(@Query("lat") String latitude, @Query("lon") String longitude);
}
