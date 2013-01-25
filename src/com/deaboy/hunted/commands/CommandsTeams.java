package com.deaboy.hunted.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.hunted.Manhunt;
import com.deaboy.hunted.chat.ChatManager;
import com.deaboy.hunted.lobby.GameLobby;
import com.deaboy.hunted.lobby.Lobby;
import com.deaboy.hunted.lobby.Team;

public class CommandsTeams
{
	private static final Argument context_game	= new Argument("game", "g", "gm", "this", "lobby", "l");
	private static final Argument context_all	= new Argument("all", "server", "ser", "a", "s");
	private static final Argument arg_player	= new Argument ("player", "plr");
	
	
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

	public static void onCommandHunter(CommandSender sender, Arguments args)
	{
		Lobby lobby;
		Player player;
		
		if (args.contains(arg_player))
		{
			player = Bukkit.getPlayer(args.getString(arg_player));
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (args.getRawArgs().length < 1)
		{
			player = Bukkit.getPlayer(args.getRawArgs()[0]);
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			// TODO Tell console they must select a player for this to work
			return;
		}
		else
		{
			player = (Player) sender;
		}
		
		
		lobby = Manhunt.getLobby(player);
		if (lobby == null || !(lobby instanceof GameLobby))
		{
			// TODO Tell player they aren't in a game lobby.
		}
		else
		{
			((GameLobby) lobby).setPlayerTeam(player.getName(), Team.HUNTERS);
		}
		
	}

	public static void onCommandPrey(CommandSender sender, Arguments args)
	{
		Lobby lobby;
		Player player;
		
		if (args.contains(arg_player))
		{
			player = Bukkit.getPlayer(args.getString(arg_player));
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (args.getRawArgs().length < 1)
		{
			player = Bukkit.getPlayer(args.getRawArgs()[0]);
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			// TODO Tell console they must select a player for this to work
			return;
		}
		else
		{
			player = (Player) sender;
		}
		
		
		lobby = Manhunt.getLobby(player);
		if (lobby == null || !(lobby instanceof GameLobby))
		{
			// TODO Tell player they aren't in a game lobby.
		}
		else
		{
			((GameLobby) lobby).setPlayerTeam(player.getName(), Team.PREY);
		}
	}

	public static void onCommandSpectate(CommandSender sender, Arguments args)
	{
		Lobby lobby;
		Player player;
		
		if (args.contains(arg_player))
		{
			player = Bukkit.getPlayer(args.getString(arg_player));
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (args.getRawArgs().length < 1)
		{
			player = Bukkit.getPlayer(args.getRawArgs()[0]);
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (sender instanceof ConsoleCommandSender)
		{
			// TODO Tell console they must select a player for this to work
			return;
		}
		else
		{
			player = (Player) sender;
		}
		
		
		lobby = Manhunt.getLobby(player);
		if (lobby == null || !(lobby instanceof GameLobby))
		{
			// TODO Tell player they aren't in a game lobby.
		}
		else
		{
			((GameLobby) lobby).setPlayerTeam(player.getName(), Team.SPECTATORS);
		}
	}

	public static void onCommandKick(CommandSender sender, Arguments args)
	{
		
		Lobby lobby;
		Player player;
		
		if (args.contains(arg_player))
		{
			player = Bukkit.getPlayer(args.getString(arg_player));
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else if (args.getRawArgs().length < 1)
		{
			player = Bukkit.getPlayer(args.getRawArgs()[0]);
			if (player == null)
			{
				// TODO Tell sender player doesn't exist
				return;
			}
		}
		else
		{
			// TODO Tell sender to select a player
			return;
		}
		
		
		lobby = Manhunt.getLobby(player);
		if (lobby == null || !(lobby instanceof GameLobby))
		{
			// TODO Tell sender that player isn't in a game lobby.
			return;
		}
		else
		{
			// TODO Boot the player from the game, forfeit them, and kick them from the server.
		}
		
	}
	
	
}
