package com.in.den.android.openweather.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.Location;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class WeatherDetailFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private Location location;

    @BindView(R.id.list_detail)
    RecyclerView recyclerView;
    @BindView(R.id.textview_cityname)
    TextView tvCityname;
    @BindView(R.id.textview_cityalias)
    TextView tvCityalias;
    @BindView(R.id.textview_country)
    TextView tvCountry;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherDetailFragment() {

    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("location", (Serializable) location);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WeatherDetailFragment newInstance(int columnCount) {
        WeatherDetailFragment fragment = new WeatherDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            location = (Location)savedInstanceState.getSerializable("location");
        }

        tvCityname.setText(location.getName());
        tvCountry.setText(location.getCountry());

        String alias= location.getAlias(); //some alias is written as "name,counrycode
        String[] tabalias = alias.split(",");
        String al = tabalias[0];
        if(!location.getName().equalsIgnoreCase(al)) {
            tvCityalias.setText(alias);
            tvCityalias.setVisibility(View.VISIBLE);
        }

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mColumnCount));
        }
        recyclerView.setAdapter(new WeatherDetailRVAdapter(location.getLocationid()));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weatherdetail_fragment_list, container, false);

        ButterKnife.bind(this, view);

        return view;

    }


    //solution to reset title proposed
    //http://stackoverflow.com/questions/13472258/handling-actionbar-title-with-the-fragment-back-stack

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        ((MainActivity)getActivity()).toolbar
                .setTitle(R.string.oneweek);
    }

    /*

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            // Set title
            getActivity().getActionBar()
                    .setTitle(R.string.oneweek);
        }
    }*/

    @OnClick(R.id.fab)
    void showMap() {
        mListener.showMap(location.getName(), location.getCoord_lat(), location.getCoord_lon());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}


