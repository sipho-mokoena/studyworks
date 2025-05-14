package com.pbde401.studyworks.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String UI_DATE_FORMAT = "MMM yyyy";
    
    public static Date parseIsoDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;
        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat(ISO_8601_FORMAT, Locale.US);
            iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601Format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String formatToIso8601(Date date) {
        if (date == null) return null;
        SimpleDateFormat iso8601Format = new SimpleDateFormat(ISO_8601_FORMAT, Locale.US);
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return iso8601Format.format(date);
    }
    
    public static Date parseUiDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return new SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault()).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String formatForUi(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault()).format(date);
    }
}
