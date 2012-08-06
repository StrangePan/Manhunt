package com.bendude56.hunted.games;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.games.Game.GameStage;

public class GameEvents
{
	private Game game;
	private World world;
	private int schedule;
	
	private Long start_setup_tick;
	private Long start_hunt_tick;
	private Long stop_hunt_tick;
	
	private Long start_timechange;
	private Long stop_timechange;
	
	/**
	 * This instanced class is meant to assist the Game class by handling the
	 * tedious, scheduled task of handling timed events.
	 * @param game
	 */
	public GameEvents(Game game)
	{
		this.game = game;
		this.world = game.getPlugin().getWorld();
		
		this.start_setup_tick = game.getStageStartTick(GameStage.SETUP);
		this.start_hunt_tick = game.getStageStartTick(GameStage.HUNT);
		this.stop_hunt_tick = game.getStageStopTick(GameStage.HUNT);
		
		start_timechange = world.getFullTime() + 200; //Start changing the time 10 seconds after pregame starts
		stop_timechange = start_setup_tick - 200; //And stop 10 seconds before the game starts
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(HuntedPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 1);
		
	}
	
	private void onTick()
	{
		Long time = world.getFullTime();
		
		if (time > start_timechange && time < stop_timechange)
		{
			if (start_setup_tick - time > 1200)
			{
				world.setFullTime(world.getFullTime() + 400);
			}
			else if (start_setup_tick - time > 40)
			{
				world.setFullTime(world.getFullTime() + 40);
			}
			else
			{
				world.setFullTime(stop_timechange);
			}
		}
		else if (true) //TODO
		{
			
		}
	}
	
	public void close()
	{
		game = null;
		Bukkit.getScheduler().cancelTask(schedule);
	}

}
