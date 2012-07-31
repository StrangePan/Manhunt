package com.bendude56.hunted.timeouts;

import org.bukkit.Bukkit;

import com.bendude56.hunted.HuntedPlugin;

public class Timeout {
	public final String player_name;
	public final Long boot_tick;
	private final int schedule;
	
	private TimeoutManager manager;

	public Timeout(String player_name, Long boot_tick, TimeoutManager manager)
	{
		this.player_name = player_name;
		this.boot_tick = boot_tick;
		
		this.manager = manager;
		
		//Start the scheduler
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(HuntedPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 5);
	}
	
	public void onTick()
	{
		if (manager.getGame().getPlugin().getWorld().getFullTime() >= boot_tick)
		{
			forfeitPlayer();
		}
	}
	
	private void forfeitPlayer()
	{
		//Cancel the tick scheduler
		Bukkit.getScheduler().cancelTask(schedule);
		
		//Forfeit the player from the game
		manager.getGame().onPlayerForfeit(player_name);
		
		//Delete the Timeout from the TimeoutManager
		manager.stopTimeout(this);
	}
	
	protected void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		manager = null;
	}
	
}
