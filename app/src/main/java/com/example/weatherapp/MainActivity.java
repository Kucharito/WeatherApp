package com.example.weatherapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnForecastItemClickListener {

    private BottomNavigationView bottomNavigationView;
    private static final String API_KEY = "f7938294059a9de1a1091e25ff901c6e";
    private ForecastFragment forecastFragment;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (findViewById(R.id.left_fragment_container) != null &&
                findViewById(R.id.right_fragment_container) != null) {

            if (getSupportFragmentManager().findFragmentById(R.id.left_fragment_container) == null &&
                    getSupportFragmentManager().findFragmentById(R.id.right_fragment_container) == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.left_fragment_container, new WeatherFragment())
                        .replace(R.id.right_fragment_container, new ForecastFragment())
                        .commit();
            }

        } else {
            // nastav listener vždy (aj keď sa vraciam z landscape)
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.weather_fragment) {
                    selectedFragment = new WeatherFragment();
                } else if (item.getItemId() == R.id.forecast_fragment) {
                    selectedFragment = new ForecastFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            });

            // ale predvolený výber nastav len pri prvom spustení
            if (savedInstanceState == null) {
                bottomNavigationView.setSelectedItemId(R.id.weather_fragment);
            }
        }
    }
    @Override
    public void onForecastItemClick(ForecastResponse.ListItem item){
        WeatherFragment weatherFragment = (WeatherFragment) getSupportFragmentManager().findFragmentById(R.id.left_fragment_container);
        if (weatherFragment != null) {
            weatherFragment.updateWeatherFromForecast(item);
        }
    }
}