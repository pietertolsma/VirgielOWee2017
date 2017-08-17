package nl.virgiel.virgielowee;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by pietertolsma on 5/24/17.
 */

public class CompassService implements SensorEventListener {

    private MainActivity context;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float mCurrentDegree = 0f;
    private CompassFragment mCompassFragment;
    private LocationService mLocationService;
    private Location virgielLocation = new Location("");

    private float[] mGravity;
    private float[] mGeomagnatic;

    float ALPHA = 10f;

    public CompassService(MainActivity activity, CompassFragment compassFragment, GoogleApiClient client) {
        context = activity;
        mLocationService = new LocationService(activity, client);
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mCompassFragment = compassFragment;
        virgielLocation.setLatitude(52.0077);
        virgielLocation.setLongitude(4.3588);
        onResume();
    }


    public float getCurrentDegree() {
        return mCurrentDegree;
    }

    protected void onResume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//        float oldAzi = mCurrentDegree;
//        if (event.sensor == mAccelerometer) {
//            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
//            mLastAccelerometerSet = true;
//        } else if (event.sensor == mMagnetometer) {
//            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
//            mLastMagnetometerSet = true;
//        }
//        if (mLastAccelerometerSet && mLastMagnetometerSet) {
//            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
//            SensorManager.getOrientation(mR, mOrientation);
//            float azimuthInRadians = mOrientation[0];
//            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
//            mCurrentDegree = -azimuthInDegress;
//        }
//        //if (Math.abs(oldAzi - mCurrentDegree) > 4) context.getLocationService().update();
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
//            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mGravity = lowPass(event.values.clone(), mGravity);
        } else if (event.sensor == mMagnetometer) {
//            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mGeomagnatic = lowPass(event.values.clone(), mGeomagnatic);
        }
        if (mGravity != null && mGeomagnatic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnatic);
            if(success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

                float bearingToLocation = (float) ((float) mLocationService.getBearingToLocation(virgielLocation)*180/Math.PI);

                float newDirection = azimuthInDegress - bearingToLocation;
                mCompassFragment.testHolder1.setText(Float.toString((float) (newDirection)));


                mGravity = null;
                mGeomagnatic = null;

                RotateAnimation ra = new RotateAnimation(mCurrentDegree, newDirection, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                ra.setDuration(250);
                ra.setFillAfter(true);

                mCompassFragment.mArrowImageView.startAnimation(ra);

                mCurrentDegree = -newDirection;
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
