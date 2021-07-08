package common.jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import common.lang.StringUtils;

public class JDBCParameterList extends ArrayList<Object> {
	public JDBCParameterList() {
	}

	public JDBCParameterList(final Collection<? extends Object> array) {
		super(array);
	}

	Object valueOf(Object value) {
		if (value instanceof String) {
			return StringUtils.trimToNull((String) value);
		} else if (value instanceof Timestamp) {
			return value;
		} else if (value instanceof Date) {
			return new java.sql.Date(((Date) value).getTime());
		}

		return value;
	}

	@Override
	public boolean add(final Object value) {
		return super.add(valueOf(value));
	}

	@Override
	public Object set(final int index, final Object value) {
		return super.set(index, valueOf(value));
	}
}
