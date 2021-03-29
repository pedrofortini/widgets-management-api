package com.widgets.management.api.application.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String convertDateToString(Date date){

        if(date == null) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
        return dateFormat.format(date);
    }
}
