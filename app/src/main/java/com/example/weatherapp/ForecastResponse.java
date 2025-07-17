package com.example.weatherapp;

import android.app.LauncherActivity;

import java.util.List;

public class ForecastResponse {
    private List<ListItem>list;
    public List<ListItem> getList(){
        return list;
    }
    public static class ListItem{
        private WeatherResponse.Main main;
        private List<Weather>weather;
        private String dt_txt;
        private WeatherResponse.Wind wind;

        public WeatherResponse.Main getMain(){
            return main;
        }
        public List<Weather> getWeather(){
            return weather;
        }
        public String getDtTxt(){
            return dt_txt;
        }

        public WeatherResponse.Wind getWind() {
            return wind;
        }
    }
    public static class Weather{
        private String description;
        private String icon;

        public String getDescription(){
            return description;
        }
        public String getIcon(){
            return icon;
        }

    }
    public static class Wind {
        private double speed;

        public double getSpeed() {
            return speed;
        }
    }


}
