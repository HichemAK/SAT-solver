package com.satsolver;

import java.util.ArrayList;

public class Utils {
    public static double mean(double[] t){
        double mean=0;
        for(double d : t){
            mean += d;
        }
        return mean/t.length;
    }

    public static double mean(ArrayList<Double> t){
        double mean=0;
        for(Double d : t){
            mean += d;
        }
        return mean/t.size();
    }

    public static double mean2(ArrayList<Integer> t){
        double mean=0;
        for(Integer d : t){
            mean += d;
        }
        return mean/t.size();
    }

    public static String arrayToString(double[] array){
        StringBuffer str = new StringBuffer("[");
        for(int i=0;i<array.length;i++){
            str.append(array[i]);
            str.append(" ");
        }
        str.append("]");
        return str.toString();
    }

    public static double variance(double[] t, double mean){
        double variance = 0;
        for(int i=0;i<t.length;i++){
            variance += (t[i] - mean)*(t[i] - mean);
        }
        variance /= t.length;
        return variance;
    }

    public static double variance(ArrayList<Double> t, double mean){
        double variance = 0;
        for(int i=0;i<t.size();i++){
            variance += (t.get(i) - mean)*(t.get(i) - mean);
        }
        variance /= t.size();
        return variance;
    }
}
