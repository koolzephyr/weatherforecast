package com.zephyr.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zephyr.weatherforecast.model.WeatherForecast;

import java.util.List;

/**
 * Created by Zephyr on 1/3/2015.
 */
public class ListViewAdapter extends ArrayAdapter<WeatherForecast> {
    Context context;
    int resource;
    List<WeatherForecast> objects;

    public ListViewAdapter(Context context, int resource, List<WeatherForecast> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource=resource;
        this.objects=objects;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView dayView;
        TextView descView;
        TextView maxView;
        TextView minView;
        TextView max;
        TextView min;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View row = convertView;


        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row= inflater.inflate(resource,parent,false);

            holder = new ViewHolder();

            holder.dayView = (TextView) row.findViewById(R.id.dateView);
            holder.descView = (TextView) row.findViewById(R.id.descriptionView);
            holder.maxView = (TextView) row.findViewById(R.id.maxTempView);
            holder.minView = (TextView) row.findViewById(R.id.minTempView);
            holder.imageView = (ImageView) row.findViewById(R.id.weatherIcon);
            row.setTag(holder);
        }else {
            holder = (ViewHolder) row.getTag();
        }

        WeatherForecast forecast = getItem(position);
        //System.out.println("Item: "+getItem(position));

        holder.dayView.setText(forecast.getDate());
        holder.descView.setText(forecast.getDescription());
        holder.maxView.setText(forecast.getMaxTemp());
        holder.minView.setText(forecast.getMinTemp());
        holder.imageView.setImageResource(forecast.getIcon());

        return row;
    }
/*

    private int getWeatherIcon(int realId){
        int icon = R.drawable.ic_unknown;

        switch (realId){
            case 502:
                icon=R.drawable.ic_heavy_rain;
                break;
            case 511:
                icon=R.drawable.ic_snowy;
                break;
            case 520:
                icon=R.drawable.ic_showers;
                break;
            case 800:
                icon=R.drawable.ic_sunny;
                break;
            case 804:
                icon=R.drawable.ic_overcast;
                break;
            case 905:
                icon=R.drawable.ic_windy;
                break;
            default:
                int simpleId = realId/100;

                switch (simpleId){
                    case 2:
                        icon=R.drawable.ic_thunder;
                        break;
                    case 3:
                        icon=R.drawable.ic_showers;
                        break;
                    case 5:
                        icon=R.drawable.ic_sun_rain;
                        break;
                    case 6:
                        icon=R.drawable.ic_snowy;
                        break;
                    case 8:
                        icon=R.drawable.ic_sunny_cloudy;
                        break;
                }
                break;
        }
        return icon;
    }
*/
}

