package com.in.den.android.openweather.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.Location;
import com.in.den.android.openweather.db.Weather;

import com.in.den.android.openweather.util.SunshineUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherLocationRVAdapter extends RecyclerView.Adapter<WeatherLocationRVAdapter.ViewHolder> {

    private final List<Location> mValues;
    private final OnListFragmentInteractionListener mListener;


    public WeatherLocationRVAdapter(List<Location> items, OnListFragmentInteractionListener listener) {
        super();
        mValues = items;
        mListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weatherlocation_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Location loc = mValues.get(position);
        List<Weather> weathers = loc.getLocationid();
        Weather w = weathers.get(0);

        holder.mItem = loc;
        holder.tvLocation.setText(loc.getName());
        holder.tvMaxTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_max(), false));
        holder.tvMinTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_min(),false));
        holder.ivWeather.setImageResource(SunshineUtil.
                getArtResourceForWeatherCondition(w.getWeather_id()));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Location mItem;

        @BindView(R.id.textViewLocation)
        TextView tvLocation;
        @BindView(R.id.imageViewWeather)
        ImageView ivWeather;
        @BindView(R.id.textViewMinTemp)
        TextView tvMinTemp;
        @BindView(R.id.textViewMaxTemp)
        TextView tvMaxTemp;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(mItem);
                    }
                }
            });
        }
    }
}
