package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntMode;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneFlag;

public abstract class MapCommands
{
	
	public static boolean mmap(CommandSender sender, String args[])
	{
		String args2[];
		
		if (args.length == 0 || args[0].equals("?") || args[0].equalsIgnoreCase("help"))
		{
			Bukkit.getServer().dispatchCommand(sender, "help mmap");
			sender.sendMessage(ChatColor.GRAY + "Available commands:\n  list, select (sel), selected, zones");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list"))
		{
			args2 = new String[args.length - 1];
			for (int i = 1; i < args.length; i++)
				args2[i-1] = args[i];
				
			return listmaps(sender, args2);
		}
		
		else if (args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("selected") || args[0].equalsIgnoreCase("sel"))
		{
			args2 = new String[args.length - 1];
			for (int i = 1; i < args.length; i++)
				args2[i-1] = args[i];
			
			return selectmap(sender, args2);
		}
		
		else if (args[0].equalsIgnoreCase("zones"))
		{
			return listzones(sender, args);
		}
		
		else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("rm"))
		{
			return deletemap(sender, args);
		}
		
		else
		{
			sender.sendMessage(ChatColor.RED + "Unknown command.");
			sender.sendMessage(ChatColor.GRAY + "Available commands: list, select (sel), selected, zones");
		}
		
		return false;
	}
	
	public static boolean mmaps(CommandSender sender, String args[])
	{
		if (args.length > 0 && (args[0].equals("?") || args[0].equalsIgnoreCase("help")))
		{
			Bukkit.dispatchCommand(sender, "help mmaps");
			return true;
		}
		
		return listmaps(sender, args);
	}
	
