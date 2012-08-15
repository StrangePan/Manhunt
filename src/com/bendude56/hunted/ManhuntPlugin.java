package com.bendude56.hunted;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.commands.CommandSwitchboard;
import com.bendude56.hunted.events.BlockEventHandler;
import com.bendude56.hunted.events.EntityEventHandler;
import com.bendude56.hunted.events.PlayerEventHandler;
import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.games.Game;
import com.bendude56.hunted.games.GameIntermission;
import com.bendude56.hunted.games.GameUtil;
import com.bendude56.hunted.loadouts.LoadoutManager;
import com.bendude56.hunted.settings.SettingsManager;
import com.bendude56.hunted.teams.TeamManager;

public class ManhuntPlugin extends JavaPlugin {

	private Logger log = Logger.getLogger("Minecraft");
	
	private SettingsManager	settings;
	private LoadoutManager	loadouts;
	private TeamManager		teams;
	private FinderManager	finders;
	private Game			game;
	private ChatManager		chat;
	private GameIntermission intermission;
	
	private World 	manhuntWorld;
	
	public Boolean locked = false;

	@Override
	public void onEnable()
	{
		//Start up all the classes.

		settings =	new SettingsManager();
		
		manhuntWorld = Bukkit.getWorld(settings.WORLD.value);
		
		loadouts =	new LoadoutManager();
		teams =		new TeamManager(this);
		chat =		new ChatManager(this);
		game =		null;
		intermission = (settings.MANHUNT_MODE.value == ManhuntMode.PUBLIC ? new GameIntermission(this) : null);
		new CommandSwitchboard();
		
		//Register Events
		getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);
		getServer().getPluginManager().registerEvents(new BlockEventHandler(this), this);
		getServer().getPluginManager().registerEvents(new EntityEventHandler(this), this);
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName()
					+ ChatColor.GREEN + " has been enabled.");
		}
	}

	@Override
	public void onDisable()
	{
		teams.restoreAllOriginalPlayerStates();
		forgetGame();
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			GameUtil.makeVisible(p);
			p.sendMessage(ChatColor.DARK_RED + getDescription().getName()
					+ ChatColor.RED + " has been disabled.");
		}
		
		
		log(Level.INFO, "Unloaded from memory.");
	}

	public void log(Level level, String message) {
		log.log(level, "[" + this.getDescription().getName() + "] " + message);
	}

	public static ManhuntPlugin getInstance() {
		return (ManhuntPlugin) Bukkit.getServer().getPluginManager().getPlugin("Manhunt");
	}
	
	public void startGame()
	{
		forgetGame();
		
		if (getSettings().MANHUNT_MODE.value == ManhuntMode.PUBLIC)
		{
			if (getTeams().getAllPlayers(true).size() >= getSettings().MINIMUM_PLAYERS.value);
			{
				getTeams().randomizeTeams();
				game = new Game(this);
			}
		}
		else
		{
			game = new Game(this);
		}
	}
	
	public void forgetGame()
	{
		game = null;
	}

	public void setWorld(World world)
	{
		if (!gameIsRunning())
		
		settings.WORLD.setValue(world.getName());
		settings.saveAll();
		
		this.manhuntWorld = world;
		
		settings = new SettingsManager();
		loadouts = new LoadoutManager();
		teams.refreshPlayers();
	}

	public void setMode(ManhuntMode mode)
	{
		settings.MANHUNT_MODE.setValue(mode);
		
		if (mode == ManhuntMode.PUBLIC)
		{
			startIntermission();
		}
	}

	public void startIntermission()
	{
		startIntermission(false);
	}
	
	public void startIntermission(boolean CancelLast)
	{
		
		if (intermission != null && CancelLast)
		{
			intermission.close();
		}
		intermission = new GameIntermission(this);
	}
	
	public void cancelIntermission()
	{
		intermission.close();
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
	
	public TeamManager getTeams()
	{
		return teams;
	}

	public ChatManager getChat()
	{
		return chat;
	}
	
	public FinderManager getFinders()
	{
		return finders;
	}

	public Game getGame() {
		return game;
	}
	
	public boolean gameIsRunning()
	{
		return (game != null);
	}
	
	public enum ManhuntMode
	{
		PRIVATE, PUBLIC;
		
		public static ManhuntMode fromString(String s)
		{
			if (s.equalsIgnoreCase("private"))
				return PRIVATE;
			if (s.equalsIgnoreCase("public"))
				return PUBLIC;
			return null;
		}
		
		public String toString()
		{
			switch (this)
			{
				case PRIVATE:	return "PRIVATE";
				case PUBLIC:	return "PUBLIC";
				default:		return null;
			}
		}
	}

}
