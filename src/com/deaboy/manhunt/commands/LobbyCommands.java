package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyClass;
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
		else if (((GameLobby) lobby).gameIsRunning())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "A game is already running.");
			return true;
		}
		else
		{
			if (!((GameLobby) lobby).startGame())
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Could not start the game.");
			}
			else
			{
				sender.sendMessage((console ? "" : ChatColor.GREEN) + "Game successfully started.");
			}
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
		else if (!((GameLobby) lobby).gameIsRunning())
		{
			sender.sendMessage((console ? "" : ChatColor.RED) + "There are no games running.");
			return true;
		}
		else
		{
			((GameLobby) lobby).cancelGame();
			sender.sendMessage((console ? "" : ChatColor.GREEN) + "Game successfully stopped.");
			return true;
		}
		
		
		
	}
	
	public static boolean mjoin(CommandSender sender, String[] args)
	{
		Bukkit.dispatchCommand(sender, CommandUtil.cmd_mlobby.getName() + (args.length > 0 ? " -" + CommandUtil.arg_join.getName() + ' ' + args[0] : "") + (args.length > 1 ? " -" + CommandUtil.arg_player.getName() + ' ' + args[1] : ""));
		return true;
	}
	public static boolean mleave(CommandSender sender, String[] args)
	{
		Bukkit.dispatchCommand(sender, CommandUtil.cmd_mlobby.getName() + " -" + CommandUtil.arg_leave.getName() + (args.length > 0 ? " -" + CommandUtil.arg_player.getName() + ' ' + args[0] : ""));
		return true;
	}
	
	public static boolean mlobby(CommandSender sender, Command cmd)
	{
		boolean action = false;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}

		for (Subcommand scmd : cmd.getSubcommands())
		{
			if (scmd.containsArgument(CommandUtil.arg_list))
			{
				action |= listlobbies(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_select))
			{
				action |= selectlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_create))
			{
				action |= createlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_delete))
			{
				action |= deletelobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_lsmaps))
			{
				action |= listmapslobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_addmap))
			{
				action |= addmaplobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_remmap))
			{
				action |= removemaplobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_lsload))
			{
				action |= listloadoutslobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_addload))
			{
				action |= addloadoutlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_remload))
			{
				action |= removeloadoutlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_join))
			{
				action |= joinlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_leave))
			{
				action |= leavelobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_open))
			{
				action |= openlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_close))
			{
				action |= closelobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_setspawn))
			{
				action |= movespawnlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_range))
			{
				action |= rangespawnlobby(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_tp))
			{
				action |= teleportlobby(sender, scmd);
			}
		}
		
		return action;
	}
	private static boolean listlobbies(CommandSender sender, Subcommand cmd)
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
			sender.sendMessage(ChatColor.GRAY + "Use /mlobby list [n] to get page n of lobbies");
			lobbies = lobbies.subList(page * perpage, Math.min( (page + 1) * perpage, lobbies.size() ));
		}
		
		for (Lobby lobby: lobbies)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + lobby.getName() + "    " + ChatColor.GRAY + lobby.getType().name());
		}
		return true;
	}
	private static boolean joinlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		String lobbyname;
		Player player;
		
		if (cmd.containsArgument(CommandUtil.arg_player))
		{
			player = Bukkit.getPlayer(cmd.getArgument(CommandUtil.arg_player).getParameter());
			if (player == null)
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + '\'' + cmd.getArgument(CommandUtil.arg_player).getParameter() + '\'' + ChatManager.color + " is not online.");
				return false;
			}
		}
		else if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + "You must select a player to join the lobby.");
			sender.sendMessage(ChatManager.leftborder + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_join).getLabel() + " -" + CommandUtil.arg_player.getName() + " <player>");
			return false;	
		}
		
		// 1. Check for -name
		// 2. Check for parameter
		// 3. Check if lobby is selected
		
		lobbyname = null;
		
		if (cmd.containsArgument(CommandUtil.arg_name))
		{
			lobbyname = cmd.getArgument(CommandUtil.arg_name).getParameter();
		}
		if (lobbyname == null)
		{
			lobbyname = cmd.getArgument(CommandUtil.arg_join).getParameter();
		}
		if (lobbyname == null)
		{
			lobby = CommandUtil.getSelectedLobby(sender);
			if (lobby != null)
				lobbyname = lobby.getName();
			else
				lobbyname = null;
		}
		
		if (lobbyname == null)
		{
			sender.sendMessage(ChatColor.RED + "Failed to join lobby.");
			sender.sendMessage(ChatColor.GRAY + "  Please select a lobby or specify one by name.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_join).getLabel() + " <lobbyname>");
			return false;
		}
		
		lobby = Manhunt.getLobby(lobbyname);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "No lobby exists with that name.");
			sender.sendMessage(ChatColor.GRAY + "  List lobbies with /" + cmd.getLabel() + " -" + CommandUtil.arg_list.getName());
			return false;
		}
		
		if (lobby == Manhunt.getPlayerLobby((Player) sender))
		{
			sender.sendMessage(ChatColor.RED + "You're already in that lobby.");
			return false;
		}
		
		Manhunt.changePlayerLobby((Player) sender, lobby.getId());
		CommandUtil.setSelectedLobby(sender, lobby);
		sender.sendMessage(ChatColor.YELLOW + "Selected lobby \"" + lobby.getName() + "\"");
		return true;
	}
	private static boolean leavelobby(CommandSender sender, Subcommand cmd)
	{
		Player p;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		else
		{
			p = (Player) sender;
		}
		
		if (Manhunt.getPlayerLobby(p) == Manhunt.getDefaultLobby())
		{
			sender.sendMessage(ChatColor.RED + "You cannot leave this lobby. This is the default lobby.");
			return false;
		}
		
		Manhunt.changePlayerLobby(p, Manhunt.getDefaultLobby().getId());
		return true;
	}
	private static boolean createlobby(CommandSender sender, Subcommand cmd)
	{
		Location loc;
		String lobbyname;
		LobbyClass lobbyclass;
		int type;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		// 1. Check for name argument
		// 2. Check for parameter
		
		lobbyname = null;
		
		if (cmd.containsArgument(CommandUtil.arg_name))
		{
			lobbyname = cmd.getArgument(CommandUtil.arg_name).getParameter();
		}
		if (!cmd.containsArgument(CommandUtil.arg_name) || lobbyname == null || lobbyname.isEmpty())
		{
			lobbyname = cmd.getArgument(CommandUtil.arg_create).getParameter();
		}
		if (lobbyname == null || lobbyname.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "You must include a lobby name while creating one.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_create).getLabel() + " <lobbyname>");
			return false;
		}
		
		if (Manhunt.getLobby(lobbyname) != null)
		{
			sender.sendMessage(ChatColor.RED + "A lobby already exists with that name.");
			sender.sendMessage(ChatColor.GRAY + "  See a list of all lobbies with '/" + cmd.getLabel() + " -" + CommandUtil.arg_list.getName());
			return false;
		}
		
		loc = ((Player) sender).getLocation();
		
		if (cmd.containsArgument(CommandUtil.arg_lobbytype))
		{
			if (cmd.getArgument(CommandUtil.arg_lobbytype).getParameter() == null)
			{
				sender.sendMessage(ChatColor.RED + "Invalid use of the " + CommandUtil.arg_lobbytype + " parameter.");
				sender.sendMessage(ChatColor.GRAY + "  Argument usage: -" + cmd.getArgument(CommandUtil.arg_lobbytype).getLabel() + " <hub|game>");
				return false;
			}
			try
			{
				type = Integer.parseInt(cmd.getArgument(CommandUtil.arg_lobbytype).getParameter());
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(ChatManager.leftborder + cmd.getArgument(CommandUtil.arg_lobbytype).getParameter() + " is not a valid integer.");
				return false;
			}
			lobbyclass = Manhunt.getLobbyClass(type);
			if (lobbyclass == null)
			{
				sender.sendMessage(ChatColor.RED + "No lobby type exists with id " + type + '.');
				sender.sendMessage(ChatColor.GRAY + "  To view registered lobby classes, use /mlobbyclasses");
				return false;
			}
		}
		else
		{
			lobbyclass = Manhunt.getLobbyClass(0);
		}
		
		if (lobbyclass == null)
			return false;
		
		Manhunt.createLobby(lobbyname, lobbyclass, loc);
		sender.sendMessage(ChatColor.GREEN + "Created lobby '" + lobbyname + "' in world '" + ((Player) sender).getWorld().getName() + "'.");
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "  with spawn at [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "]");
		return true;
	}
	private static boolean deletelobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to close.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		if (Manhunt.deleteLobby(lobby.getId()))
		{
			sender.sendMessage(ChatColor.GREEN + "Successfully deleted lobby '" + lobby.getName() + "'.");
			return true;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "There was a problem deleting lobby '" + lobby.getName() + "'.");
			return false;
		}
	}
	private static boolean movespawnlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + CommandUtil.IS_SERVER);
			return false;
		}
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to close.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		lobby.setSpawnLocation(((Player) sender).getLocation());
		sender.sendMessage(ChatManager.leftborder + "Moved " + lobby.getName() + "'s spawn to your current location.");
		return true;
	}
	private static boolean rangespawnlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		int range;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + CommandUtil.IS_SERVER);
			return false;
		}
		
		if (cmd.getArgument(CommandUtil.arg_range).getParameter() != null)
		{
			try
			{
				range = Integer.parseInt(cmd.getArgument(CommandUtil.arg_range).getParameter());
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + cmd.getArgument(CommandUtil.arg_range).getParameter() + "is not a valid range. Must be a positive integer.");
				return false;
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Please include a positive integer when setting the spawn range.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_range).getLabel() + " <int>");
			return false;
		}
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to close.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		lobby.setSpawnRange(range);
		sender.sendMessage(ChatManager.leftborder + "Spawn range of " + lobby.getName() + " set to " + range);
		return true;
	}
	private static boolean teleportlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		Player player;
		String playername;
		
		if (cmd.containsArgument(CommandUtil.arg_player) || cmd.getArgument(CommandUtil.arg_tp).getParameter() != null)
		{
			playername = cmd.containsArgument(CommandUtil.arg_player) ? cmd.getArgument(CommandUtil.arg_player).getParameter() : cmd.getArgument(CommandUtil.arg_tp).getParameter();
			player = Bukkit.getPlayer(playername);
			if (player == null)
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + '\'' + playername + '\'' + ChatManager.color + " is not online.");
				return false;
			}
		}
		else if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + "You must select a player to teleport.");
			sender.sendMessage(ChatManager.leftborder + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_tp).getLabel() + " <player>");
			return false;	
		}
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a lobby to teleport to.");
			return false;
		}
		
		((Player) sender).teleport(ManhuntUtil.safeTeleport(lobby.getSpawnLocation()));
		sender.sendMessage(ChatManager.leftborder + "Teleported to " + lobby.getName());
		return true;
	}
	
	private static boolean closelobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		
		// 1. Check for selected lobby
		lobby = CommandUtil.getSelectedLobby(sender);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to close.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		if (!lobby.isEnabled())
		{
			sender.sendMessage(ChatColor.YELLOW + "Lobby '" + lobby.getName() + "' is already closed.");
			return false;
		}
		
		Manhunt.closeLobby(lobby.getId());
		sender.sendMessage(ChatColor.YELLOW + "Closed lobby '" + lobby.getName() + "'.");
		return true;
	}
	private static boolean openlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		
		// Get selected lobby
		lobby = CommandUtil.getSelectedLobby(sender);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to open.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		if (lobby.isEnabled())
		{
			sender.sendMessage(ChatColor.YELLOW + "Lobby '" + lobby.getName() + "' is already open.");
			return false;
		}
		
		Manhunt.openLobby(lobby.getId());
		sender.sendMessage(ChatColor.YELLOW+ "Opened lobby '" + lobby.getName() + "'.");
		return true;
	}
	private static boolean selectlobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		String lobbyname = cmd.getArgument(CommandUtil.arg_select).getParameter();
		
		if (lobbyname == null)
		{
			sender.sendMessage(ChatColor.RED + "You must include the name of the lobby you want to select.");
			sender.sendMessage(ChatColor.GRAY + " /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_select).getLabel() + " <lobby name>");
			return false;
		}
		
		lobby = Manhunt.getLobby(lobbyname);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "No lobby exists by that name.");
			sender.sendMessage(ChatColor.GRAY + "Use \"/" + cmd.getLabel() + " -" + CommandUtil.arg_list.getName() + "\" to view a list of lobbies.");
			return false;
		}
		
		CommandUtil.setSelectedLobby(sender, lobby);
		sender.sendMessage(ChatColor.YELLOW + "Selected lobby '" + lobby.getName() + "'.");
		return true;
	}
	private static boolean listmapslobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		List<Map> maps;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a lobby to view its maps.");
			return false;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage(ChatColor.RED + lobby.getName() + " is not a game lobby");
			return false;
		}
		
		maps = ((GameLobby) lobby).getMaps();
		sender.sendMessage(ChatManager.bracket1_ + "List of " + lobby.getName() + "'s Maps" + ChatManager.bracket2_);
		if (maps.isEmpty())
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "No maps. To add a map, use /" + cmd.getLabel() + " -" + CommandUtil.arg_addmap.getName() + " <map1> [map2] [map3] ...");
			return true;
		}
		for (Map map : maps)
		{
			sender.sendMessage(ChatManager.leftborder + map.getFullName());
		}
		return true;
	}
	private static boolean addmaplobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		List<String> mapnames;
		int success;
		int fail;
		Map map;
		World world;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to add maps to.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		if (sender instanceof Player)
		{
			world = Manhunt.getWorld(((Player) sender).getWorld());
		}
		else
		{
			world = null;
		}
		
		mapnames = new ArrayList<String>();
		success = 0;
		fail = 0;
		
		if (cmd.getArgument(CommandUtil.arg_addmap).getParameters().isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "Please list the maps you want to add.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_addmap).getLabel() + " <map1> [map2] [map3]...");
			return false;
		}
		
		for (String mapname : cmd.getArgument(CommandUtil.arg_addmap).getParameters())
		{
			if (!mapname.contains(".") && world != null)
			{
				mapname = world.getName() + '.' + mapname;
			}
			if (mapnames.contains(mapname))
			{
				continue;
			}
			else
			{
				mapnames.add(mapname);
			}
			
			map = Manhunt.getMap(mapname);
			if (map == null)
			{
				sender.sendMessage(ChatColor.RED + "The map '" + mapname + "' does not exist.");
				fail++;
				continue;
			}
			else if (!(lobby instanceof GameLobby))
			{
				sender.sendMessage(ChatColor.RED + lobby.getName() + " is not a game lobby.");;
				return false;
			}
			else if (((GameLobby) lobby).getMaps().contains(map))
			{
				sender.sendMessage(ChatColor.YELLOW + "Lobby already contains '" + mapname + "'.");
				fail++;
				continue;
			}
			
			((GameLobby) lobby).registerMap(map);
			sender.sendMessage(ChatColor.GREEN + "Added map '" + mapname + "' to lobby '" + lobby.getName() + "'.");
			success++;
		}
		
		sender.sendMessage((success == 0 ? ChatColor.RED + "No maps added to" : fail == 0 ? ChatColor.GREEN + "Successfully added maps to" : ChatColor.YELLOW + "Some maps were added to") + " " + lobby.getName() + "." + ChatColor.GRAY + "  (" + success + " added, " + fail + " failed)");
		
		if (success == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	private static boolean removemaplobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		List<String> mapnames;
		int success;
		int fail;
		Map map;
		World world;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select the lobby you wish to remove maps from.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <lobbyname>");
			return false;
		}
		
		if (sender instanceof Player)
		{
			world = Manhunt.getWorld(((Player) sender).getWorld());
		}
		else
		{
			world = null;
		}
		
		mapnames = new ArrayList<String>();
		success = 0;
		fail = 0;
		
		if (cmd.getArgument(CommandUtil.arg_remmap).getParameters().isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "Please list the maps you want to remove.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_remmap).getLabel() + " <map1> [map2] [map3]...");
			return false;
		}
		
		for (String mapname : cmd.getArgument(CommandUtil.arg_remmap).getParameters())
		{
			if (!mapname.contains(".") && world != null)
			{
				mapname = world.getName() + '.' + mapname;
			}
			if (mapnames.contains(mapname))
			{
				continue;
			}
			else
			{
				mapnames.add(mapname);
			}
			
			map = Manhunt.getMap(mapname);
			if (map == null)
			{
				sender.sendMessage(ChatColor.RED + "The map '" + mapname + "' does not exist.");
				fail++;
				continue;
			}
			else if(!(lobby instanceof GameLobby))
			{
				sender.sendMessage(ChatColor.RED + lobby.getName() + " is not a game lobby.");
				return false;
			}
			else if (!((GameLobby) lobby).getMaps().contains(map))
			{
				sender.sendMessage(ChatColor.YELLOW + "Lobby already does not contain '" + mapname + "'.");
				fail++;
				continue;
			}
			
			((GameLobby) lobby).unregisterMap(map);
			sender.sendMessage(ChatColor.GREEN + "Removed map '" + mapname + "' from lobby '" + lobby.getName() + "'.");
			success++;
		}
		
		sender.sendMessage((success == 0 ? ChatColor.RED + "No maps removed from" : fail == 0 ? ChatColor.GREEN + "Successfully removed maps from" : ChatColor.YELLOW + "Some maps were removed from") + " " + lobby.getName() + "." + ChatColor.GRAY + "  (" + success + " removed, " + fail + " failed)");
		
		if (success == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	private static boolean listloadoutslobby(CommandSender sender, Subcommand cmd)
	{
		Lobby lobby;
		List<Loadout> loadouts;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a lobby to view its loadouts.");
			return false;
		}
		else if (lobby.getType() != LobbyType.GAME)
		{
			sender.sendMessage(ChatColor.RED + lobby.getName() + " is not a game lobby");
			return false;
		}
		
		loadouts = ((GameLobby) lobby).getLoadouts();
		sender.sendMessage(ChatManager.bracket1_ + "List of " + lobby.getName() + "'s Loadouts" + ChatManager.bracket2_);
		if (loadouts.isEmpty())
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "No loadouts. To add a loadout, use /" + cmd.getLabel() + " -" + CommandUtil.arg_addmap.getName() + " <loadout> -" + CommandUtil.arg_team.getName() + " <team>");
			return true;
		}
		for (Loadout loadout : loadouts)
		{
			sender.sendMessage(ChatManager.leftborder + loadout.getName() + ChatColor.GRAY + "  " + (((GameLobby) lobby).containsHunterLoadout(loadout.getName()) ? ' ' + Team.HUNTERS.getName(false) : "") + (((GameLobby) lobby).containsPreyLoadout(loadout.getName()) ? ' ' + Team.PREY.getName(false) : ""));
		}
		return true;
	}
	private static boolean addloadoutlobby(CommandSender sender, Subcommand cmd)
	{
		GameLobby lobby;
		String loadoutname;
		Loadout loadout;
		Team team;
		boolean action;
		
		if (CommandUtil.getSelectedLobby(sender) == null || CommandUtil.getSelectedLobby(sender).getType() != LobbyType.GAME)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "You must select a game lobby to do that.");
			return false;
		}
		else
		{
			lobby = (GameLobby) CommandUtil.getSelectedLobby(sender);
		}
		
		loadoutname = cmd.getArgument(CommandUtil.arg_addload).getParameter();
		if (loadoutname == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Please specify the loadout to add.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: -" + cmd.getArgument(CommandUtil.arg_addload).getLabel() + " <loadout>");
			return false;
		}
		else if (Manhunt.getLoadouts().getLoadout(loadoutname) == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + loadoutname + " is not an existing loadout.");
			return false;
		}
		else
		{
			loadout = Manhunt.getLoadouts().getLoadout(loadoutname);
		}
		
		action = false;
		if (!cmd.containsArgument(CommandUtil.arg_team) || cmd.getArgument(CommandUtil.arg_team).getParameters().isEmpty())
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "You must specify the teams that can use this loadout.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: -" + CommandUtil.arg_team.getName() + " <hunter | prey>");
			return false;
		}
		else for (String teamname : cmd.getArgument(CommandUtil.arg_team).getParameters())
		{
			team = Team.fromString(teamname);
			switch (team)
			{
			case HUNTERS:
				if (lobby.containsHunterLoadout(loadoutname))
				{
					sender.sendMessage(ChatManager.leftborder + ChatColor.RED + loadout.getName() + " is already a Hunter loadout.");
				}
				else
				{
					sender.sendMessage(ChatManager.leftborder + loadout.getName() + " added as a Hunter loadout.");
					lobby.addHunterLoadout(loadout);
					action = true;
				}
				break;
				
			case PREY:
				if (lobby.containsHunterLoadout(loadoutname))
				{
					sender.sendMessage(ChatManager.leftborder + ChatColor.RED + loadout.getName() + " is already a Prey loadout.");
				}
				else
				{
					sender.sendMessage(ChatManager.leftborder + loadout.getName() + " added as a Prey loadout.");
					lobby.addHunterLoadout(loadout);
					action = true;
				}
				break;
				
			default:
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + teamname + " is not a valid team. Choose \"Hunter\" or \"Prey\"");
				break;
			}
		}
		
		return action;
	}
	private static boolean removeloadoutlobby(CommandSender sender, Subcommand cmd)
	{
		GameLobby lobby;
		String loadoutname;
		Team team;
		boolean action;
		
		if (CommandUtil.getSelectedLobby(sender) == null || CommandUtil.getSelectedLobby(sender).getType() != LobbyType.GAME)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "You must select a game lobby to do that.");
			return false;
		}
		else
		{
			lobby = (GameLobby) CommandUtil.getSelectedLobby(sender);
		}
		
		loadoutname = cmd.getArgument(CommandUtil.arg_addload).getParameter();
		if (loadoutname == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Please specify the loadout to add.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: -" + cmd.getArgument(CommandUtil.arg_addload).getLabel() + " <loadout>");
			return false;
		}
		else if (!lobby.containsLoadout(loadoutname))
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + loadoutname + " does not contain that loadout.");
			return false;
		}
		
		action = false;
		if (!cmd.containsArgument(CommandUtil.arg_team) || cmd.getArgument(CommandUtil.arg_team).getParameters().isEmpty())
		{
			lobby.removeLoadout(loadoutname);
			sender.sendMessage(ChatManager.leftborder + "Removed loadout '" + loadoutname + "' from " + lobby.getName() + ".");
			return true;
		}
		else for (String teamname : cmd.getArgument(CommandUtil.arg_team).getParameters())
		{
			team = Team.fromString(teamname);
			switch (team)
			{
			case HUNTERS:
				lobby.removeHunterLoadout(loadoutname);
				sender.sendMessage(ChatManager.leftborder + loadoutname + " is no longer a Hunter loadout.");
				action = true;
				break;
				
			case PREY:
				lobby.removePreyLoadout(loadoutname);
				sender.sendMessage(ChatManager.leftborder + loadoutname + " is no longer a Prey loadout.");
				action = true;
				break;
				
			default:
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + teamname + " is not a valid team. Choose \"Hunter\" or \"Prey\"");
				break;
			}
		}
		
		return action;
	}
	
}
