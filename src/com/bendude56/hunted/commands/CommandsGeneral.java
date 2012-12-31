package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.Team;

public class CommandsGeneral
{
	public static void onCommandStartgame(CommandSender sender, Arguments args)
	{
		GameLobby lobby;
		
		lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (lobby == null)
		{
			sender.sendMessage("You must first select a lobby to start!");
			return;
		}
		
		if (lobby.getGame().isRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (lobby.getPlayers(Team.PREY).size() == 0 || lobby.getPlayers(Team.HUNTERS).size() == 0 )
		{
			sender.sendMessage(ChatColor.RED + "There must be at least one prey and one hunter to start the game!");
			return;
		}
		
		lobby.getGame().startGame();
	}
	
	public static void onCommandStopgame(CommandSender sender, Arguments args)
	{
		GameLobby lobby;
		
		lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (lobby == null)
		{
			sender.sendMessage("You must first select a lobby!");
			return;
		}
		
		if (!lobby.getGame().isRunning())
		{
			sender.sendMessage(CommandUtil.NO_GAME_RUNNING);
			return;
		}
		
		lobby.getGame().stopGame();
	}

	public static void onCommandSpawn(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is: /m spawn [spawn] [player]";
		
		GameLobby lobby;
		
		lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
		
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
		
		if (Manhunt.getSettings().OP_CONTROL.getValue() && !sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (lobby == null)
		{
			sender.sendMessage("You must first select a lobby!");
			return;
		}
		
		if (lobby.getGame().isRunning() && (lobby.getPlayerTeam(p) == Team.HUNTERS || lobby.getPlayerTeam(p) == Team.PREY))
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
		}
		
		
		
		if (args.length == 1)
		{
			p2 = Bukkit.getPlayer(args[0]);
		}
		else if (args.length == 0)
		{
			p2 = null;
		}
		else
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (p2 != null)
		{
			p2.teleport(ManhuntUtil.safeTeleport(lobby.getLocation()));
			p2.sendMessage(ChatColor.GREEN + "You have been teleported to the " + lobby.getName() + " spawn.");
			p.sendMessage(ChatColor.GREEN + p2.getName() + " has teleported to the " + lobby.getName() + " spawn.");
		}
		else
		{
			p.teleport(ManhuntUtil.safeTeleport(lobby.getLocation()));
			p.sendMessage(ChatColor.GREEN + "You have teleported to the " + lobby.getName() + " spawn.");
		}
		
	}

	

}
