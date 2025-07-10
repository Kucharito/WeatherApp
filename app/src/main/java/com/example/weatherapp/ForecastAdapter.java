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
    private OnForecastItemClickListener listener;
    public ForecastAdapter(List<ForecastResponse.ListItem> forecastList, OnForecastItemClickListener listener) {
        this.forecastList = forecastList;
        this.listener = listener;
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
        int iconResId = holder.itemView.getContext().getResources().getIdentifier("ic_" + iconCode, "drawable", holder.itemView.getContext().getPackageName());
        if(iconResId != 0) {
            holder.iconImageView.setImageResource(iconResId);
        } else {
            holder.iconImageView.setImageResource(R.drawable.ic_01d); // Default icon if not found
        }


        //String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

        /*Glide.with(holder.itemView.getContext())
                .load(iconUrl)
                .into(holder.iconImageView);*/
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }


    class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, tempTextView, descTextView;
        ImageView iconImageView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onForecastItemClick(forecastList.get(position));
                    }
                }
            });
        }
    }
}