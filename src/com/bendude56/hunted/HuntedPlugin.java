package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bendude56.hunted.config.SettingsFile;

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
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName()
					+ ChatColor.RED + " plugin has been disabled!");
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
		if (settings.WORLD.value != manhuntWorld.getName()
				&& Bukkit.getWorld(settings.WORLD.value) != null) {
			manhuntWorld = Bukkit.getWorld(settings.WORLD.value);
		}
		worlddata = new WorldDataFile();
		game = new Game();
		new CmdExec();
		//Register Events
		getServer().getPluginManager().registerEvents(new HuntedPlayerListener(), this);
		getServer().getPluginManager().registerEvents(new HuntedBlockListener(), this);
		getServer().getPluginManager().registerEvents(new HuntedEntityListener(), this);
		
		// new HuntedInventoryListener();
		log(Level.INFO, "Version " + getDescription().getVersion()
				+ " loaded into memory...");
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld() == getWorld()) {
				game.onLogin(p);
			}
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName()
					+ " v" + getDescription().getVersion() + ChatColor.YELLOW
					+ " is up running!");
		}
	}

	public void log(Level level, String message) {
		log.log(level, "[" + this.getDescription().getName() + "] " + message);
	}

	public static HuntedPlugin getInstance() {
		return (HuntedPlugin) Bukkit.getServer().getPluginManager()
				.getPlugin("Manhunt");
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

	public void setWorld(World newWorld) {
		if (newWorld != null && newWorld != manhuntWorld) {
			worlddata.saveWorldFile();
			game.broadcastAll(ChatColor.RED
					+ "The Manhunt world has been moved to \""
					+ newWorld.getName() + "\"");
			game.broadcastAll(ChatColor.RED
					+ "Type \"/m spawn\" to warp there now!");

			manhuntWorld = newWorld;

			settings.WORLD.value = manhuntWorld.getName();
			settings.WORLD.save();
			worlddata.loadWorldFile();
			game.reloadPlayers();
			game.broadcastAll(ChatColor.GREEN
					+ "This world is the new Manhunt world!");
			game.broadcastHunters(ChatColor.WHITE + "You have joined team "
					+ ChatColor.DARK_RED + "Hunters.");
			game.broadcastHunted(ChatColor.WHITE + "You have joined team "
					+ ChatColor.BLUE + "Prey.");
			game.broadcastSpectators(ChatColor.WHITE + "You have become a "
					+ ChatColor.YELLOW + "Spectator.");
		}
	}
}
