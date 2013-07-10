package com.deaboy.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.chat.ChatManager;

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
		Bukkit.getPluginCommand("mspawn").setExecutor(this);
		Bukkit.getPluginCommand("msetspawn").setExecutor(this);
		Bukkit.getPluginCommand("mworld").setExecutor(this);
		Bukkit.getPluginCommand("manhuntmode").setExecutor(this);
		
		Bukkit.getPluginCommand("mlobby").setExecutor(this);
		
		Bukkit.getPluginCommand("mmap").setExecutor(this);
		Bukkit.getPluginCommand("mzone").setExecutor(this);
		Bukkit.getPluginCommand("mpoint").setExecutor(this);
		
		Bukkit.getPluginCommand("mstartgame").setExecutor(this);
		Bukkit.getPluginCommand("mstopgame").setExecutor(this);
		Bukkit.getPluginCommand("mjoin").setExecutor(this);
		Bukkit.getPluginCommand("mleave").setExecutor(this);
		Bukkit.getPluginCommand("mverify").setExecutor(this);
		Bukkit.getPluginCommand("mcancel").setExecutor(this);
		Bukkit.getPluginCommand("msettings").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command c, String cmd, String[] arguments)
	{
		Command command = CommandUtil.parseCommand(c, cmd, arguments);
		boolean action = false;
		
		if (arguments.length == 0)
		{
			CommandUtil.sendHelp(sender, command);
			return true;
		}
		for (Subcommand subcommand : command.getSubcommands())
		{
			if (subcommand.containsArgument(CommandUtil.arg_help))
			{
				action |= CommandUtil.sendHelp(sender, subcommand);
			}
			else if (subcommand.containsArgument(CommandUtil.arg_args))
			{
				action |= CommandUtil.sendArguments(sender, subcommand);
			}
		}
		
		if (c.getName().equalsIgnoreCase("manhunt"))
			action |= HelpCommands.manhunt(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mspawn"))
			action |= WorldCommands.mspawn(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("msetspawn"))
			action |= WorldCommands.msetspawn(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mworld"))
			action |= WorldCommands.mworld(sender, command);
		
		else if (c.getName().equalsIgnoreCase("manhuntmode"))
			action |= PlayerCommands.manhuntmode(sender, arguments);
		
		
		
		else if (c.getName().equalsIgnoreCase("mlobby"))
			action |= LobbyCommands.mlobby(sender, command);
		
		
		
		else if (c.getName().equalsIgnoreCase("mmap"))
			action |= MapCommands.mmap(sender, command);
		
		else if (c.getName().equalsIgnoreCase("mzone"))
			action |= MapCommands.mzone(sender, command);
		
		else if (c.getName().equalsIgnoreCase("mpoint"))
			action |= MapCommands.mpoint(sender, command);
		
		
		
		else if (c.getName().equalsIgnoreCase("mstartgame"))
			action |= LobbyCommands.mstartgame(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mstopgame"))
			action |= LobbyCommands.mstopgame(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mjoin"))
			action |= LobbyCommands.mjoin(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mleave"))
			action |= LobbyCommands.mleave(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mverify"))
			action |= mverify(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mcancel"))
			action |= mcancel(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("msettings"))
			action |= SettingCommands.msettings(sender, command);
		
		else
			return false;
		
		if (!action)
		{
			sender.sendMessage(ChatColor.GRAY + "No actions performed.");
		}
		
		return true;
	}
	
	
	private static boolean mverify(CommandSender sender, String[] arguments)
	{
		if (CommandUtil.isVerifying(sender))
			CommandUtil.executeCommand(sender);
		else
			sender.sendMessage(ChatManager.leftborder + "No command to verify.");
		
		return true;
	}
	
	private static boolean mcancel(CommandSender sender, String[] arguments)
	{
		if (CommandUtil.isVerifying(sender))
		{
			CommandUtil.cancelCommand(sender);
			sender.sendMessage(ChatManager.leftborder + "Cancelled command.");
		}
		else
			sender.sendMessage(ChatManager.leftborder + "No command to cancel.");
		
		return true;
	}
	
	
	
}
