package comp5216.sydney.edu.au.mortgage;

import java.text.DecimalFormat;

public class Mortgage {
    public final DecimalFormat MONEY = new DecimalFormat("$#,##0.00");

    private float amount;
    private int years;
    private float rate;

    public Mortgage(){
        setAmount(100000.0f);
        setRate(0.035f);
        setYears(30);
    }

    public void setAmount(float newAmount){
        if (newAmount>=0) amount = newAmount;
    }

    public void setYears(int newYears){
        if (newYears>=0) years = newYears;
    }

    public void setRate(float newRate){
        if (newRate>=0) rate = newRate;
    }

    public float getAmount(){
        return amount;
    }

    public int getYears(){
        return years;
    }

    public float getRate(){
        return rate;
    }

    public String getFormattedAmount(){
        return MONEY.format(amount);
    }

    public float monthlyPayment() {
        float mRate = rate/12;
        double temp = Math.pow(1/(1+mRate), years*12);
        return mRate*amount /(float)(1-temp);
    }

    public String formattedMonthlyPayment(){
        return MONEY.format(monthlyPayment());
    }

    public float totalPayment(){
        return monthlyPayment()*years*12;
    }

    public String formattedTotalPayment(){
        return MONEY.format(totalPayment());
    }
}
