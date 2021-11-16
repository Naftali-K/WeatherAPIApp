package com.nk.myweather.services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nk.myweather.MySingleton;
import com.nk.myweather.model.WeatherReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Naftali
 * @Date: 14.11.2021 11:18
 */
public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    Context context;
    String cityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }


    public interface VolleyResponseListener{
        void onError(String message);
        void onResponse(String cityID);
    }


    public void getCityID(String cityName, final VolleyResponseListener volleyResponseListener){

        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        cityID = "";

                        try {
                            JSONObject cityInfo = response.getJSONObject(0); //taking first item in array
                            cityID = cityInfo.getString("woeid"); //take item by key "woeid"
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Toast.makeText(context, "City name: " + cityName + " City ID: " + cityID, Toast.LENGTH_SHORT).show();
                        volleyResponseListener.onResponse(cityID);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError(error.toString());
            }
        }) {
            //--------------------------------------------------------------------------------------
            //example of settings header to request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "visual-crossing-weather.p.rapidapi.com");
                params.put("x-rapidapi-key", "02c479a5famsh296ea9a0a1a9a37p105f04jsn29e0d44e930b");
                return params;
            }
            //--------------------------------------------------------------------------------------
            // example for post sent parameters in request
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_name", "user01");
                params.put("password", "123456789");
                return params;
            }
            //--------------------------------------------------------------------------------------

        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }



    public interface ForeCastByIDResponse {
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModelList);
    }

    public void getCityForecastByID(String cityID, final ForeCastByIDResponse foreCastByIDResponse){

        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

        List<WeatherReportModel> weatherReportModelList = new ArrayList<>();

        // get the Json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    JSONArray consolidatedWeatherList = response.getJSONArray("consolidated_weather");

                    for (int i = 0; i < consolidatedWeatherList.length(); i++){
                        WeatherReportModel oneDayWeather = new WeatherReportModel();

                        JSONObject firstDayFromAPI = (JSONObject) consolidatedWeatherList.get(i);

                        oneDayWeather.setId(firstDayFromAPI.getInt("id"));
                        oneDayWeather.setWeather_state_name(firstDayFromAPI.getString("weather_state_name"));
                        oneDayWeather.setWeather_state_abbr(firstDayFromAPI.getString("weather_state_abbr"));
                        oneDayWeather.setWind_direction_compass(firstDayFromAPI.getString("wind_direction_compass"));
                        oneDayWeather.setCreated(firstDayFromAPI.getString("created"));
                        oneDayWeather.setApplicable_date(firstDayFromAPI.getString("applicable_date"));
                        oneDayWeather.setMin_temp(firstDayFromAPI.getLong("min_temp"));
                        oneDayWeather.setMax_temp(firstDayFromAPI.getLong("max_temp"));
                        oneDayWeather.setThe_temp(firstDayFromAPI.getLong("the_temp"));
                        oneDayWeather.setWind_speed(firstDayFromAPI.getLong("wind_speed"));
                        oneDayWeather.setWind_direction(firstDayFromAPI.getLong("wind_direction"));
                        oneDayWeather.setAir_pressure(firstDayFromAPI.getLong("air_pressure"));
                        oneDayWeather.setHumidity(firstDayFromAPI.getInt("humidity"));
                        oneDayWeather.setVisibility(firstDayFromAPI.getLong("visibility"));
                        oneDayWeather.setPredictability(firstDayFromAPI.getInt("predictability"));

                        weatherReportModelList.add(oneDayWeather);
                    }

//                    Toast.makeText(context, oneDay.toString(), Toast.LENGTH_SHORT).show();

                    foreCastByIDResponse.onResponse(weatherReportModelList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    foreCastByIDResponse.onError(e.getMessage().toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                foreCastByIDResponse.onError(error.toString());
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByName{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModelList);
    }



    public void getCityForecastByName(String cityName, GetCityForecastByName getCityForecastByName){

        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                getCityForecastByName.onError(message);
            }

            @Override
            public void onResponse(String cityID) {
                getCityForecastByID(cityID, new ForeCastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        getCityForecastByName.onError(message);
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModelList) {
                        getCityForecastByName.onResponse(weatherReportModelList);
                    }
                });
            }
        });
    }
}
