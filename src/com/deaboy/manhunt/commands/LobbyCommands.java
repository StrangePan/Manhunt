package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;

public abstract class LobbyCommands
{
	public static boolean mstartgame(CommandSender sender, String[] args)
	{
		boolean console;
		Lobby lobby;
		
		if (!sender.isOp())
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
		
		
		
		if (lobby == null && args.length > 0)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " does not exist.");
			return false;
		}
		else if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "You are not in a Manhunt game lobby.");
			return true;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + (args.length > 0 ? args[0] : "you are in") + " is not a game lobby");
			return true;
		}
		else if (!lobby.isEnabled())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + (args.length > 0 ? args[0] : "you are in") + " is closed.");
			return true;
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
		
		if (!sender.isOp())
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
			if (args.length > 0)
				sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + args[0] + " does not exist.");
			else
				sender.sendMessage(ChatColor.RED + "You are not in a Manhunt game lobby.");
			return false;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "The lobby " + (args.length > 0 ? args[0] : "you are in") + " is not a game lobby");
			return false;
		}
		else if (!lobby.gameIsRunning())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "There are no games running.");
			return true;
		}
		else
		{
			sender.sendMessage((console ? "" : ChatColor.GREEN) + "Game successfully stopped.");
			lobby.stopGame();
			return true;
		}
		
		
		
	}
	
	public static boolean mjoin(CommandSender sender, String[] args)
	{
		if (args.length == 0 || args[0].equals("?") || args[0].equalsIgnoreCase("help"))
		{
			Bukkit.dispatchCommand(sender, "help mjoin");
			return true;
		}
		
		Lobby l;
		Player p;
		
		if (sender instanceof Player && args.length == 1)
			p = (Player) sender;
		else if (args.length == 2)
			p = Bukkit.getPlayerExact(args[1]);
		else
		{
			sender.sendMessage(ChatColor.RED + "Proper useage: /mjoin <lobby> " + (sender instanceof Player ? "[player]" : "<player>"));
			return true;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "There is no player by that name.");
			return true;
		}
		
		// Get the lobby
		l = Manhunt.getLobby(args[0]);
		
		if (l == null)
		{
			sender.sendMessage(ChatColor.RED + "Lobby does not exist.");
			return true;
		}
		
		if (Manhunt.getPlayerLobby(p) == l)
		{
			if (args.length == 2)
				sender.sendMessage(ChatColor.RED + "Player is already in that lobby");
			else
				sender.sendMessage(ChatColor.RED + "You are already in that lobby.");
			return true;
		}
		else if (args.length == 2)
			sender.sendMessage(ChatColor.GREEN + "Player sent to lobby " + args[0]);
		Manhunt.changePlayerLobby(p, l.getId());
		
		return true;
	}
	
	public static boolean mleave(CommandSender sender, String[] args)
	{
		if (args.length > 0 && (args[0].equals("?") || args[0].equalsIgnoreCase("help")))
		{
			Bukkit.dispatchCommand(sender, "help mleave");
			return true;
		}
		
		Player p;
		
		if (sender instanceof Player && args.length == 0)
			p = (Player) sender;
		else if (args.length == 2)
			p = Bukkit.getPlayerExact(args[0]);
		else
		{
			sender.sendMessage(ChatColor.RED + "Proper useage: /mleave " + (sender instanceof Player ? "[player]" : "<player>"));
			return true;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "There is no player by that name.");
			return true;
		}
		
		if (Manhunt.getPlayerLobby(p) == Manhunt.getDefaultLobby())
		{
			if (args.length == 1)
				sender.sendMessage(ChatColor.RED + "That player is already in the default lobby.");
			else
				sender.sendMessage(ChatColor.RED + "You're already in the default lobby.");
			return true;
		}
		
		if (args.length == 2)
			sender.sendMessage(ChatColor.GREEN + "Player sent to lobby " + args[0]);
		Manhunt.changePlayerLobby(p, Manhunt.getDefaultLobby().getId());
		
		return true;
	}
	
	public static boolean mlobby(CommandSender sender, String command, String[] args)
	{
		boolean action = false;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		Command cmd = Command.fromTemplate(CommandUtil.cmd_mlobby, command, args);
		
		if (cmd.containsArgument(CommandUtil.arg_help))
		{
			Bukkit.getServer().dispatchCommand(sender, "help " + cmd.getName());
			action = true;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_list))
		{
			action |= listlobbies(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_select))
		{
			action |= selectlobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_create))
		{
			action |= createlobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_delete))
		{
			action |= deletelobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_addmap))
		{
			action |= addmap(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_remmap))
		{
			action |= removemap(sender,cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_join))
		{
			action |= joinlobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_leave))
		{
			action |= leavelobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_open))
		{
			action |= openlobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_close))
		{
			action |= closelobby(sender, cmd);
		}

		if (!action)
		{
			sender.sendMessage(ChatColor.GRAY + "No actions performed.");
		}
		
		return true;
	}
	private static boolean listlobbies(CommandSender sender, Command cmd)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<Lobby> lobbies;
		
		// Get the page #
		if (cmd.containsArgument(CommandUtil.arg_page) && cmd.getArgument(CommandUtil.arg_page).getParameter() != null)
		{
			try
			{
				page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_page).getParameter());
			}
			catch (NumberFormatException e)
			{
				page = 1;
			}
		}
		else if (cmd.getArgument(CommandUtil.arg_list).getParameter() != null)
		{
			 if (cmd.getArgument(CommandUtil.arg_list).getParameter().equalsIgnoreCase("all"))
			 {
				 all = true;
				 page = 1;
			 }
			 else
			 {
				 try
				 {
					 page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_list).getParameter());
				 }
				 catch (Exception e)
				 {
					 page = 1;
				 }
			 }
		}
		else
		{
			page = 1;
		}
		
		page--;
		
		// Assemble list of settings
		lobbies = Manhunt.getLobbies();
 
		if (!all)
		{
			if (page * perpage > lobbies.size() - 1 )
				page = (lobbies.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (lobbies.size() == 0)
			{
				sender.sendMessage("There are no lobbies to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Lobbies " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) lobbies.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /mlobby list [n] to get page n of worlds");
			lobbies = lobbies.subList(page * perpage, Math.min( (page + 1) * perpage, lobbies.size() ));
		}
		
		for (Lobby lobby: lobbies)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + lobby.getName() + "    " + ChatColor.GRAY + lobby.getType().name());
		}
		return true;
	}
	private static boolean mlobby_join(CommandSender sender, String[] args)
	{
		Lobby l;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "That command can only be performed by a Player.");
			sender.sendMessage(ChatColor.GRAY + "Please use /mjoin <lobby> <player>");
			return true;
		}
		
		if (args.length != 1)
		{
			sender.sendMessage(ChatColor.RED + "Usage: /mlobby join");
			return true;
		}
		
		l = CommandUtil.getSelectedLobby(sender);
		
		if (l == null)
		{
			sender.sendMessage(ChatColor.RED + "You must have a lobby seleted.");
			sender.sendMessage(ChatColor.GRAY + "Use /mlobby select <lobby> or /mjoin <lobby>");
			return true;
		}
		else if (l == Manhunt.getPlayerLobby((Player) sender))
		{
			sender.sendMessage(ChatColor.RED + "You're already in lobby " + l.getName() + ".");
			sender.sendMessage(ChatColor.GRAY + "Use /mjoin to join another lobby.");
			return true;
		}
		else
		{
			Manhunt.changePlayerLobby((Player) sender, l.getId());
			return true;
		}
	}
	private static boolean mlobby_leave(CommandSender sender, String[] args)
	{
		Bukkit.dispatchCommand(sender, "mleave");
		
		return true;
	}
	private static boolean mlobby_create(CommandSender sender, String[] args)
	{
		Location loc;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "Only a player may perform this command.");
			return true;
		}
		if (args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage("/mlobby create <name>");
			return true;
		}
		
		if (!CommandUtil.isVerified(sender))
		{
			String cmd = "mlobby";
			for (String s : args)
				cmd += " " + s;
			CommandUtil.addVerifyCommand(sender, cmd, "Are you sure you want to create a new lobby?");
			return true;
		}
		
		if (Manhunt.getLobby(args[1]) != null)
		{
			sender.sendMessage(ChatColor.RED + "A lobby already exists by that name!");
			return true;
		}
		
		loc = ((Player) sender).getLocation();
		
		Manhunt.createLobby(args[1], LobbyType.GAME, loc);
		sender.sendMessage(ChatColor.GREEN + "Created lobby in world " + ((Player) sender).getWorld().getName());
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "at location [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "]");
		return true;
	}
	private static boolean mlobby_close(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		if (sender instanceof Player && args.length != 1 && args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage("/mlobby close [lobby]");
			return true;
		}
		if (!(sender instanceof Player) && args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage("/mlobby close <lobby>");
			return true;
		}
		
		Lobby lobby;
		
		if (args.length == 1)
			lobby = Manhunt.getPlayerLobby((Player) sender);
		else
			lobby = Manhunt.getLobby(args[1]);
		
		if (lobby == null && args.length == 1)
		{
			sender.sendMessage(ChatColor.RED + "You are not in a Manhunt lobby.");
			return true;
		}
		else if (lobby == null && args.length == 2)
		{
			sender.sendMessage(ChatColor.RED + "There is no lobby with that name.");
			return true;
		}
		
		// TODO Stop lobby
		sender.sendMessage("Lobby closed.");
		return true;
	}
	private static boolean mlobby_select(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		if (args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage("/mlobby select <lobby>");
			return true;
		}
		
		Lobby lobby;
		lobby = Manhunt.getLobby(args[1]);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "No lobby exists by that name.");
			sender.sendMessage(ChatColor.GRAY + "Type \"/mlobby list\" to view a list of lobbies.");
			return true;
		}
		
		CommandUtil.setSelectedLobby(sender, lobby);
		sender.sendMessage(ChatColor.GREEN + "Selected lobby \"" + lobby.getName() + "\"");
		return true;
	}
	private static boolean mlobby_selected(CommandSender sender, String[] args)
	{
		Lobby lobby = CommandUtil.getSelectedLobby(sender);
		
		if (hasSelectedLobby(sender))
		{
			sender.sendMessage(ChatManager.leftborder + "Selected Lobby: " + lobby.getName());
			sender.sendMessage(ChatManager.leftborder + "  ID: " + lobby.getId() + "    Type: " + lobby.getType().name());
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + "No lobby selected.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "Use /mlobby select <lobby name> to select a lobby.");
		}
		return true;
	}
	private static boolean mlobby_addmap(CommandSender sender, String[] args)
	{
		if (!hasSelectedLobby(sender))
			return true;
		
		if (args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage(ChatColor.GRAY + "Usage: /mlobby addmap <map name>");
			return true;
		}
		
		Lobby lobby = CommandUtil.getSelectedLobby(sender);
		World world;
		Map map;
		
		if (sender instanceof ConsoleCommandSender)
			world = null;
		else if (sender instanceof Player)
			world = Manhunt.getWorld(((Player) sender).getWorld());
		else if (sender instanceof BlockCommandSender)
			world = Manhunt.getWorld(((BlockCommandSender) sender).getBlock().getWorld());
		else
			world = null;
		
		if (args[1].contains("."))
		{
			map = Manhunt.getMap(args[1]);
			if (map == null)
			{
				sender.sendMessage(ChatColor.RED + "No map by that name.");
				sender.sendMessage(ChatColor.GRAY + "Use \"/map list\" to see a list of available maps.");
				return true;
			}
		}
		else if (world == null)
		{
			if (sender instanceof ConsoleCommandSender)
				sender.sendMessage(ChatColor.RED + "You must include a map's full name.");
			else
				sender.sendMessage(ChatColor.RED + "You are not in a world containing that map.");
			sender.sendMessage(ChatColor.GRAY + "Use \"/map list\" to see a list of available maps.");
			return true;
		}
		else if (world.getMap(args[1]) == null)
		{
			sender.sendMessage(ChatColor.RED + "No map by that name.");
			sender.sendMessage(ChatColor.GRAY + "Use \"/map list\" to see a list of available maps.");
			return true;
		}
		else
		{
			map = world.getMap(args[1]);
		}
		
		if (lobby.addMap(map))
			sender.sendMessage(ChatColor.GREEN + "Added map " + map.getFullName() + " to lobby " + lobby.getName());
		else
			sender.sendMessage(ChatColor.RED + "There was a problem adding the map.");
				
		return true;
	}
	private static boolean mlobby_remmap(CommandSender sender, String[] args)
	{
		if (!hasSelectedLobby(sender))
			return true;
		
		if (args.length != 2)
		{
			sender.sendMessage(CommandUtil.INVALID_USAGE);
			sender.sendMessage(ChatColor.GRAY + "Usage: /mlobby delmap <map name>");
			return true;
		}
		
		Lobby lobby = CommandUtil.getSelectedLobby(sender);
		Map map = null;
		
		for (Map m : lobby.getMaps())
		{
			if (m.getFullName().equals(args[1]))
			{
				map = m;
				break;
			}
			else if (m.getName().equals(args[1]) && map == null)
				map = m;
			else if (m.getName().equals(args[1]) && map != null)
			{
				sender.sendMessage(ChatColor.RED + "The lobby has more than one map with the name " + args[1] + ".");
				return true;
			}
		}
		
		if (map == null)
		{
			sender.sendMessage(ChatColor.RED + "The lobby has no maps with that name.");
			return true;
		}
		else
		{
			if (lobby.removeMap(map))
				sender.sendMessage(ChatColor.GREEN + "Removed map from lobby.");
			else
				sender.sendMessage(ChatColor.RED + "There was a problem removing the map.");
			return true;
		}
	}
	
	
	
	public static boolean mteam(CommandSender sender, String[] args)
	{
		if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equals("?"))
		{
			Bukkit.dispatchCommand(sender, "help mteam");
			sender.sendMessage(ChatColor.GRAY + "Available commands:\n  join");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("join"))
			return mteam_join(sender, args);
		
		return true;
	}
	
	private static boolean mteam_join(CommandSender sender, String[] args)
	{
		if (!(sender instanceof Player) && args.length != 3)
		{
			sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/mteam join <team> <player>");
			return true;
		}
		else if (args.length < 2 || args.length > 3)
		{
			sender.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/mteam join <team> [player]");
			return true;
		}
		
		Player p;
		Team t;
		
		if (args.length == 3)
			p = Bukkit.getPlayer(args[2]);
		else
			p = (Player) sender;
		
		switch (args[1].toLowerCase())
		{
		case "hunter":
		case "hunters":
		case "hunt":
			t = Team.HUNTERS;
			break;
		case "prey":
			t = Team.PREY;
			break;
		case "spectate":
		case "spectator":
		case "spectators":
			t = Team.SPECTATORS;
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid team name.");
			sender.sendMessage(ChatColor.GRAY + "Available teams: hunters, prey, spectators.");
			return true;
		}
		
		Manhunt.getPlayerLobby(p).setPlayerTeam(p, t);
		
		return true;
	}
	
	
	
	private static boolean hasSelectedLobby(CommandSender sender)
	{
		if (CommandUtil.getSelectedLobby(sender) != null)
			return true;
		else
		{
			sender.sendMessage(ChatColor.RED + "You have no lobbies selected.");
			sender.sendMessage(ChatColor.GRAY + "Use \"/mlobby select <lobby>\" so select a lobby.");
			return false;
		}
	}
	
}
