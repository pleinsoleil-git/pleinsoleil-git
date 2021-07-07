package common.io;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TempFile implements AutoCloseable {
	File m_file;

	public TempFile(final File dir) {
		m_file = dir;
	}

	public static TempFile create() throws Exception {
		return create(null, null);
	}

	public static TempFile create(final String prefix, final String suffix) throws Exception {
		return new TempFile(Files.createTempFile(prefix, suffix).toFile());
	}

	public File get() {
		return m_file;
	}

	public File attach(final File dir) {
		try {
			return m_file;
		} finally {
			m_file = dir;
		}
	}

	public File detach() {
		return attach(null);
	}

	@Override
	public void close() throws Exception {
		if (m_file != null) {
			if (m_file.exists() == true) {
				log.debug(String.format("Delete file[%s]", m_file.getPath()));
				FileUtils.forceDelete(m_file);
			}
		}
	}
}
