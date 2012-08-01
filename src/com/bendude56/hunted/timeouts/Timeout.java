package com.bendude56.hunted.timeouts;

import java.util.Date;

import org.bukkit.Bukkit;

import com.bendude56.hunted.HuntedPlugin;

public class Timeout {
	public final String player_name;
	public final Long boot_time;
	private final int schedule;
	
	private TimeoutManager manager;

	public Timeout(String player_name, TimeoutManager manager)
	{
		this.player_name = player_name;
		this.boot_time = (new Date()).getTime() + manager.getGame().getPlugin().getSettings().OFFLINE_TIMEOUT.value * 1000;
		
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
		if ((new Date()).getTime() >= boot_time)
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
