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

public class WorldDataFile extends Properties {
	private static final long serialVersionUID = 0L;
	
	private String location;
	
	private int globalBoundry;
	private int hunterBoundry;
	private int noBuildRange;

	private Location hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location prepSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	
	public WorldDataFile() {
		location = "Manhunt.properties";
		loadWorldFile();
	}
	
	public void loadWorldFile() {
		File file = new File(HuntedPlugin.getInstance().getWorld().getWorldFolder().toString() + "/" + location);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Manhunt World Data file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Manhunt World Data file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
		
		loadWorldValues();
		saveWorldFile();
	}
	
	public void saveWorldFile() {
		File file = new File(HuntedPlugin.getInstance().getWorld().getWorldFolder() + "/" + location);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Manhunt World Data file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file), "- Manhunt World Configurations and Settings -");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE, "Problem loading the Manhunt World Data file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public void loadDefaults() {
		
		globalBoundry = 128;
		hunterBoundry = 8;
		noBuildRange = 8;
		
		hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		prepSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	}
	
	public void loadWorldValues() {
		
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
		
		if (containsKey("hunterSpawn")) {
			if (getProperty("hunterSpawn").split(",").length == 5) {
				String[] loc = getProperty("hunterSpawn").split(",");
				try {
					hunterSpawn.setX(Double.parseDouble(loc[0]));
					hunterSpawn.setY(Double.parseDouble(loc[1]));
					hunterSpawn.setZ(Double.parseDouble(loc[2]));
					hunterSpawn.setPitch(Float.parseFloat(loc[3]));
					hunterSpawn.setYaw(Float.parseFloat(loc[4]));
				} catch (NumberFormatException e) {
					hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		put("hunterSpawn", hunterSpawn.getX() + "," + hunterSpawn.getY() + "," + hunterSpawn.getZ() + "," + hunterSpawn.getPitch() + "," + hunterSpawn.getYaw());
		
		if (containsKey("preySpawn")) {
			if (getProperty("preySpawn").split(",").length == 5) {
				String[] loc = getProperty("preySpawn").split(",");
				try {
					preySpawn.setX(Double.parseDouble(loc[0]));
					preySpawn.setY(Double.parseDouble(loc[1]));
					preySpawn.setZ(Double.parseDouble(loc[2]));
					preySpawn.setPitch(Float.parseFloat(loc[3]));
					preySpawn.setYaw(Float.parseFloat(loc[4]));
				} catch (NumberFormatException e) {
					preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		put("preySpawn", preySpawn.getX() + "," + preySpawn.getY() + "," + preySpawn.getZ() + "," + preySpawn.getPitch() + "," + preySpawn.getYaw());
		
		if (containsKey("prepSpawn")) {
			if (getProperty("prepSpawn").split(",").length == 5) {
				String[] loc = getProperty("prepSpawn").split(",");
				try {
					prepSpawn.setX(Double.parseDouble(loc[0]));
					prepSpawn.setY(Double.parseDouble(loc[1]));
					prepSpawn.setZ(Double.parseDouble(loc[2]));
					prepSpawn.setPitch(Float.parseFloat(loc[3]));
					prepSpawn.setYaw(Float.parseFloat(loc[4]));
				} catch (NumberFormatException e) {
					prepSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				prepSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			prepSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		put("prepSpawn", prepSpawn.getX() + "," + prepSpawn.getY() + "," + prepSpawn.getZ() + "," + prepSpawn.getPitch() + "," + prepSpawn.getYaw());
	}


	public int globalBoundry() 		{ return globalBoundry; }
	public int hunterBoundry() 		{ return hunterBoundry; }
	public int noBuildRange() 		{ return noBuildRange; }
	
	public Location hunterSpawn()	{ return hunterSpawn; }
	public Location preySpawn()		{ return preySpawn; }
	public Location prepSpawn()		{ return prepSpawn; }
	
	public void changeSetting(String setting, String value) {
		if (containsKey(setting)) {
			put(setting, value);
			loadWorldValues();
			saveWorldFile();
			HuntedPlugin.getInstance().log(Level.INFO, setting + " set to " + value);
		}
	}
	
 	public void changeSetting(String setting, Location loc) {
		if (containsKey(setting)) {
			String value = loc.getX() + ","
					+ loc.getY() + ","
					+ loc.getZ() + ","
					+ loc.getPitch() + ","
					+ loc.getYaw();
			put(setting, value);
			loadWorldValues();
			saveWorldFile();
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