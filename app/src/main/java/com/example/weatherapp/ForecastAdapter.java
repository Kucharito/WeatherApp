package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.ListItem item = forecastList.get(position);
        String formattedDate = formatDateTime(item.getDtTxt(), holder.itemView.getContext());
        Context context= holder.itemView.getContext();
        holder.dateTextView.setText(formattedDate);
        holder.tempTextView.setText(String.format("%.1f°C", item.getMain().getTemp()));
        holder.descTextView.setText((item.getWeather().get(0).getDescription()).substring(0, 1).toUpperCase() + (item.getWeather().get(0).getDescription()).substring(1));
        holder.feelsLikeTextView.setText(context.getString(R.string.feels_like_label,item.getMain().getTempFeelsLike()));
        holder.humidityTextView.setText(context.getString(R.string.humidity_label, item.getMain().getHumidity()));
        holder.windTextView.setText(context.getString(R.string.wind_speed_label, (item.getWind().getSpeed())*3.6));
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
        TextView dateTextView, tempTextView, descTextView, feelsLikeTextView, humidityTextView, windTextView;
        ImageView iconImageView;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
            descTextView = itemView.findViewById(R.id.descTextView);
            feelsLikeTextView = itemView.findViewById(R.id.feelsLikeTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
            windTextView = itemView.findViewById(R.id.windTextView);
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

    private String formatDateTime(String dtTxt, Context context) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE HH:mm (dd.MM.yyyy)", Locale.getDefault());

        try{
            Date date = inputFormat.parse(dtTxt);
            return outputFormat.format(date);
        }
        catch(Exception e) {
            e.printStackTrace();
            return dtTxt;
        }
    }
}