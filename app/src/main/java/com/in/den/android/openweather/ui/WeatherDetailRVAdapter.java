package com.in.den.android.openweather.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.Weather;

import com.in.den.android.openweather.util.SunshineUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherDetailRVAdapter extends RecyclerView.Adapter<WeatherDetailRVAdapter.ViewHolder> {

    private final List<Weather> mValues;


    public WeatherDetailRVAdapter(List<Weather> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weatherdetail_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Weather w = mValues.get(position);

        holder.mItem = w;

        SunshineUtil.DateStringHolder dh = SunshineUtil.formatLongToDate(w.getDt(), Locale.getDefault());

        holder.tvDateDay.setText(dh.day);
        holder.tvDateMonth.setText(dh.month);
        holder.tvDateWeek.setText(dh.dayofweek);

        holder.tvMatinTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_morning(), true));
        holder.tvJourTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_day(), true));
        holder.tvSoirTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_eve(), true));
        holder.tvNuitTemp.setText(SunshineUtil.freindlyformatTemperature(w.getTemp_night(), true));

        holder.tvHumiditeVal.setText(w.getHumidity().toString());
        holder.tvPressionVal.setText(w.getPressure().toString());
        holder.tvVentVal.setText(w.getSpeed().toString());
        holder.tvDegreeVal.setText(w.getDeg().toString());

        holder.ivWeather.setImageResource(SunshineUtil.
                getArtResourceForWeatherCondition(w.getWeather_id()));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        @BindView(R.id.textView_date_day)
        TextView tvDateDay;
        @BindView(R.id.textView_date_month)
        TextView tvDateMonth;
        @BindView(R.id.textView_date_week)
        TextView tvDateWeek;
        @BindView(R.id.textView_morning_tmp)
        TextView tvMatinTemp;
        @BindView(R.id.textView_jour_tmp)
        TextView tvJourTemp;
        @BindView(R.id.textView_soir_tmp)
        TextView tvSoirTemp;
        @BindView(R.id.textView_nuit_tmp)
        TextView tvNuitTemp;
        @BindView(R.id.imageView_weather)
        ImageView ivWeather;


        @BindView(R.id.separater)
        View viewSeparater;
        @BindView(R.id.textView_humidite)
        TextView tvHumidite;
        @BindView(R.id.textView_pression)
        TextView tvPression;
        @BindView(R.id.textView_vent)
        TextView tvVent;
        @BindView(R.id.textView_degree)
        TextView tvDegree;
        @BindView(R.id.textView_humidite_v)
        TextView tvHumiditeVal;
        @BindView(R.id.textView_pression_v)
        TextView tvPressionVal;
        @BindView(R.id.textView_vent_v)
        TextView tvVentVal;
        @BindView(R.id.textView_degree_v)
        TextView tvDegreeVal;


        public Weather mItem;

        private List<View> detailview = new ArrayList<View>();

        public ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);

            detailview.add(viewSeparater);
            detailview.add(tvHumidite);
            detailview.add(tvHumiditeVal);
            detailview.add(tvPression);
            detailview.add(tvPressionVal);
            detailview.add(tvVent);
            detailview.add(tvVentVal);
            detailview.add(tvDegree);
            detailview.add(tvDegreeVal);

            hideDetail();

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchDetail();
                }
            });
        }

        public void showDetail() {
            for(int i = 0; i < detailview.size(); i++) {
                detailview.get(i).setVisibility(View.VISIBLE);
            }
        }

        public void hideDetail() {
            for(int i = 0; i < detailview.size(); i++) {
                detailview.get(i).setVisibility(View.GONE);
            }
        }

        public void switchDetail() {
            if(detailview.get(0).getVisibility()==View.VISIBLE) {
                hideDetail();
            }
            else {
                showDetail();
            }
        }
    }
}
