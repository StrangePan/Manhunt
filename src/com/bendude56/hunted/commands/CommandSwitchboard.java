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
		if (arg.equalsIgnoreCase("list"))
		{
			CommandsTeams.onCommandList(sender, args);
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
		if (arg.equalsIgnoreCase("setmode"))
		{
			CommandsSettings.onCommandSetmode(sender, args);
		}
		
		//LOADOUT COMMANDS
		
		if (arg.equalsIgnoreCase("listinv"))
		{
			CommandsLoadouts.onCommandListinv(sender, args);
		}
		if (arg.equalsIgnoreCase("saveloadout") || arg.equalsIgnoreCase("saveinv") || arg.equalsIgnoreCase("newloadout") || arg.equalsIgnoreCase("newinv"))
		{
			CommandsLoadouts.onCommandNewinv(sender, args);
		}
		if (arg.equalsIgnoreCase("loadloadout") || arg.equalsIgnoreCase("loadinv"))
		{
			CommandsLoadouts.onCommandLoadinv(sender, args);
		}
		if (arg.equalsIgnoreCase("deleteloadout") || arg.equalsIgnoreCase("delloadout") || arg.equalsIgnoreCase("delinv") || arg.equalsIgnoreCase("deleteinv"))
		{
			CommandsLoadouts.onCommandDelinv(sender, args);
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
			CommandsTeams.onCommandHunter(sender, args);
		}
		if (arg.equalsIgnoreCase("prey"))
		{
			CommandsTeams.onCommandPrey(sender, args);
		}
		if (arg.equalsIgnoreCase("spectator"))
		{
			CommandsTeams.onCommandSpectate(sender, args);
		}
		
		//COMMANDS FOR CONTORLLING PLAYERS
		
		if (arg.equalsIgnoreCase("lock"))
		{
			CommandsTeams.onCommandLock(sender, args);
		}
		if (arg.equalsIgnoreCase("kick"))
		{
			CommandsTeams.onCommandKick(sender, args);
		}
		if (arg.equalsIgnoreCase("quit"))
		{
			CommandsTeams.onCommandQuit(sender, args);
		}
		
		//COMMANDS FOR EDITING SPAWN
		
		if (arg.equalsIgnoreCase("spawn"))
		{
			CommandsGeneral.onCommandSpawn(sender, args);
		}
		if (arg.equalsIgnoreCase("setspawn"))
		{
			CommandsGeneral.onCommandSetspawn(sender, args);
		}
		
		//COMMANDS FOR STARTING/STOPING THE GAME
		
		if (arg.equalsIgnoreCase("startgame"))
		{
			CommandsGeneral.onCommandStartgame(sender, args);
		}
		if (arg.equalsIgnoreCase("stopgame"))
		{
			CommandsGeneral.onCommandStopgame(sender, args);
		}
		
		
		return true;
	}
}
