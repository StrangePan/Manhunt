package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;

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
	
	public static boolean mlobby(CommandSender sender, Command cmd)
	{
		boolean action = false;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
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
		if (cmd.containsArgument(CommandUtil.arg_lsmaps))
		{
			action |= listmapslobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_addmap))
		{
			action |= addmaplobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_remmap))
		{
			action |= removemaplobby(sender,cmd);
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
		if (cmd.containsArgument(CommandUtil.arg_setspawn))
		{
			action |= movespawnlobby(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_range))
		{
			action |= rangespawnlobby(sender, cmd);
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
			sender.sendMessage(ChatColor.GRAY + "Use /mlobby list [n] to get page n of lobbies");
			lobbies = lobbies.subList(page * perpage, Math.min( (page + 1) * perpage, lobbies.size() ));
		}
		
		for (Lobby lobby: lobbies)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + lobby.getName() + "    " + ChatColor.GRAY + lobby.getType().name());
		}
		return true;
	}
	private static boolean joinlobby(CommandSender sender, Command cmd)
	{
		Lobby lobby;
		String lobbyname;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
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
	private static boolean leavelobby(CommandSender sender, Command cmd)
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
	private static boolean createlobby(CommandSender sender, Command cmd)
	{
		Location loc;
		String lobbyname;
		LobbyType type;
		
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
			type = LobbyType.fromName(cmd.getArgument(CommandUtil.arg_lobbytype).getParameter());
			if (type == null)
			{
				if (cmd.getArgument(CommandUtil.arg_lobbytype).getParameter() != null)
				{
					sender.sendMessage(ChatColor.RED + "Unable to parse '" + cmd.getArgument(CommandUtil.arg_lobbytype).getParameter() + "' as a lobby type.");
					sender.sendMessage(ChatColor.GRAY + "  Argument usage: -" + cmd.getArgument(CommandUtil.arg_lobbytype).getLabel() + " <hub|game>");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "Invalid use of the " + CommandUtil.arg_lobbytype + " parameter.");
					sender.sendMessage(ChatColor.GRAY + "  Argument usage: -" + cmd.getArgument(CommandUtil.arg_lobbytype).getLabel() + " <hub|game>");
				}
				return false;
			}
		}
		else
		{
			type = LobbyType.GAME;
		}
		
		Manhunt.createLobby(lobbyname, type, loc);
		sender.sendMessage(ChatColor.GREEN + "Created lobby '" + lobbyname + "' in world '" + ((Player) sender).getWorld().getName() + "'.");
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "  with spawn at [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "]");
		return true;
	}
	private static boolean deletelobby(CommandSender sender, Command cmd)
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
	private static boolean movespawnlobby(CommandSender sender, Command cmd)
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
	private static boolean rangespawnlobby(CommandSender sender, Command cmd)
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
	private static boolean closelobby(CommandSender sender, Command cmd)
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
	private static boolean openlobby(CommandSender sender, Command cmd)
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
	private static boolean selectlobby(CommandSender sender, Command cmd)
	{
		Lobby lobby;
		String lobbyname = cmd.getArgument(CommandUtil.arg_select).getParameter();
		
		if (lobbyname == null)
		{
			sender.sendMessage(ChatColor.RED + "You must include the name of the lobby you want to select.");
			sender.sendMessage(ChatColor.GRAY + " /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_select).getLabel() + " <zonename>");
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
	private static boolean listmapslobby(CommandSender sender, Command cmd)
	{
		Lobby lobby;
		List<Map> maps;
		
		lobby = CommandUtil.getSelectedLobby(sender);
		if (lobby == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a lobby to view it's maps.");
			return false;
		}
		
		maps = lobby.getMaps();
		sender.sendMessage(ChatManager.bracket1_ + "List of " + lobby.getName() + "'s Maps" + ChatManager.bracket2_);
		if (maps.isEmpty())
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "No maps. To add a map, use /" + cmd.getLabel() + " -" + CommandUtil.arg_addmap.getName() + " <map1> [map2] [map3] ...");
		}
		for (Map map : lobby.getMaps())
		{
			sender.sendMessage(ChatManager.leftborder + map.getFullName());
		}
		return true;
	}
	private static boolean addmaplobby(CommandSender sender, Command cmd)
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
			if (lobby.getMaps().contains(map))
			{
				sender.sendMessage(ChatColor.YELLOW + "Lobby already contains '" + mapname + "'.");
				fail++;
				continue;
			}
			
			lobby.addMap(map);
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
	private static boolean removemaplobby(CommandSender sender, Command cmd)
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
			if (!lobby.getMaps().contains(map))
			{
				sender.sendMessage(ChatColor.YELLOW + "Lobby already does not contain '" + mapname + "'.");
				fail++;
				continue;
			}
			
			lobby.removeMap(map);
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
	
}
