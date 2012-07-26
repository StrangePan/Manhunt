package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import preyfinder.IPreyFinderManager;
import preyfinder.PreyFinderManager;

import teams.ITeamManager;
import teams.TeamManager;

import com.bendude56.hunted.config.SettingsManager;
import com.bendude56.hunted.loadout.LoadoutManager;

public class HuntedPlugin extends JavaPlugin {

	private Logger log = Logger.getLogger("Minecraft");
	public boolean spoutEnabled;
	public SpConnect spoutConnect;
	private World manhuntWorld;
	private SettingsManager settings;
	private LoadoutManager loadouts;
	private ITeamManager teams;
	private IPreyFinderManager finders;
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
		settings = new SettingsManager();
		if (settings.WORLD.value != manhuntWorld.getName()
				&& Bukkit.getWorld(settings.WORLD.value) != null) {
			manhuntWorld = Bukkit.getWorld(settings.WORLD.value);
		}
		loadouts = new LoadoutManager();
		teams = new TeamManager();
		finders = new PreyFinderManager();
		
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

	public SettingsManager getSettings() {
		return settings;
	}
	
	public LoadoutManager getLoadouts() {
		return loadouts;
	}
	
	public ITeamManager getTeams()
	{
		return teams;
	}
	
	public IPreyFinderManager getPreyFinder()
	{
		return finders;
	}

	public Game getGame() {
		return game;
	}
}
