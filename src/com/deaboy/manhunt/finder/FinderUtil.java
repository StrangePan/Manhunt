package com.deaboy.manhunt.finder;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;

public class FinderUtil
{

	public static void sendMessageNoOnlinePrey(Player p)
	{
		GameLobby lobby;
		Team t;
		
		lobby = Manhunt.getLobby(p);
		
		if (lobby.getPlayerTeam(p) == Team.HUNTERS)
		{
			t = Team.PREY;
		}
		else if (lobby.getPlayerTeam(p) == Team.PREY)
		{
			t = Team.HUNTERS;
		}
		else
		{
			return;
		}
		
		p.sendMessage(ChatColor.RED + "There are no " + t.getColor() + t.getName(true) + ChatColor.RED + " online! :(");
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
		GameLobby lobby;
		Team t;
		
		lobby = Manhunt.getLobby(p);
		
		if (lobby.getPlayerTeam(p) == Team.HUNTERS)
		{
			t = Team.PREY;
		}
		else if (lobby.getPlayerTeam(p) == Team.PREY)
		{
			t = Team.HUNTERS;
		}
		else
		{
			return;
		}
		p.sendMessage(ChatManager.color + "The nearest " + t.getColor() + t.getName(false) + ChatManager.color + " is " + t.getColor() + "very close by" + ChatManager.color + "!");
	}

	/**
	 * Tells the player the direction to the nearest enemy, or if
	 * the nearest enemy is within 10 blocks of them.
	 * @param p
	 */
	public static void sendMessageFinderResults(Player p)
	{
		/*
		
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
			double d = ManhuntUtil.getDistance(p, e, true);
			
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
			relative_direction = "to your left";
		else if (relative_angle > 135)
			relative_direction = "behind you";
		else if (relative_angle > 45)
			relative_direction = "to your right";
		else
			relative_direction = "ahead of you";
		
		//SEND THE ACTUAL RESULT
		Team t1, t2;
		
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			t1 = Team.HUNTERS;
			t2 = Team.PREY;
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			t1 = Team.PREY;
			t2 = Team.HUNTERS;
		}
		else
		{
			p.sendMessage(ChatColor.RED + "Sorry... you aren't a hunter or a prey.");
			return;
		}
		
		if (p.getFoodLevel() >= 4) p.setFoodLevel(p.getFoodLevel()-4);
		p.sendMessage(ChatManager.color + "The nearest " + t2.getColor() + t2.getName(false) + ChatManager.color + " is " + t2.getColor() + direction + ChatManager.color + " of you! (Somehere " + t2.getColor() + relative_direction + ChatManager.color + ".)");
		enemy.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "A " + t1.getColor() + "Prey Finder 9000" + ChatColor.RED + " has gotten your location!" + ChatManager.bracket2_);
		return;
		*/
	}

	/**
	 * Tells the player they've found the enemy and points their
	 * compass towards the enemy. Alerts the enemy that a PreyFinder
	 * has gotten their location.
	 * @param p
	 */
	public static void findNearestEnemy(Player p)
	{
		/*
		TeamManager teams = ManhuntPlugin.getInstance().getTeams();
		Team t1, t2;
		
		//GET ALL ENEMIES
		List<Player> enemies;
		
		if (teams.getTeamOf(p) == Team.HUNTERS)
		{
			t1 = Team.HUNTERS;
			t2 = Team.PREY;
		}
		else if (teams.getTeamOf(p) == Team.PREY)
		{
			t1 = Team.PREY;
			t2 = Team.HUNTERS;
		}
		else
		{
			return;
		}
		enemies = teams.getTeamPlayers(t2);
		
		//GET NEAREST ENEMY and the DISTANCE TO THEM
		Player enemy = null;
		double distance = -1;
		
		for (Player e : enemies)
		{
			double d = ManhuntUtil.getDistance(p, e, true);
			
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
		else
		{
			p.setCompassTarget(enemy.getLocation());
			p.sendMessage(ChatManager.bracket1_ + "The nearest " + t2.getColor() + t2.getName(false) + ChatManager.color + " has been found!" + ChatManager.bracket2_);
			enemy.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "A " + t1.getColor() + "Prey Finder 9000" + ChatColor.RED + " has gotten your location!" + ChatManager.bracket2_);
		}
		*/
	}

}
