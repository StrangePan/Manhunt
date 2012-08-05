package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class takes the Manhunt command and determines where it should be redirected.
 * @author Deaboy
 *
 */
public class CommandSwitchboard implements CommandExecutor
{
	public CommandSwitchboard() {
		Bukkit.getPluginCommand("manhunt").setExecutor(this);
		Bukkit.getPluginCommand("m").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] args)
	{
		if (args.length == 0)
		{
			//Display quick reference sheet
			return true;
		}
		
		String arg = args[0]; //shortcut
		
		//COMMANDS FOR GENERAL MANHUNT HELP AND INFO
		
		if (arg.equalsIgnoreCase("help"))
		{
			HelpCommands.onCommandHelp(sender, args);
		}
		if (arg.equalsIgnoreCase("rules"))
		{
			HelpCommands.onCommandRules(sender);
		}
		if (arg.equalsIgnoreCase("info"))
		{
			HelpCommands.onCommandInfo(sender);
		}
		if (arg.equalsIgnoreCase("status"))
		{
			HelpCommands.onCommandStatus(sender);
		}
		
		//SETTINGS COMMANDS
		
		if (arg.equalsIgnoreCase("settings"))
		{
			//Display page of settings
		}
		if (arg.equalsIgnoreCase("set"))
		{
			//Changes a setting
		}
		if (arg.equalsIgnoreCase("setworld"))
		{
			//Sets the Manhunt world.
		}
		
		//LOADOUT COMMANDS
		
		if (arg.equalsIgnoreCase("loadout") || arg.equalsIgnoreCase("inv")) //TODO FINISH
		{
			//
		}
		
		//COMMANDS FOR SETTING UP THE TEAMS
		
		if (arg.equalsIgnoreCase("hunter"))
		{
			
		}
		
		
		return true;
	}
}
