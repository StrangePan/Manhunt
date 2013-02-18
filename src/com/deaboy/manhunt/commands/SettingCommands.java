package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.settings.Setting;

public abstract class SettingCommands
{
	
	public static boolean msettings(CommandSender sender, String args[])
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<Setting> settings;
		
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		
		// Get the page #
		if (args.length == 0)
			page = 1;
		else if (args[0].equalsIgnoreCase("all"))
			all = true;
		else try
		{
			page = Integer.parseInt(args[0]);	
		}
		catch (NumberFormatException e)
		{
			page = 1;
		}
		
		page--;
		
		// Assemble list of settings
		settings = Manhunt.getSettings().getVisibleSettings();
		
		if (!all)
		{
			if (page * perpage > settings.size() - 1 )
				page = (settings.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
		}
		
		if (settings.size() == 0)
		{
			sender.sendMessage("There are no settings to display.");
			return true;
		}
		
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Settings " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) settings.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /msettings [n] to get page n of settings");
			settings = settings.subList(page * perpage, Math.min( (page + 1) * perpage, settings.size() ));
		}
		for (Setting setting : settings)
		{
			sender.sendMessage(ChatColor.GOLD + setting.getLabel() + " " + ChatColor.GREEN + "[" + setting.getValue().toString() + "]  " + ChatColor.WHITE + setting.getDescription());
		}
		return true;
		
		
		
		
		
	}
	
	public static boolean mset(CommandSender sender, String args[])
	{
		Setting setting = null;
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		
		// Check the arguments
		if (args.length != 2)
		{
			sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + "/mset <setting> < value>\nChange a global Manhunt setting. Use " + ChatColor.GOLD + "/msettings" + ChatColor.WHITE + " to view a list of available settings.");
			return true;
		}
		
		
		for (Setting s : Manhunt.getSettings().getVisibleSettings())
			if (s.getLabel().equalsIgnoreCase(args[0]))
			{
				setting = s;
				break;
			}
		
		if (setting == null)
		{
			sender.sendMessage(ChatColor.RED + "That setting does not exist, or is not visible.");
			return true;
		}
		
		if (setting.setValue(args[1]))
		{
			sender.sendMessage(ChatColor.GOLD + setting.getLabel() + ChatColor.GREEN + " has been set to " + ChatColor.GOLD + "[" + setting.getValue() + "]");
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + setting.getDescription());
			return true;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Invalid value for that setting.");
			return true;
		}
		
		
	}
	
}
