package models;

import java.util.Calendar;

public class TimeConv {
    public static String timeToStr(java.sql.Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        String month = "" + (cal.get(Calendar.MONTH) + 1);
        String year = "" + cal.get(Calendar.YEAR);

        if (day.length() < 2)
            day = "0" + day;
        if (month.length() < 2)
            month = "0" + month;

        while (year.length() < 4)
            year = "0" + year;

        return day + "-" + month + "-" + year;
    }
}