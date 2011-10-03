package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HuntedPlugin extends JavaPlugin {
	
	private Logger log = Logger.getLogger("Minecraft");
	public boolean spoutEnabled;

	@Override
	public void onDisable() {
		log(Level.INFO, "Unloaded from memory...");
	}

	@Override
	public void onEnable() {
		new CmdExec(this);
		new HuntedPlayerListener(this);
		if (getServer().getPluginManager().isPluginEnabled("Spout")) {
			spoutEnabled = true;
		} else {
			log(Level.WARNING, "Spout installation not detected!");
			spoutEnabled = false;
		}
		log(Level.INFO, "Version " + getDescription().getVersion() + " loaded into memory...");
	}
	
	public void log(Level level, String message) {
		log.log(level, "[" + this.getDescription().getName() + "] " + message);
	}
	
	public static HuntedPlugin getInstance() {
		return (HuntedPlugin) Bukkit.getServer().getPluginManager().getPlugin("HuntedPlugin");
	}

}
