package nl.jouwidealestudententijd.tokompas;

/**
 * Created by buijn on 17/08/2017.
 */

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class LocationService implements LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private MainActivity context;
    private CompassService mCompassService;
    public Location userLocation;

    public LocationService(MainActivity context, GoogleApiClient client) {
        this.mGoogleApiClient = client;
        this.context = context;
    }

    public float getBearingToLocation(Location destination) {
        if(userLocation != null) {
            return userLocation.bearingTo(destination);
        }
        return 0f;
    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
    }
}

