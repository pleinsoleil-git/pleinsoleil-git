package common.lang.time;

import java.sql.Timestamp;

import common.lang.StringUtils;

public class TimestampUtils {
	public static Timestamp valueOf(final String value) throws Exception {
		if (StringUtils.isNotEmpty(value) == true) {
			try {
				return Timestamp.valueOf(value);
			} catch (Exception e) {
			}

			return new Timestamp(DateUtils.parseDateTime(value).getTime());
		}

		return null;
	}
}
