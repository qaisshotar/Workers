package com.example.workers;

import android.text.format.DateFormat;

import java.sql.Date;
import java.util.Calendar;

public class Format {

    static  public String DateFormat(long MilliSecond){

        java.util.Date today = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        String dateString = null;
        String datetoday = DateFormat.format("dd", new Date(MilliSecond)).toString();

        if (Integer.parseInt(datetoday) == calendar.get(Calendar.DAY_OF_MONTH)) {
            dateString = DateFormat.format("HH:mm aa", new Date(MilliSecond)).toString();
        } else {
            dateString = DateFormat.format("dd MMM yyyy", new Date(MilliSecond)).toString();
        }
        return dateString;
    }
}
