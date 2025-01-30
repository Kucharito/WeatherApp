package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastResponse.ListItem> forecastList;

    public ForecastAdapter(List<ForecastResponse.ListItem> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.ListItem item = forecastList.get(position);
        holder.dateTextView.setText(item.getDtTxt());
        holder.tempTextView.setText(String.format("%.1f°C", item.getMain().getTemp()));
        holder.descTextView.setText(item.getWeather().get(0).getDescription());
        String iconCode = item.getWeather().get(0).getIcon();
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

        Glide.with(holder.itemView.getContext())
                .load(iconUrl)
                .into(holder.iconImageView);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, tempTextView, descTextView;
        ImageView iconImageView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

        }
    }
}
