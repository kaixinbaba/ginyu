package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: junjiexun
 * @date: 2020/8/17 10:47 下午
 * @description: 日期处理工具类
 */
@SuppressWarnings("all")
public class Dates {

    /**
     * Date time pattern. {@value}
     */
    public static final String PTTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * Date time pattern. {@value}
     */
    public static final String PTTERN_DATETIME_NO_SECOND = "yyyy-MM-dd HH:mm";
    /**
     * Date pattern. {@value}
     */
    public static final String PTTERN_DATE = "yyyy-MM-dd";
    /**
     * Date pattern. {@value}
     */
    public static final String PTTERN_SIMPLE_DATE = "yyyyMMdd";
    /**
     * Date time pattern. {@value}
     */
    public static final String PTTERN_SIMPLE_DATETIME = "yyyyMMddHHmmss";
    /**
     * PH date format.  {@value}
     */
    public static final String PTTERN_PH_DATE = "MMM d, yyyy";
    /**
     * ID date format.  {@value}
     */
    public static final String PATTERN_ID_DATE = "d MMM, yyyy";
    /**
     * Date pattern. {@value}
     */
    public static final String YEAR_MONTH = "yyyyMM";
    /**
     * Time pattern. {@value}
     */
    public static final String TIME = "HHmmss";
    /**
     * Date time pattern. {@value}
     */
    public static final String DATE_TIME_S = "yyyyMMddHHmmssSSS";
    /**
     * Date time pattern. {@value}
     */
    public static final String P_DATE_TIME_S2 = "yyyy-MM-dd HH:mm:ss.SS";
    /**
     * Date time pattern. {@value}
     */
    public static final String P_DATE_TIME_S = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * Date time pattern. {@value}
     */
    public static final String P_DATE_TIME_ISO_S = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    /**
     * Date time pattern. {@value}
     */
    public static final String P_DATE_DATE_TIME = "MMM dd HH:mm";
    private static final int MILLISCONDOFDAY = 1000 * 60 * 60 * 24;
    private static final Map<String, DateTimeFormatter> cache = new ConcurrentHashMap<>();
    static TimeZone defaultTimeZone = TimeZone.getDefault();

    static {
        cache.put(PTTERN_DATE, DateTimeFormatter.ofPattern(PTTERN_DATE));
        cache.put(PTTERN_DATETIME, DateTimeFormatter.ofPattern(PTTERN_DATETIME));
        cache.put(PTTERN_DATETIME_NO_SECOND, DateTimeFormatter.ofPattern(PTTERN_DATETIME_NO_SECOND));
        cache.put(PTTERN_SIMPLE_DATE, DateTimeFormatter.ofPattern(PTTERN_SIMPLE_DATE));
        cache.put(PTTERN_SIMPLE_DATETIME, DateTimeFormatter.ofPattern(PTTERN_SIMPLE_DATETIME));
        cache.put(YEAR_MONTH, DateTimeFormatter.ofPattern(YEAR_MONTH));
        cache.put(TIME, DateTimeFormatter.ofPattern(TIME));
        cache.put(DATE_TIME_S, DateTimeFormatter.ofPattern(DATE_TIME_S));
        cache.put(P_DATE_TIME_S, DateTimeFormatter.ofPattern(P_DATE_TIME_S));
        cache.put(P_DATE_TIME_S2, DateTimeFormatter.ofPattern(P_DATE_TIME_S2));
        cache.put(PTTERN_PH_DATE, DateTimeFormatter.ofPattern(PTTERN_PH_DATE));
        cache.put(P_DATE_TIME_ISO_S, DateTimeFormatter.ofPattern(P_DATE_TIME_ISO_S));
        cache.put(P_DATE_DATE_TIME, DateTimeFormatter.ofPattern(P_DATE_DATE_TIME));
        cache.put(PATTERN_ID_DATE, DateTimeFormatter.ofPattern(PATTERN_ID_DATE));
    }


    public static TimeZone getDefaultTimeZone() {
        return defaultTimeZone;
    }

    /**
     * 获取当前Date
     *
     * @return
     */
    public static Date now() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 日期转换成字符串类型
     *
     * @param date
     * @return yyyy-MM-dd 格式的字符串
     */
    public static String date2string(Date date) {
        return date2string(date, PTTERN_DATE);
    }

    /**
     * 日期转换成字符串类型
     *
     * @param date
     * @return 参数指定格式的字符串
     */
    public static String date2string(Date date, String pattern) {
        DateTimeFormatter dtf = cache.get(pattern);
        try {
            LocalDate localDate = date.toInstant().atZone(getDefaultTimeZone().toZoneId()).toLocalDate();
            return localDate.format(dtf);
        } catch (UnsupportedTemporalTypeException e) {
            LocalDateTime localDateTime = date.toInstant().atZone(getDefaultTimeZone().toZoneId()).toLocalDateTime();
            return localDateTime.format(dtf);
        }
    }

