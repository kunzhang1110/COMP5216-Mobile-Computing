package au.edu.sydney.comp5216.easydiet.profile;

import android.util.Log;

import java.util.ArrayList;

public class HighProteinCalculator extends DietCalculator {


    public HighProteinCalculator(User _user){
        super(_user);
        this.dailyCalorieTarget = calculateDailyCalorieTarget();
        this.dailyCarboTargetRange = calculateDailyCarboTarget();
        this.dailyProteinTargetRange = calculateDailyProteinTarget();
        this.dailyFatTargetRange = calculateDailyFatTarget();
        Log.i("xxxHighProtein", "_user" + _user.getTargetWeight() + _user.getWeight());
    }

    public ArrayList<Double> calculateDailyProteinTarget(){

        double weight = user.getWeight();
        ArrayList<Double> dailyProteinTargetRange = new ArrayList<>();

        double dailyProteinLower = 1.6 * weight;
        double dailyProteinUpper = 2.0 * weight;

        dailyProteinTargetRange.add(dailyProteinLower);
        dailyProteinTargetRange.add(dailyProteinUpper);

        return dailyProteinTargetRange;
    }

    public ArrayList<Double> calculateDailyCarboTarget(){

        ArrayList<Double> dailyCarboTargetRange = new ArrayList<>();

        double dailyCarboLower = 0.0;
        double dailyCarboUpper = (calculateDailyCalorieTarget() * 0.20)/4;

        dailyCarboTargetRange.add(dailyCarboLower);
        dailyCarboTargetRange.add(dailyCarboUpper);

        return dailyCarboTargetRange;
    }


    public ArrayList<Double> calculateDailyFatTarget(){
        ArrayList<Double> dailyDailyFatTargetRange = new ArrayList<>();
        dailyDailyFatTargetRange.add(0.0);
        dailyDailyFatTargetRange.add(0.0);
        return dailyDailyFatTargetRange;
    }

    @Override
    public String toString() {
        return "HighProteinCalculator{" +
                "user=" + user +
                ", dailyCarboTargetRange=" + dailyCarboTargetRange +
                ", dailyProteinTargetRange=" + dailyProteinTargetRange +
                ", dailyFatTargetRange=" + dailyFatTargetRange +
                ", dailyCalorieTarget=" + dailyCalorieTarget +
                ", dailyCalorieTarget=" + dailyCalorieTarget +
                '}';
    }


}
