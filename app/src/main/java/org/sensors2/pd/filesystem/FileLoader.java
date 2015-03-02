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

	public File getFile() {
		File file = new File(this.filePath);
		if (file != null) {
			if (file.getAbsolutePath().endsWith(".pd")) {
				return file;
			} else if (file.getAbsolutePath().endsWith(".zip")) {
				InputStream in = null;
				try {
					File dir = ctx.getFilesDir();
					in = new BufferedInputStream(new FileInputStream(file));
					IoUtils.extractZipResource(in, dir, true);
					File patchFile = new File(dir, file.getName().replace(".zip", ".pd"));
					return patchFile;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

			}
		} else {

		}
		return null;
	}
}
