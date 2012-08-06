package com.bendude56.hunted.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * Contains various static methods for messages and other game-related things.
 * @author Deaboy
 *
 */
public class GameUtil {
	/**
	 * Broadcasts a string to the given Team(s)
	 * @param message The message to broadcast
	 * @param team The Team(s) to broadcast to
	 */
	public static void broadcast(String message, Team...team)
	{
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		
		for (Team t : team)
		{
			for (Player p : plugin.getTeams().getTeamPlayers(t))
			{
				p.sendMessage(message);
			}
		}
	}

	/**
	 * Makes a player invisible to all other players on the server
	 * @param p
	 */
	public static void makeInvisible(Player p)
	{
		for (Player p2 : Bukkit.getOnlinePlayers())
		{
			p2.hidePlayer(p);
		}
	}

	/**
	 * Makes a player visible to all other players on the server
	 * @param p
	 */
	public static void makeVisible(Player p)
	{
		for (Player p2: Bukkit.getOnlinePlayers())
		{
			p2.showPlayer(p);
		}
	}

}
