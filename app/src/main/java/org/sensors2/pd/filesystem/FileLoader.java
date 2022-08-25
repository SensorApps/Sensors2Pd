package org.sensors2.pd.filesystem;

import android.content.Context;

import org.puredata.core.utils.IoUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thomas on 09.12.14.
 */
public class FileLoader {
	private final String filePath;
	private final Context ctx;

	public FileLoader(String filePath, Context ctx) {
		this.filePath = filePath;
		this.ctx = ctx;
	}

	public File getFile() throws IOException {
		File file = new File(this.filePath);
		if (file.getAbsolutePath().endsWith(".pd")) {
			return file;
		} else if (file.getAbsolutePath().endsWith(".zip")) {
			InputStream inputStream = null;
			InputStream fileStream = null;
			try {
				File dir = ctx.getFilesDir();
				fileStream = new FileInputStream(file);
				inputStream = new BufferedInputStream(fileStream);
				IoUtils.extractZipResource(inputStream, dir, true);
				return new File(dir, file.getName().replace(".zip", ".pd"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				fileStream.close();
				inputStream.close();
			}
		}
		return null;
	}
}
