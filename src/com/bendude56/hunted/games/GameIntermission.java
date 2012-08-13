package com.bendude56.hunted.games;

import java.util.Date;

import org.bukkit.Bukkit;

import com.bendude56.hunted.ManhuntPlugin;

public class GameIntermission
{
	private ManhuntPlugin plugin;
	
	private Long restartTime;
	
	private Integer schedule;
	
	public GameIntermission(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
		
		restartTime = new Date().getTime() + plugin.getSettings().INTERMISSION.value * 60000;
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 1);
	}
	
	private void onTick()
	{
		Long time = new Date().getTime();
		
		if (time > restartTime)
		{
			plugin.startGame();
			
			close();
		}
	}
	
	public void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		plugin = null;
	}

}