    /**
     * Gets the current date as a string.
     *
     * @return the date string formatted as 'yyyyMMdd'
     */
    public static String today() {
        return LocalDate.now(getDefaultTimeZone().toZoneId()).format(cache.get(PTTERN_SIMPLE_DATE));
    }

    /**
     * Gets the current date as a string.
     *
     * @return the date string formatted as 'yyyy-MM-dd'
     */
    public static String prettyToday() {
        return LocalDate.now(getDefaultTimeZone().toZoneId()).format(cache.get(PTTERN_DATE));
    }

    /**
     * Gets the current date & time as a string.
     *
     * @return the date string formatted as 'yyyyMMddHHmmss'
     */
    public static String nowTime() {
        return LocalDateTime.now(getDefaultTimeZone().toZoneId()).format(cache.get(PTTERN_SIMPLE_DATETIME));
    }

    /**
     * Gets the current time as a string.
     *
     * @return the date string formatted as 'HHmmss'
     */
    public static String nowJustTime() {
        return LocalDateTime.now(getDefaultTimeZone().toZoneId()).format(cache.get(TIME));
    }

    /**
     * Gets the current date & time as a string.
     *
     * @return the date string formatted as 'yyyy-MM-dd HH:mm:ss'
     */
    public static String prettyNowTime() {
        return LocalDateTime.now(getDefaultTimeZone().toZoneId()).format(cache.get(PTTERN_DATETIME));
    }


    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = getDefaultTimeZone().toZoneId();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    public static LocalDateTime toLocalDateTime(Date date, TimeZone timeZone) {
        Instant instant = date.toInstant();
        ZoneId zoneId = timeZone.toZoneId();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    /**
     * Converts a {@code LocalDateTime} to {@code Date}.
     */
    public static Date toDate(LocalDateTime dateTime) {
        return toDate(dateTime, getDefaultTimeZone().toZoneId());
    }

    public static Date toDate(LocalDateTime dateTime, ZoneId zone) {
        Instant instant = dateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * Converts a {@code LocalDate} to {@code Date}.
     */
    public static Date toDate(LocalDate date) {
        return toDate(date, getDefaultTimeZone().toZoneId());
    }

    public static Date toDate(LocalDate date, ZoneId zone) {
        Instant instant = date.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date string2date(String date) {
        return toDate(LocalDate.parse(date, cache.get(PTTERN_DATE)));
    }

    public static Date string2date(String date, String pattern) {
        return toDate(LocalDate.parse(date, cache.get(pattern)));
    }

    public static Date string2dateTime(String dateTime, String pattern) {
        return toDate(LocalDateTime.parse(dateTime, cache.get(pattern)));
    }

    public static Date minusMinutes(Date date, long minutes) {
        return toDate(toLocalDateTime(date).minusMinutes(minutes));
    }

    public static Date minusSeconds(Date date, long seconds) {
        return toDate(toLocalDateTime(date).minusSeconds(seconds));
    }

    public static Date minusHours(Date date, long hours) {
        return toDate(toLocalDateTime(date).minusHours(hours));
    }

    public static Date minusDays(Date date, long days) {
        return toDate(toLocalDateTime(date).minusDays(days));
    }

    public static Date minusWeeks(Date date, long weeks) {
        return toDate(toLocalDateTime(date).minusWeeks(weeks));
    }

    public static Date minusMonths(Date date, long months) {
        return toDate(toLocalDateTime(date).minusMonths(months));
    }

    public static Date minusYears(Date date, long years) {
        return toDate(toLocalDateTime(date).minusYears(years));
    }


    public static Date plusMinutes(Date date, long minutes) {
        return toDate(toLocalDateTime(date).plusMinutes(minutes));
    }

    public static Date plusSeconds(Date date, long seconds) {
        return toDate(toLocalDateTime(date).plusSeconds(seconds));
    }

    public static Date plusHours(Date date, long hours) {
        return toDate(toLocalDateTime(date).plusHours(hours));
    }

    public static Date plusDays(Date date, long days) {
        return toDate(toLocalDateTime(date).plusDays(days));
    }

    public static Date plusWeeks(Date date, long weeks) {
        return toDate(toLocalDateTime(date).plusWeeks(weeks));
    }

    public static Date plusMonths(Date date, long months) {
        return toDate(toLocalDateTime(date).plusMonths(months));
    }

    public static Date plusYears(Date date, long years) {
        return toDate(toLocalDateTime(date).plusYears(years));
    }
}
