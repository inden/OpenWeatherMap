package com.in.den.android.openweather.ui;

/**
 * Created by harumi on 08/03/2017.
 */

import com.in.den.android.openweather.db.Location;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnListFragmentInteractionListener {

    void onListFragmentInteraction(Location item);

    void showMap(String name, Double coord_lat, Double coord_lon);
}
