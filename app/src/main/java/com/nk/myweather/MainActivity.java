package com.nk.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nk.myweather.model.WeatherReportModel;
import com.nk.myweather.services.WeatherDataService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button getCityIdBtn, getWeatherByCityIdBtn, getWeatherByCityNameBtn;
    TextView cityIdTextView;
    EditText cityNameEditText;
    ListView weatherListView;

    WeatherDataService weatherDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCityIdBtn = findViewById(R.id.get_city_id_btn);
        getWeatherByCityIdBtn = findViewById(R.id.get_weather_by_city_id_btn);
        getWeatherByCityNameBtn = findViewById(R.id.get_weather_by_city_name_btn);

        cityIdTextView = findViewById(R.id.city_id_text_view);

        cityNameEditText = findViewById(R.id.city_name_edit_text);
        weatherListView = findViewById(R.id.weather_list_view);

        weatherDataService = new WeatherDataService(getBaseContext());


        getCityIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Get city ID. City name: " + cityNameEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                weatherDataService.getCityID(cityNameEditText.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        cityIdTextView.setText("City name: " + cityNameEditText.getText().toString() + "\tCity ID: " + cityID);
                    }
                });
            }
        });


        getWeatherByCityIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Get Weather by city ID", Toast.LENGTH_SHORT).show();
                weatherDataService.getCityForecastByID(cityNameEditText.getText().toString(), new WeatherDataService.ForeCastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        cityIdTextView.setText(message);
                    }
                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModelList) {
//                        cityIdTextView.setText(weatherReportModelList.toString());

//                        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, weatherReportModelList);
//                        weatherListView.setAdapter(arrayAdapter);
                        setListAdapter(weatherReportModelList);
                    }
                });
            }
        });


        getWeatherByCityNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "Get Weather by city NAME. City Name: " + cityNameEditText.getText(), Toast.LENGTH_SHORT).show();
                weatherDataService.getCityForecastByName(cityNameEditText.getText().toString(), new WeatherDataService.GetCityForecastByName() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModelList) {
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, weatherReportModelList);
//                        weatherListView.setAdapter(arrayAdapter);
                        setListAdapter(weatherReportModelList);
                    }
                });
            }
        });
    }

    private void setListAdapter(List<WeatherReportModel> weatherReportModelList){
        RowListAdapter adapter = new RowListAdapter(getBaseContext(), R.layout.list_adapter_row, weatherReportModelList, this);
        weatherListView.setAdapter(adapter);
    }
}