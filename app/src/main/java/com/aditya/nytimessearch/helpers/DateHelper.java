package com.aditya.nytimessearch.helpers;

import android.content.Context;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by amodi on 3/18/17.
 */

public class DateHelper {

    public static String getMediumFormatDate(Context context, Date date) {
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(
            context.getApplicationContext());
        return dateFormat.format(date);
    }

    public static Date getDateFromLong(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        return c.getTime();
    }

    @Nullable
    public static String getFormattedDate(@Nullable Date date) {
        if (date == null) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }
}
