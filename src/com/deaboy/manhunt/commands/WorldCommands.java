package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.chat.ChatManager;

public abstract class WorldCommands
{
	public static boolean mworld(CommandSender sender, Command cmd)
	{
		boolean action = false;
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_list))
		{
			action |= worldlist(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_info))
		{
			action |= worldinfo(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_lsmaps))
		{
			action |= worldlistmaps(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_issues))
		{
			action |= worldissues(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_tp))
		{
			action |= worldspawn(sender, cmd);
		}
		if (cmd.containsArgument(CommandUtil.arg_setspawn))
		{
			action |= worldsetspawn(sender, cmd);
		}
		
		if (!action)
		{
			sender.sendMessage(ChatColor.GRAY + "No actions performed.");
		}
		
		return true;
	}
	private static boolean worldlist(CommandSender sender, Command cmd)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<org.bukkit.World> worlds;
		
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		
		// Get the page #
		if (cmd.containsArgument(CommandUtil.arg_page) && cmd.getArgument(CommandUtil.arg_page).getParameter() != null)
		{
			if (cmd.getArgument(CommandUtil.arg_page).getParameter().equalsIgnoreCase("all"))
			{
				all = true;
				page = 1;
			}
			else
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
		worlds = Bukkit.getWorlds();
 
		if (!all)
		{
			if (page * perpage > worlds.size() - 1 )
				page = (worlds.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (worlds.size() == 0)
			{
				sender.sendMessage("There are no worlds to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Worlds " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) worlds.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all && worlds.size() > perpage)
		{
			sender.sendMessage(ChatColor.GRAY + "Use " + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_list).getLabel() + " [n] to get page n of worlds");
			worlds = worlds.subList(page * perpage, Math.min( (page + 1) * perpage, worlds.size() ));
		}
		
		for (org.bukkit.World world : worlds)
		{
			sender.sendMessage(ChatManager.leftborder + (Manhunt.getWorld(world) == null ? ChatColor.GRAY : ChatColor.WHITE) + world.getName());
		}
		return true;
	}
	private static boolean worldsetspawn(CommandSender sender, Command cmd)
	{
		Location loc;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		loc = ((Player) sender).getLocation();
		loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		sender.sendMessage(ChatManager.leftborder + loc.getWorld().getName() + " spawn point " + ChatColor.GREEN + "successfully set.");
		return true;
	}
	private static boolean worldspawn(CommandSender sender, Command cmd)
	{
		org.bukkit.World world;
		String worldname;
		Player player;
		String playername;
		
		playername = cmd.getArgument(CommandUtil.arg_tp).getParameter();
		if (cmd.containsArgument(CommandUtil.arg_world))
			worldname = cmd.getArgument(CommandUtil.arg_world).getParameter();
		else
			worldname = null;
		
		if ((worldname == null || playername == null) && !(sender instanceof Player))
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "You must specify which player to send to which world's spawn.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_tp).getLabel() + " <playername> -" + CommandUtil.arg_world + " <worldname>");
			return false;
		}
		
		if (worldname == null)
		{
			world = ((Player) sender).getWorld();
		}
		else
		{
			world = Bukkit.getWorld(worldname);
			if (world == null)
			{
				sender.sendMessage(ChatManager.leftborder + worldname + " is " + ChatColor.RED + "not a valid world.");
				sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Use /" + cmd.getLabel() + " -" + CommandUtil.arg_list.getName() + " to see a list of worlds.");
				return false;
			}
		}
		
		if (playername == null)
		{
			player = ((Player) sender);
		}
		else
		{
			player = Bukkit.getPlayer(playername);
			if (player == null)
			{
				sender.sendMessage(ChatManager.leftborder + "Player '" + playername + "' is " + ChatColor.RED + "not online.");
				return false;
			}
		}
		
		player.teleport(world.getSpawnLocation());
		player.sendMessage(ChatManager.leftborder + "Teleported to " + world.getName() + "'s spawn.");
		if (player != sender)
			sender.sendMessage(ChatManager.leftborder + "Sent " + player.getName() + " to " + world.getName() + "'s spawn.");
		return true;
	}
	private static boolean worldissues(CommandSender sender, Command cmd)
	{
		World world;
		String worldname;
		List<String> issues;
		
		if (cmd.containsArgument(CommandUtil.arg_world))
			worldname = cmd.getArgument(CommandUtil.arg_world).getParameter();
		else
			worldname = cmd.getArgument(CommandUtil.arg_issues).getParameter();
		if (worldname == null && !(sender instanceof Player))
		{
			sender.sendMessage(ChatManager.leftborder + "You must specify a world to view its issues.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_issues).getLabel() + " <worldname>");
			return false;
		}
		
		if (worldname == null)
		{
			world = Manhunt.getWorld(((Player) sender).getWorld());
		}
		else
		{
			world = Manhunt.getWorld(worldname);
			if (world == null)
			{
				sender.sendMessage(ChatManager.leftborder + "World '" + worldname + "' " + ChatColor.RED + " does not exist.");
				return false;
			}
		}
		
		issues = new ArrayList<String>();
		for (Map map : world.getMaps())
		{
			if (map.hasIssues())
				issues.add(map.getName());
		}
		
		sender.sendMessage(ChatManager.bracket1_ + "Issues in " + world.getName() + ChatManager.bracket2_);
		if (issues.isEmpty())
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  There are no issues with this world.");
			return false;
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Use /" + CommandUtil.cmd_mmap.getName() + " -" + CommandUtil.arg_select.getName() + " <map> -" + CommandUtil.arg_issues + " to view more info.");
			for (String mapname : issues)
			{
				sender.sendMessage(ChatManager.leftborder + mapname + ChatColor.RED + " has some issues!");
			}
			return true;
		}
		
	}
	private static boolean worldinfo(CommandSender sender, Command cmd)
	{
		World world;
		String worldname;
		boolean issues = false;
		
		if (cmd.containsArgument(CommandUtil.arg_world))
			worldname = cmd.getArgument(CommandUtil.arg_world).getParameter();
		else
			worldname = cmd.getArgument(CommandUtil.arg_issues).getParameter();

		if (worldname == null && !(sender instanceof Player))
		{
			sender.sendMessage(ChatManager.leftborder + "You must specify a world to view its info.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_info).getLabel() + " <worldname>");
			return false;
		}
		
		if (worldname == null)
		{
			world = Manhunt.getWorld(((Player) sender).getWorld());
		}
		else
		{
			world = Manhunt.getWorld(worldname);
			if (world == null)
			{
				sender.sendMessage(ChatManager.leftborder + "World '" + worldname + "' " + ChatColor.RED + " does not exist.");
				return false;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + world.getName() + "'s Info" + ChatManager.bracket2_);
		sender.sendMessage(ChatManager.leftborder + "Spawn: [" + world.getSpawn().getLocation().getBlockX() + "," + world.getSpawn().getLocation().getBlockY() + "," + world.getSpawn().getLocation().getBlockZ() + "] (" + world.getSpawn().getRange() + " blocks)");
		sender.sendMessage(ChatManager.leftborder + "Maps: " + (world.getMaps().isEmpty() ? ChatColor.GRAY + "none" : world.getMaps().size()));
		for (Map map : world.getMaps())
		{
			if (map.hasIssues())
			{
				issues = true;
				break;
			}
		}
		if (issues)
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Issues detected! " + ChatColor.GRAY + "Use /" + cmd.getLabel() + " -" + CommandUtil.arg_issues.getName() + " to view.");
		else
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "No issues.");
		return true;
	}
	private static boolean worldlistmaps(CommandSender sender, Command cmd)
	{
		World world;
		String worldname;
		final String spacing = "   ";
		
		if (cmd.containsArgument(CommandUtil.arg_world))
			worldname = cmd.getArgument(CommandUtil.arg_world).getParameter();
		else
			worldname = cmd.getArgument(CommandUtil.arg_issues).getParameter();

		if (worldname == null && !(sender instanceof Player))
		{
			sender.sendMessage(ChatManager.leftborder + "You must specify a world to view its info.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_info).getLabel() + " <worldname>");
			return false;
		}
		
		if (worldname == null)
		{
			world = Manhunt.getWorld(((Player) sender).getWorld());
		}
		else
		{
			world = Manhunt.getWorld(worldname);
			if (world == null)
			{
				sender.sendMessage(ChatManager.leftborder + "World '" + worldname + "' " + ChatColor.RED + " does not exist.");
				return false;
			}
		}

		sender.sendMessage(ChatManager.bracket1_ + "Loaded Manhunt Maps" + ChatManager.bracket2_);
		sender.sendMessage(ChatColor.GOLD + world.getName());
		if (world.getMaps().size() == 0)
		{
			sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.GRAY + "[NONE]");
		}
		else
		{
			for (Map map : world.getMaps())
				sender.sendMessage(ChatManager.leftborder + spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
		}
		return true;
	}
	
	public static boolean mspawn(CommandSender sender, String args[])
	{
		Bukkit.dispatchCommand(sender, CommandUtil.cmd_mworld.getName() + " -" + CommandUtil.arg_tp.getName());
		return true;
	}
	public static boolean msetspawn(CommandSender sender, String args[])
	{
		Bukkit.dispatchCommand(sender, CommandUtil.cmd_mworld.getName() + " -" + CommandUtil.arg_setspawn.getName());
		return true;
	}
}

