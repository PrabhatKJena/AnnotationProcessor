package edu.pk.rnd.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	public static final String LOG_FILE = "D:/temp/log.txt";
	private FileWriter writer = null;

	public Logger() {
		try {
			new File(LOG_FILE).getParentFile().mkdirs();
			writer = new FileWriter(LOG_FILE, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			writer.close();
		} catch (IOException e1) {
		}
	}
	public void log(Object message) {
		if (message == null) {
			return;
		}

		try {
			writer.append(message.toString());
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}