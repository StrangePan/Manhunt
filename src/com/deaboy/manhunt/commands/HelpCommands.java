package com.deaboy.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.chat.ChatManager;

public class HelpCommands
{
	
	public static boolean manhunt(CommandSender sender, String[] args)
	{
		int command;
		boolean console;
		
		console = sender instanceof ConsoleCommandSender;
		
		if (args.length == 0)
			command = 0;
		else if (args[0].equalsIgnoreCase("help"))
			command = 0;
		else if (args[0].equalsIgnoreCase("info"))
			command = 1;
		else
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "Unknown command \"" + args[0] + "\"");
			return false;
		}
		
		
		if (command == 0)
		{
			try
			{
				command = (args.length == 2 ? Integer.parseInt(args[1]) : 1);
			}
			catch (NumberFormatException e)
			{
				command = 1;
			}
			
			Bukkit.dispatchCommand(sender, "help manhunt " + command);
			return true;
		}
		else if (command == 1)
		{
			sender.sendMessage(ChatManager.bracket1_ + ChatColor.DARK_RED + ManhuntPlugin.getInstance().getDescription().getName() + " info" + ChatManager.bracket2_);
			sender.sendMessage(ChatManager.leftborder + ChatColor.DARK_BLUE + "Version:  " + ChatColor.GREEN + ManhuntPlugin.getInstance().getDescription().getVersion());
			sender.sendMessage(ChatManager.leftborder + ChatColor.DARK_BLUE + "Author:   " + ChatColor.GREEN + ManhuntPlugin.getInstance().getDescription().getAuthors().get(0));
			return true;
		}
		else
		{
			return false;
		}
		
		
	}
	
	
	
}
