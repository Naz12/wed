package com.teknosols.a3orrsy.other.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String diffForHumans(long time) {



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int currentyear = Calendar.getInstance().get(Calendar.YEAR);

        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm a");
            return "yesterday at " + timeformat.format(calendar.getTime());
        }else if(year == currentyear){
            SimpleDateFormat monthformat = new SimpleDateFormat("MMM dd hh:mm a");
            return "On " + monthformat.format(calendar.getTime());
        }
        else {
            SimpleDateFormat yearformat = new SimpleDateFormat("yyyy MMM dd hh:mm a");
            return "On " + yearformat.format(calendar.getTime());
        }
    }


    public static Date getDateCurrentTimeZone(String dateString) {


        try{
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(date.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone =  calendar.getTime();
            //return sdf.format(currenTimeZone);
//            return currenTimeZone;
            return date;
        }catch (Exception e) {
        }

        return null;
    }


}
