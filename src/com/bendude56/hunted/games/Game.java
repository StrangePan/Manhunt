package com.bendude56.hunted.games;

import org.bukkit.World;

import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.teams.TeamManager;
import com.bendude56.hunted.timeouts.TimeoutManager;

/**
 * This class is an instance class (only one running per world at a time)
 * Owns it's own timeout and finder managers. Also contains game logic.
 * @author The Scoop
 *
 */
public class Game
{
	public final GameManager manager;
	public final World world;
	public TeamManager teams;
	public TimeoutManager timeouts;
	public FinderManager finders;
	
	private Long end_hunt_tick;
	private Long end_setup_tick;
	
	public Game(GameManager manager)
	{
		//Initialize important classes
		this.manager = manager;
		this.world = manager.mWorld.world;
		this.teams = manager.mWorld.teams;
		this.timeouts = new TimeoutManager(this);
		this.finders = new FinderManager();
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
	
	private void destroySelf()
	{
		timeouts = null;
		finders = null;
	}
	
}