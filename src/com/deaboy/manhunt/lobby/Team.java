package com.deaboy.manhunt.lobby;

import org.bukkit.ChatColor;

public enum Team
{
	HUNTERS, PREY, SPECTATORS, STANDBY, NONE;
	
	public static Team fromString(String team)
	{
		if (team.equalsIgnoreCase("hunter") || team.equalsIgnoreCase("hunters"))
			return HUNTERS;
		if (team.equalsIgnoreCase("prey"))
			return PREY;
		if (team.equalsIgnoreCase("spectator") || team.equalsIgnoreCase("spectators"))
			return SPECTATORS;
		if (team.equalsIgnoreCase("standby"))
			return STANDBY;
		if (team.equalsIgnoreCase("none") || team.equalsIgnoreCase("null"))
			return NONE;
		return null;
	}
	
	public ChatColor getColor()
	{
		switch (this)
		{
		case HUNTERS:	return ChatColor.DARK_RED;
		case PREY:		return ChatColor.BLUE;
		case SPECTATORS:return ChatColor.YELLOW;
		case STANDBY:	return ChatColor.WHITE;
		default:		return ChatColor.WHITE;
		}
	}
	
	public String getName(boolean plural)
	{
		switch (this)
		{
		case HUNTERS:	return (plural ? "Hunters" : "Hunter");
		case PREY:		return (plural ? "Prey" : "Prey");
		case SPECTATORS:return (plural ? "Spectators" : "Spectator");
		case STANDBY:	return (plural ? "Standbies" : "Standby");
		default:		return "";
		}
	}
}
