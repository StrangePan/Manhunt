package com.deaboy.manhunt.commands;

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
	
	public CommandSwitchboard()
	{
		Bukkit.getPluginCommand("manhunt").setExecutor(this);
		Bukkit.getPluginCommand("mstartgame").setExecutor(this);
		Bukkit.getPluginCommand("mstopgame").setExecutor(this);
		Bukkit.getPluginCommand("msettings").setExecutor(this);
		Bukkit.getPluginCommand("mset").setExecutor(this);
		Bukkit.getPluginCommand("mworlds").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command c, String cmd, String[] arguments)
	{
		
		if (c.getName().equalsIgnoreCase("manhunt"))
			return HelpCommands.manhunt(sender, arguments);
		
		if (c.getName().equalsIgnoreCase("mstartgame"))
			return LobbyCommands.mstartgame(sender, arguments);
		
		if (c.getName().equalsIgnoreCase("mstopgame"))
			return LobbyCommands.mstopgame(sender, arguments);
		
		if (c.getName().equalsIgnoreCase("msettings"))
			return SettingCommands.msettings(sender, arguments);
		
		if (c.getName().equalsIgnoreCase("mset"))
			return SettingCommands.mset(sender, arguments);
		
		if (c.getName().equalsIgnoreCase("mworlds"))
			return WorldCommands.mworlds(sender, arguments);
		
		
		
		return false;
		
	}
	
	
	
}