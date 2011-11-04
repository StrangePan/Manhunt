package com.bendude56.hunted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class SettingsFile extends Properties {
	private static final long serialVersionUID = 0L;
	
	public String location;
	public String directory;
	
	public boolean spawnPassive;
	public boolean spawnHostile;
	
	public boolean envDeath;
	public boolean envHunterRespawn;
	public boolean envHuntedRespawn;
	
	public boolean friendlyFire;
	public boolean pvpInstantDeath;
	
	public boolean opPermission;
	public boolean allTalk;
	
	public int offlineTimeout;
	public int dayLimit;
	
	public int globalBoundry;
	public int hunterBoundry;
	
	public SettingsFile() {
		location = "plugins/Hunted/config.db";
		directory = "plugins/Hunted/";
		loadFile();
	}
	
	public void loadFile() {
		File file = new File(location);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Hunted config file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
			try {
				load(new FileInputStream(file));
				return;
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Hunted config file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		loadValues();
		saveFile();
	}
	
	public void saveFile() {
		File file = new File(location);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Hunted config file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file), "-Manhunt Settings-");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Hunted config file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public void loadDefaults() {
		opPermission = true;
		allTalk = false;
		spawnPassive = true;
		spawnHostile = true;
		envDeath = false;
		envHunterRespawn = false;
		envHuntedRespawn = false;
		friendlyFire = false;
		pvpInstantDeath = false;
		dayLimit = 3;
		offlineTimeout = 5;
		globalBoundry = -1;
		hunterBoundry = 16;
	}
	
	public void loadValues() {
		if (containsKey("opPermission")) {
			if (getProperty("opPermission").length() > 0 && getProperty("opPermission").equalsIgnoreCase("true")) {
				opPermission = true;
			} else {
				opPermission = false;
			}
		} else opPermission = true;
		put("opPermission", Boolean.toString(opPermission));
		
		if (containsKey("allTalk")) {
			if (getProperty("allTalk").length() > 0 && getProperty("allTalk").equalsIgnoreCase("true")) {
				allTalk = true;
			} else {
				allTalk = false;
			}
		} else allTalk = false;
		put("allTalk", Boolean.toString(allTalk));
		
		if (containsKey("spawnPassive")) {
			if (getProperty("spawnPassive").length() > 0 && getProperty("spawnPassive").equalsIgnoreCase("true")) {
				spawnPassive = true;
			} else {
				spawnPassive = false;
			}
		} else spawnPassive = true;
		put("spawnPassive", Boolean.toString(spawnPassive));
		
		if (containsKey("spawnHostile")) {
			if (getProperty("spawnHostile").length() > 0 && getProperty("spawnHostile").equalsIgnoreCase("true")) {
				spawnHostile = true;
			} else {
				spawnHostile = false;
			}
		} else spawnHostile = true;
		put("spawnHostile", Boolean.toString(spawnHostile));
		
		if (containsKey("envDeath")) {
			if (getProperty("envDeath").length() > 0 && getProperty("envDeath").equalsIgnoreCase("true")) {
				envDeath = true;
			} else {
				envDeath = false;
			}
		} else envDeath = false;
		put("envDeath", Boolean.toString(envDeath));
		
		if (containsKey("envHunterRespawn")) {
			if (getProperty("envHunterRespawn").length() > 0 && getProperty("envHunterRespawn").equalsIgnoreCase("true")) {
				envHunterRespawn = true;
			} else {
				envHunterRespawn = false;
			}
		} else envHunterRespawn = true;
		put("envHunterRespawn", Boolean.toString(envHunterRespawn));
		
		if (containsKey("envHuntedRespawn")) {
			if (getProperty("envHuntedRespawn").length() > 0 && getProperty("envHuntedRespawn").equalsIgnoreCase("true")) {
				envHuntedRespawn = true;
			} else {
				envHuntedRespawn = false;
			}
		} else envHuntedRespawn = true;
		put("envHuntedRespawn", Boolean.toString(envHuntedRespawn));
		
		if (containsKey("friendlyFire")) {
			if (getProperty("friendlyFire").length() > 0 && getProperty("friendlyFire").equalsIgnoreCase("true")) {
				friendlyFire = true;
			} else {
				friendlyFire = false;
			}
		} else friendlyFire = false;
		put("friendlyFire", Boolean.toString(friendlyFire));
		
		if (containsKey("pvpInstantDeath")) {
			if (getProperty("pvpInstantDeath").length() > 0 && getProperty("pvpInstantDeath").equalsIgnoreCase("true")) {
				pvpInstantDeath = true;
			} else {
				pvpInstantDeath = false;
			}
		} else pvpInstantDeath = false;
		put("pvpInstantDeath", Boolean.toString(pvpInstantDeath));
		
		if (containsKey("dayLimit")) {
			if (getProperty("dayLimit").length() > 0) {
				try {
					dayLimit = Integer.parseInt(getProperty("dayLimit"));
				} catch (NumberFormatException e) {
					dayLimit = 3;
				}
			} else dayLimit = 3;
		} else dayLimit = 3;
		put("dayLimit", Integer.toString(dayLimit));
		
		if (containsKey("offlineTimeout")) {
			if (getProperty("offlineTimeout").length() > 0) {
				try {
					offlineTimeout = Integer.parseInt(getProperty("offlineTimeout"));
				} catch (NumberFormatException e) {
					offlineTimeout = 5;
				}
			} else offlineTimeout = 5;
		} else offlineTimeout = 5;
		put("offlineTimeout", Integer.toString(offlineTimeout));
		
		if (containsKey("globalBoundry")) {
			if (getProperty("globalBoundry").length() > 0) {
				try {
					globalBoundry = Integer.parseInt(getProperty("globalBoundry"));
				} catch (NumberFormatException e) {
					globalBoundry = -1;
				}
			} else globalBoundry = -1;
		} else globalBoundry = -1;
		put("globalBoundry", Integer.toString(globalBoundry));
		
		if (containsKey("hunterBoundry")) {
			if (getProperty("hunterBoundry").length() > 0) {
				try {
					hunterBoundry = Integer.parseInt(getProperty("hunterBoundry"));
				} catch (NumberFormatException e) {
					hunterBoundry = 16;
				}
			} else hunterBoundry = 16;
		} else hunterBoundry = 16;
		put("hunterBoundry", Integer.toString(hunterBoundry));
	}
	
	public void changeSetting(String setting, String value) {
		if (containsKey(setting)) {
			put(setting, value);
			saveFile();
		}
	}
}