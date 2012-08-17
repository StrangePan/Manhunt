package com.bendude56.hunted.finder;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;


import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.teams.TeamManager;
import com.bendude56.hunted.teams.TeamManager.Team;

public class FinderUtil {

	public static void sendMessageNoOnlinePrey(Player p)
	{
		TeamManager teams = ManhuntPlugin.getInstance().getTeams();
		
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			p.sendMessage(ChatColor.RED + "There are no prey online! :(");
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			p.sendMessage(ChatColor.RED + "There are no hunters online! :(");
		}
	}

	public static void sendMessageFinderInitialize(Player p)
	{
		p.sendMessage(ChatColor.GOLD + "Prey Finder activated! Stand still for " + 8 + " seconds.");
	}

	public static void sendMessageFinderCancel(Player p)
	{
		p.sendMessage(ChatColor.RED + "The Prey Finder was cancelled!");
	}

	private static void sendMessageFinderResultsNearby(Player p)
	{
		TeamManager teams = ManhuntPlugin.getInstance().getTeams();
		
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			p.sendMessage(ChatColor.GOLD + "The nearest " + ChatColor.BLUE + "Prey" + ChatColor.GOLD + " is " + ChatColor.BLUE + "very close by" + ChatColor.GOLD + "!");
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			p.sendMessage(ChatColor.GOLD + "The nearest " + ChatColor.DARK_RED + "Hunter" + ChatColor.GOLD + " is " + ChatColor.DARK_RED + "very close by" + ChatColor.GOLD + "!");
		}
	}

	/**
	 * Tells the player the direction to the nearest enemy, or if
	 * the nearest enemy is within 10 blocks of them.
	 * @param p
	 */
	public static void sendMessageFinderResults(Player p)
	{
		TeamManager teams = ManhuntPlugin.getInstance().getTeams();
		
		//GET ALL ENEMIES
		List<Player> enemies;
		
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			enemies = teams.getTeamPlayers(Team.PREY);
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			enemies = teams.getTeamPlayers(Team.HUNTERS);
		}
		else
		{
			return;
		}
		
		//GET NEAREST ENEMY and the DISTANCE TO THEM
		Player enemy = null;
		double distance = -1;
		
		for (Player e : enemies)
		{
			double d = ManhuntUtil.getDistance(p, enemy, true);
			
			if (distance == -1 || d < distance)
			{
				distance = d;
				enemy = e;
			}
		}
		
		//SEND PREEMPTIVE MESSAGES
		if (enemy == null)
		{
			sendMessageNoOnlinePrey(p);
			return;
		}
		
		if (distance < 16)
		{
			sendMessageFinderResultsNearby(p);
			return;
		}
		
		//GET DIRECTIONAL INFORMATION
		double angle;
		double relative_angle;
		String direction;
		String relative_direction;
		
		angle = ManhuntUtil.getDirection(p.getLocation(), enemy.getLocation());
		relative_angle = ManhuntUtil.getDirectionFacing(p);
		relative_angle = ManhuntUtil.getDirectionDifference(angle, relative_angle);
		
		if (angle > 338)
			direction = "South";
		else if (angle > 293)
			direction = "South-West";
		else if (angle > 248)
			direction = "West";
		else if (angle > 203)
			direction = "North-West";
		else if (angle > 158)
			direction = "North";
		else if (angle > 113)
			direction = "North-East";
		else if (angle > 68)
			direction = "East";
		else if (angle > 23)
			direction = "South-East";
		else
			direction = "South";
		
		if (relative_angle > 315)
			relative_direction = "ahead of you";
		else if (relative_angle > 225)
			relative_direction = "to your right";
		else if (relative_angle > 135)
			relative_direction = "behind you";
		else if (relative_angle > 45)
			relative_direction = "to your left";
		else
			relative_direction = "ahead of you";
		
		//SEND THE ACTUAL RESULT
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			p.sendMessage(ChatColor.GOLD + "The nearest " + ChatColor.BLUE + "Prey" + ChatColor.GOLD + " is " + ChatColor.BLUE + direction + ChatColor.GOLD + " of you! (Somehere " + ChatColor.BLUE + relative_direction + ChatColor.GOLD + ".)");
			enemy.sendMessage(ChatColor.GOLD + "--[   " + ChatColor.RED + "A " + ChatColor.DARK_RED + "Prey Finder 9000" + ChatColor.RED + " has gotten your location!" + ChatColor.GOLD + "   ]---");
			return;
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			p.sendMessage(ChatColor.GOLD + "The nearest " + ChatColor.DARK_RED + "Hunter" + ChatColor.GOLD + " is " + ChatColor.DARK_RED + direction + ChatColor.GOLD + " of you! (Somehere " + ChatColor.DARK_RED + relative_direction + ChatColor.GOLD + ".)");
			enemy.sendMessage(ChatColor.GOLD + "--[   " + ChatColor.RED + "A " + ChatColor.BLUE + "Prey Finder 9000" + ChatColor.RED + " has gotten your location!" + ChatColor.GOLD + "   ]---");
			return;
		}
		else
		{
			p.sendMessage(ChatColor.RED + "Sorry... you aren't a hunter or a prey.");
			return;
		}
		
	}

}
