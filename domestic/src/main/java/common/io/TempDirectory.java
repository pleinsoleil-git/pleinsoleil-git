package common.io;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TempDirectory implements AutoCloseable {
	File m_dir;

	public TempDirectory(final File dir) {
		m_dir = dir;
	}

	public static TempDirectory create() throws Exception {
		return create(null);
	}

	public static TempDirectory create(final String prefix) throws Exception {
		return new TempDirectory(Files.createTempDirectory(prefix).toFile());
	}

	public File get() {
		return m_dir;
	}

	public File attach(final File dir) {
		try {
			return m_dir;
		} finally {
			m_dir = dir;
		}
	}

	public File detach() {
		return attach(null);
	}

	@Override
	public void close() throws Exception {
		if (m_dir != null) {
			if (m_dir.exists() == true) {
				log.debug(String.format("Delete derectory[%s]", m_dir.getPath()));
				FileUtils.forceDelete(m_dir);
			}
		}
	}
}
