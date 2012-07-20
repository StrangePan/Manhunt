package com.bendude56.hunted.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import com.bendude56.hunted.HuntedPlugin;

public class ManhuntFile extends Properties{

	private static final long serialVersionUID = -1911970219353417406L;
	private final String title;
	private final String directory;
	private final String filename;
	
	public ManhuntFile(String title, String directory, String filename)
	{
		this.title = title;
		this.directory = directory;
		this.filename = filename;
	}
	
	public void loadFile() {
		File file = new File(directory + "/" + filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt file \"" + title + "\"!");
				return;
			}
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt file \"" + title + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
		saveFile();
	}
	
	public void saveFile() {
		File file = new File(directory + "/" + filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt file \"" + title + "!\"");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file),
					"- Manhunt " + title + " File -");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt file \"" + title + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}

}
