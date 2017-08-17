package nl.jouwidealestudententijd.tokompas;

/**
 * Created by buijn on 17/08/2017.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import static android.content.Context.SENSOR_SERVICE;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CompassService implements SensorEventListener {

    private MainActivity context;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float mCurrentDegreeVirgiel = 0f;
    private float mCurrentDegreeHome = 0f;
    public LocationService mLocationService;
    private Location virgielLocation = new Location("");
    private double lastSin = 0f;
    private double lastCos = 0f;

    private float[] mGravity;
    private float[] mGeomagnetic;

    float ALPHA = 0.95f;

    public CompassService(MainActivity activity, GoogleApiClient client) {
        context = activity;
        mLocationService = new LocationService(activity, client);
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        virgielLocation.setLatitude(52.0077);
        virgielLocation.setLongitude(4.3588);
        onResume();
    }

    protected void onResume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
//            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mGravity = lowPass(event.values.clone(), mGravity);
        } else if (event.sensor == mMagnetometer) {
//            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mGeomagnetic = lowPass(event.values.clone(), mGeomagnetic);
        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if(success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

                float bearingToLocationVirgiel = (float) ((float) mLocationService.getBearingToLocation(virgielLocation)*180/Math.PI);
                if(MainActivity.homeLocation != null) {
                    float bearingToLocationHome = (float) ((float) mLocationService.getBearingToLocation(MainActivity.homeLocation)*180/Math.PI);
                    float newDirectionHome = azimuthInDegress - bearingToLocationHome;

                    RotateAnimation ra2 = new RotateAnimation(mCurrentDegreeHome, (float) smoothenAngle(newDirectionHome), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    ra2.setDuration(500);
                    ra2.setFillAfter(true);

                    MainActivity.mHomeArrowImageView.startAnimation(ra2);
                    mCurrentDegreeHome = -newDirectionHome;
                }

                float newDirectionVirgiel = azimuthInDegress - bearingToLocationVirgiel;

                mGravity = null;
                mGeomagnetic = null;

                RotateAnimation ra1 = new RotateAnimation(mCurrentDegreeVirgiel, (float) smoothenAngle(newDirectionVirgiel), Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                ra1.setDuration(500);
                ra1.setFillAfter(true);



                MainActivity.mVirgielArrowImageView.startAnimation(ra1);

                mCurrentDegreeVirgiel = -newDirectionVirgiel;
            }
        }
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    protected double smoothenAngle(float angle) {
        lastSin = ALPHA * lastSin + (1-ALPHA) * sin(angle);
        lastCos = ALPHA * lastCos + (1-ALPHA) * cos(angle);
        return atan2(lastSin, lastCos);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /**
         * SENSOR_STATUS_ACCURACY_HIGH = 3
         SENSOR_STATUS_ACCURACY_MEDIUM = 2
         SENSOR_STATUS_ACCURACY_LOW = 1
         SENSOR_STATUS_UNRELIABLE = 0
         */
        if (accuracy < 2) {
            //Accuracy too low..
            Toast.makeText(this.context, "Je kompas is niet accuraat! Probeer 8jes te maken met je mobiel...", Toast.LENGTH_SHORT);
        }
    }
}
