package a00100.app.job.a00100.crawl.job.rakuten.request.process;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.BooleanUtils;

import a00100.app.job.a00100.crawl.Connection;
import a00100.app.job.a00100.crawl.job.job.request.Request;
import common.jdbc.JDBCParameterList;
import common.jdbc.JDBCUtils;
import lombok.Data;
import lombok.val;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Accessors(prefix = "m_", chain = false)
public class Process {
	static final ThreadLocal<Process> m_instances = new ThreadLocal<Process>() {
		@Override
		protected Process initialValue() {
			return new Process();
		}
	};
	static final ThreadLocal<_Current> m_currents = new ThreadLocal<_Current>();

	Process() {
	}

	public static Process getInstance() {
		return m_instances.get();
	}

	public static _Current getCurrent() {
		return m_currents.get();
	}

	public void execute() throws Exception {
		try {
			val request = Request.getCurrent();
			val executor = Executors.newFixedThreadPool(request.getExecutionNums().intValue());
			val completion = new ExecutorCompletionService<_Task>(executor);

			try {
				int taskNums = 0;

				do {
					for (val r : query()) {
						taskNums++;
						completion.submit(r);
					}

					if (taskNums > 0) {
						m_currents.set(completion.take().get());
						m_currents.get().execute();
					}
				} while (--taskNums > 0);

				executor.shutdown();
			} catch (Exception e) {
				executor.shutdownNow();
				throw e;
			}
		} finally {
			m_instances.remove();
		}
	}

	Collection<_Task> query() throws Exception {
		String sql;
		sql = "WITH s_params AS\n"
			+ "(\n"
				+ "SELECT ?::BIGINT AS request_id\n"
			+ ")\n"
			+ "SELECT j20.id\n"
			+ "FROM s_params AS t10\n"
			+ "INNER JOIN j_crawl_request AS j10\n"
				+ "ON j10.id = t10.request_id\n"
				+ "AND j10.aborted = FALSE\n"
			+ "INNER JOIN j_crawl_process AS j20\n"
				+ "ON j20.foreign_id = j10.id\n"
				+ "AND j20.aborted = FALSE\n"
				+ "AND j20.deleted = FALSE\n"
			+ "WHERE NOT EXISTS\n"
			+ "(\n"
				+ "SELECT NULL\n"
				+ "FROM j_crawl_process_status AS j900\n"
				+ "WHERE j900.foreign_id = j20.id\n"
			+ ")\n"
			+ "ORDER BY j20.priority NULLS LAST,\n"
				+ "j20.id\n";

		val conn = Connection.getCurrent().getDefault();
		val rs = new BeanListHandler<_Task>(_Task.class);
		return JDBCUtils.query(conn, sql, rs, new JDBCParameterList() {
			{
				val request = Request.getCurrent();
				add(request.getId());
			}
		});
	}

	@Data
	public static class _Current {
		Long m_id;

		void execute() throws Exception {
		}
	}

	public static class _Task extends _Current implements Callable<_Task> {
		@Override
		public _Task call() {
			try {
				m_currents.set(this);
				_execute();
			} catch (Exception e) {
				log.error("", e);
			} finally {
				m_currents.remove();
			}

			return this;
		}

		void _execute() throws Exception {
			log.info(String.format("Process[id=%d]", getId()));

			if (aborted() == true) {
			} else {
			}
		}

		boolean aborted() throws Exception {
			String sql;
			sql = "WITH s_params AS\n"
				+ "(\n"
					+ "SELECT ?::BIGINT AS process_id\n"
				+ ")\n"
				+ "SELECT j10.aborted\n"
				+ "FROM s_params AS t10\n"
				+ "INNER JOIN j_crawl_process AS j10\n"
					+ "ON j10.id = t10.process_id\n";

			val conn = Connection.getCurrent().getDefault();
			val rs = new ScalarHandler<Boolean>();
			return BooleanUtils.isTrue(JDBCUtils.query(conn, sql, rs, new JDBCParameterList() {
				{
					add(getId());
				}
			}));
		}
	}
}
