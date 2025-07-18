package com.example.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Locale;

public class ForecastFragment extends Fragment {

    private RecyclerView forecastRecyclerView;
    private ForecastAdapter forecastAdapter;
    private OnForecastItemClickListener listener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnForecastItemClickListener) {
            listener = (OnForecastItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnForecastItemClickListener");
        }
    }

    private static final String API_KEY = "f7938294059a9de1a1091e25ff901c6e";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_fragment, container, false);

        forecastRecyclerView = rootView.findViewById(R.id.recyclerViewForecast);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        String savedCity = requireActivity().getSharedPreferences("WeatherAppPrefs", 0)
                .getString("city", "");
        if (!savedCity.isEmpty()) {
            getForecastData(savedCity);
        } else {
            Toast.makeText(requireContext(), "Zadajte mesto pre zobrazenie predpovede", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

    private void getForecastData(String city) {
        Locale currentLocale = getResources().getConfiguration().getLocales().get(0);
        String languageCode = currentLocale.getLanguage();

        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric" + "&lang=" + languageCode;

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> parseForecastData(response.toString()),
                error -> Toast.makeText(requireContext(), "Chyba pri načítaní predpovede", Toast.LENGTH_SHORT).show()
        );
        queue.add(jsonObjectRequest);
    }

    private void parseForecastData(String jsonData) {
        Gson gson = new Gson();
        ForecastResponse forecastResponse = gson.fromJson(jsonData, ForecastResponse.class);

        if (forecastResponse != null && forecastResponse.getList() != null) {
            forecastAdapter = new ForecastAdapter(forecastResponse.getList(), listener);
            forecastRecyclerView.setAdapter(forecastAdapter);
        } else {
            Toast.makeText(requireContext(), "Predpoveď nie je k dispozícii", Toast.LENGTH_SHORT).show();
        }
    }

}