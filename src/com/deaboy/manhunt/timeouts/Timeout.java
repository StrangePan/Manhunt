package com.deaboy.manhunt.timeouts;

import java.io.Closeable;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.NewManhuntPlugin;
import com.deaboy.manhunt.lobby.Lobby;

public class Timeout implements Closeable
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
		this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(NewManhuntPlugin.getInstance(), new Runnable()
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
		Lobby lobby = Manhunt.getLobby(lobby_id);
		
		Player p = Bukkit.getPlayer(player_name);
		
		if (p == null || p.getWorld() != lobby.getCurrentMap().getWorld())
		{
			Manhunt.getTimeoutManager().stopTimeout(this);
		}
	}
	
	@Override
	public void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
	}
	
}
