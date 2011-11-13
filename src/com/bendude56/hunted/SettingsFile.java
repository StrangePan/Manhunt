package com.bendude56.hunted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.material.MaterialData;
//import org.bukkit.material.TexturedMaterial;

public class SettingsFile extends Properties {
	private static final long serialVersionUID = 0L;
	
	public String location;
	public String directory;
	
	public boolean spawnPassive;
	public boolean spawnHostile;
	
	public boolean envDeath;
	public boolean envHunterRespawn;
	public boolean envPreyRespawn;
	
	public boolean friendlyFire;
	public boolean pvpInstantDeath;
	
	public boolean opPermission;
	public boolean allTalk;
	public boolean autoHunter;
	public boolean loadouts;
	public boolean woolHats;
	
	//public ItemStack[] preyLoadout;
	//public ItemStack[] hunterLoadout;
	
	public int offlineTimeout;
	public int dayLimit;
	
	public int globalBoundry;
	public int hunterBoundry;
	public int noBuildRange;
	
	public Location hunterSpawn;
	public Location preySpawn;
	
	public SettingsFile() {
		directory = "plugins/Hunted/";
		location = directory + "config.db";
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
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Hunted config file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
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
		envPreyRespawn = false;
		friendlyFire = false;
		pvpInstantDeath = false;
		autoHunter = true;
		loadouts = false;
		woolHats = true;
		dayLimit = 3;
		offlineTimeout = 5;
		globalBoundry = -1;
		hunterBoundry = 16;
		noBuildRange = 8;
		hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
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
		
		if (containsKey("envPreyRespawn")) {
			if (getProperty("envPreyRespawn").length() > 0 && getProperty("envPreyRespawn").equalsIgnoreCase("true")) {
				envPreyRespawn = true;
			} else {
				envPreyRespawn = false;
			}
		} else envPreyRespawn = true;
		put("envPreyRespawn", Boolean.toString(envPreyRespawn));
		
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
		
		if (containsKey("loadouts")) {
			if (getProperty("loadouts").length() > 0 && getProperty("loadouts").equalsIgnoreCase("true")) {
				loadouts = true;
			} else {
				loadouts = false;
			}
		} else loadouts = false;
		put("loadouts", Boolean.toString(loadouts));
		
		if (containsKey("woolHats")) {
			if (getProperty("woolHats").length() > 0 && getProperty("woolHats").equalsIgnoreCase("true")) {
				woolHats = true;
			} else {
				woolHats = false;
			}
		} else woolHats = false;
		put("woolHats", Boolean.toString(woolHats));
		
		if (containsKey("autoHunter")) {
			if (getProperty("autoHunter").length() > 0 && getProperty("autoHunter").equalsIgnoreCase("true")) {
				autoHunter = true;
			} else {
				autoHunter = false;
			}
		} else autoHunter = true;
		put("autoHunter", Boolean.toString(autoHunter));
		
		if (containsKey("dayLimit")) {
			if (getProperty("dayLimit").length() > 0) {
				try {
					dayLimit = Integer.parseInt(getProperty("dayLimit"));
					if (dayLimit < 1) dayLimit = 1;
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
					if (offlineTimeout < -1) offlineTimeout = -1;
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
					if (globalBoundry < 256 && globalBoundry > -1) globalBoundry = 256;
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
					if (hunterBoundry < -1) hunterBoundry = -1;
				} catch (NumberFormatException e) {
					hunterBoundry = 16;
				}
			} else hunterBoundry = 16;
		} else hunterBoundry = 16;
		put("hunterBoundry", Integer.toString(hunterBoundry));
		
		if (containsKey("noBuildRange")) {
			if (getProperty("noBuildRange").length() > 0) {
				try {
					noBuildRange = Integer.parseInt(getProperty("noBuildRange"));
					if (noBuildRange < -1) noBuildRange = -1;
				} catch (NumberFormatException e) {
					noBuildRange = 8;
				}
			} else noBuildRange = 8;
		} else noBuildRange = 8;
		put("noBuildRange", Integer.toString(noBuildRange));
		
		if (containsKey("hunterSpawn")) {
			if (getProperty("hunterSpawn").split(",").length >= 3) {
				String[] loc = getProperty("hunterSpawn").split(",");
				try {
					hunterSpawn.setX(Double.parseDouble(loc[0]));
					hunterSpawn.setY(Double.parseDouble(loc[1]));
					hunterSpawn.setZ(Double.parseDouble(loc[2]));
				} catch (NumberFormatException e) {
					hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		
		if (containsKey("preySpawn")) {
			if (getProperty("preySpawn").split(",").length >= 3) {
				String[] loc = getProperty("preySpawn").split(",");
				try {
					preySpawn.setX(Double.parseDouble(loc[0]));
					preySpawn.setY(Double.parseDouble(loc[1]));
					preySpawn.setZ(Double.parseDouble(loc[2]));
				} catch (NumberFormatException e) {
					preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		
		/*if (containsKey("hunterLoadout")) {
			hunterLoadout = new ItemStack[41];
			String[] stack = getProperty("hunterLoadout").split("/");
			for (int i = 0 ; (i < stack.length && i < 41) ; i++) {
				if (stack[i].split(",").length == 3) {
					try {
						hunterLoadout[i] = new ItemStack(Material.matchMaterial(stack[i].split(",")[0]),
								Integer.parseInt(stack[i].split(",")[1]),
								Short.parseShort(stack[i].split(",")[2]));
					} catch (Exception e) {
						hunterLoadout[i] = new ItemStack(Material.AIR, 0);
					}
				} else {
					hunterLoadout[i] = new ItemStack(Material.AIR, 0);
				}
			}
		}
		
		if (containsKey("preyLoadout")) {
			preyLoadout = new ItemStack[41];
			String[] stack = getProperty("preyLoadout").split("/");
			for (int i = 0 ; (i < stack.length && i < 41) ; i++) {
				if (stack[i].split(",").length == 3) {
					try {
						preyLoadout[i] = new ItemStack(Material.matchMaterial(stack[i].split(",")[0]),
								Integer.parseInt(stack[i].split(",")[1]),
								Short.parseShort(stack[i].split(",")[2]));
					} catch (Exception e) {
						preyLoadout[i] = new ItemStack(Material.AIR, 0);
					}
				} else {
					preyLoadout[i] = new ItemStack(Material.AIR, 0);
				}
			}
		}*/
	}
	
	public void changeSetting(String setting, String value) {
		if (containsKey(setting)) {
			put(setting, value);
			loadValues();
			saveFile();
		}
	} public void changeSetting(String setting, Location loc) {
		if (containsKey(setting)) {
			String value = "";
			value += loc.getBlockX() + ",";
			value += loc.getBlockY() + ",";
			value += loc.getBlockZ();
			put(setting, value);
			saveFile();
		}
	} /*public void changeSetting(String setting, ItemStack[] inventory) {
		if (containsKey(setting)) {
			String value = "";
			for (ItemStack i : inventory) {
				value += "/";
				value += i.getData().getItemType().toString() + ",";
				value += i.getAmount() + ",";
				value += i.getDurability() + ",";
			}
			put(setting, value);
			saveFile();
		}
	}*/
}