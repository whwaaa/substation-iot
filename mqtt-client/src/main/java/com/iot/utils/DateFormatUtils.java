package com.iot.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wuhanwei
 * @version 1.0
 * @date 2021/9/15
 */
public class DateFormatUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String DateToString(Date date){
        return new SimpleDateFormat().format(date);
    }

    public static Date StringToDate(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
}
