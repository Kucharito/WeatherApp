package com.example.weatherapp;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather>weather;
    //private String name;

    public Main getMain(){
        return main;
    }
    public List<Weather> getWeather(){
        return weather;
    }

    public class Main{
        private double temp;
        public double getTemp(){
            return temp;
        }
    }
    public class Weather{
        private String description;
        private String icon;

        public String getDescription(){
            return description;
        }

        public String getIcon(){
            return icon;
        }
    }

}
