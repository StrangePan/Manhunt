package com.deaboy.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;

public abstract class MapCommands
{
	
	public static boolean mmap(CommandSender sender, String args[])
	{
		String args2[];
		
		if (args.length == 0)
		{
			Bukkit.getServer().dispatchCommand(sender, "help mmap");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list"))
		{
			args2 = new String[args.length - 1];
			for (int i = 1; i < args.length; i++)
				args2[i-1] = args[i];
				
			return listmaps(sender, args2);
		}
		
		if (args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("selected") || args[0].equalsIgnoreCase("sel"))
		{
			args2 = new String[args.length - 1];
			for (int i = 1; i < args.length; i++)
				args2[i-1] = args[i];
			
			return selectmap(sender, args2);
		}
		
		return false;
	}
	
	private static boolean listmaps(CommandSender sender, String args[])
	{
		final String spacing = "   ";
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return false;
		}
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatManager.bracket1_ + "All Loaded Manhunt Maps" + ChatManager.bracket2_);
			if (Manhunt.getWorlds().size() == 0)
			{
				sender.sendMessage(ChatColor.RED + "  No worlds loaded into Manhunt.");
			}
			for (World world : Manhunt.getWorlds())
			{
				sender.sendMessage(ChatColor.GOLD + world.getName());
				if (world.getMaps().size() == 0)
					sender.sendMessage(spacing + ChatColor.GRAY + "[NONE]");
				else
					for (Map map : world.getMaps())
						sender.sendMessage(spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
			}
		}
		else
		{
			sender.sendMessage(ChatManager.bracket1_ + "Loaded Manhunt Maps" + ChatManager.bracket2_);
			for (String wname : args)
			{
				World world = Manhunt.getWorld(wname);
				if (world == null)
				{
					sender.sendMessage(ChatColor.RED + wname + " is not a valid world.");
				}
				else
				{
					sender.sendMessage(ChatColor.GOLD + world.getName());
					if (world.getMaps().size() == 0)
						sender.sendMessage(spacing + ChatColor.GRAY + "[NONE]");
					else
						for (Map map : world.getMaps())
							sender.sendMessage(spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
				}
			}
		}
		
		return true;
	}
	
	private static boolean selectmap(CommandSender sender, String args[])
	{
		if (args.length == 0 || args[0].equalsIgnoreCase("selected"))
		{
			if (CommandUtil.getSelectedMap(sender) == null)
			{
				sender.sendMessage(ChatColor.RED + "You have not selected any maps.");
				sender.sendMessage(ChatManager.leftborder + "Use /mmap select <mapname>");
				return true;
			}
			else
			{
				sender.sendMessage(ChatManager.color + "Selected map: " + CommandUtil.getSelectedMap(sender).getFullName());
				return true;
			}
		}
		else
		{
			Map map = Manhunt.getMap(args[0]);
			if (map == null)
			{
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid map name.");
				sender.sendMessage(ChatManager.leftborder + "Use /mmap list [page] to see available maps.");
				return true;
			}
			else
			{
				CommandUtil.setSelectedMap(sender, map);
				sender.sendMessage(ChatColor.GREEN + "You have selected map " + map.getFullName() + ".");
				return true;
			}
			
		}
	}
	
	
}
