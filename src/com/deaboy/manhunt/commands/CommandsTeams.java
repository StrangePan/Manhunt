package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;

public class CommandsTeams
{
	private static final Argument context_game = new Argument("game", "g", "gm", "this", "lobby", "l");
	private static final Argument context_all = new Argument("all", "server", "ser", "a", "s");
	
	
	public static void onCommandList(CommandSender sender, Arguments args)
	{
		Lobby lobby;
		List<Player> players;
		boolean context;
		
		if (args.contains(context_game))
		{
			context = true;
			
			if (args.getString(context_game) == null)
				lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
			else if (Manhunt.getLobby(args.getString(context_game)) == null)
				{return;} // TODO Tell player they didn't select a valid lobby
			else
				lobby = Manhunt.getLobby(args.getString(context_game));
		}
		else if (args.contains(context_all))
		{
			context = false;
			lobby = null;
		}
		else
		{
			context = Manhunt.getSettings().CONTEXT_LIST.getValue();
			if (context)
				lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
			else
				lobby = null;
		}
		
		
		if (lobby == null)
		{
			players = new ArrayList<Player>();
			for (Player p : Bukkit.getOnlinePlayers())
				players.add(p);
		}
		else
		{
			players = lobby.getPlayers();
		}
		
		
		// List the players in the list "players"
		for (Player p : players)
		{
			// TODO Send a list of player names using
			p.getDisplayName();
		}
		
		
		
		
		
		
	}

	public static void onCommandQuit(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m quit";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
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
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		Player p;
		
		if (plugin.locked)
		{
			sender.sendMessage(CommandUtil.LOCKED);
			return;
		}
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
				return;
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
				return;
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
		GameUtil.broadcast(ChatManager.leftborder + Team.HUNTERS.getColor() + p.getName() + ChatColor.WHITE + " has joined team " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true), Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		
	}

	public static void onCommandPrey(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m spectate [player]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		Player p;
		
		if (plugin.locked)
		{
			sender.sendMessage(CommandUtil.LOCKED);
			return;
		}
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
				return;
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
				return;
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
		GameUtil.broadcast(ChatManager.leftborder + Team.PREY.getColor() + p.getName() + ChatColor.WHITE + " has joined team " + Team.PREY.getColor() + Team.PREY.getName(true), Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		
	}

	public static void onCommandSpectate(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m spectate [player]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		Player p;
		
		if (plugin.locked)
		{
			sender.sendMessage(CommandUtil.LOCKED);
			return;
		}
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
				return;
			}
			else if (plugin.locked)
			{
				sender.sendMessage(CommandUtil.LOCKED);
				return;
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
		GameUtil.broadcast(ChatManager.leftborder + Team.SPECTATORS.getColor() + p.getName() + ChatColor.WHITE + " has become a " + Team.SPECTATORS.getColor() + Team.SPECTATORS.getName(false), Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		
	}

	public static void onCommandLock(CommandSender sender, String[] args)
	{
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
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
		
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
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
