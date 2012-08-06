package com.bendude56.hunted.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * Simple class full of static methods for messaging players and teams.
 * @author Deaboy
 *
 */
public class ChatManager {
	private HuntedPlugin plugin;

	public final static ChatColor color = ChatColor.GOLD;
	public final static String bracket1 = color + "---[";
	public final static String bracket1_ = bracket1 + " ";
	public final static String bracket2 = color + "]---";
	public final static String bracket2_ = " " + bracket2;
	public final static String leftborder = color + "|  ";
	public final static String divider = color + "--------------------";

	public void onPlayerchat(PlayerChatEvent e)
	{
		
	}

	public ChatManager(HuntedPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void broadcastAll(String s, boolean spectators)
	{
		for (Player p : plugin.getTeams().getAllPlayers(spectators))
		{
			p.sendMessage(s);
		}
	}
	
	public void broadcastTeam(String s, Team t)
	{
		for (Player p : plugin.getTeams().getTeamPlayers(t))
		{
			p.sendMessage(s);
		}
	}
}
