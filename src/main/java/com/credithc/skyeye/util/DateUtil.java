package com.credithc.skyeye.util;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/**
 * @Description : 日期工具类
 * @Author ：dongbin
 * @Date ：2017/6/12
 */
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final SimpleDateFormat STD_DATE_FORMAT;
    public static final SimpleDateFormat STD_TIME_FORMAT;
    public static final SimpleDateFormat STD_DATE_TIME_FORMAT;
    public static final SimpleDateFormat STD_SHORT_DATE_TIME_FORMAT;
    public static final SimpleDateFormat STD_DATE_TIME_FORMATX;

    public static final SimpleDateFormat STD_DATE_POINT_FORMAT;

    public static final SimpleDateFormat FILE_DATE_TIME_FORMAT;
    public static final SimpleDateFormat INT_DATE_FORMAT;

    public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_HMS    = "HHmmss";
    public static final String SIMPLE_DATE        = "yyyy-MM-dd";

    static {
    	STD_DATE_POINT_FORMAT =  new SimpleDateFormat("yyyy.MM.dd");
        STD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        STD_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
        STD_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        STD_SHORT_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        STD_DATE_TIME_FORMATX = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        FILE_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
        INT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    }

    // 默认时区"GMT+08:00"
    public final static TimeZone defaultTimezone = TimeZone.getTimeZone("GMT+08:00");

    public static Calendar getCalendar(TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = defaultTimezone;
        }
        return Calendar.getInstance(defaultTimezone);
    }

    public static Calendar getCalendar() {
        return getCalendar(null);
    }

    public static boolean isLeapYear(int year) {
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }



    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime(COMMON_DATE_FORMAT);
    }

    /**
     * 获取当前时间，根据格式
     * @return
     */
    public static String getCurrentTime(String format) {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return dateFormat.format(currentDate).toString();
    }
    /**
     * 将指定日期格式化
     * @param format
     * @param date
     * @return
     */
    public static String getFormatTime(String format, Date date) {
        SimpleDateFormat sDateformat = new SimpleDateFormat(format);
        return sDateformat.format(date).toString();
    }


    /**
     * 根据传入的格式返回昨天的日期
     * @param format 日期格式
     * @return
     */
   public static String getYesterday(String format){
       SimpleDateFormat dateFormat = new SimpleDateFormat(format);
       Date date=DateUtils.addDays(new Date(),-1);
       return dateFormat.format(date);
   }

    /**
     * 与当前时间秒差
     * @param time 时间
     * @param format 格式
     * @return 秒差
     */
    public static Long getDurationSeconds(String time,SimpleDateFormat format){
        Date date;
        try {
            date = format.parse(time);
            DateTime start =new DateTime(date);
            DateTime end = DateTime.now();
            //计算区间毫秒数
            Duration duration = new Duration(start, end);
            return duration.getStandardSeconds();
        } catch (ParseException e) {
            logger.info("getDurationSeconds error:"+time,e);
        }
        return null;
    }

}
