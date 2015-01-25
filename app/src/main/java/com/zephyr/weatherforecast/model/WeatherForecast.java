package com.zephyr.weatherforecast.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zephyr on 1/3/2015.
 */
public class WeatherForecast implements Parcelable{
    private String location;
    private String temperature;
    private String maxTemp;
    private String minTemp;
    private double windSpeed;
    private String windDirection;
    private int humidity;
    private double pressure;
    private int cloud;
    private double snow;
    private double rain;
    private String date;
    private String description;
    private String detDescription;
    private int icon;
    public WeatherForecast(String temperature, String maxTemp, String minTemp, double windSpeed,
                           String windDirection, int humidity, double pressure, int cloud,
                           double snow, double rain, String date, String description,
                           String detDescription, int icon) {
        this.temperature = temperature;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.pressure = pressure;
        this.cloud = cloud;
        this.snow = snow;
        this.rain = rain;
        this.date = date;
        this.description = description;
        this.detDescription = detDescription;
        this.icon = icon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetDescription() {
        return detDescription;
    }

    public void setDetDescription(String detDescription) {
        this.detDescription = detDescription;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
/* everything below here is for implementing Parcelable */

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(temperature);
        dest.writeInt(icon);
        dest.writeString(detDescription);
        dest.writeString(maxTemp);
        dest.writeString(minTemp);
        dest.writeDouble(windSpeed);
        dest.writeString(windDirection);
        dest.writeInt(humidity);
        dest.writeDouble(pressure);
        dest.writeInt(cloud);
        dest.writeDouble(snow);
        dest.writeDouble(rain);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<WeatherForecast> CREATOR = new Parcelable.Creator<WeatherForecast>() {
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private WeatherForecast(Parcel in) {
        temperature=in.readString();
        icon=in.readInt();
        detDescription=in.readString();
        maxTemp=in.readString();
        minTemp=in.readString();
        windSpeed=in.readDouble();
        windDirection=in.readString();
        humidity=in.readInt();
        pressure=in.readDouble();
        cloud=in.readInt();
        snow=in.readDouble();
        rain=in.readDouble();
    }
}
