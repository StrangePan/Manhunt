package com.bendude56.hunted.teams;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * A class with static team-related methods.
 * @author Deaboy
 *
 */
public class TeamUtil {

	/**
	 * Alerts the player to which team they have joined.
	 * @param p
	 */
	public static void sendMessageJoinTeam(Player p)
	{
		TeamManager teams = ManhuntPlugin.getInstance().getTeams();
		
		p.sendMessage(ChatColor.GOLD + "You have joined the " + getTeamColor(teams.getTeamOf(p)) + getTeamName(teams.getTeamOf(p), true) + ChatColor.GOLD + ".");
	}

	/**
	 * Returns the color of the team.
	 * @param t
	 * @return
	 */
	public static ChatColor getTeamColor(Team t)
	{
		switch (t) {
			case HUNTERS:	return ChatColor.DARK_RED;
			case PREY:		return ChatColor.BLUE;
			case SPECTATORS:return ChatColor.YELLOW;
			default:		return ChatColor.WHITE;
		}
	}

	/**
	 * Returns the name of the team the player belongs to.
	 * @param team
	 * @param plural Whether or not to return the plural name.
	 * @return
	 */
	public static String getTeamName(Team team, boolean plural)
	{
		switch (team) {
			case HUNTERS:	return (plural ? "Hunters" : "Hunter");
			case PREY:		return (plural ? "Prey" : "Prey");
			case SPECTATORS:return (plural ? "Spectators" : "Spectator");
			default:		return "";
		}
	}

}
