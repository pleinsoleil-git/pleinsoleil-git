package common.lang.time;

import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {
	public static final FastDateFormat DATE_NO_T_FORMAT = FastDateFormat.getInstance("yyyy/MM/dd");
	public static final FastDateFormat DATETIME_NO_T_FORMAT = FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss");
	public static final FastDateFormat ISO_DATETIME_NO_T_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	public static final FastDateFormat MONTH_FORMAT = FastDateFormat.getInstance("yyyy/MM");
	public static final FastDateFormat MINUTE_FORMAT = FastDateFormat.getInstance("HH:mm");

	public static String getYear(final Date date) {
		return DateFormatUtils.format(date, "yyyy");
	}

	public static String getMonth(final Date date) {
		return DateFormatUtils.format(date, "MM");
	}

	public static String getDay(final Date date) {
		return DateFormatUtils.format(date, "dd");
	}
}
