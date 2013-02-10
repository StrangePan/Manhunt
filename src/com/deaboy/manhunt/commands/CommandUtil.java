package com.deaboy.manhunt.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.Map;

public class CommandUtil
{
	public static String NO_PERMISSION = ChatColor.RED + "You don't have permission to do that.";
	public static String GAME_RUNNING = ChatColor.RED + "You can't do that while the game is running.";
	public static String NO_GAME_RUNNING = ChatColor.RED + "There are no Manhunt games running.";
	public static String IS_SERVER = ChatColor.RED + "The server cannot do that.";
	public static String WRONG_WORLD = ChatColor.RED + "The player must be in the manhunt world first!";
	public static String LOCKED = ChatColor.RED + "The teams are locked.";
	
	
	
	private HashMap<String, Map> selected_maps;
	
	
	
	//---------------- Constructors ----------------//
	public CommandUtil()
	{
		this.selected_maps = new HashMap<String, Map>();
	}
	
	
	//---------------- Getters ----------------//
	public static Map getSelectedMap(String name)
	{
		if (Manhunt.getCommandUtil().selected_maps.containsKey(name))
		{
			return Manhunt.getCommandUtil().selected_maps.get(name);
		}
		return null;
	}
	
	
	
	//---------------- Setters ----------------//
	public static void setSelectedMap(String name, Map map)
	{
		Manhunt.getCommandUtil().selected_maps.put(name, map);
	}
	
	public static void setSelectedMap(CommandSender sender, Map map)
	{
		if (!(sender instanceof ConsoleCommandSender))
		{
			Manhunt.getCommandUtil().selected_maps.put(sender.getName(), map);
		}
	}
	
}
