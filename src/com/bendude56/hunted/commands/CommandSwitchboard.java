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
			CommandsHelp.onCommandHelp(sender, args);
		}
		if (arg.equalsIgnoreCase("rules"))
		{
			CommandsHelp.onCommandRules(sender);
		}
		if (arg.equalsIgnoreCase("info"))
		{
			CommandsHelp.onCommandInfo(sender);
		}
		if (arg.equalsIgnoreCase("status"))
		{
			CommandsHelp.onCommandStatus(sender);
		}
		
		//SETTINGS COMMANDS
		
		if (arg.equalsIgnoreCase("settings"))
		{
			CommandsSettings.onCommandSettings(sender, args);
		}
		if (arg.equalsIgnoreCase("set"))
		{
			CommandsSettings.onCommandSet(sender, args);
		}
		if (arg.equalsIgnoreCase("setworld"))
		{
			CommandsSettings.onCommandSetworld(sender, args);
		}
		
		//LOADOUT COMMANDS
		
		if (arg.equalsIgnoreCase("loadout") || arg.equalsIgnoreCase("loadouts") || arg.equalsIgnoreCase("inv") || arg.equalsIgnoreCase("invs"))
		{
			//List loadouts
		}
		if (arg.equalsIgnoreCase("saveloadout") || arg.equalsIgnoreCase("saveinv") || arg.equalsIgnoreCase("newloadout") || arg.equalsIgnoreCase("newinv")) //TODO FINISH
		{
			//Save a new loadout
		}
		if (arg.equalsIgnoreCase("loadloadout") || arg.equalsIgnoreCase("loadinv"))
		{
			//Load a loadout
		}
		if (arg.equalsIgnoreCase("deleteloadout") || arg.equalsIgnoreCase("delloadout") || arg.equalsIgnoreCase("delinv") || arg.equalsIgnoreCase("deleteinv"))
		{
			//Delete a loadout
		}
		if (arg.equalsIgnoreCase("hunterloadout") || arg.equalsIgnoreCase("hunterinv"))
		{
			//Sets the hunter loadout or loads it
		}
		if (arg.equalsIgnoreCase("preyloadout") || arg.equalsIgnoreCase("preyinv"))
		{
			//Sets the prey loadout or loads it
		}
		
		//COMMANDS FOR SETTING UP THE TEAMS
		
		if (arg.equalsIgnoreCase("hunter"))
		{
			//Changes a player to team hunter
		}
		if (arg.equalsIgnoreCase("prey"))
		{
			//Changes a player to team prey
		}
		if (arg.equalsIgnoreCase("spectator"))
		{
			//Changes a player to team spectator
		}
		
		//COMMANDS FOR CONTORLLING PLAYERS
		
		if (arg.equalsIgnoreCase("lock"))
		{
			//Locks teams
		}
		if (arg.equalsIgnoreCase("kick"))
		{
			//Kicks a player from the manhunt game
		}
		if (arg.equalsIgnoreCase("quit"))
		{
			//Quits the manhunt game
		}
		
		//COMMANDS FOR EDITING SPAWN
		
		if (arg.equalsIgnoreCase("spawn"))
		{
			//Teleports the player to a spawn
		}
		if (arg.equalsIgnoreCase("setspawn"))
		{
			//Sets a spawn point
		}
		
		//COMMANDS FOR STARTING/STOPING THE GAME
		
		if (arg.equalsIgnoreCase("startgame"))
		{
			//Starts the manhunt game
		}
		if (arg.equalsIgnoreCase("stopgame"))
		{
			//Stops the game
		}
		
		
		return true;
	}
}
