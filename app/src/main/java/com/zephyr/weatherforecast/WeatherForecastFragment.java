package com.zephyr.weatherforecast;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zephyr.weatherforecast.data.WeatherContract;
import com.zephyr.weatherforecast.data.WeatherDbHelper;
import com.zephyr.weatherforecast.model.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class WeatherForecastFragment extends Fragment {
    ListView forecastList;
    ListViewAdapter adapter;
    WeatherDbHelper dbHelper;
    SQLiteDatabase db;

    public WeatherForecastFragment(){

    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }
   private void updateWeather()
    {
        FetchWeatherForecast fetchWeather = new FetchWeatherForecast();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        fetchWeather.execute(location);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_refresh)
        {
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        forecastList = (ListView) rootView.findViewById(R.id.listView);
        List<WeatherForecast> testItems = new ArrayList<>();
        adapter = new ListViewAdapter(getActivity(),R.layout.row_item,testItems);
        forecastList.setAdapter(adapter);

        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherForecast forecast = adapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailListItem.class).putExtra("Forecast",forecast);
                startActivity(intent);
            }
        });
        return rootView;
    }
    public class FetchWeatherForecast extends AsyncTask<String,Void,List<WeatherForecast>> {
        String LOG_TAG = FetchWeatherForecast.class.getSimpleName();
        String forecastJsonStr=null;

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.show();
        }

        @Override
        protected List<WeatherForecast> doInBackground(String... params) {
            int numDays = 7;
            String mode = "Json";
            String unit = "metric";
            //System.out.println("I am in background");
            List<WeatherForecast> forecastList=new ArrayList<>();
            forecastJsonStr = new WeatherClient().getForecastJsonStr(LOG_TAG,params[0],unit,mode,numDays);
            try{
                forecastList=getWeatherDataFromJson(forecastJsonStr);
            }catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);

            }
            return forecastList;
        }

        private String getReadableDateString(long time) {
            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
            return format.format(date);
        }
        private String formatTemp(double temp){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitType = preferences.getString(getString(R.string.pref_unit_key), getString(R.string.pref_unit_metric));
            Log.v(LOG_TAG,unitType );

            String unit = " \u2103";
            temp=temp-273.15;

            if (unitType.equals(getString(R.string.pref_unit_key)))
            {
                temp = (temp * 1.8) + 32;
                unit=" \u2109";
            }

            return String.valueOf(Math.round(temp))+unit;
        }

        private String formatDegree(int deg){
            String degree="N";
            if(deg<0||deg>360){
                degree="unknown";
            }
            else if(deg>=337.5||deg<22.5){
                degree="N";
            }
            else if(deg>=22.5&&deg<67.5){
                degree="NE";
            }
            else if(deg>=67.5&&deg<112.5){
                degree="E";
            }
            else if(deg>=112.5&&deg<157.5){
                degree="SE";
            }
            else if(deg>=157.5&&deg<202.5){
                degree="S";
            }
            else if(deg>=202.5&&deg<247.5){
                degree="SW";
            }
            else if(deg>=247.5&&deg<292.5){
                degree="W";
            }
            else if(deg>=292.5&&deg<337.5){
                degree="NW";
            }
            return degree;
        }

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

        private List<WeatherForecast> getWeatherDataFromJson(String forecastJsonStr)
                throws JSONException {

            final String OWM_LIST = "list";

            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DAY = "day";
            final String OWM_DATETIME = "dt";
            final String OWM_PRESSURE = "pressure";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_SPEED = "speed";
            final String OWM_DEGREE = "deg";
            final String OWM_CLOUDS = "clouds";
            final String OWM_RAIN = "rain";
            final String OWM_SNOW = "snow";
            final String OWM_DESCRIPTION = "main";
            final String OWM_DETAIL= "description";
            final String OWM_ID = "id";


            List<WeatherForecast> fList=new ArrayList<WeatherForecast>();

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
            openDatabase(getActivity());
            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                long dateTime = dayForecast.getLong(OWM_DATETIME);
                String date = getReadableDateString(dateTime);

                double pressure = dayForecast.getDouble(OWM_PRESSURE);
                int humidity = dayForecast.getInt(OWM_HUMIDITY);
                double speed = dayForecast.getDouble(OWM_SPEED);

                int deg = dayForecast.getInt(OWM_DEGREE);
                String degree = formatDegree(deg);

                int cloud = dayForecast.getInt(OWM_CLOUDS);
                double rain = 0;
                double snow =0;
                if(dayForecast.has(OWM_RAIN)){
                     rain = dayForecast.getDouble(OWM_RAIN);
                }
                if(dayForecast.has(OWM_SNOW)){
                    snow = dayForecast.getDouble(OWM_SNOW);
                }

                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                String desc = weatherObject.getString(OWM_DESCRIPTION);
                String detail = weatherObject.getString(OWM_DETAIL);
                int id = weatherObject.getInt(OWM_ID);
                int icon=getWeatherIcon(id);

                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double temp = temperatureObject.getDouble(OWM_DAY);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);
                String temperature = formatTemp(temp);
                String maxTemp = formatTemp(high);
                String minTemp = formatTemp(low);
                //System.out.println("humidity: "+humidity);

                createEntry(date,temperature,maxTemp,minTemp,speed,degree,humidity,pressure,cloud,
                        snow,rain,desc,detail,icon);

                WeatherForecast forecast = new WeatherForecast(temperature,maxTemp,minTemp,speed,
                                                               degree,humidity,pressure,cloud,snow,
                                                               rain,date,desc,detail,icon);
                fList.add(forecast);

            }
            //consider this
            dbClose();
            return fList;
        }

        public void openDatabase(Context mContext){
            dbHelper = new WeatherDbHelper(mContext);
            db = dbHelper.getWritableDatabase();
        }
        public void createEntry(String date,String temp,String max,String min,
                                double windSpeed, String direction, int humidity,double pressure,
                                int cloud, double snow, double rain, String desc, String detDesc,
                                int icon){
            ContentValues values = new ContentValues();
            String location=null;
            values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, location);
            long locationRowId;
            locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
            Log.d(LOG_TAG, "New row id: " + locationRowId);
            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATETEXT, date);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON,String.valueOf(icon));
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DESC, desc);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, detDesc);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, max);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, min);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_TEMP, temp);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DIRECTION, direction);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, min);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DESC, desc);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ICON,String.valueOf(icon));

        }


        public void dbClose(){
            getData();
            dbHelper.close();
        }

        public void getData(){
            String[] columns = {
                    WeatherContract.LocationEntry._ID,
                    WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
            };

            // A cursor is your primary interface to the query results.
            Cursor cursor = db.query(
                    WeatherContract.LocationEntry.TABLE_NAME,  // Table to Query
                    columns,
                    null, // Columns for the "where" clause
                    null, // Values for the "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    null // sort order
            );
            if (cursor.moveToFirst()) {
                // Get the value in each column by finding the appropriate column index.
                int locationIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
                String location = cursor.getString(locationIndex);


                Log.d(LOG_TAG,location);
            } else {
                Log.e(LOG_TAG,"getData fail");
            }
        }
        @Override
        protected void onPostExecute(List<WeatherForecast> list) {
            super.onPostExecute(list);
            pd.dismiss();
            adapter.clear();
            for(WeatherForecast forecast : list){
                adapter.add(forecast);
            }
        }
    }

}
