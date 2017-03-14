package com.in.den.android.openweather.util;

import com.in.den.android.openweather.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by harumi on 03/11/2016.
 */

public class SunshineUtil {

    public static final String DEGREE  = "\u00b0";

    public static String getFormetedTemperature(double temp) {
        return temp + " " + DEGREE;
    }


   /*
   @ldate date represented in seconds
    */
    public static DateStringHolder formatLongToDate(long ldate, Locale locale) {

        Date d = new Date(ldate * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd-EEE", locale);
        String datestring = sdf.format(d);
        String[] datedata = datestring.split("-");
        DateStringHolder dsh = new DateStringHolder();
        dsh.year = datedata[0];
        dsh.month = datedata[1];
        dsh.day = datedata[2];
        dsh.dayofweek = datedata[3];
        return dsh;
    }

    public static class DateStringHolder {
        public String dayofweek;
        public String year;
        public String day;
        public String month;

    }

    public static String formatTemperature(double temperature) {

        return getFormetedTemperature(temperature);
    }

    public static String freindlyformatTemperature(double temperature, boolean bdegree) {
        String s = String.valueOf(round(temperature, 1));
        if(bdegree) {
            s = s + " " + DEGREE;
        }
        return s;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }
}
