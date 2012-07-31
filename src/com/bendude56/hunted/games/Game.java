package com.bendude56.hunted.games;

import org.bukkit.World;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.teams.TeamManager;
import com.bendude56.hunted.timeouts.TimeoutManager;

/**
 * This class is a persistent class that manages a Manhunt game.
 * Owns it's own timeout and finder managers. Also contains game logic.
 * @author Deaboy
 *
 */
public class Game
{
	private World world;
	private HuntedPlugin plugin;
	public TimeoutManager timeouts;
	public FinderManager finders;
	
	private Long end_hunt_tick;
	private Long end_setup_tick;
	
	public Game(HuntedPlugin plugin)
	{
		//Initialize important classes
		this.plugin = plugin;
		this.world = plugin.getWorld();
		this.timeouts = new TimeoutManager(this);
		this.finders = new FinderManager(this);
	}
	
	/*
	 * Gives a 30 second countdown until the game starts
	 * gradually sets the world's time.
	 * Teleports the hunters to their setup spawn, sets their loadouts, sets their stats
	 * Teleports the prey to their spawn, sets their loadouts, sets their stats
	 * 
	 * Releases the hunters
	 * 
	 * Lets players know every time a day has passed
	 * 
	 * Lets players know when the last day has come
	 * 
	 * Counts down the last 10 seconds of the game
	 * 
	 * Resets the world, reset the player gamemodes, etc.
	 * FORGETS ITS OWN TIMEOUT MANAGER AND FINDER MANAGER
	 */

	/**
	 * Returns this class's pointer to the Manhunt plugin for quick access.
	 * @return
	 */
	public HuntedPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Closes the class and all of the classes it tracks
	 */
	public void close()
	{
		timeouts.close();
		timeouts = null;
		finders.close();
		finders = null;
		plugin = null;
	}

}