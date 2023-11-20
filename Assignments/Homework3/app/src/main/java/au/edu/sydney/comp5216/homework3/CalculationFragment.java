package au.edu.sydney.comp5216.homework3;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class CalculationFragment extends Fragment{

    EditText etInputDistance;
    EditText etInputTime;
    TextView tvShowSpeed;
    TextView tvShowPace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_calculation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView();
        etInputDistance = v.findViewById(R.id.etInputDistance);
        etInputTime = v.findViewById(R.id.etInputTime);
        tvShowSpeed = v.findViewById(R.id.tvShowSpeed);
        tvShowPace = v.findViewById(R.id.tvShowPace);

        Button btnCalc = (Button) v.findViewById(R.id.btnCalc);
        Button btnClear = (Button) v.findViewById(R.id.btnClear);

        //TODO:validate input

        btnCalc.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int distance = Integer.parseInt(etInputDistance.getText().toString());  //in km
                int time =  Integer.parseInt(etInputTime.getText().toString()); //in min

                float speed = StatCalculator.getSpeed(distance, time);  // in km/hr
                float pace =  StatCalculator.getPace(distance,time);   // in min./km

                tvShowSpeed.setText(String.format(Locale.US,"%.2f", speed) + " km/hr");
                tvShowPace.setText(String.format(Locale.US, "%.2f", pace) + " min/km");
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                etInputDistance.setText("");
                etInputTime.setText("");
                tvShowSpeed.setText("");
                tvShowPace.setText("");
                etInputDistance.setHint(R.string.enter_distance);
                etInputTime.setHint(R.string.enter_duration);
            }
        });
    }


}
