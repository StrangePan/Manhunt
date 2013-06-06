package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.map.ManhuntMap;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneFlag;

public abstract class MapCommands
{
	
	public static boolean mmap(CommandSender sender, String args[])
	{
		boolean action = false;
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		Command cmd = Command.fromTemplate(CommandUtil.cmd_mmap, "mzone", args);
		
		if (cmd.containsArgument(CommandUtil.arg_help))
		{
			Bukkit.getServer().dispatchCommand(sender, "help " + cmd.getName());
			action = true;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_list))
		{
			action |= listmaps(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_select))
		{
			action |= selectmap(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_create))
		{
			action |= createmap(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_delete))
		{
			action |= deletemap(sender, cmd);
		}
		
		if (!action)
		{
			sender.sendMessage(ChatColor.GRAY + "No actions performed.");
		}
		
		return true;
	}
	private static boolean listmaps(CommandSender sender, Command cmd)
	{
		final String spacing = "   ";
		
		if (cmd.containsArgument(CommandUtil.arg_world))
		{
			sender.sendMessage(ChatManager.bracket1_ + "Loaded Manhunt Maps" + ChatManager.bracket2_);
			for (String wname : cmd.getArgument(CommandUtil.arg_world).getParameters())
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
		else
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
		
		return true;
	}
	private static boolean selectmap(CommandSender sender, Command cmd)
	{
		Map map;
		String mapname;
		
		if (cmd.getArgument(CommandUtil.arg_select).getParameter() == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid parameter usage: -" + cmd.getArgument(CommandUtil.arg_select).getLabel());
			sender.sendMessage(ChatColor.GRAY + " Parameter usage: -select <map name>");
			return false;
		}
		
		mapname = cmd.getArgument(CommandUtil.arg_select).getParameter();
		if (!mapname.contains("."))
		{
			if (sender instanceof Player)
				mapname = Manhunt.getWorld(((Player) sender).getWorld()).getName() + '.' + mapname;
		}
		
		map = Manhunt.getMap(mapname);
		if (map == null)
		{
			sender.sendMessage(ChatColor.RED + "No map named '" + mapname + "' exists.");
			sender.sendMessage(ChatManager.leftborder + "Use /mmap -list [-world <world name>] to see available maps.");
			return false;
		}
		else
		{
			CommandUtil.setSelectedMap(sender, map);
			sender.sendMessage(ChatColor.GREEN + "You have selected map " + map.getFullName() + ".");
			return true;
		}
	}
	private static boolean createmap(CommandSender sender, Command cmd)
	{
		String mapname;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		if (!cmd.containsArgument(CommandUtil.arg_name))
		{
			sender.sendMessage(ChatColor.RED + "Name argument missing.");
			sender.sendMessage(ChatColor.GRAY + " Parameter usage: -name <name>");
			return false;
		}
		
		mapname = cmd.getArgument(CommandUtil.arg_name).getParameter();
		if (mapname == null || mapname.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "Invalid parameter use: name");
			sender.sendMessage(ChatColor.GRAY + " Parameter usage: -name <name>");
			return false;
		}
		
		if (Manhunt.getWorld(((Player) sender).getWorld()).getMap(mapname) != null)
		{
			sender.sendMessage(ChatColor.RED + "A map by that name already exists!");
			return false;
		}
		
		Manhunt.getWorld(((Player) sender).getWorld()).addMap(mapname, new ManhuntMap(mapname, ((Player) sender).getLocation(), Manhunt.getWorld(((Player) sender).getWorld())));
		sender.sendMessage(ChatColor.GREEN + "Map '" + mapname + "' created in world '" + Manhunt.getWorld(((Player) sender).getWorld()).getName() + "'.");
		return true;
	}
	private static boolean deletemap(CommandSender sender, Command cmd)
	{
		Map map;
		
		map = CommandUtil.getSelectedMap(sender);
		if (map == null)
		{
			sender.sendMessage(ChatColor.RED + "You must first select a map!");
			sender.sendMessage(ChatColor.GRAY + " Use /mmap -select <map name>");
			return false;
		}
		
		for (Lobby lobby : Manhunt.getLobbies())
		{
			lobby.removeMap(map);
		}
		
		map.getWorld().removeMap(map.getName());
		
		sender.sendMessage(ChatColor.GREEN + "Map '" + map.getName() + "' has been deleted.");
		
		return true;
	}
	
	
	
	// Zones
	public static boolean mzone(CommandSender sender, String args[])
	{
		boolean action = false;
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		Command cmd = Command.fromTemplate(CommandUtil.cmd_mzone, "mzone", args);
		
		if (cmd.containsArgument(CommandUtil.arg_help))
		{
			Bukkit.getServer().dispatchCommand(sender, "help mzone");
			action = true;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_list))
		{
			action |= listzones(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_select))
		{
			action |= selectzone(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_create))
		{
			action |= createzone(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_delete))
		{
			action |= deletezone(sender, cmd);
		}
		
		if (!action)
		{
			sender.sendMessage(ChatColor.GRAY + "No actions performed.");
		}
		
		return true;
	}
	private static boolean listzones(CommandSender sender, Command cmd)
	{
		int perpage = 8;
		boolean all = false;
		Map m;
		List<Zone> zones;
		int page;
		
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
		
		
		m = CommandUtil.getSelectedMap(sender);
		
		if (m == null)
		{
			sender.sendMessage(ChatColor.RED + "You must first select a map!");
			sender.sendMessage(ChatColor.GRAY + "Use /map select <map full name>");
			return false;
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
				return false;
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
	private static boolean selectzone(CommandSender sender, Command cmd)
	{
		String zonename;
		Zone zone;
		Map map;
		
		map = CommandUtil.getSelectedMap(sender);
		if (map == null)
		{
			sender.sendMessage(ChatColor.RED + "You must select a map before you can select a zone within!");
			sender.sendMessage(ChatColor.GRAY+ " Use /mmap -s <mapname> to select a map.");
			return false;
		}
		
		zonename = cmd.getArgument(CommandUtil.arg_select).getParameter();
		zone = map.getZone(zonename);
		if (zonename == null || zonename.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "You must include the name of the zone you want to select.");
			sender.sendMessage(ChatColor.GRAY + " /mzone -s <zonename>");
			return false;
		}
		else if (zone == null)
		{
			sender.sendMessage(ChatColor.RED + "No zone with that name exists in the selected map.");
			sender.sendMessage(ChatColor.GRAY + " To see available zones, use /mzone -list [all] [-page <page>]");
			sender.sendMessage(ChatColor.GRAY + " To select a different map, use /mmap -s <mapname>");
			return false;
		}
		
		CommandUtil.setSelectedZone(sender, zone);
		sender.sendMessage(ChatColor.GREEN + "Selected zone '" + zone.getName() + "' in map '" + map.getName() + "'.");
		return true;
	}
	private static boolean createzone(CommandSender sender, Command cmd)
	{
		// Declarations.
		Zone zone;
		Map map;
		String zonename;
		List<ZoneFlag> flags;
		Location primarycorner;
		Location secondarycorner;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		map = CommandUtil.getSelectedMap(sender);
		if (map == null)
		{
			sender.sendMessage(CommandUtil.MAP_NOT_SELECTED);
			return false;
		}
		
		zonename = cmd.getArgument(CommandUtil.arg_name).getParameter();
		if (zonename == null || zonename.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "Name argument missing.");
			sender.sendMessage(ChatColor.GRAY + " Parameter usage: -name <name>");
			return false;
		}
		
		primarycorner = Manhunt.getPlayerSelectionPrimaryCorner((Player) sender);
		secondarycorner = Manhunt.getPlayerSelectionSecondaryCorner((Player) sender);
		if (primarycorner == null || secondarycorner == null)
		{
			if (primarycorner == null)
				sender.sendMessage(CommandUtil.CORNER_PRIMARY_NOT_SELECTED);
			if (secondarycorner == null)
				sender.sendMessage(CommandUtil.CORNER_SECONDARY_NOT_SELECTED);
			return false;
		}
		
		if (map.getZone(zonename) != null)
		{
			int i;
			for (i = 0; map.getZone(zonename + i) != null; i++);
			zonename += i;
		}
		
		flags = new ArrayList<ZoneFlag>();
		if (cmd.containsArgument(CommandUtil.arg_zoneflags))
		{
			for (String f : cmd.getArgument(CommandUtil.arg_zoneflags).getParameters())
			{
				if (ZoneFlag.fromName(f) != null)
					flags.add(ZoneFlag.fromName(f));
			}
		}
		
		zone = map.createZone(zonename, primarycorner, secondarycorner, flags);
		if (zone == null)
		{
			sender.sendMessage(ChatColor.RED + "There was an error creating the zone.");
			return false;
		}
		else
		{
			sender.sendMessage(ChatColor.GREEN + "Zone '" + zone.getName() + "' successfully created in map '" + map.getName() + "'");
			sender.sendMessage(ChatColor.GRAY + " ("
					+ zone.getPrimaryCorner().getBlockX() + ","
					+ zone.getPrimaryCorner().getBlockY() + ","
					+ zone.getPrimaryCorner().getBlockZ() + ")  ("
					+ zone.getSecondaryCorner().getBlockX() + ","
					+ zone.getSecondaryCorner().getBlockY() + ","
					+ zone.getSecondaryCorner().getBlockZ() + ")");
			if (flags.isEmpty())
			{
				sender.sendMessage(ChatColor.GRAY + " No flags.");
			}
			else
			{
				sender.sendMessage(ChatColor.GRAY + " Flags:");
				for (ZoneFlag flag : flags)
				{
					sender.sendMessage(ChatColor.GRAY + "  - " + flag.getName());
				}
			}
		
			return true;
		}
	}
	private static boolean deletezone(CommandSender sender, Command cmd)
	{
		String zonename;
		
		if (CommandUtil.getSelectedMap(sender) == null)
		{
			sender.sendMessage(CommandUtil.MAP_NOT_SELECTED);
			return false;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_name))
		{
			zonename = cmd.getArgument(CommandUtil.arg_name).getParameter();
			if (zonename == null || zonename.isEmpty())
			{
				sender.sendMessage(ChatColor.RED + "Invalid parameter usage: -" + cmd.getArgument(CommandUtil.arg_name).getLabel());
				sender.sendMessage(ChatColor.GRAY + " Parameter usage: -name <name>");
				return false;
			}
			else if (CommandUtil.getSelectedMap(sender).getZone(zonename) == null)
			{
				sender.sendMessage(ChatColor.RED + "No zone with that name exists in the selected map.");
				sender.sendMessage(ChatColor.GRAY + " To see available zones, use /mzone -list [all] [-page <page>]");
				sender.sendMessage(ChatColor.GRAY + " To select a different map, use /mmap -s <mapname>");
				return false;
			}
		}
		else
		{
			if (CommandUtil.getSelectedZone(sender) == null)
			{
				sender.sendMessage(ChatColor.RED + "You have not selected a zone to delete.");
				return false;
			}
			else
			{
				zonename = CommandUtil.getSelectedZone(sender).getName();
			}
		}
		
		sender.sendMessage(ChatColor.GREEN + "Zone '" + zonename + "' removed from map '" + CommandUtil.getSelectedZone(sender).getName() +"'");
		CommandUtil.getSelectedMap(sender).removeZone(zonename);
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
	
}
