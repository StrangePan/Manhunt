package com.bendude56.hunted.games;

import org.bukkit.World;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.teams.TeamManager.Team;
import com.bendude56.hunted.timeouts.TimeoutManager;

/**
 * This class is a persistent class that manages a Manhunt game.
 * Owns it's own timeout and finder managers. Also contains game logic.
 * @author Deaboy
 *
 */
public class Game
{
	private HuntedPlugin plugin;
	public TimeoutManager timeouts;
	public FinderManager finders;
	
	private World world; //Eh, why not?
	
	private Long start_setup_tick;
	private Long start_hunt_tick;
	private Long stop_hunt_tick;
	
	public Game(HuntedPlugin plugin)
	{
		//Initialize important classes
		this.plugin = plugin;
		this.timeouts = new TimeoutManager(this);
		this.finders = new FinderManager(this);
		
		//Save pointer to world
		this.world = plugin.getWorld();
		
		//Calculate milestones ticks
		Long start_setup_tick = world.getFullTime(); //Set up the start_setup_tick, giving it a baseline
		start_setup_tick += (24000 - start_setup_tick % 24000); //Calculating. Next day
		start_setup_tick += 12000 - (plugin.getSettings().SETUP_TIME.value * 1200); //Compensate for shorter setup times
		this.start_setup_tick = start_setup_tick; //Save the start_tick;
		
		Long start_hunt_tick = start_setup_tick; //Set up the start_hunt_tick, giving it a baseline
		start_hunt_tick += plugin.getSettings().SETUP_TIME.value * 1200;
		this.start_hunt_tick = start_hunt_tick; //Save the end_setup_tick
		
		Long stop_hunt_tick = start_hunt_tick; //Set up the end_hunt_tick, giving it a baseline.
		stop_hunt_tick += plugin.getSettings().DAY_LIMIT.value * 24000;
		this.stop_hunt_tick = stop_hunt_tick;
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
	 * Stops the Manhunt Game. Private, because only other in-class
	 * public methods may stop this game.
	 */
	private void stopGame() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Forfeits a player. Only meant to be used by Timeouts.
	 * @param player_name
	 */
	public void onPlayerForfeit(String player_name)
	{
		GameUtil.broadcastPlayerForfeit(player_name);
		plugin.getTeams().deletePlayer(player_name);
		
		checkTeamCounts();
	}

	/**
	 * Private method, checks team count and will stop the game if
	 * one team has won. 
	 */
	private void checkTeamCounts() //TODO Make this more efficient
	{
		Team winners;
		Team losers;
		if (plugin.getTeams().getTeamNames(Team.HUNTERS).size() == 0)
		{
			winners = Team.PREY;
			losers = Team.HUNTERS;
			GameUtil.broadcastManhuntWinners(winners, losers);
			stopGame();
		}
		else if (plugin.getTeams().getTeamNames(Team.PREY).size() == 0)
		{
			winners = Team.HUNTERS;
			losers = Team.PREY;
			GameUtil.broadcastManhuntWinners(winners, losers);
			stopGame();
		}
	}

	/**
	 * Returns this class's pointer to the Manhunt plugin for quick access.
	 * @return
	 */
	public HuntedPlugin getPlugin()
	{
		return plugin;
	}

	/**
	 * Closes ONLY the classes this class tracks
	 */
	public void close()
	{
		timeouts.close();
		timeouts = null;
		finders.close();
		finders = null;
	}


}