package com.bendude56.hunted.games;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * Contains various static methods for messages and other game-related things.
 * @author Deaboy
 *
 */
public class GameUtil {

	/**
	 * Broadcast to everyone that the game has started
	 */
	public static void broadcastGameStart() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Broadcasts to all players the results of the match.
	 * @param winners
	 * @param losers
	 */
	protected static void broadcastManhuntWinners(Team winners, Team losers) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Announces that the prey have won because the time ran out.
	 */
	protected static void broadcastTimoeutWinners()
	{
		// TODO
	}

	/**
	 * Broadcasts to all players that a player has forfeit.
	 * @param player_name
	 */
	protected static void broadcastPlayerForfeit(String player_name) {
		// TODO Auto-generated method stub
		
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