	private static boolean listmaps(CommandSender sender, String args[])
	{
		final String spacing = "   ";
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return false;
		}
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatManager.bracket1_ + "All Loaded Manhunt Maps" + ChatManager.bracket2_);
			if (Manhunt.getWorlds().size() == 0)
			{
				sender.sendMessage(ChatColor.RED + "  No worlds loaded into Manhunt.");
			}
			for (World world : Manhunt.getWorlds())
			{
				sender.sendMessage(ChatManager.leftborder + world.getName());
				if (world.getMaps().size() == 0)
					sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.GRAY + "[NONE]");
				else
					for (Map map : world.getMaps())
						sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
			}
		}
		else
		{
			sender.sendMessage(ChatManager.bracket1_ + "Loaded Manhunt Maps" + ChatManager.bracket2_);
			for (String wname : args)
			{
				World world = Manhunt.getWorld(wname);
				if (world == null)
				{
					sender.sendMessage(ChatManager.leftborder + ChatColor.RED + wname + " is not a valid world.");
				}
				else
				{
					sender.sendMessage(ChatColor.GOLD + world.getName());
					if (world.getMaps().size() == 0)
						sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.GRAY + "[NONE]");
					else
						for (Map map : world.getMaps())
							sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
				}
			}
		}
		
		return true;
	}
	
	private static boolean selectmap(CommandSender sender, String args[])
	{
		if (args.length == 0 || args[0].equalsIgnoreCase("selected"))
		{
			if (CommandUtil.getSelectedMap(sender) == null)
			{
				sender.sendMessage(ChatColor.RED + "You have not selected any maps.");
				sender.sendMessage(ChatManager.leftborder + "Use /mmap select <mapname>");
				return true;
			}
			else
			{
				sender.sendMessage(ChatManager.color + "Selected map: " + CommandUtil.getSelectedMap(sender).getFullName());
				return true;
			}
		}
		else
		{
			Map map = Manhunt.getMap(args[0]);
			if (map == null)
			{
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid map name.");
				sender.sendMessage(ChatManager.leftborder + "Use /mmap list [page] to see available maps.");
				return true;
			}
			else
			{
				CommandUtil.setSelectedMap(sender, map);
				sender.sendMessage(ChatColor.GREEN + "You have selected map " + map.getFullName() + ".");
				return true;
			}
			
		}
	}
	
	private static boolean deletemap(CommandSender sender, String args[])
	{
		Map map;
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
		}
		
		map = CommandUtil.getSelectedMap(sender);
		
		if (map == null)
		{
			sender.sendMessage(ChatColor.RED + "You must first select a map!");
			sender.sendMessage(ChatColor.GRAY + "Use /mmap select <map full name>");
			return true;
		}
		
		if (!CommandUtil.isVerified(sender))
		{
			CommandUtil.addVerifyCommand(sender, "/mmap delete");
			return true;
		}
		
		for (Lobby lobby : Manhunt.getLobbies())
		{
			lobby.removeMap(map);
		}
		
		map.getWorld().removeMap(map.getName());
		
		sender.sendMessage("That map has been deleted.");
		
		return true;
	}
	
	
	// Zones
	public static boolean mzone(CommandSender sender, String args[])
	{
		String args2[];
		
		if (args.length == 0 || args[0].equals("?") || args[0].equalsIgnoreCase("help"))
		{
			Bukkit.getServer().dispatchCommand(sender, "help mzone");
			sender.sendMessage(ChatColor.GRAY + "Available commands:\n  list, new, delete");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list"))
		{
			return listzones(sender, args);
		}
		else if (args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add"))
		{
			args2 = new String[args.length - 1];
			for (int i = 1; i < args.length; i++)
				args2[i-1] = args[i];
			return newzone(sender, args2);
		}
		else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm"))
		{
			return deletezone(sender, args);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Unknown command.");
			sender.sendMessage(ChatColor.GRAY + "Available commands: list, new");
		}
		
		
		return true;
	}
	
	public static boolean mzones(CommandSender sender, String args[])
	{
		if (args.length > 0 && (args[0].equals("?") || args[0].equalsIgnoreCase("help")))
		{
			Bukkit.dispatchCommand(sender, "help mzones");
			return true;
		}
		
		String args2[] = new String[args.length + 1];
		for (int i = 0; i < args.length; i++)
			args2[i+1] = args[i];
		
		return listzones(sender, args2);
	}
	
	private static boolean listzones(CommandSender sender, String args[])
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		Map m;
		List<Zone> zones;
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		m = CommandUtil.getSelectedMap(sender);
		
		if (m == null)
		{
			sender.sendMessage(ChatColor.RED + "You must first select a map!");
			sender.sendMessage(ChatColor.GRAY + "Use /map select <map full name>");
			return true;
		}
		
		if (args.length == 1)
			page = 1;
		else if (args[1].equalsIgnoreCase("all"))
			all = true;
		else try
		{
			page = Integer.parseInt(args[0]);	
		}
		catch (NumberFormatException e)
		{
			page = 1;
		}
		
		page--;
		
		// Assemble list of settings
		zones = m.getZones();
 
		if (!all)
		{
			if (page * perpage > zones.size() - 1 )
				page = (zones.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (zones.size() == 0)
			{
				sender.sendMessage("There are no zones to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Map Zones " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) zones.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /map zones [n] to get page n of zones");
			zones = zones.subList(page * perpage, Math.min( (page + 1) * perpage, zones.size() ));
		}
		
		for (Zone zone : zones)
		{
			String flaginfo = new String();
			for (ZoneFlag flag : ZoneFlag.values())
			{
				if (zone.checkFlag(flag))
					flaginfo += flag.getName() + ", ";
			}
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + zone.getName() + "    " + flaginfo);
		}
		return true;
	}
	
	private static boolean deletezone(CommandSender sender, String args[])
	{
		// Declarations
		Zone z;
		Map m;
		
		
		// Permission checks
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		// Check for arguments
		if (args.length != 2)
		{
			Bukkit.dispatchCommand(sender, "help mzone");
			sender.sendMessage(ChatColor.RED + "Proper usage: /mzone delete <zone name>");
			sender.sendMessage(ChatColor.GRAY + "Use /mzone list to list all zones in the selected map.");
			return true;
		}
		
		m = CommandUtil.getSelectedMap(sender);
		
		// Check for selected map
		if (m == null)
		{
			sender.sendMessage(ChatColor.RED + "You must first select a map!");
			sender.sendMessage(ChatColor.GRAY + "Use /mmap select <map full name>");
			return true;
		}
		
		z = m.getZone(args[2]);
		
		if (z == null)
		{
			sender.sendMessage(ChatColor.RED + "There are no zones in this map by that name.");
			sender.sendMessage(ChatColor.GRAY + "Use /mzone list to list all zones in the selected map.");
			return true;
		}
		
		m.removeZone(z.getName());
		
		sender.sendMessage(ChatColor.GREEN + "Removed zone " + z.getName() + " removed from map.");
		
		return true;
	}
	
	public static boolean newzone(CommandSender sender, String args[])
	{
		Player p;
		Location c1;
		Location c2;
		Map m;
		String name;
		Zone z;
		
		// Verify sender is a Player
		// Servers and command blocks may not use this command
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return true;
		}
		
		// Verify arguments
		if (args.length > 2 || args.length < 1 || args[0].equals("?") || args[0].equals("help"))
		{
			Bukkit.dispatchCommand(sender, "help newzone");
			return true;
		}
		if (args.length == 2)
		{
			name = args[1];
		}
		else
		{
			name = "";
		}
		
		
		p = (Player) sender;
		c1 = Manhunt.getPlayerSelectionPrimaryCorner(p);
		c2 = Manhunt.getPlayerSelectionSecondaryCorner(p);
		m = Manhunt.getPlayerSelectedMap(p);
		zt = ZoneFlag.fromName(args[0]);
		
		if (!p.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		if (Manhunt.getPlayerMode(p) != ManhuntMode.EDIT)
		{
			sender.sendMessage(ChatColor.RED + "You have to be in EDIT mode for that!");
			sender.sendMessage(ChatColor.GRAY + "Use /manhuntmode edit");
			return true;
		}
		if (m == null)
		{
			sender.sendMessage(ChatColor.RED + "You need to select a map to edit first!");
			sender.sendMessage(ChatColor.GRAY + "Use /map select <full map name>");
			return true;
		}
		if (c1 == null || c2 == null)
		{
			sender.sendMessage(ChatColor.RED + "You must mark off a region first!");
			return true;
		}
		if (p.getWorld() != m.getWorld().getWorld())
		{
			sender.sendMessage(ChatColor.RED + "You're not in the same world as the map!");
			return true;
		}
		if (p.getWorld() != c1.getWorld())
		{
			sender.sendMessage(ChatColor.RED + "You're not in the same world as your selection!");
			return true;
		}
		if (zt == null)
		{
			sender.sendMessage(ChatColor.RED + args[0] + " is not a valid zone type.");
			String types = ChatColor.GRAY + "Choose from the following:";
			for (ZoneFlag t : ZoneFlag.values())
				types += " " + t.getName();
			sender.sendMessage(types);
		}
		if (!name.isEmpty())
		{
			if (m.getZone(name) != null)
			{
				sender.sendMessage(ChatColor.RED + "A zone with that name already exists.");
				return true;
			}
		}
		
		
		z = m.createZone(zt, name, c1, c2);
		
		if (z == null)
			sender.sendMessage(ChatColor.RED + "There was an error creating a new zone.");
		else
			sender.sendMessage(ChatManager.leftborder + "Successfully created zone " + ChatColor.GREEN + z.getName() + ChatManager.color + " in map " + ChatColor.GREEN + m.getName() + ChatManager.color + ".");
		
		return true;
	}
	
	
	// Spawns
	public static boolean mspawn(CommandSender sender, String args[])
	{
		if (args.length == 0 || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))
		{
			Bukkit.dispatchCommand(sender, "/help mspawn");
			return true;
		}
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		else if (args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("create"))
		{
			if (args.length == 2 && (args[1].equalsIgnoreCase("hunter") || args[1].equalsIgnoreCase("prey")))
			{
				
			}
			else
			{
				sender.sendMessage(ChatColor.GRAY + "Usage: /mspawn " + args[0] + " <hunter|prey|spectator>");
				return true;
			}
			
			
			
		}
		
		return true;
	}
	
	public static boolean mspawns(CommandSender sender, String args[])
	{
		Bukkit.dispatchCommand(sender, "/mspawn list " + (args.length == 0 ? "" : args[0]));
		return true;
	}
	
	
}
