package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;

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

	public static void onCommandKick(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(ChatColor.RED + "Proper Syntax is /m kick <player>");
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
