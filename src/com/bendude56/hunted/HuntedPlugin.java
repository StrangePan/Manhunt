package com.bendude56.hunted;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class HuntedPlugin extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onDisable() {
		PluginDescriptionFile desc = this.getDescription();
		log.info(desc.getName() + " has been unloaded from memory...");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile desc = this.getDescription();
		log.info(desc.getName() + " version " + desc.getVersion() + " has been loaded into memory...");
	}

}
