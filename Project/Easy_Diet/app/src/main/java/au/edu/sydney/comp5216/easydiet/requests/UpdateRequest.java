package au.edu.sydney.comp5216.easydiet.requests;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import au.edu.sydney.comp5216.easydiet.profile.User;

public class UpdateRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://covert-hips.000webhostapp.com/Update.php";
    private Map<String, String> params;

    public UpdateRequest(User _user, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        Log.i("xxx", "UpdateRequest" + String.valueOf(_user.getUserID()) + " : " + String.valueOf(_user.getPal()) );
        params.put("userId", String.valueOf(_user.getUserID()));
        params.put("pal", String.valueOf(_user.getPal()));

        params.put("dietChoice", String.valueOf(_user.getDietChoice()));
        params.put("targetWeight", String.valueOf(_user.getTargetWeight()));
        params.put("targetDuration", String.valueOf(_user.getTargetDuration()));

        params.put("dailyCarboTargetHi", String.valueOf(_user.getDailyCarboTargetHi()));
        params.put("dailyCarboTargetLo", String.valueOf(_user.getDailyCarboTargetLo()));
        params.put("dailyProteinTargetHi", String.valueOf(_user.getDailyProteinTargetHi()));
        params.put("dailyProteinTargetLo", String.valueOf(_user.getDailyProteinTargetLo()));
        params.put("dailyFatTargetHi", String.valueOf(_user.getDailyFatTargetHi()));
        params.put("dailyFatTargetLo", String.valueOf(_user.getDailyFatTargetLo()));
        params.put("dailyCalorieTarget", String.valueOf(_user.getDailyCalorieTarget()));

    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
