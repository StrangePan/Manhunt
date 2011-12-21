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
	
	private int mapBoundry;
	private int pregameBoundry;
	private int noBuildRange;
	private boolean boxedBoundry;

	private Location hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	
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
		boxedBoundry = false;
		
		mapBoundry = 128;
		pregameBoundry = 8;
		noBuildRange = 8;
		
		hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	}
	
	public void loadWorldValues() {
		
		if (containsKey("boxedBoundry")) {
			if (getProperty("boxedBoundry").length() > 0 && getProperty("boxedBoundry").equalsIgnoreCase("true")) {
				boxedBoundry = true;
			} else {
				boxedBoundry = false;
			}
		} else boxedBoundry = true;
		put("boxedBoundry", Boolean.toString(boxedBoundry));
		
		if (containsKey("mapBoundry")) {
			if (getProperty("mapBoundry").length() > 0) {
				try {
					mapBoundry = Integer.parseInt(getProperty("mapBoundry"));
					if (mapBoundry < 64 && mapBoundry > -1) mapBoundry = 64;
					if (mapBoundry <= -1) mapBoundry = -1;
				} catch (NumberFormatException e) {
					mapBoundry = 128;
				}
			} else mapBoundry = 128;
		} else mapBoundry = 128;
		put("mapBoundry", Integer.toString(mapBoundry));
		
		if (containsKey("pregameBoundry")) {
			if (getProperty("pregameBoundry").length() > 0) {
				try {
					pregameBoundry = Integer.parseInt(getProperty("pregameBoundry"));
					if (pregameBoundry < -1) pregameBoundry = -1;
					if (pregameBoundry > 64) pregameBoundry = 64;
				} catch (NumberFormatException e) {
					pregameBoundry = 8;
				}
			} else pregameBoundry = 8;
		} else pregameBoundry = 8;
		put("pregameBoundry", Integer.toString(pregameBoundry));
		
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
		
		if (containsKey("pregameSpawn")) {
			if (getProperty("pregameSpawn").split(",").length == 5) {
				String[] loc = getProperty("pregameSpawn").split(",");
				try {
					pregameSpawn.setX(Double.parseDouble(loc[0]));
					pregameSpawn.setY(Double.parseDouble(loc[1]));
					pregameSpawn.setZ(Double.parseDouble(loc[2]));
					pregameSpawn.setPitch(Float.parseFloat(loc[3]));
					pregameSpawn.setYaw(Float.parseFloat(loc[4]));
				} catch (NumberFormatException e) {
					pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
				}
			} else {
				pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
			}
		} else {
			pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		}
		put("pregameSpawn", pregameSpawn.getX() + "," + pregameSpawn.getY() + "," + pregameSpawn.getZ() + "," + pregameSpawn.getPitch() + "," + pregameSpawn.getYaw());
	}

	public boolean boxedBoundry()	{ return boxedBoundry; }

	public int mapBoundry() 		{ return mapBoundry; }
	public int pregameBoundry() 	{ return pregameBoundry; }
	public int noBuildRange() 		{ return noBuildRange; }
	
	public Location hunterSpawn()	{ return hunterSpawn; }
	public Location preySpawn()		{ return preySpawn; }
	public Location pregameSpawn()		{ return pregameSpawn; }
	
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