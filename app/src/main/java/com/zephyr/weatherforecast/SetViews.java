package com.zephyr.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zephyr.weatherforecast.model.WeatherForecast;

/**
 * Created by Zephyr on 1/24/2015.
 */
public class SetViews {
    private TextView locView;
    private TextView tempView;
    private ImageView imageView;
    private TextView descView;
    private TextView unitView;
    private TextView maxView;
    private TextView minView;
    private TextView speedView;
    private TextView degView;
    private TextView humView;
    private TextView presView;
    private TextView cloudView;
    private TextView rainView;
    private TextView snowView;

   public void getView(View row){
       locView = (TextView) row.findViewById(R.id.loc);
       tempView = (TextView) row.findViewById(R.id.temp);
       descView = (TextView) row.findViewById(R.id.desc);
       unitView = (TextView) row.findViewById(R.id.unit);
       maxView = (TextView) row.findViewById(R.id.maxTemp);
       minView = (TextView) row.findViewById(R.id.minTemp);
       speedView = (TextView) row.findViewById(R.id.speed);
       degView = (TextView) row.findViewById(R.id.degree);
       humView = (TextView) row.findViewById(R.id.humidity);
       presView = (TextView) row.findViewById(R.id.pressure);
       cloudView = (TextView) row.findViewById(R.id.cloud);
       rainView = (TextView) row.findViewById(R.id.rain);
       snowView = (TextView) row.findViewById(R.id.snow);
       imageView = (ImageView) row.findViewById(R.id.icWeather);


   }

   public void setView(WeatherForecast forecast, View rootView, Context context){
       getView(rootView);

       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

       String location = prefs.getString(context.getResources().getString(R.string.pref_location_key),context.getResources().getString(R.string.pref_location_default));
       locView.setText(location);
       descView.setText(forecast.getDetDescription());

       String[] temp = forecast.getTemperature().split(" ");
       tempView.setText(temp[0]);

       String unit = prefs.getString(context.getResources().getString(R.string.pref_unit_key), context.getResources().getString(R.string.pref_unit_metric));
       if(unit.equals("metric")){
           unitView.setText("\u2103");
       }
       else{
           unitView.setText("\u2109");
       }

       maxView.setText(forecast.getMaxTemp());
       minView.setText(forecast.getMinTemp());
       speedView.setText(String.valueOf(forecast.getWindSpeed()));
       degView.setText(forecast.getWindDirection());
       humView.setText(String.valueOf(forecast.getHumidity()));
       presView.setText(String.valueOf(forecast.getPressure()));
       cloudView.setText(String.valueOf(forecast.getCloud()));
       rainView.setText(String.valueOf(forecast.getRain()));
       snowView.setText(String.valueOf(forecast.getSnow()));
       imageView.setImageResource(forecast.getIcon());
   }
}
