package com.savypan.italker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);

    /***
     * 获取一个简单的时间字符串
     * @param date
     * @return
     */
    public static String getSampleDate(Date date) {
        return FORMAT.format(date);
    }
}
