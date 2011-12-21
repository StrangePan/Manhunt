package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HuntedPlugin extends JavaPlugin {
	
	private Logger log = Logger.getLogger("Minecraft");
	public boolean spoutEnabled;
	public SpConnect spoutConnect;
	private World manhuntWorld;
	private SettingsFile settings;
	private WorldDataFile worlddata;
	private Game game;

	@Override
	public void onDisable() {
		for (String s : getGame().getCreative()) {
			if (Bukkit.getPlayerExact(s) != null) {
				Bukkit.getPlayerExact(s).setGameMode(GameMode.CREATIVE);
			}
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName() + ChatColor.RED + " plugin has been disabled!");
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
		worlddata = new WorldDataFile();
		game = new Game();
		new CmdExec();
		new HuntedPlayerListener();
		new HuntedEntityListener();
		new HuntedBlockListener();
		//new HuntedInventoryListener();
		log(Level.INFO, "Version " + getDescription().getVersion() + " loaded into memory...");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld() == getWorld()) {
				game.onLogin(p);
			}
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName() + " v" + getDescription().getVersion() + ChatColor.YELLOW + " is up running!");
		}
	}
	
	public void log(Level level, String message) {
		log.log(level, "[" + this.getDescription().getName() + "] " + message);
	}
	
	public static HuntedPlugin getInstance() {
		return (HuntedPlugin) Bukkit.getServer().getPluginManager().getPlugin("Manhunt");
	}
	
	public World getWorld() {
		return manhuntWorld;
	}
	
	public SettingsFile getSettings() {
		return settings;
	}
	
	public WorldDataFile getWorldData() {
		return worlddata;
	}
	
	public Game getGame() {
		return game;
	}

}
