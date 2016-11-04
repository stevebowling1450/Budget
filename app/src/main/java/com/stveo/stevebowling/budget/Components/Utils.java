package com.stveo.stevebowling.budget.Components;

import java.util.Locale;

/**
 * Created by stevebowling on 11/2/16.
 */

public class Utils {
    public static String  formatDouble(Double number){
        String prefix = number < 0 ? "-$" : "$";
        return prefix + String .format(Locale.US, "%.2f", Math.abs(number));
        //$100.00 or -$9.87
    }
}
