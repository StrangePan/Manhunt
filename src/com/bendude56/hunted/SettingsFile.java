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
	
	private String location;
	private String directory;
	
	private boolean spawnPassive;
	private boolean spawnHostile;
	
	private boolean envDeath;
	private boolean envHunterRespawn;
	private boolean envPreyRespawn;
	private boolean preyFinder;
	
	private boolean friendlyFire;
	private boolean pvpInstantDeath;
	private boolean flyingSpectators;
	
	private boolean opPermission;
	//private boolean easyCommands;
	private boolean allTalk;
	private boolean autoHunter;
	private boolean loadouts;
	private boolean teamHats;
	private boolean northCompass;
	
	private int offlineTimeout;
	private int dayLimit;
	private int locatorTimer;
	private int prepTime;
	
	private int globalBoundry;
	private int hunterBoundry;
	private int noBuildRange;
	
	private Location hunterSpawn = new Location(HuntedPlugin.getInstance().getWorld(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getX(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getY(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getZ());
	private Location preySpawn = new Location(HuntedPlugin.getInstance().getWorld(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getX(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getY(),
									HuntedPlugin.getInstance().getWorld().getSpawnLocation().getZ());
	
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
		opPermission = false;
		//easyCommands = true;
		allTalk = true;
		spawnPassive = true;
		spawnHostile = true;
		envDeath = false;
		envHunterRespawn = false;
		envPreyRespawn = false;
		preyFinder = true;
		friendlyFire = false;
		pvpInstantDeath = false;
		flyingSpectators = true;
		autoHunter = true;
		loadouts = true;
		teamHats = true;
		northCompass = true;
		dayLimit = 3;
		offlineTimeout = 2;
		globalBoundry = -1;
		hunterBoundry = 8;
		noBuildRange = 8;
		locatorTimer = 120;
		prepTime = 10;
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
		} else opPermission = false;
		put("opPermission", Boolean.toString(opPermission));
		
		/*if (containsKey("easyCommands")) {
			if (getProperty("easyCommands").length() > 0 && getProperty("easyCommands").equalsIgnoreCase("true")) {
				easyCommands = true;
			} else {
				easyCommands = false;
			}
		} else easyCommands = true;
		put("easyCommands", Boolean.toString(easyCommands));*/
		
		if (containsKey("allTalk")) {
			if (getProperty("allTalk").length() > 0 && getProperty("allTalk").equalsIgnoreCase("true")) {
				allTalk = true;
			} else {
				allTalk = false;
			}
		} else allTalk = true;
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
		
		if (containsKey("preyFinder")) {
			if (getProperty("preyFinder").length() > 0 && getProperty("preyFinder").equalsIgnoreCase("true")) {
				preyFinder = true;
			} else {
				preyFinder = false;
			}
		} else preyFinder = true;
		put("preyFinder", Boolean.toString(preyFinder));
		
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
		
		if (containsKey("flyingSpectators")) {
			if (getProperty("flyingSpectators").length() > 0 && getProperty("flyingSpectators").equalsIgnoreCase("true")) {
				flyingSpectators = true;
			} else {
				flyingSpectators = false;
			}
		} else flyingSpectators = true;
		put("flyingSpectators", Boolean.toString(flyingSpectators));
		
		if (containsKey("loadouts")) {
			if (getProperty("loadouts").length() > 0 && getProperty("loadouts").equalsIgnoreCase("true")) {
				loadouts = true;
			} else {
				loadouts = false;
			}
		} else loadouts = true;
		put("loadouts", Boolean.toString(loadouts));
		
		if (containsKey("teamHats")) {
			if (getProperty("teamHats").length() > 0 && getProperty("teamHats").equalsIgnoreCase("true")) {
				teamHats = true;
			} else {
				teamHats = false;
			}
		} else teamHats = true;
		put("teamHats", Boolean.toString(teamHats));
		
		if (containsKey("northCompass")) {
			if (getProperty("northCompass").length() > 0 && getProperty("northCompass").equalsIgnoreCase("true")) {
				northCompass = true;
			} else {
				northCompass = false;
			}
		} else northCompass = true;
		put("northCompass", Boolean.toString(northCompass));
		
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
					offlineTimeout = 2;
				}
			} else offlineTimeout = 2;
		} else offlineTimeout = 2;
		put("offlineTimeout", Integer.toString(offlineTimeout));
		
		if (containsKey("prepTime")) {
			if (getProperty("prepTime").length() > 0) {
				try {
					prepTime = Integer.parseInt(getProperty("prepTime"));
					if (prepTime <= 0) prepTime = 0;
					if (prepTime > 10) prepTime = 10;
				} catch (NumberFormatException e) {
					prepTime = 10;
				}
			} else prepTime = 10;
		} else prepTime = 10;
		put("prepTime", Integer.toString(prepTime));
		
		if (containsKey("globalBoundry")) {
			if (getProperty("globalBoundry").length() > 0) {
				try {
					globalBoundry = Integer.parseInt(getProperty("globalBoundry"));
					if (globalBoundry < 64 && globalBoundry > -1) globalBoundry = 64;
					if (globalBoundry <= -1) globalBoundry = -1;
				} catch (NumberFormatException e) {
					globalBoundry = 128;
				}
			} else globalBoundry = 128;
		} else globalBoundry = 128;
		put("globalBoundry", Integer.toString(globalBoundry));
		
		if (containsKey("hunterBoundry")) {
			if (getProperty("hunterBoundry").length() > 0) {
				try {
					hunterBoundry = Integer.parseInt(getProperty("hunterBoundry"));
					if (hunterBoundry < -1) hunterBoundry = -1;
				} catch (NumberFormatException e) {
					hunterBoundry = 8;
				}
			} else hunterBoundry = 8;
		} else hunterBoundry = 8;
		put("hunterBoundry", Integer.toString(hunterBoundry));
		
		if (containsKey("noBuildRange")) {
			if (getProperty("noBuildRange").length() > 0) {
				try {
					noBuildRange = Integer.parseInt(getProperty("noBuildRange"));
					if (noBuildRange < 0) noBuildRange = -1;
				} catch (NumberFormatException e) {
					noBuildRange = 8;
				}
			} else noBuildRange = 8;
		} else noBuildRange = 8;
		put("noBuildRange", Integer.toString(noBuildRange));
		
		if (containsKey("locatorTimer")) {
			if (getProperty("locatorTimer").length() > 0) {
				try {
					locatorTimer = Integer.parseInt(getProperty("locatorTimer"));
					if (locatorTimer < 0) locatorTimer = 0;
				} catch (NumberFormatException e) {
					locatorTimer = 120;
				}
			} else locatorTimer = 120;
		} else locatorTimer = 120;
		put("locatorTimer", Integer.toString(locatorTimer));
		
		if (containsKey("hunterSpawn")) {
			if (getProperty("hunterSpawn").split(",").length == 3) {
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
		put("hunterSpawn", hunterSpawn.getBlockX() + "," + hunterSpawn.getBlockY() + "," + hunterSpawn.getBlockZ());
		
		if (containsKey("preySpawn")) {
			if (getProperty("preySpawn").split(",").length == 3) {
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
		put("reySpawn", preySpawn.getBlockX() + "," + preySpawn.getBlockY() + "," + preySpawn.getBlockZ());
		
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

	
	public boolean spawnPassive() 	{ return spawnPassive; }
	public boolean spawnHostile() 	{ return spawnHostile; }
	
	public boolean envDeath()		{ return envDeath; }
	public boolean envHunterRespawn(){ return envHunterRespawn; }
	public boolean envPreyRespawn()	{ return envPreyRespawn; }
	public boolean preyFinder()		{ return preyFinder; }
	
	public boolean friendlyFire()	{ return friendlyFire; }
	public boolean pvpInstantDeath(){ return pvpInstantDeath; }
	public boolean flyingSpectators(){ return flyingSpectators; }
	
	public boolean opPermission()	{ return opPermission; }
	//public boolean easyCommands()	{ return easyCommands; }
	public boolean allTalk()		{ return allTalk; }
	public boolean autoHunter()		{ return autoHunter; }
	public boolean loadouts()		{ return loadouts; }
	public boolean teamHats()		{ return teamHats; }
	public boolean northCompass()	{ return northCompass; }
	
	public int offlineTimeout()		{ return offlineTimeout; }
	public int dayLimit()			{ return dayLimit; }
	
	public int globalBoundry() 		{ return globalBoundry; }
	public int hunterBoundry() 		{ return hunterBoundry; }
	public int noBuildRange() 		{ return noBuildRange; }
	public int locatorTimer()		{ return locatorTimer; }
	public int prepTime()			{ return prepTime; }
	
	public Location hunterSpawn()	{ return hunterSpawn; }
	public Location preySpawn()		{ return preySpawn; }
	
 	public void changeSetting(String setting, String value) {
		if (containsKey(setting)) {
			put(setting, value);
			loadValues();
			saveFile();
			HuntedPlugin.getInstance().log(Level.INFO, setting + " set to " + value);
		}
	}
	
	public void changeSetting(String setting, Location loc) {
		if (containsKey(setting)) {
			String value = loc.getBlockX() + ","
					+ loc.getBlockY() + ","
					+ loc.getBlockZ();
			put(setting, value);
			loadValues();
			saveFile();
			HuntedPlugin.getInstance().log(Level.INFO, setting + " changed to " + value);
		}
	}
	
	
	/*public void changeSetting(String setting, ItemStack[] inventory) {
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