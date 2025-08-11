package com.example.weatherapp;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
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
                        .setCustomAnimations(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right
                        )

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
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_right
                            )
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (v instanceof EditText){
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    // Click outside the EditText, hide the keyboard
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}