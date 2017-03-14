package com.in.den.android.openweather.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.in.den.android.openweather.OWApp;
import com.in.den.android.openweather.R;
import com.in.den.android.openweather.db.DaoSession;
import com.in.den.android.openweather.db.Location;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class WeatherLocationFragment extends Fragment {

    private static final String TAG= "WeatherLocationFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.list_loc)
    RecyclerView recyclerView;

    @Inject
    DaoSession daoSession;

    private List<Location> locations;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherLocationFragment() {
    }

    /*
    public void setListLocation(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("locations", (Serializable) locations);
        if(locations == null) {
            Log.d(TAG, "onSaveInstanceState() location is null");
        }
    }*/



    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static WeatherLocationFragment newInstance(int columnCount) {
        WeatherLocationFragment fragment = new WeatherLocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        if (savedInstanceState != null) {
            locations = (List<Location>)savedInstanceState.getSerializable("locations");
            if(locations == null) {
                //this problem happens when back to the fragment from another one with multiple rotations
                //this is temporary solution to avoid null pointer exception
                Log.d(TAG, "onActivityCreated() lcoation null in savedinstancestate");
                //locations = new ArrayList<Location>();
                locations = ((MainActivity)getActivity()).locations;
            }
        }*/

        locations = ((MainActivity)getActivity()).locations;

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mColumnCount));
        }
        recyclerView.setAdapter(new WeatherLocationRVAdapter(locations, mListener));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((OWApp) getActivity().getApplication()).getApplicationComponent().inject(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weatherlocation_fragment_list, container, false);

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
                .setTitle(R.string.today);
    }

    /*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            // Set title
            getActivity().getActionBar()
                    .setTitle(R.string.today);
        }
    }*/


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
