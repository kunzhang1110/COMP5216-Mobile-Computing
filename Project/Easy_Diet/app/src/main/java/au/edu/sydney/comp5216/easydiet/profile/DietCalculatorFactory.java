package au.edu.sydney.comp5216.easydiet.profile;

import android.util.Log;

public class DietCalculatorFactory {

    public static DietCalculator getCalulator(User _user,int dietChoice){
        if(dietChoice == 0){
            DietCalculator o = new HighProteinCalculator(_user);
            Log.i("xxx", "HighProteinCalculator:" + o.toString());
            return new HighProteinCalculator(_user);
        } else if (dietChoice == 1){
            return new HighFatCalculator(_user);
        }
        return null;
    }


}
