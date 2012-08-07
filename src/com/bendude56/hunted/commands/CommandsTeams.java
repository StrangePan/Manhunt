package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.teams.TeamManager.Team;

public class CommandsTeams
{
	public static void onCommandQuit(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m quit";
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
		
		if (args.length != 1)
		{
			sender.sendMessage(SYNTAX);
		}
		
		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerForfeit(p.getName());
		}
		else
		{
			String[] array = {"spectate"};
			CommandsTeams.onCommandSpectate(sender, array);
		}
	}

	public static void onCommandHunter(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m spectate [player]";
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		Player p;
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (!sender.isOp())
		{
			if (plugin.getSettings().OP_CONTROL.value)
			{
				sender.sendMessage(CommandUtil.NO_PERMISSION);
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
			}
		}
		
		if (args.length == 1)
		{
			if (sender instanceof Player)
			{
				p = (Player) sender;
			}
			else
			{
				sender.sendMessage(CommandUtil.IS_SERVER);
				return;
			}
		}
		else if (args.length == 2)
		{
			p = Bukkit.getPlayer(args[1]);
		}
		else
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "That player does not exist.");
			return;
		}
		
		if (p.getWorld() != plugin.getWorld())
		{
			sender.sendMessage(CommandUtil.WRONG_WORLD);
			return;
		}
		
		plugin.getTeams().changePlayerTeam(p, Team.HUNTERS);
		
	}

	public static void onCommandPrey(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m spectate [player]";
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		Player p;
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (!sender.isOp())
		{
			if (plugin.getSettings().OP_CONTROL.value)
			{
				sender.sendMessage(CommandUtil.NO_PERMISSION);
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
			}
		}
		
		if (args.length == 1)
		{
			if (sender instanceof Player)
			{
				p = (Player) sender;
			}
			else
			{
				sender.sendMessage(CommandUtil.IS_SERVER);
				return;
			}
		}
		else if (args.length == 2)
		{
			p = Bukkit.getPlayer(args[1]);
		}
		else
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "That player does not exist.");
			return;
		}
		
		if (p.getWorld() != plugin.getWorld())
		{
			sender.sendMessage(CommandUtil.WRONG_WORLD);
			return;
		}
		
		plugin.getTeams().changePlayerTeam(p, Team.PREY);
		
	}

	public static void onCommandSpectate(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m spectate [player]";
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		Player p;
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (!sender.isOp())
		{
			if (plugin.getSettings().OP_CONTROL.value)
			{
				sender.sendMessage(CommandUtil.NO_PERMISSION);
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
			}
		}
		
		if (args.length == 1)
		{
			if (sender instanceof Player)
			{
				p = (Player) sender;
			}
			else
			{
				sender.sendMessage(CommandUtil.IS_SERVER);
				return;
			}
		}
		else if (args.length == 2)
		{
			p = Bukkit.getPlayer(args[1]);
		}
		else
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "That player does not exist.");
			return;
		}
		
		if (p.getWorld() != plugin.getWorld())
		{
			sender.sendMessage(CommandUtil.WRONG_WORLD);
			return;
		}
		
		plugin.getTeams().changePlayerTeam(p, Team.SPECTATORS);
		
	}

	public static void onCommandLock(CommandSender sender, String[] args)
	{
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if  (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		plugin.locked = !plugin.locked;
		sender.sendMessage(ChatColor.GOLD + "Manhunt is " + (plugin.locked ? "LOCKED" : "UNLOCKED") + ".");
	}

	public static void onCommandKick(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(ChatColor.RED + "Proper syntax is /m kick <player>");
			return;
		}
		
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		
		if (plugin.getTeams().getTeamOf(args[1]) == null)
		{
			sender.sendMessage(ChatColor.RED + "That player does not exist!");
		}
		else
		{
			Player p = Bukkit.getPlayer(args[1]);
			OfflinePlayer p2 = Bukkit.getOfflinePlayer(args[1]);
			
			if (p != null)
			{
				p.kickPlayer("You have been kicked.");
			}
			
			if (plugin.gameIsRunning())
			{
				plugin.getGame().timeouts.stopTimeout(p2.getName());
				plugin.getGame().onPlayerForfeit(p2.getName());
			}
			
			sender.sendMessage(ChatColor.GREEN + p2.getName() + " has been kicked.");
		}
	}
	
	
}
