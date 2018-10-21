package au.edu.sydney.comp5216.easydiet.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://covert-hips.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String userName,
                           int age, String password,
                           double height, double weight, String gender, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("userName", userName);
        params.put("password", password);
        params.put("age", age + "");
        params.put("height", height + "");
        params.put("weight", weight + "");
        params.put("gender", gender + "");

    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
