package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.deaboy.manhunt.chat.ChatManager;

/**
 * This class takes the Manhunt command and determines where it should be redirected.
 * @author Deaboy
 *
 */
public class CommandSwitchboard implements CommandExecutor, TabCompleter
{
	
	
	public CommandSwitchboard()
	{
		Bukkit.getPluginCommand("manhunt").setExecutor(this);
		Bukkit.getPluginCommand("mlobbyclasses").setExecutor(this);
		Bukkit.getPluginCommand("mgameclasses").setExecutor(this);
		Bukkit.getPluginCommand("mspawn").setExecutor(this);
		Bukkit.getPluginCommand("msetspawn").setExecutor(this);
		Bukkit.getPluginCommand("mworld").setExecutor(this);
		Bukkit.getPluginCommand("manhuntmode").setExecutor(this);
		
		Bukkit.getPluginCommand("mlobby").setExecutor(this);
		Bukkit.getPluginCommand("mloadout").setExecutor(this);
		
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
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command c, String cmd, String[] arguments)
	{
		Command command = CommandUtil.parseCommand(c, cmd, arguments);
		boolean action = false;
		
		if (command != null)
		{
			if (arguments.length == 0 || arguments.length == 1 && (arguments[0].equals("help") || arguments[0].equals("?")))
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
		
		
		else if (c.getName().equalsIgnoreCase("mlobbyclasses"))
			action |= ManhuntCommands.mlobbyclasses(sender, arguments);
		
		else if (c.getName().equalsIgnoreCase("mgameclasses"))
			action |= ManhuntCommands.mgameclasses(sender, arguments);
		
		
		else if (c.getName().equalsIgnoreCase("mlobby"))
			action |= LobbyCommands.mlobby(sender, command);
		
		else if (c.getName().equalsIgnoreCase("mloadout"))
			action |= LoadoutCommands.mloadout(sender, command);
		
		
		
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
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args)
	{
		List<String> list;
		CommandTemplate command;
		List<ArgumentTemplate> subcommands;
		ArgumentTemplate subcommand;
		
		list = new ArrayList<String>();
		
		// Combine arguments wrapped in quotes.
		args = handleQuotes(args);
		
		command = CommandUtil.matchCommand(cmd.getName());
		if (command == null)
			return null;
		
		subcommands = new ArrayList<ArgumentTemplate>();
		for (ArgumentTemplate argument : command.getArguments())
		{
			if (argument.isSubcommand())
			{
				subcommands.add(argument);
			}
		}
		
		
		if (args[args.length-1].startsWith("-") && !args[args.length-1].contains(" ")) // Complete the argument
		{
			for (ArgumentTemplate template : command.getArguments())
			{
				list.add("-" + template.getName());
			}
		}
		else	// Could be the name of a zone, map, etc.
		{
			// Get the last subcommand used
			subcommand = null;
			for (int i = args.length - 2; i >= 0 && subcommand == null; i--)
			{
				if (args[i].startsWith("-"))
				{
					for (ArgumentTemplate sub : subcommands)
					{
						if (sub.matches(args[i].substring(1)))
						{
							subcommand = sub;
							break;
						}
					}
				}
			}
			if (subcommand == null)
			{
				return null;
			}
		}
		
		
		// Remove anything that doesn't match
		for (int i = 0; i < list.size(); i++)
		{
			while (i < list.size() && !list.get(i).startsWith(args[args.length-1]))
			{
				list.remove(i);
			}
		}
		
		
		// If any elements contain spaces, wrap argument in quotes.
		for (int i = 0; i < list.size(); i++)
		{
			if (!list.get(i).startsWith("-") && list.get(i).contains(" "))
			{
				list.set(i, list.get(i).replace(' ', '_'));
			}
		}
		
		return list;
	}
	
	private static String[] handleQuotes(String[] args_preprocessed)
	{
		boolean quoted = false;
		int i;
		int j;
		String[] args;
		j = 0;
		for (i = 0; i < args_preprocessed.length; i++)
		{
			if (!quoted)
			{
				args_preprocessed[j] = args_preprocessed[i];
				if (args_preprocessed[i].startsWith("\""))
				{
					quoted = true;
					if (args_preprocessed[i].length() > 1 && args_preprocessed[i].endsWith("\""))
					{
						quoted = false;
						args_preprocessed[j] = args_preprocessed[j].substring(1, args_preprocessed[j].length() - 1);
					}
				}
			}
			else
			{
				args_preprocessed[j] = args_preprocessed[j] + ' ' + args_preprocessed[i];
				if (args_preprocessed[i].endsWith("\""))
				{
					quoted = false;
					args_preprocessed[j] = args_preprocessed[j].substring(1, args_preprocessed[j].length() - 1);
				}
			}
			if (!quoted)
			{
				j++;
			}
		}
		args = new String[j];
		for (i = 0; i < args.length; i++)
		{
			args[i] = args_preprocessed[i];
		}
		return args;
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
