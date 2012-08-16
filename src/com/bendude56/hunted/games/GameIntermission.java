package com.bendude56.hunted.games;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.teams.TeamManager.Team;

public class GameIntermission
{
	private ManhuntPlugin plugin;
	
	private Long restartTime;
	
	private Integer schedule;
	private Integer countdown;
	
	public GameIntermission(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
		
		int minutes = plugin.getSettings().INTERMISSION.value < 1 ? 1 : plugin.getSettings().INTERMISSION.value;
		restartTime = new Date().getTime() + minutes * 60000;
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 1);
		
		countdown = minutes;
	}
	
	private void onTick()
	{
		Long time = new Date().getTime();
		
		if (countdown > 0 && time > restartTime - 60000*countdown)
		{
			GameUtil.broadcast(ChatManager.bracket1_ + "Manhunt will begin in " + ChatColor.BLUE + countdown + (countdown > 1 ? " minutes." : " minute.") + ChatManager.bracket2_, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
			countdown--;
		}
		if (time > restartTime)
		{
			if (plugin.getTeams().getAllPlayers(true).size() >= plugin.getSettings().MINIMUM_PLAYERS.value)
			{
				plugin.startGame();
			}
			else
			{
				GameUtil.broadcast(ChatManager.bracket1_ + ChatColor.RED + "There are not enough players to start the game." + ChatManager.bracket2_, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
				restartTime += 60000;
				countdown = 1;
			}
			close();
			
		}
	}
	
	public void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		plugin = null;
	}

}
