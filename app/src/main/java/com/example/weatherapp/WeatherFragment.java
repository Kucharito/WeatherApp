package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Locale;


public class WeatherFragment extends Fragment {
    private TextView descriptionValue;
    private TextView temperatureValue;
    private EditText inputField;
    private Button okButton;
    private ForecastAdapter forecastAdapter;
    private RecyclerView forecastRecyclerView;
    private Button forecastButton;
    private CardView temperatureCard;
    private CardView descriptionCard;
    private CardView iconCard;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        descriptionValue = rootView.findViewById(R.id.descriptionValue);
        temperatureValue = rootView.findViewById(R.id.temperatureValue);
        inputField = rootView.findViewById(R.id.inputField);
        temperatureCard = rootView.findViewById(R.id.temperatureCard);
        descriptionCard = rootView.findViewById(R.id.descriptionCard);
        iconCard = rootView.findViewById(R.id.iconCard);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("WeatherAppPrefs", 0);
        String savedCity = sharedPreferences.getString("city", "");
        if(!savedCity.isEmpty()) {
            inputField.setText(savedCity);
            getCurrentWeatherData(savedCity);

        }


        okButton = rootView.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = inputField.getText().toString().trim();
                if (!city.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("city", city);
                    editor.apply();
                    getCurrentWeatherData(city);
                } else {
                    Toast.makeText(requireActivity(), getString(R.string.toast_enter_city), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void getCurrentWeatherData(String city) {
        Locale currentLocale = getResources().getConfiguration().getLocales().get(0);
        String languageCode= currentLocale.getLanguage();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=f7938294059a9de1a1091e25ff901c6e&units=metric" + "&lang=" + languageCode;

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> parseCurrentWeatherData(response.toString()),
                error -> Toast.makeText(requireContext(), getString(R.string.toast_weather_error), Toast.LENGTH_SHORT).show()
        );
        queue.add(jsonObjectRequest);
    }


    private void parseCurrentWeatherData(String jsonData) {
        Log.d("WeatherFragment", "JSON Data: " + jsonData);
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(jsonData, WeatherResponse.class);

        if (weatherResponse != null && weatherResponse.getMain() != null) {
            descriptionValue.setText(weatherResponse.getWeather().get(0).getDescription());
            temperatureValue.setText(String.format("%.1f°C", weatherResponse.getMain().getTemp()));

            String iconCode = weatherResponse.getWeather().get(0).getIcon();
            ImageView weatherIcon = getView().findViewById(R.id.weatherIcon);

            int iconResId = getResources().getIdentifier("ic_" + iconCode, "drawable", requireContext().getPackageName());
            if (iconResId!=0){
                weatherIcon.setImageResource(iconResId);
            }
            else{
                weatherIcon.setImageResource(R.drawable.ic_01d); //default
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.toast_weather_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateWeatherFromForecast(ForecastResponse.ListItem item) {
        if (item != null && item.getMain() != null) {
            descriptionValue.setText(item.getWeather().get(0).getDescription());
            temperatureValue.setText(String.format("%.1f°C", item.getMain().getTemp()));

            String iconCode = item.getWeather().get(0).getIcon();
            ImageView weatherIcon = getView().findViewById(R.id.weatherIcon);

            int iconResId = getResources().getIdentifier("ic_" + iconCode, "drawable", requireContext().getPackageName());
            if (iconResId != 0){
                weatherIcon.setImageResource(iconResId);
            } else {
                weatherIcon.setImageResource(R.drawable.ic_01d); //default icon
            }

        }
    }

}