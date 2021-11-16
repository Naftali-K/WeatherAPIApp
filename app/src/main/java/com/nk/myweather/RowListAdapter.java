package com.nk.myweather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nk.myweather.model.WeatherReportModel;
import com.nk.myweather.services.ImageUrlService;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @Author: Naftali
 * @Date: 15.11.2021 16:41
 */
public class RowListAdapter extends ArrayAdapter<WeatherReportModel> {

    Activity activity;
    Context context;
    int resource;

    Handler handler = new Handler();

    public RowListAdapter(@NonNull Context context, int resource, @NonNull List<WeatherReportModel> objects, Activity activity) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String weather_state_name = getItem(position).getWeather_state_name();
        String weather_state_abbr = getItem(position).getWeather_state_abbr();
        String applicable_date = getItem(position).getApplicable_date();
        float min_temp = getItem(position).getMin_temp();
        float max_temp = getItem(position).getMax_temp();
        float the_temp = getItem(position).getThe_temp();
        float wind_speed = getItem(position).getWind_speed();
        float air_pressure = getItem(position).getAir_pressure();
        int humidity = getItem(position).getHumidity();
        float visibility = getItem(position).getVisibility();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView weather_image_view;
        TextView weather_text_view, date_weather, max_min_temp_text_view, temp_text_view, wind_speed_text_view, air_pressure_text_view, humidity_text_view, visibility_text_view;

        weather_text_view = convertView.findViewById(R.id.weather_text_view);
            weather_text_view.setText(weather_state_name);
        date_weather = convertView.findViewById(R.id.date_weather);
            date_weather.setText(applicable_date);
        max_min_temp_text_view = convertView.findViewById(R.id.max_min_temp_text_view);
            max_min_temp_text_view.setText(max_temp + " - " + min_temp);
        temp_text_view = convertView.findViewById(R.id.temp_text_view);
            temp_text_view.setText(String.valueOf(the_temp));
        wind_speed_text_view = convertView.findViewById(R.id.wind_speed_text_view);
            wind_speed_text_view.setText(String.valueOf(wind_speed));
        air_pressure_text_view = convertView.findViewById(R.id.air_pressure_text_view);
            air_pressure_text_view.setText(String.valueOf(air_pressure));
        humidity_text_view = convertView.findViewById(R.id.humidity_text_view);
            humidity_text_view.setText(String.valueOf(humidity));
        visibility_text_view = convertView.findViewById(R.id.visibility_text_view);
            visibility_text_view.setText(String.valueOf(visibility));


        weather_image_view = convertView.findViewById(R.id.weather_image_view);
        String URL = "https://www.metaweather.com/static/img/weather/png/"+ weather_state_abbr + ".png";
//        Picasso.get().load(URL).into(weather_image_view);

        ImageUrlService imageUrlService = new ImageUrlService(URL, new ImageUrlService.ImageResponse() {
            @Override
            public void updateImage(Bitmap bitmap) {
//                weather_image_view.setImageBitmap(bitmap); //not work, because defferent Thread

                // this way working, but not right way
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        weather_image_view.setImageBitmap(bitmap);
//                    }
//                });

                //this is right way
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        weather_image_view.setImageBitmap(bitmap);
                    }
                });
            }
        });
        imageUrlService.start();
//        new Thread(imageUrlService).start();

        return convertView;
    }
}
