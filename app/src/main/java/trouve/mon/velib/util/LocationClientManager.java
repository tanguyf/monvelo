package trouve.mon.velib.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import trouve.mon.velib.R;
import trouve.mon.velib.station.GoogleMapFragment;
import trouve.mon.velib.station.Station;

public class LocationClientManager {

    //----------------- Static methods ------------------

    private static GoogleApiClient googleApiClient;
    private static FragmentManager fragmentManager;


    //----------------- Instance fields ------------------
    private static LocationRequest locationRequest;

    private LocationClientManager() {

    }

    public static GoogleApiClient setUp(Context c, FragmentManager fm) {
        fragmentManager = fm;
        googleApiClient = new GoogleApiClient.Builder(c)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new Listener())
                .addOnConnectionFailedListener(new Listener())
                .build();
        return googleApiClient;
    }

    public static GoogleApiClient getClient() {
        return googleApiClient;
    }

    //----------------- Instance methods ------------------

    public static int distanceFromLastLocation(Station station) {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                float result = lastLocation.distanceTo(station.getLocation());
                int d = Math.round(result);
                station.setCacheDistanceFromLocation(d);
                return d;
            }
        }
        return 0;
    }


    //----------------- Interface Implementation ------------------

    public static class Listener implements LocationListener,
            ConnectionCallbacks,
            OnConnectionFailedListener {

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Helper.showMessageLong(R.string.msg_no_gps);
        }

        @Override
        public void onConnected(Bundle connectionHint) {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000); // Update location every 5 seconds

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);

            Fragment fragment = fragmentManager.findFragmentByTag(GoogleMapFragment.FRAGMENT_TAG);
            if (fragment != null) {
                GoogleMapFragment mapFragment = (GoogleMapFragment) fragment;
                mapFragment.centerMapOnMyLocation(false);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onLocationChanged(Location location) {
        }
    }
}
