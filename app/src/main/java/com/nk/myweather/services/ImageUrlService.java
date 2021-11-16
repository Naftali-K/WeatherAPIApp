package com.nk.myweather.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Naftali
 * @Date: 15.11.2021 13:16
 */
public class ImageUrlService extends Thread {

    String URL;
    ImageResponse imageResponse;

    public ImageUrlService(String URL, ImageResponse imageResponse) {
        this.URL = URL;
        this.imageResponse = imageResponse;
    }

    @Override
    public void run() {
        Bitmap bitmap = null;

        try {
            InputStream inputStream = new java.net.URL(URL).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            imageResponse.updateImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    //    @Override
//    protected Bitmap doInBackground(String... strings) {
//        String urlLink = strings[0];
//        Bitmap bitmap = null;
//        try {
//            InputStream inputStream = new java.net.URL(urlLink).openStream();
//            bitmap = BitmapFactory.decodeStream(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//    }
    public interface ImageResponse {
        void updateImage(Bitmap bitmap);
    }
}

