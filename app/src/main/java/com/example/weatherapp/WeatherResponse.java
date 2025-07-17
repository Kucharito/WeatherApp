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
        private double feels_like;
        private double humidity;
        public double getTemp(){
            return temp;
        }
        public double getTempFeelsLike(){
            return feels_like;
        }
        public double getHumidity(){
            return humidity;
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
