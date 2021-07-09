package common.app.job;

public enum JobStatus {
	ABORT(-2), FAILD(-1), WAIT(0), PROCESSING(1), SUCCESS(2);

	final int m_id;

	JobStatus(final int id) {
		m_id = id;
	}

	public int original() {
		return m_id;
	}
}
