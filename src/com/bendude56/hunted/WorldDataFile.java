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
	
	private int mapBoundary;
	private int pregameBoundary;
	private int noBuildRange;
	private boolean boxBoundary;
	private boolean noBuild;

	private Location hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	private Location pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	
	public WorldDataFile() {
		location = "Manhunt_world.properties";
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
		boxBoundary = false;
		
		mapBoundary = 128;
		pregameBoundary = 8;
		noBuildRange = 8;
		
		hunterSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		preySpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
		pregameSpawn = HuntedPlugin.getInstance().getWorld().getSpawnLocation();
	}
	
	public void loadWorldValues() {
		
		if (containsKey("boxBoundary")) {
			if (getProperty("boxBoundary").length() > 0 && getProperty("boxBoundary").equalsIgnoreCase("true")) {
				boxBoundary = true;
			} else {
				boxBoundary = false;
			}
		} else boxBoundary = false;
		put("boxBoundary", Boolean.toString(boxBoundary));
		
		if (containsKey("noBuild")) {
			if (getProperty("noBuild").length() > 0 && getProperty("noBuild").equalsIgnoreCase("true")) {
				noBuild = true;
			} else {
				noBuild = false;
			}
		} else noBuild = false;
		put("noBuild", Boolean.toString(noBuild));
		
		if (containsKey("mapBoundary")) {
			if (getProperty("mapBoundary").length() > 0) {
				try {
					mapBoundary = Integer.parseInt(getProperty("mapBoundary"));
					if (mapBoundary < 64 && mapBoundary > -1) mapBoundary = 64;
					if (mapBoundary <= -1) mapBoundary = -1;
				} catch (NumberFormatException e) {
					mapBoundary = 128;
				}
			} else mapBoundary = 128;
		} else mapBoundary = 128;
		put("mapBoundary", Integer.toString(mapBoundary));
		
		if (containsKey("pregameBoundary")) {
			if (getProperty("pregameBoundary").length() > 0) {
				try {
					pregameBoundary = Integer.parseInt(getProperty("pregameBoundary"));
					if (pregameBoundary < -1) pregameBoundary = -1;
					if (pregameBoundary > 64) pregameBoundary = 64;
				} catch (NumberFormatException e) {
					pregameBoundary = 8;
				}
			} else pregameBoundary = 8;
		} else pregameBoundary = 8;
		put("pregameBoundary", Integer.toString(pregameBoundary));
		
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

	public boolean boxBoundary()	{ return boxBoundary; }
	public boolean noBuild()		{ return noBuild; }

	public int mapBoundary() 		{ return mapBoundary; }
	public int pregameBoundary() 	{ return pregameBoundary; }
	public int noBuildRange() 		{ return noBuildRange; }
	
	public Location hunterSpawn()	{ return hunterSpawn; }
	public Location preySpawn()		{ return preySpawn; }
	public Location pregameSpawn()	{ return pregameSpawn; }
	
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