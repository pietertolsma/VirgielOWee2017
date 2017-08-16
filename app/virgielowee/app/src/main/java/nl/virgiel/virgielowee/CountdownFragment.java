package nl.virgiel.virgielowee;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountdownFragment extends Fragment {


    private Date preOweeDate, oweeDate;

    private TextView mCountDownTextView;
    private TextView mEventTextView;

    private Runnable updater;
    private final Handler timerHandler = new Handler();


    public CountdownFragment() {
        // Required empty public constructor
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 8, 14, 0, 0);
        preOweeDate = cal.getTime();
        cal.set(2017 + 1900, 8, 20, 0, 0);
        oweeDate = cal.getTime();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countdown, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        mCountDownTextView = (TextView) view.findViewById(R.id.countdown_text);
        mEventTextView = (TextView) view.findViewById(R.id.countdown_event_text);


        view.setBackgroundColor(Color.RED);
        updateTime();
    }

    private void updateTime() {
        updater = new Runnable() {
            @Override
            public void run() {

                Calendar thatDay = Calendar.getInstance();
                thatDay.setTime(preOweeDate);
                Calendar tod = Calendar.getInstance();

                long different = thatDay.getTimeInMillis() - tod.getTimeInMillis();
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;

                mCountDownTextView.setText(elapsedDays + "D " + elapsedHours + "H " + elapsedMinutes + "m " + elapsedSeconds + "s");


                timerHandler.postDelayed(updater,1000);
            }
        };
        timerHandler.post(updater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(updater);
    }

}
