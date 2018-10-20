package au.edu.sydney.comp5216.easydiet;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import au.edu.sydney.comp5216.easydiet.profile.DietCalculator;
import au.edu.sydney.comp5216.easydiet.profile.DietCalculatorFactory;
import au.edu.sydney.comp5216.easydiet.profile.HighProteinCalculator;
import au.edu.sydney.comp5216.easydiet.profile.User;

import static org.junit.Assert.assertEquals;

public class HighProteinDietCalculatorTest {
    User user;
    HighProteinCalculator highProteinDietCalculator;

    @Before
    public void setUp() throws Exception {
        user = new User(28, "a", "a", "a", 20, 173, 60, 5, "MALE",
  0, 58, 5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        highProteinDietCalculator = new HighProteinCalculator(user);
        System.out.print("here");
        System.out.print("here" + highProteinDietCalculator.toString());
    }


//    @Test
//    public void calculateBMR() {
//        double bmr = highProteinDietCalculator.calculateBMR();
//        assertEquals(bmr, 3000, 2000);
//        System.out.print(bmr);
//    }
//
//    @Test
//    public void calculateTotalCalorieDeficit() {
//        double totalCalorieDeficit = highProteinDietCalculator.calculateTotalCalorieDeficit();
//        System.out.print(totalCalorieDeficit);
//    }

    @Test
    public void calculateDailyCalorieTarget() {
        double dailyCalorieTarget = highProteinDietCalculator.calculateDailyCalorieTarget();
        System.out.print("Daily Calories: " +dailyCalorieTarget);
    }

    @Test
    public void calculateDailyCarboTarget() {
        ArrayList<Double> dailyCarboTargetRange = highProteinDietCalculator.calculateDailyCarboTarget();
        System.out.print("lower: " + dailyCarboTargetRange.get(0) + " | upper: " + dailyCarboTargetRange.get(1));
    }

}