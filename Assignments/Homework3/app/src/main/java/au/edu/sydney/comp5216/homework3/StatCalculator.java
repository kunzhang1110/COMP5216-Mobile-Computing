package au.edu.sydney.comp5216.homework3;

import java.util.concurrent.TimeUnit;

public class StatCalculator {

    private float distance; //in meters
    private float time; //in minutes

    StatCalculator(float _distance, float _time){
        this.distance = _distance;
        this.time = _time;
    }


    /**
     *
     * @param _distance in km
     * @param _time in min
     * @return
     */
    protected static float getSpeed(float _distance, float _time){
        return _distance/(_time/60);
    }

    protected static float getPace(float _distance, float _time){
        return _time/_distance;
    }

    protected static String getTimeStringFromMillis(Long _millis){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(_millis),
                TimeUnit.MILLISECONDS.toMinutes(_millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(_millis) % TimeUnit.MINUTES.toSeconds(1));
    }

}
