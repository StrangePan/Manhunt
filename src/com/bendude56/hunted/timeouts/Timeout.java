package com.bendude56.hunted.timeouts;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.lobby.GameLobby;

public class Timeout
{
	public final String player_name;
	public final Long boot_time;
	private final int schedule;
	private final long lobby_id;

	public Timeout(String player_name, long lobby, long time)
	{
		this.player_name = player_name;
		this.boot_time = time;
		this.lobby_id = lobby;
		
		//Start the scheduler
		this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 5);
	}
	
	public void onTick()
	{
		if (new Date().getTime() >= boot_time)
		{
			forfeitPlayer();
		}
	}
	
	private void forfeitPlayer()
	{
		GameLobby lobby = Manhunt.getLobby(lobby_id);
		
		Player p = Bukkit.getPlayer(player_name);
		
		if (p == null || p.getWorld() != lobby.getCurrentMap().getWorld())
		{
			Manhunt.getTimeoutManager().stopTimeout(this);
		}
		
		stop();
	}
	
	protected void stop()
	{
		// TODO Delete the Timeout from the TimeoutManager
		
		Bukkit.getScheduler().cancelTask(schedule);
		
	}
	
}
