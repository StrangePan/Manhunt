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
import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.games.Game;
import com.bendude56.hunted.listeners.BlockEventHandler;
import com.bendude56.hunted.listeners.EntityEventHandler;
import com.bendude56.hunted.listeners.PlayerEventHandler;
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
	
	private World 	manhuntWorld;
	
	public Boolean locked = false;

	@Override
	public void onEnable()
	{
		//Start up all the classes.

		settings =	new SettingsManager();
		loadouts =	new LoadoutManager();
		teams =		new TeamManager(this);
		chat =		new ChatManager(this);
		game =		null;
		new CommandSwitchboard();
		
		manhuntWorld = Bukkit.getWorld(settings.WORLD.value);
		
		//Register Events
		getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);
		getServer().getPluginManager().registerEvents(new BlockEventHandler(this), this);
		getServer().getPluginManager().registerEvents(new EntityEventHandler(this), this);
		
	}

	@Override
	public void onDisable()
	{
		
		teams.restoreAllGameModes();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
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
		stopGame();
		game = new Game(this);
	}
	
	public void stopGame()
	{
		if (game == null)
		{
			return;
		}
		game.stopGame(false);
		game = null;
	}

	public void setWorld(World world)
	{
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
			startGame();
		}
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
		return game != null;
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