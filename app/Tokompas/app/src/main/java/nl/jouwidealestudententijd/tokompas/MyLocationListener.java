package nl.jouwidealestudententijd.tokompas;


import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;


/*---------- Listener class to get coordinates ------------- */
public class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        MainActivity.userLocation = loc;
    }
}