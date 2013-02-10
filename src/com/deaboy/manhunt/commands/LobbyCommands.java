package com.deaboy.manhunt.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;

public abstract class LobbyCommands
{
	
	public static boolean mstartgame(CommandSender sender, String[] args)
	{
		boolean console;
		Lobby lobby;
		
		if (sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		console = sender instanceof ConsoleCommandSender;
		
		if (console && args.length == 0)
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		if (args.length > 1)
		{
			return false;
		}
		
		if (args.length == 0)
			lobby = Manhunt.getPlayerLobby((Player) sender);
		else if (args.length == 1)
			lobby = Manhunt.getLobby(args[0]);
		else
			lobby = null;
		
		
		
		if (lobby == null)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " does not exist.");
			return false;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " is not a game lobby");
			return false;
		}
		else if (lobby.gameIsRunning())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "A game is already running.");
			return true;
		}
		else
		{
			sender.sendMessage((console ? "" : ChatColor.GREEN) + "Game successfully started.");
			lobby.startGame();
			return true;
		}
		
		
		
	}
	
	public static boolean mstopgame(CommandSender sender, String[] args)
	{
		boolean console;
		Lobby lobby;
		
		if (sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		console = sender instanceof ConsoleCommandSender;
		
		if (console && args.length == 0)
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		if (args.length > 1)
		{
			return false;
		}
		
		if (args.length == 0)
			lobby = Manhunt.getPlayerLobby((Player) sender);
		else if (args.length == 1)
			lobby = Manhunt.getLobby(args[0]);
		else
			lobby = null;
		
		
		
		if (lobby == null)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " does not exist.");
			return false;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " is not a game lobby");
			return false;
		}
		else if (!lobby.gameIsRunning())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "A game isn't running.");
			return true;
		}
		else
		{
			sender.sendMessage((console ? "" : ChatColor.GREEN) + "Game successfully stopped.");
			lobby.stopGame();
			return true;
		}
		
		
		
	}
	
	
	
}
