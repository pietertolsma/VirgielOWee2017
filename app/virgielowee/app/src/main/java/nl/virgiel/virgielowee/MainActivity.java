package nl.virgiel.virgielowee;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationService mLocationService;
    private CompassFragment mCompassFragment;
    private CountdownFragment mCountdownFragment;

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mCompassFragment = new CompassFragment();
        mCountdownFragment = new CountdownFragment();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setContentView(R.layout.activity_main);

        mLocationService = new LocationService(this, mGoogleApiClient, mCompassFragment);

        Location loc = mLocationService.getLastLocation();

        if (findViewById(R.id.compass_container) != null && findViewById(R.id.countdown_container) != null) {
            if (savedInstanceState != null) return;
            getSupportFragmentManager().beginTransaction().add(R.id.compass_container, mCompassFragment)
                    .add(R.id.countdown_container, mCountdownFragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public LocationService getLocationService() {
        return mLocationService;
    }

}