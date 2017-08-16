package nl.virgiel.virgielowee;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by pietertolsma on 5/24/17.
 */

public class CompassService implements SensorEventListener {

    private MainActivity context;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    public CompassService(MainActivity activity) {
        context = activity;
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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

    @Override
    public void onSensorChanged(SensorEvent event) {

        float oldAzi = mCurrentDegree;
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            mCurrentDegree = -azimuthInDegress;
        }
        //if (Math.abs(oldAzi - mCurrentDegree) > 4) context.getLocationService().update();
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
