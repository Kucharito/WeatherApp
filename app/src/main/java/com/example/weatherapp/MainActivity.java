package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

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
    private boolean isForecastVisible = false;


    private static final String API_KEY = "f7938294059a9de1a1091e25ff901c6e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        descriptionValue = findViewById(R.id.descriptionValue);
        temperatureValue = findViewById(R.id.temperatureValue);
        inputField = findViewById(R.id.inputField);
        temperatureCard = findViewById(R.id.temperatureCard);
        descriptionCard = findViewById(R.id.descriptionCard);
        iconCard=findViewById(R.id.iconCard);

        forecastRecyclerView = findViewById(R.id.forecastRecyclerView);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = inputField.getText().toString().trim();
                if (!city.isEmpty()) {
                    getCurrentWeatherData(city);
                } else {
                    Toast.makeText(MainActivity.this, "Zadajte nazov mesta", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forecastButton = findViewById(R.id.forecastButton);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isForecastVisible){
                    forecastRecyclerView.setVisibility(View.GONE);
                    temperatureCard.setVisibility(View.VISIBLE);
                    descriptionCard.setVisibility(View.VISIBLE);
                    iconCard.setVisibility(View.VISIBLE);
                    inputField.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    forecastButton.setText("FORECAST");
                    isForecastVisible = false;
                }
                else {
                    String city = inputField.getText().toString().trim();
                    if (!city.isEmpty()) {
                        getForecastData(city);
                        temperatureCard.setVisibility(View.GONE);
                        descriptionCard.setVisibility(View.GONE);
                        iconCard.setVisibility(View.GONE);
                        inputField.setVisibility(View.GONE);
                        okButton.setVisibility(View.GONE);
                        forecastButton.setText("BACK");
                        isForecastVisible = true;
                    } else {
                        Toast.makeText(MainActivity.this, "Zadajte nazov mesta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getCurrentWeatherData(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseCurrentWeatherData(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Chyba pri nacitani pocasia", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void parseCurrentWeatherData(String jsonData) {
        Gson gson = new Gson();
        WeatherResponse weatherResponse = gson.fromJson(jsonData, WeatherResponse.class);

        if (weatherResponse != null && weatherResponse.getMain() != null) {
            descriptionValue.setText(weatherResponse.getWeather().get(0).getDescription());
            temperatureValue.setText(String.format("%.1f°C", weatherResponse.getMain().getTemp()));

            String iconCode = weatherResponse.getWeather().get(0).getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

            ImageView weatherIcon = findViewById(R.id.weatherIcon);
            Glide.with(this)
                    .load(iconUrl)
                    .into(weatherIcon);
        } else {
            Toast.makeText(this, "Data o pocasi nesu k dispozici", Toast.LENGTH_SHORT).show();
        }
    }

    private void getForecastData(String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseForecastData(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Chyba pri nacitani predpovede", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void parseForecastData(String jsonData) {
        Gson gson = new Gson();
        ForecastResponse forecastResponse = gson.fromJson(jsonData, ForecastResponse.class);

        if (forecastResponse != null && forecastResponse.getList() != null) {
            forecastAdapter = new ForecastAdapter(forecastResponse.getList());
            forecastRecyclerView.setAdapter(forecastAdapter);
            forecastRecyclerView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Predpoved není k dispozici", Toast.LENGTH_SHORT).show();
        }
    }
}
