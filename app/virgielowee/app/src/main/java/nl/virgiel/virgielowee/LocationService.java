package nl.virgiel.virgielowee;

import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

/**
 * Created by pietertolsma on 5/24/17.
 */

public class LocationService implements LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private MainActivity context;

    private CompassFragment mCompassFragment;
    private CompassService mCompassService;

    private Location TOKO = new Location("");
    private Location userLocation = new Location("");

    public LocationService(MainActivity context, GoogleApiClient client) {
        this.mGoogleApiClient = client;
        this.context = context;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public float getBearingToLocation(Location destination) {
        return getUserLocation().bearingTo(destination);
    }

    /**
     * Credit to https://stackoverflow.com/questions/5479753/using-orientation-sensor-to-point-towards-a-specific-location
     * and http://www.techrepublic.com/article/pro-tip-create-your-own-magnetic-compass-using-androids-internal-sensors/
     * **/
//    public float getAngle() {
////        float currentAngle = -mCompassService.getCurrentDegree();
////        Location currentLoc = getLastLocation();
////        if (currentLoc == null) return 0.0f;
////        GeomagneticField geoField = new GeomagneticField(
////                (float) currentLoc.getLatitude(),
////                (float) currentLoc.getLongitude(),
////                (float) currentLoc.getAltitude(),
////                System.currentTimeMillis());
////        currentAngle += geoField.getDeclination();
////        float bearing = currentLoc.bearingTo(getToko());
////        float direction = currentAngle - bearing;
////
////        if (direction > 180.0f) return - 180.0f + direction - 180.0f;
////
////        if (direction > 360.0f) return direction - 360.0f;
////
//        return -1 * mCompassService.getCurrentDegree();
//    }

//    public Location getLastLocation() {
//        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( context, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
//                    1);
//        }
//
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            return mLastLocation;
//        }
//        return null;
//    }

    @Override
    public void onLocationChanged(Location location) {
        userLocation = location;
}
}
