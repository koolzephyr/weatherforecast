package com.zephyr.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zephyr on 1/3/2015.
 */
public class WeatherClient {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String JsonStr = null;
    private static String API_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&cnt=7&q=";

    public String getForecastJsonStr(String LOG_TAG, String location, String unit, String mode, int numDays){


        try {
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUARY_PARAM = "q";
            final String FORMAT_PARM = "mode";
            final String UNITS_PARM = "units";
            final String DAYS_PARM = "cnt";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUARY_PARAM,location)
                    .appendQueryParameter(FORMAT_PARM, mode)
                    .appendQueryParameter(UNITS_PARM, unit)
                    .appendQueryParameter(DAYS_PARM, Integer.toString(numDays)).build();

            System.out.println("uri"+builtUri);


            URL url= new URL(API_URL+location);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            Log.d(LOG_TAG, "inputStream " + inputStream);

            if (inputStream == null) {
                JsonStr = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                JsonStr = null;
            }
            JsonStr = buffer.toString();

            Log.d(LOG_TAG, "forecastJsonStr "+JsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            JsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                    return  null;
                }
            }
            return JsonStr;
        }
    }
}
