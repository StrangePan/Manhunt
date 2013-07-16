package com.deaboy.manhunt.finder;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;

public class FinderUtil
{

	public static void sendMessageNoOnlinePrey(Player p)
	{
		GameLobby lobby;
		Team t;
		
		if (Manhunt.getLobby(p.getName()).getType() != LobbyType.GAME)
		{
			return;
		}
		
		lobby = (GameLobby) Manhunt.getLobby(p.getName());
		
		if (lobby.getPlayerTeam(p.getName()) == Team.HUNTERS)
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
	
	
}