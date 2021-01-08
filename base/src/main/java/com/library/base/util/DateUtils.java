package com.library.base.util;

import com.library.base.LogConsole;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 火龙裸 on 2020/3/12.
 */

public class DateUtils {


    /**
     * 北京时间
     * 用于转化服务器时间
     *
     * @param format
     * @return
     */
    public static SimpleDateFormat getChineseFormat(String... format) {
        SimpleDateFormat simpleDateFormat;
        if (format.length <= 0) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat(format[0], Locale.getDefault());
        }
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return simpleDateFormat;
    }

    /**
     * 当前手机，所在时区的时间
     * 用于展示使用
     *
     * @param format
     * @return
     */
    public static SimpleDateFormat getPhoneFormat(String... format) {
        SimpleDateFormat simpleDateFormat;
        if (format.length <= 0) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else {
            simpleDateFormat = new SimpleDateFormat(format[0], Locale.getDefault());
        }
        return simpleDateFormat;
    }


    /**
     * 是否为同一天，简易检测
     */
    public static boolean isSameDayEasyCheck(String day1, String day2) {
        try {
            Calendar todayCalendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            todayCalendar.setTime(DateUtils.getChineseFormat().parse(day2));
            calendar.setTime(DateUtils.getChineseFormat().parse(day1));
            if (calendar.get(Calendar.YEAR) == (todayCalendar.get(Calendar.YEAR))) {
                int diffDay = todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
                return diffDay == 0;
            }
        } catch (Exception e) {
            LogConsole.e(e.getMessage());
        }
        return false;
    }

    /**
     * 获取时间差
     */
    public static int getDayDiff(String startTime) {
        try {
            Calendar todayCalendar = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.getChineseFormat().parse(startTime));
            if (calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                int diffDay = todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
                return diffDay;
            } else {
                int diffDay = 0;
                int yearCount = todayCalendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
                for (int i = 0; i < yearCount; i++) {
                    Calendar itemCalendar = getLastDay(calendar.get(Calendar.YEAR) + i, 12);
                    if (i > 0 && i < yearCount - 1){
                        diffDay += itemCalendar.get(Calendar.DAY_OF_YEAR);
                    }else{
                        if (i == yearCount - 1) {
                            diffDay += todayCalendar.get(Calendar.DAY_OF_YEAR);
                        }
                        if (i == 0) {
                            diffDay += itemCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
                        }
                    }
                }
                return diffDay;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static Calendar getLastDay(int year, int month) {
        Calendar c = Calendar.getInstance();    //获取Calendar类的实例
        c.clear();
        c.set(Calendar.YEAR, year);             //设置年份
        c.set(Calendar.MONTH, month - 1);       //设置月份，因为月份从0开始，所以用month - 1
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);    //获取当前时间下，该月的最大日期的数字
        c.set(Calendar.DAY_OF_MONTH, lastDay);  //将获取的最大日期数设置为Calendar实例的日期数
        return c;                     //返回日期
    }
}
