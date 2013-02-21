package com.deaboy.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.ManhuntPlugin;

public class HelpCommands
{
	
	public static boolean manhunt(CommandSender sender, String[] args)
	{
		if (args.length == 0 || args[0].equalsIgnoreCase("help"))
		{
			Bukkit.dispatchCommand(sender, "help manhunt " + (args.length == 0 ? "" : args[1]));
		}
		else if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("info"))
		{
			Bukkit.dispatchCommand(sender, "version " + ManhuntPlugin.getInstance().getDescription().getName());
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	
	
}
