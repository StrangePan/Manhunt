package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;



import com.bendude56.hunted.finder.IFinderManager;
import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.loadouts.LoadoutManager;
import com.bendude56.hunted.settings.SettingsManager;
import com.bendude56.hunted.teams.ITeamManager;
import com.bendude56.hunted.teams.TeamManager;

public class HuntedPlugin extends JavaPlugin {

	private Logger log = Logger.getLogger("Minecraft");
	
	private SettingsManager	settings;
	private LoadoutManager	loadouts;
	private ITeamManager	teams;
	private IFinderManager	finders;
	private ManhuntGame		game;
	public SpConnect		spoutConnect;
	
	public boolean	spoutEnabled;
	private World 	manhuntWorld;

	@Override
	public void onEnable()
	{
		
		//Start up all the classes.
		if (getServer().getPluginManager().isPluginEnabled("Spout"))
		{
			spoutEnabled = true;
			spoutConnect = new SpConnect();
		}
		else
		{
			spoutEnabled = false;
		}

		settings =	new SettingsManager();
		loadouts =	new LoadoutManager();
		teams =		new TeamManager();
		finders =	new FinderManager();
		game =		new ManhuntGame();
		new CmdExec();
		
		manhuntWorld = Bukkit.getWorld(settings.WORLD.value);
		
		//Register Events
		getServer().getPluginManager().registerEvents(new HuntedPlayerListener(), this);
		getServer().getPluginManager().registerEvents(new HuntedBlockListener(), this);
		getServer().getPluginManager().registerEvents(new HuntedEntityListener(), this);
		
	}

	@Override
	public void onDisable()
	{
		
		//Save players who are in creative mode in the Team Manager
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName()
					+ ChatColor.RED + " has been disabled.");
		}
		
		log(Level.INFO, "Unloaded from memory.");
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
	
	public IFinderManager getFinders()
	{
		return finders;
	}

	public ManhuntGame getGame() {
		return game;
	}

}
