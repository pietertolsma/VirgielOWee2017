package nl.virgiel.virgielowee;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompassFragment extends Fragment {

    private GoogleApiClient mGoogleApiClient;

    private ImageView mArrowImageView;

    private float currentAngle = 0.0f;

    public CompassFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        mArrowImageView = (ImageView) view.findViewById(R.id.arrow);

        this.getActivity().findViewById(R.id.compass_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity main = (MainActivity) getActivity();
                Location loc = main.getLocationService().getLastLocation();
                updateArrow(main.getLocationService().getAngle());
                Toast.makeText(main, Float.toString(main.getLocationService().getAngle()) + " Degree to target", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void updateArrow(float degree) {
        float newAngle = -degree - 45.0f;

        if (newAngle < 0) newAngle += 360.0f;

        RotateAnimation ra = new RotateAnimation(
                currentAngle,
                newAngle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAngle = newAngle;
        ra.setDuration(250);

        ra.setFillAfter(true);

        mArrowImageView.startAnimation(ra);
    }

}
