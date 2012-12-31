package com.bendude56.hunted.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.map.World;

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
	
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] arguments)
	{
		//-------- Declarations --------//
		Arguments args;
		GameLobby lobby;
		Map map;
		World world;
		
		
		
		//-------- Assignmnets --------//
		args = Arguments.fromStringArray(arguments);
		
		
		
		// Check for lobby selection
		if (args.contains("lobby"))
		{
			lobby = Manhunt.getLobby(args.getString("lobby"));
			if (lobby == null && args.getLong("lobby") != null)
			{
				lobby = Manhunt.getLobby(args.getLong("lobby"));
			}
			
			if (lobby != null)
			{
				Manhunt.getCommandHelper().setSelectedLobby(sender, lobby);
			}
			else
			{
				sender.sendMessage("The lobby " + args.getString("lobby") + " was not found.");
				return true;
			}
		}
		
		// Check for map selection
		if (args.contains("map"))
		{
			if (sender instanceof ConsoleCommandSender)
			{
				sender.sendMessage("The console cannot select maps.");
				return true;
			}
			else
			{
				world = Manhunt.getWorld(((Player) sender).getWorld());
				if (world == null)
				{
					sender.sendMessage("You must be in a world with a map.");
					return true;
				}
				
				map = world.getMap(args.getString("map"));
				if (map == null)
				{
					sender.sendMessage("The map " + args.getString("map") + " in world " + world.getName() + " was not found.");
					return true;
				}
				
				Manhunt.getCommandHelper().setSelectedMap(sender, map);
			}
		}
			
		
		
		/*
		String arg;
		
		if (args.length > 0)
		{
			arg = args[0];
		}
		else
		{
			arg = null;
		}
		
		if (arg == null)
		{
			Bukkit.getServer().dispatchCommand(sender, "m help");
		}
		else if (arg.equalsIgnoreCase("help"))
		{
			CommandsHelp.onCommandHelp(sender, args);
		}
		else if (arg.equalsIgnoreCase("rules"))
		{
			CommandsHelp.onCommandRules(sender);
		}
		else if (arg.equalsIgnoreCase("info"))
		{
			CommandsHelp.onCommandInfo(sender);
		}
		else if (arg.equalsIgnoreCase("status"))
		{
			CommandsHelp.onCommandStatus(sender);
		}
		else if (arg.equalsIgnoreCase("list"))
		{
			CommandsTeams.onCommandList(sender, args);
		}
		
		//SETTINGS COMMANDS
		
		else if (arg.equalsIgnoreCase("settings"))
		{
			CommandsSettings.onCommandSettings(sender, args);
		}
		else if (arg.equalsIgnoreCase("set"))
		{
			CommandsSettings.onCommandSet(sender, args);
		}
		else if (arg.equalsIgnoreCase("setworld"))
		{
			CommandsSettings.onCommandSetworld(sender, args);
		}
		else if (arg.equalsIgnoreCase("setmode"))
		{
			CommandsSettings.onCommandSetmode(sender, args);
		}
		
		//LOADOUT COMMANDS
		
		else if (arg.equalsIgnoreCase("listinv") || arg.equalsIgnoreCase("invlist"))
		{
			CommandsLoadouts.onCommandListinv(sender, args);
		}
		else if (arg.equalsIgnoreCase("saveloadout") || arg.equalsIgnoreCase("saveinv") || arg.equalsIgnoreCase("newloadout") || arg.equalsIgnoreCase("newinv") || arg.equalsIgnoreCase("invsave") || arg.equalsIgnoreCase("invnew"))
		{
			CommandsLoadouts.onCommandNewinv(sender, args);
		}
		else if (arg.equalsIgnoreCase("loadloadout") || arg.equalsIgnoreCase("loadinv") || arg.equalsIgnoreCase("invload"))
		{
			CommandsLoadouts.onCommandLoadinv(sender, args);
		}
		else if (arg.equalsIgnoreCase("deleteloadout") || arg.equalsIgnoreCase("delloadout") || arg.equalsIgnoreCase("delinv") || arg.equalsIgnoreCase("deleteinv") || arg.equalsIgnoreCase("invdelete") || arg.equalsIgnoreCase("invdel"))
		{
			CommandsLoadouts.onCommandDelinv(sender, args);
		}
		else if (arg.equalsIgnoreCase("hunterloadout") || arg.equalsIgnoreCase("hunterinv") || arg.equalsIgnoreCase("invhunter"))
		{
			CommandsLoadouts.onCommandHunterinv(sender, args);
		}
		else if (arg.equalsIgnoreCase("preyloadout") || arg.equalsIgnoreCase("preyinv") || arg.equalsIgnoreCase("invprey"))
		{
			CommandsLoadouts.onCommandPreyinv(sender, args);
		}
		
		//COMMANDS FOR SETTING UP THE TEAMS
		
		else if (arg.equalsIgnoreCase("hunter"))
		{
			CommandsTeams.onCommandHunter(sender, args);
		}
		else if (arg.equalsIgnoreCase("prey"))
		{
			CommandsTeams.onCommandPrey(sender, args);
		}
		else if (arg.equalsIgnoreCase("spectator") || arg.equalsIgnoreCase("spectate"))
		{
			CommandsTeams.onCommandSpectate(sender, args);
		}
		
		//COMMANDS FOR CONTORLLING PLAYERS
		
		else if (arg.equalsIgnoreCase("lock"))
		{
			CommandsTeams.onCommandLock(sender, args);
		}
		else if (arg.equalsIgnoreCase("kick"))
		{
			CommandsTeams.onCommandKick(sender, args);
		}
		else if (arg.equalsIgnoreCase("quit"))
		{
			CommandsTeams.onCommandQuit(sender, args);
		}
		
		//COMMANDS FOR EDITING SPAWN
		
		else if (arg.equalsIgnoreCase("spawn"))
		{
			CommandsGeneral.onCommandSpawn(sender, args);
		}
		else if (arg.equalsIgnoreCase("setspawn"))
		{
			CommandsGeneral.onCommandSetspawn(sender, args);
		}
		
		//COMMANDS FOR STARTING/STOPING THE GAME
		
		else if (arg.equalsIgnoreCase("startgame"))
		{
			CommandsGeneral.onCommandStartgame(sender, args);
		}
		else if (arg.equalsIgnoreCase("stopgame"))
		{
			CommandsGeneral.onCommandStopgame(sender, args);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Unknown Manhunt command. Type /m help for a list of available commands.");
		}
		*/
		
		return true;
		
	}
	
}
