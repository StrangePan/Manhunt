package com.bendude56.hunted.commands;

import org.bukkit.ChatColor;

public class CommandUtil
{
	public static String NO_PERMISSION = ChatColor.RED + "You don't have permission to do that.";
	public static String GAME_RUNNING = ChatColor.RED + "You can't do that while the game is running.";
	public static String NO_GAME_RUNNING = ChatColor.RED + "There are no Manhunt games running.";
	public static String IS_SERVER = ChatColor.RED + "The server cannot do that.";
	public static String WRONG_WORLD = ChatColor.RED + "The player must be in the manhunt world first!";
	public static String LOCKED = ChatColor.RED + "The teams are locked.";
}
