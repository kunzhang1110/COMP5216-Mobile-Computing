package au.edu.sydney.comp5216.easydiet.profile;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DietCalculator {

    protected User user;
    protected ArrayList<Double> dailyCarboTargetRange;
    protected ArrayList<Double> dailyProteinTargetRange;
    protected ArrayList<Double> dailyFatTargetRange;
    protected double dailyCalorieTarget;

    // PAL values
    private static final LinkedHashMap<Integer, Double> palValueMap;
    static{
        palValueMap = new LinkedHashMap<>();
        palValueMap.put(0, 1.2); //Sedentary
        palValueMap.put(1, 1.375); //Mild
        palValueMap.put(2, 1.55); //Moderate
        palValueMap.put(3, 1.7); //Heavy
        palValueMap.put(4, 1.9); //Extreme
    }


    DietCalculator(User _user){
        this.user = _user;
    }   //dependency injection

    public double calculateBMR(){
        double weight = user.getWeight();
        double height = user.getHeight();
        double age = user.getAge();
        double bmr;
        String gender = user.getGender();
        int pal = user.getPal();

        // Implementing Harris–Benedict equations revised by Mifflin and St Jeor
        // weight in kg
        // height in cm
        // age in year

        bmr = 10 * weight + 6.25 * height - 5 * age;
        if(gender == "MALE"){
            bmr += 5;

        }else{
            bmr -= 161;
        }
        Log.i("dcCalculateBMR", "" + bmr*palValueMap.get(pal));
        return bmr*palValueMap.get(pal);
    }

    public double calculateTotalCalorieDeficit() {
        double bodyWeightLoss = user.getWeight() - user.getTargetWeight();  //
        double muscleLoss = bodyWeightLoss * 0.077;    // fat free mass accounts for 7.7% of total weight loss
        double fatLoss = bodyWeightLoss - muscleLoss;
        double totalCalorieDeficit = fatLoss* 7700 + muscleLoss * 840;
        Log.i("dcCalorieDeficit", "" + totalCalorieDeficit);
        return totalCalorieDeficit;
    }

    public double calculateDailyCalorieTarget(){
        int targetDuration = user.getTargetDuration();
        double dailyCalorieTarget = calculateBMR() - calculateTotalCalorieDeficit()/targetDuration;
        Log.i("dcCalorieTarget", "" + dailyCalorieTarget);
        return dailyCalorieTarget;
    }

    public abstract ArrayList<Double> calculateDailyProteinTarget();

    public abstract ArrayList<Double> calculateDailyCarboTarget();

    public abstract ArrayList<Double> calculateDailyFatTarget();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Double> getDailyCarboTargetRange() {
        return dailyCarboTargetRange;
    }

    public void setDailyCarboTargetRange(ArrayList<Double> dailyCarboTargetRange) {
        this.dailyCarboTargetRange = dailyCarboTargetRange;
    }

    public ArrayList<Double> getDailyProteinTargetRange() {
        return dailyProteinTargetRange;
    }

    public void setDailyProteinTargetRange(ArrayList<Double> dailyProteinTargetRange) {
        this.dailyProteinTargetRange = dailyProteinTargetRange;
    }

    public ArrayList<Double> getDailyFatTargetRange() {
        return dailyFatTargetRange;
    }

    public void setDailyFatTargetRange(ArrayList<Double> dailyFatTargetRange) {
        this.dailyFatTargetRange = dailyFatTargetRange;
    }

    public double getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(double dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    public static LinkedHashMap<Integer, Double> getPalValueMap() {
        return palValueMap;
    }



}
