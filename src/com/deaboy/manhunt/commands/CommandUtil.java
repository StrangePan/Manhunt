package com.deaboy.manhunt.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.Map;

public class CommandUtil
{
	public static final String NO_PERMISSION = ChatColor.RED + "You don't have permission to do that.";
	public static final String GAME_RUNNING = ChatColor.RED + "You can't do that while the game is running.";
	public static final String NO_GAME_RUNNING = ChatColor.RED + "There are no Manhunt games running.";
	public static final String IS_SERVER = ChatColor.RED + "The server cannot do that.";
	public static final String WRONG_WORLD = ChatColor.RED + "The player must be in the manhunt world first!";
	public static final String LOCKED = ChatColor.RED + "The teams are locked.";
	public static final String INVALID_USAGE = ChatColor.RED + "Invalid usage.";
	public static final String NO_WORLD_BY_NAME = ChatColor.RED + "There is no Manhunt world by that name!";
	
	
	
	private HashMap<String, Map> selected_maps;
	private HashMap<CommandSender, String> vcommands;
	private HashMap<CommandSender, Boolean> verified;
	
	
	
	//---------------- Constructors ----------------//
	public CommandUtil()
	{
		this.selected_maps = new HashMap<String, Map>();
		this.vcommands = new HashMap<CommandSender, String>();
		this.verified = new HashMap<CommandSender, Boolean>();
	}
	
	
	
	//---------------- Map Selection ----------------//
	public static void setSelectedMap(CommandSender sender, Map map)
	{
		Manhunt.getCommandUtil().selected_maps.put(sender.getName(), map);
	}
	
	public static Map getSelectedMap(CommandSender sender)
	{
		if (Manhunt.getCommandUtil().selected_maps.containsKey(sender.getName()))
			return Manhunt.getCommandUtil().selected_maps.get(sender.getName());
		else
			return null;
	}
	
	public static Map getSelectedMap(String name)
	{
		if (Manhunt.getCommandUtil().selected_maps.containsKey(name))
			return Manhunt.getCommandUtil().selected_maps.get(name);
		else
			return null;
	}
	
	
	
	//---------------- Verification ----------------//
	public static void addVerifyCommand(CommandSender sender, String command, String message)
	{
		addVerifyCommand(sender, command);
		sender.sendMessage(message);
		sender.sendMessage(ChatColor.GRAY + "To confirm, type \"/mverify\" or \"/cancel\"");
	}
	
	public static void addVerifyCommand(CommandSender sender, String command)
	{
		Manhunt.getCommandUtil().vcommands.put(sender, command);
		Manhunt.getCommandUtil().verified.put(sender, false);
	}
	
	public static boolean isVerified(CommandSender sender)
	{
		if (isVerifying(sender))
			return Manhunt.getCommandUtil().verified.get(sender);
		else
			return false;
	}
	
	public static boolean isVerifying(CommandSender sender)
	{
		return (Manhunt.getCommandUtil().verified.containsKey(sender)
				&& Manhunt.getCommandUtil().vcommands.containsKey(sender));
	}
	
	public static void executeCommand(CommandSender sender)
	{
		if (isVerifying(sender))
		{
			Manhunt.getCommandUtil().verified.put(sender,true);
			Bukkit.dispatchCommand(sender, Manhunt.getCommandUtil().vcommands.get(sender));
			cancelCommand(sender);
		}
	}
	
	public static void cancelCommand(CommandSender sender)
	{
		if (Manhunt.getCommandUtil().vcommands.containsKey(sender))
			Manhunt.getCommandUtil().vcommands.remove(sender);
		if (Manhunt.getCommandUtil().verified.containsKey(sender))
			Manhunt.getCommandUtil().verified.remove(sender);
	}
	
	
	
}
