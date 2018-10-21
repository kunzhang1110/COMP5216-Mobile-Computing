package au.edu.sydney.comp5216.easydiet.profile;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    // User Access Information
    private int userID;
    private String name;
    private String userName;
    private String password;

    // User Physical Information
    private int age;
    private int height;
    private double weight;
    private int pal;   //Physical Activity Level (PAL) 0~4
    private String gender; //"MALE" or "FEMALE"

    // User Calculation Input Information NULLABLE
    private int dietChoice;
    private double targetWeight;
    private int targetDuration;

    // User Calculation Output Information NULLABLE
    private double dailyCarboTargetHi;
    private double dailyCarboTargetLo;
    private double dailyProteinTargetHi;
    private double dailyProteinTargetLo;
    private double dailyFatTargetHi;
    private double dailyFatTargetLo;
    private double dailyCalorieTarget;

    public User(int userID, String name, String userName, String password,
                int age, int height, double weight, int pal, String gender,
                int dietChoice, double targetWeight, int targetDuration,
                double dailyCarboTargetHi, double dailyCarboTargetLo, double dailyProteinTargetHi, double dailyProteinTargetLo,
                double dailyFatTargetHi, double dailyFatTargetLo, double dailyCalorieTarget) {
        this.userID = userID;
        this.name = name;
        this.userName = userName;
        this.password = password;

        this.age = age;
        this.height = height;
        this.weight = weight;
        this.pal = pal;
        this.gender = gender;

        this.dietChoice = dietChoice;
        this.targetWeight = targetWeight;
        this.targetDuration = targetDuration;

        this.dailyCarboTargetHi = dailyCarboTargetHi;
        this.dailyCarboTargetLo = dailyCarboTargetLo;
        this.dailyProteinTargetHi = dailyProteinTargetHi;
        this.dailyProteinTargetLo = dailyProteinTargetLo;
        this.dailyFatTargetHi = dailyFatTargetHi;
        this.dailyFatTargetLo = dailyFatTargetLo;
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPal() {
        return pal;
    }

    public void setPal(int pal) {
        this.pal = pal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getDietChoice() {
        return dietChoice;
    }

    public void setDietChoice(int dietChoice) {
        this.dietChoice = dietChoice;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public int getTargetDuration() {
        return targetDuration;
    }

    public void setTargetDuration(int targetDuration) {
        this.targetDuration = targetDuration;
    }

    public double getDailyCarboTargetHi() {
        return dailyCarboTargetHi;
    }

    public void setDailyCarboTargetHi(double dailyCarboTargetHi) {
        this.dailyCarboTargetHi = dailyCarboTargetHi;
    }

    public double getDailyCarboTargetLo() {
        return dailyCarboTargetLo;
    }

    public void setDailyCarboTargetLo(double dailyCarboTargetLo) {
        this.dailyCarboTargetLo = dailyCarboTargetLo;
    }

    public double getDailyProteinTargetHi() {
        return dailyProteinTargetHi;
    }

    public void setDailyProteinTargetHi(double dailyProteinTargetHi) {
        this.dailyProteinTargetHi = dailyProteinTargetHi;
    }

    public double getDailyProteinTargetLo() {
        return dailyProteinTargetLo;
    }

    public void setDailyProteinTargetLo(double dailyProteinTargetLo) {
        this.dailyProteinTargetLo = dailyProteinTargetLo;
    }

    public double getDailyFatTargetHi() {
        return dailyFatTargetHi;
    }

    public void setDailyFatTargetHi(double dailyFatTargetHi) {
        this.dailyFatTargetHi = dailyFatTargetHi;
    }

    public double getDailyFatTargetLo() {
        return dailyFatTargetLo;
    }

    public void setDailyFatTargetLo(double dailyFatTargetLo) {
        this.dailyFatTargetLo = dailyFatTargetLo;
    }

    public double getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(double dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", pal=" + pal +
                ", gender='" + gender + '\'' +
                ", dietChoice=" + dietChoice +
                ", targetWeight=" + targetWeight +
                ", targetDuration=" + targetDuration +
                ", dailyCarboTargetHi=" + dailyCarboTargetHi +
                ", dailyCarboTargetLo=" + dailyCarboTargetLo +
                ", dailyProteinTargetHi=" + dailyProteinTargetHi +
                ", dailyProteinTargetLo=" + dailyProteinTargetLo +
                ", dailyFatTargetHi=" + dailyFatTargetHi +
                ", dailyFatTargetLo=" + dailyFatTargetLo +
                ", dailyCalorieTarget=" + dailyCalorieTarget +
                '}';
    }
}
