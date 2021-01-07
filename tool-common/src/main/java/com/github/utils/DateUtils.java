package com.github.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期处理
 *
 * @author qinxuewu
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	// 每天的毫秒数
	private static long day = 1000 * 60 * 60 * 24;

	// 日期字符串的格式
	private static DateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);

	/**
	 * 日期格式化 日期格式为：yyyy-MM-dd
	 * 
	 * @param date
	 *            日期
	 * @return 返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 返回yyyy-MM-dd HH:mm:ss"格式日期
	 */
	public static String getNowTimeStr() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
		return df.format(new Date());

	}

	/**
	 * 日期格式化 日期格式为：yyyy-MM-dd
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式，如：DateUtils.DATE_TIME_PATTERN
	 * @return 返回yyyy-MM-dd格式日期
	 */
	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param pattern
	 *            日期的格式，如：DateUtils.DATE_TIME_PATTERN
	 */
	public static Date stringToDate(String strDate, String pattern) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}

		DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
		return fmt.parseLocalDateTime(strDate).toDate();
	}

	/**
	 * 根据周数，获取开始日期、结束日期
	 * 
	 * @param week
	 *            周期 0本周，-1上周，-2上上周，1下周，2下下周
	 * @return 返回date[0]开始日期、date[1]结束日期
	 */
	public static Date[] getWeekStartAndEnd(int week) {
		DateTime dateTime = new DateTime();
		LocalDate date = new LocalDate(dateTime.plusWeeks(week));

		date = date.dayOfWeek().withMinimumValue();
		Date beginDate = date.toDate();
		Date endDate = date.plusDays(6).toDate();
		return new Date[] { beginDate, endDate };
	}

	/**
	 * 对日期的【秒】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param seconds
	 *            秒数，负数为减
	 * @return 加/减几秒后的日期
	 */
	public static Date addDateSeconds(Date date, int seconds) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusSeconds(seconds).toDate();
	}

	/**
	 * 对日期的【分钟】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param minutes
	 *            分钟数，负数为减
	 * @return 加/减几分钟后的日期
	 */
	public static Date addDateMinutes(Date date, int minutes) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusMinutes(minutes).toDate();
	}

	/**
	 * 对日期的【小时】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param hours
	 *            小时数，负数为减
	 * @return 加/减几小时后的日期
	 */
	public static Date addDateHours(Date date, int hours) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusHours(hours).toDate();
	}

	/**
	 * 对日期的【天】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param days
	 *            天数，负数为减
	 * @return 加/减几天后的日期
	 */
	public static Date addDateDays(Date date, int days) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusDays(days).toDate();
	}

	/**
	 * 对日期的【周】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param weeks
	 *            周数，负数为减
	 * @return 加/减几周后的日期
	 */
	public static Date addDateWeeks(Date date, int weeks) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusWeeks(weeks).toDate();
	}

	/**
	 * 对日期的【月】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param months
	 *            月数，负数为减
	 * @return 加/减几月后的日期
	 */
	public static Date addDateMonths(Date date, int months) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusMonths(months).toDate();
	}

	/**
	 * 对日期的【年】进行加/减
	 *
	 * @param date
	 *            日期
	 * @param years
	 *            年数，负数为减
	 * @return 加/减几年后的日期
	 */
	public static Date addDateYears(Date date, int years) {
		DateTime dateTime = new DateTime(date);
		return dateTime.plusYears(years).toDate();
	}

	/**
	 * 取两个日期间隔
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getDateTimeBetween(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate.getTime());
		DateTime end = new DateTime(endDate.getTime());
		Period period = new Period(start, end);
		String result = period.getDays() + "天" + period.getHours() + "小时"
				+ period.getMinutes() + "分" + period.getSeconds() + "秒";
		return result;
	}

	/**
	 * 取两个日期相隔多少分钟
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDateTimeMinutesBetween(Date startDate, Date endDate) {
		DateTime start = new DateTime(startDate.getTime());
		DateTime end = new DateTime(endDate.getTime());
		Period period = new Period(start, end);
		return period.getMinutes();
	}

	/**
	 * 返回前一天的 00:00:00 字符串格式
	 *
	 * @return
	 */
	public static String getLastDayStart() {
		return format.format(getTodayStartMills() - day);
	}

	/**
	 * 返回前一天的 23:59:59 字符串格式
	 *
	 * @return
	 */
	public static String getLastDayEnd() {
		return format.format(getTodayStartMills() - 1);
	}

	/**
	 * 返回当日的 00:00:00 字符串格式
	 *
	 * @return
	 */
	public static String getTodayStart() {
		long zero = getTodayStartMills();
		return format.format(zero);
	}

	/**
	 * 返回当日的 23:59:59 字符串格式
	 *
	 * @return
	 */
	public static String getTodayEnd() {
		return format.format(getTodayStartMills() + day - 1);
	}

	/**
	 * 返回当日的 00:00:00 毫秒格式
	 *
	 * @return
	 */
	public static long getTodayStartMills() {
		long current = System.currentTimeMillis();
		long zero = ((current + TimeZone.getDefault().getRawOffset()) / day * day)
				- TimeZone.getDefault().getRawOffset();
		return zero;
	}

	/**
	 * 返回前一天的 00:00:00 毫秒格式
	 *
	 * @return
	 */
	public static long getLastDayStartMills() {
		return getTodayStartMills() - day;
	}

	/**
	 * 获取某日期区间的所有日期 日期倒序
	 *
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param dateFormat
	 *            日期格式
	 * @return 区间内所有日期
	 */
	public static List<String> getPerDaysByStartAndEndDate(String startDate,
			String endDate, String dateFormat) {
		DateFormat format = new SimpleDateFormat(dateFormat);
		try {
			Date sDate = format.parse(startDate);
			Date eDate = format.parse(endDate);
			long start = sDate.getTime();
			long end = eDate.getTime();
			if (start > end) {
				return null;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(eDate);
			List<String> res = new ArrayList<String>();
			while (end >= start) {
				res.add(format.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				end = calendar.getTimeInMillis();
			}
			return res;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取服务器启动时间
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 计算两个时间差
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	
}
