package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class HuntedPlugin extends JavaPlugin {
	
	private Logger log = Logger.getLogger("Minecraft");
	public boolean spoutEnabled;
	public SpConnect spoutConnect;
	private World manhuntWorld;
	private SettingsFile settings;
	private Game game;
	
	/*public boolean friendlyFire = false;
	public boolean pvpOnly = false;
	public boolean hostileMobs = true;
	public boolean passiveMobs = true;
	public int offlineTimeout = 1;
	public boolean allowSpectators;*/

	@Override
	public void onDisable() {
		for (String s : game.getCreative()) {
			if (Bukkit.getPlayerExact(s) != null) {
				Bukkit.getPlayerExact(s).setGameMode(GameMode.CREATIVE);
			}
		}
		log(Level.INFO, "Unloaded from memory...");
	}

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().isPluginEnabled("Spout")) {
			spoutEnabled = true;
			spoutConnect = new SpConnect();
		} else {
			log(Level.WARNING, "Spout installation not detected!");
			spoutEnabled = false;
		}
		
		manhuntWorld = Bukkit.getWorlds().get(0);
		settings = new SettingsFile();
		game = new Game();
		new CmdExec();
		new HuntedPlayerListener();
		new HuntedEntityListener();
		new HuntedBlockListener();
		//new HuntedInventoryListener();
		log(Level.INFO, "Version " + getDescription().getVersion() + " loaded into memory...");
	}
	
	public void log(Level level, String message) {
		log.log(level, "[" + this.getDescription().getName() + "] " + message);
	}
	
	public static HuntedPlugin getInstance() {
		return (HuntedPlugin) Bukkit.getServer().getPluginManager().getPlugin("Hunted");
	}
	
	public World getWorld() {
		return manhuntWorld;
	}
	
	public SettingsFile getSettings() {
		return settings;
	}
	
	public Game getGame() {
		return game;
	}

}
