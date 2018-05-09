package com.mobile.urbanfix.urban_fix;

import android.support.annotation.DrawableRes;

import java.util.HashMap;
import java.util.Map;

public class Sentiments {
    private static Map<SENTIMENTS, Integer> sentiment = new HashMap<>();
    static{
        sentiment.put(SENTIMENTS.SATISFIED, R.drawable.sentiment_satisfied);
        sentiment.put(SENTIMENTS.NEUTRAL, R.drawable.sentiment_neutral);
        sentiment.put(SENTIMENTS.DISSATISFIED, R.drawable.sentiment_dissatisfied);
    }

    @DrawableRes
    public static int getId (SENTIMENTS face){
        return sentiment.get(face);
    }

    public enum SENTIMENTS {SATISFIED, NEUTRAL, DISSATISFIED};
}
