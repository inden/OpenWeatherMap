package com.in.den.android.openweather.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.util.LocationInfo;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by harumi on 05/03/2017.
 */

public class LocationsArrayAdapter extends ArrayAdapter<LocationInfo> {

    static Callback callback;
    //when used the instance from inject, images didn't appear. So I replaced
    //it from an instance created in the class.
    // @Inject
    LayoutInflater layoutInflater;

    interface Callback {
        void deleteLocation(String location);
    }

    public LocationsArrayAdapter(Context context, int resource, List<LocationInfo> list, Callback callback) {
        super(context, resource, list);

        this.callback = callback;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.loc_setting_listitem, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationInfo info = (LocationInfo) getItem(position);
        holder.tvLocation.setText(firstLetterUpper(info.getName()));
        if (!info.isBfound()) {
            holder.imgNotFound.setVisibility(View.VISIBLE);
        } else {
            holder.imgNotFound.setVisibility(View.GONE);

        }

        return convertView;
    }

    static String firstLetterUpper(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    static class ViewHolder {
        @BindView(R.id.imgNotFound)
        ImageView imgNotFound;
        @BindView(R.id.tv_location)
        TextView tvLocation;
        @BindView(R.id.btn_delete)
        ImageButton btnDelete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.btn_delete)
        public void deleteLocation() {
            String location = tvLocation.getText().toString();
            callback.deleteLocation(location);
        }
    }
}
