package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

public class CommandsGeneral
{
	public static void onCommandStartgame(CommandSender sender, String args[])
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		plugin.startGame();
	}
	
	public static void onCommandStopgame(CommandSender sender, String args[])
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		
		if (!plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.NO_GAME_RUNNING);
			return;
		}
		
		plugin.startGame();
	}

	public static void onCommandSpawn(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is: /m spawn [spawn] [player]";
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		Player p;
		Player p2;
		
		if (sender instanceof Player)
		{
			p = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (plugin.getSettings().OP_CONTROL.value && !sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning() && (plugin.getTeams().getTeamOf(p) == Team.HUNTERS || plugin.getTeams().getTeamOf(p) == Team.PREY))
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
		}
		
		Location loc;
		String spawn;
		
		if (args.length == 1)
		{
			spawn = "world";
			p2 = null;
		}
		else
		{
			if (args.length == 2)
			{
				spawn = args[1];
				p2 = Bukkit.getPlayer(args[1]);
			}
			else if (args.length == 3)
			{
				spawn = args[1];
				p2 = Bukkit.getPlayer(args[2]);
			}
			else
			{
				sender.sendMessage(SYNTAX);
				return;
			}
		}
		
		if (spawn.equalsIgnoreCase("world"))
		{
			loc = plugin.getWorld().getSpawnLocation();
		}
		else if (spawn.equalsIgnoreCase("hunter"))
		{
			loc = plugin.getSettings().SPAWN_HUNTER.value;
		}
		else if (spawn.equalsIgnoreCase("prey"))
		{
			loc = plugin.getSettings().SPAWN_PREY.value;
		}
		else if (spawn.equalsIgnoreCase("setup"))
		{
			loc = plugin.getSettings().SPAWN_SETUP.value;
		}
		else
		{
			if (p2 != null)
			{
				loc = plugin.getWorld().getSpawnLocation();
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "'" + spawn + "'" + " is not a valid spawn location.");
				return;
			}
		}
		
		if (p2 != null)
		{
			p2.teleport(ManhuntUtil.safeTeleport(loc));
			p2.sendMessage(ChatColor.GREEN + "You have been teleported to the " + spawn + " spawn.");
			p.sendMessage(ChatColor.GREEN + p2.getName() + " has teleported to the " + spawn + " spawn.");
		}
		else
		{
			p.teleport(ManhuntUtil.safeTeleport(loc));
			p.sendMessage(ChatColor.GREEN + "You have teleported to the " + args[1] + " spawn.");
		}
		
	}

	public static void onCommandSetspawn(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is: /m setspawn <spawn>";
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		Player p;
		
		if (sender instanceof Player)
		{
			p = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
		}
		
		String spawn;
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
		}
		
		spawn = args[1];
		
		if (spawn.equalsIgnoreCase("world"))
		{
			plugin.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
		}
		else if (spawn.equalsIgnoreCase("hunter"))
		{
			plugin.getSettings().SPAWN_HUNTER.setValue(p.getLocation());
		}
		else if (spawn.equalsIgnoreCase("prey"))
		{
			plugin.getSettings().SPAWN_PREY.setValue(p.getLocation());
		}
		else if (spawn.equalsIgnoreCase("setup"))
		{
			plugin.getSettings().SPAWN_SETUP.setValue(p.getLocation());
		}
		else if (spawn.equalsIgnoreCase("all"))
		{
			plugin.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			plugin.getSettings().SPAWN_HUNTER.setValue(p.getLocation());
			plugin.getSettings().SPAWN_PREY.setValue(p.getLocation());
			plugin.getSettings().SPAWN_SETUP.setValue(p.getLocation());

			sender.sendMessage(ChatColor.GREEN + "All spawns have been moved.");
			return;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "'" + spawn + "'" + " is not a valid spawn location.");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN + "The " + spawn + " has been moved.");
	}

}
