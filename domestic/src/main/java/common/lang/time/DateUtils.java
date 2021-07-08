package common.lang.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import common.lang.StringUtils;
import lombok.val;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static Date valueOf(final String value) throws Exception {
		return parseDate(value);
	}

	public static Date parseMonth(final String value) throws Exception {
		final String FORMATS[] = {
				"yyyy/MM",
				"yyyy-MM",
				"yyyyMM",
		};

		if (StringUtils.isNotEmpty(value) == true) {
			return DateUtils.parseDate(value, FORMATS);
		}

		return null;
	}

	public static Date parseDate(final String value) throws Exception {
		final String FORMATS[] = {
				"yyyy/MM/dd",
				"yyyy-MM-dd",
				"yyyyMMdd",
		};

		if (StringUtils.isNotEmpty(value) == true) {
			return DateUtils.parseDate(value, FORMATS);
		}

		return null;
	}

	public static Date parseTime(final String value) throws Exception {
		final String FORMATS[] = {
				"HH:mm:ss",
				"HH:mm",
				"HH.mm.ss",
				"HH.mm",
		};

		if (StringUtils.isNotEmpty(value) == true) {
			return DateUtils.parseDate(value, FORMATS);
		}

		return null;
	}

	public static Date parseDateTime(final String value) throws Exception {
		final String FORMATS[] = {
				"yyyy/MM/dd HH:mm:ss",
				"yyyy/MM/dd HH:mm",
				"yyyy/MM/dd HH",
				"yyyy/MM/dd",
				"yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd HH:mm",
				"yyyy-MM-dd HH",
				"yyyy-MM-dd",
		};

		if (StringUtils.isNotEmpty(value) == true) {
			return DateUtils.parseDate(value, FORMATS);
		}

		return null;
	}

	public static Calendar toCalendar(final Date value) {
		val result = GregorianCalendar.getInstance();
		result.setTime(value);
		return result;
	}

	public static int getYear() {
		return getYear(now());
	}

	public static int getYear(final Date value) {
		return toCalendar(value).get(Calendar.YEAR);
	}

	public static int getMonth() {
		return getMonth(now());
	}

	public static int getMonth(final Date value) {
		return toCalendar(value).get(Calendar.MONTH) + 1;
	}

	public static int getDay() {
		return getDay(now());
	}

	public static int getDay(final Date value) {
		return toCalendar(value).get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour() {
		return getHour(now());
	}

	public static int getHour(final Date value) {
		return toCalendar(value).get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return getMinute(now());
	}

	public static int getMinute(final Date value) {
		return toCalendar(value).get(Calendar.MINUTE);
	}

	public static int getSecond() {
		return getSecond(now());
	}

	public static int getSecond(final Date value) {
		return toCalendar(value).get(Calendar.SECOND);
	}

	public static int getMillisecond() {
		return getMillisecond(now());
	}

	public static int getMillisecond(final Date value) {
		return toCalendar(value).get(Calendar.MILLISECOND);
	}

	public static int getDayOfWeek() {
		return getDayOfWeek(now());
	}

	public static int getDayOfWeek(final Date value) {
		return toCalendar(value).get(Calendar.DAY_OF_WEEK);
	}

	public static int getWeekOfMonth() {
		return getWeekOfMonth(now());
	}

	public static int getWeekOfMonth(final Date value) {
		return toCalendar(value).get(Calendar.WEEK_OF_MONTH);
	}

	public static int getWeekOfYear() {
		return getWeekOfYear(now());
	}

	public static int getWeekOfYear(final Date value) {
		return toCalendar(value).get(Calendar.WEEK_OF_YEAR);
	}

	public static Date truncate(final Date value) {
		return DateUtils.truncate(value, Calendar.DATE);
	}

	public static Date now() {
		return new Date();
	}

	public static Date today() {
		return truncate(now());
	}

	public static Date firstDay() {
		return firstDay(today());
	}

	public static Date firstDay(final Date value) {
		val result = toCalendar(value);
		result.set(Calendar.DAY_OF_MONTH, 1);
		return result.getTime();
	}

	public static Date lastDay() {
		return lastDay(today());
	}

	public static Date lastDay(final Date value) {
		return addDays(addMonths(firstDay(value), 1), -1);
	}

	public static int diffMonth(final Date value1, final Date value2) {
		int year[] = { DateUtils.getYear(value1), DateUtils.getYear(value2) };
		int month[] = { DateUtils.getMonth(value1), DateUtils.getMonth(value2) };
		return (year[0] - year[1]) * 12 + (month[0] - month[1]);
	}

	public static boolean period(final Date c, final Date s, final Date e) {
		return (c.compareTo(s) >= 0 && c.compareTo(e) <= 0 ? true : false);
	}
}
