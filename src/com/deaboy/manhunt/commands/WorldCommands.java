package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;

public abstract class WorldCommands
{
	
	public static boolean mworld(CommandSender sender, String[] args)
	{
		if (args.length == 0 || args[0].equals("?"))
		{
			Bukkit.dispatchCommand(sender, "help mworld");
			sender.sendMessage(ChatColor.GRAY + "Available commands:\n  list");
		}
		
		else if (args[0].equalsIgnoreCase("list"))
		{
			String command = "mworlds";
			
			for (int i = 1; i < args.length; i++)
				command += " " + args[i];
			
			Bukkit.dispatchCommand(sender, command);
		}
		
		else
		{
			sender.sendMessage(ChatColor.RED + "Unknown command.");
			sender.sendMessage(ChatColor.GRAY + "Available commands: list");
		}
		
		return true;
	}
	
	public static boolean mworlds(CommandSender sender, String[] args)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<World> worlds;
		
		
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
		worlds = Bukkit.getWorlds();
 
		if (!all)
		{
			if (page * perpage > worlds.size() - 1 )
				page = (worlds.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (worlds.size() == 0)
			{
				sender.sendMessage("There are no worlds to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Worlds " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) worlds.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /mworlds [n] to get page n of worlds");
			worlds = worlds.subList(page * perpage, Math.min( (page + 1) * perpage, worlds.size() ));
		}
		
		for (World world : worlds)
		{
			sender.sendMessage(ChatManager.leftborder + (Manhunt.getWorld(world) == null ? ChatColor.GRAY : ChatColor.WHITE) + world.getName());
		}
		return true;
		
		
		
		
	}
	
	public static boolean mspawn(CommandSender sender, String[] args)
	{
		World world;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return false;
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		if (args.length == 0)
			world = ((Player) sender).getWorld();
		else
			world = Bukkit.getWorld(args[0]);
		
		if (world == null)
		{
			sender.sendMessage(ChatColor.RED + "That world does not exist.");
			return true;
		}
		
		
		((Player) sender).teleport(world.getSpawnLocation());
		
		return true;
	}

	public static boolean msetspawn(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return false;
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		sender.sendMessage(ChatColor.GREEN + "Spawn point set!");
		((Player) sender).getWorld().setSpawnLocation(((Player) sender).getLocation().getBlockX(), ((Player) sender).getLocation().getBlockY(), ((Player) sender).getLocation().getBlockZ());
		
		return true;
	}
	
}
