package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.settings.Setting;

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
		List<SubcommandTemplate> subcommands;
		SubcommandTemplate subcommand;
		ArgumentTemplate argument;
		int argument_index = -1;
		int subcommand_index = -1;
		
		list = new ArrayList<String>();
		
		// Combine arguments wrapped in quotes.
		args = handleQuotes(args);
		
		command = CommandUtil.matchCommand(cmd.getName());
		if (command == null)
			return null;
		
		subcommands = new ArrayList<SubcommandTemplate>(command.getSubcommands());
		
		// Get the last subcommand and argument used
		subcommand = null;
		argument = null;
		for (int i = args.length - 2; i >= 0 && subcommand == null; i--)
		{
			if (args[i].startsWith("-"))
			{
				for (SubcommandTemplate sub : subcommands)
				{
					if (sub.getRootArgument().matches(args[i].substring(1)))
					{
						subcommand = sub;
						subcommand_index = i;
						break;
					}
				}
			}
		}
		if (subcommand != null)
		{
			for (int i = args.length -2; i >= 0 && argument == null; i--)
			{
				if (subcommand.getRootArgument().matches(args[i].substring(1)))
				{
					argument = subcommand.getRootArgument();
					argument_index = i;
				}
				else
				{
					for (ArgumentTemplate arg : subcommand.getArguments())
					{
						if (arg.matches(args[i].substring(1)))
						{
							argument = arg;
							argument_index = i;
							break;
						}
					}
				}
			}
		}
		
		if (args[args.length-1].startsWith("-") && !args[args.length-1].contains(" ")) // Complete the argument
		{
			if (subcommand != null)
			{
				for (ArgumentTemplate template : subcommand.getArguments())
				{
					if (args[args.length-1].equals("-") || template.getName().startsWith(args[args.length-1].substring(1)))
					{
						list.add("-" + template.getName());
					}
					else
					{
						// Gotta be more specific and take aliases into account
						List<String> aliases = template.getAliases();
						aliases.add(template.getName());
						// Sort by length so as to return the shortest possibility every time
						Collections.sort(aliases, new Comparator<String>()
						{
							public int compare(String o1, String o2)
							{
								return o1.length() - o2.length();
							}
						});
						for (String a : aliases)
						{
							if (a.toLowerCase().startsWith(args[args.length-1].substring(1).toLowerCase()))
							{
								list.add("-" + a);
								break;
							}
						}
					}
				}
			}
			for (SubcommandTemplate template : command.getSubcommands())
			{
				if (args[args.length-1].equals("-") || template.getRootArgument().getName().startsWith(args[args.length-1].substring(1)))
				{
					list.add("-" + template.getRootArgument().getName());
				}
				else
				{
					// Gotta be more specific and take aliases into account
					List<String> aliases = template.getRootArgument().getAliases();
					aliases.add(template.getRootArgument().getName());
					// Sort by length so as to return the shortest possibility every time
					Collections.sort(aliases, new Comparator<String>()
					{
						public int compare(String o1, String o2)
						{
							return o1.length() - o2.length();
						}
					});
					for (String a : aliases)
					{
						if (a.toLowerCase().startsWith(args[args.length-1].substring(1).toLowerCase()))
						{
							list.add("-" + a);
							break;
						}
					}
				}
			}
		}
		else if (subcommand != null)	// Could be the name of a zone, map, etc.
		{
			// Return list of LOBBIES
			if (command == CommandUtil.cmd_mlobby && (subcommand.getRootArgument() == CommandUtil.arg_select && args.length - subcommand_index == 2
													|| subcommand.getRootArgument() == CommandUtil.arg_join && (argument == CommandUtil.arg_name && args.length - argument_index == 2
																												|| args.length - subcommand_index == 2)))
			{
				for (Lobby lobby : Manhunt.getLobbies())
				{
					list.add(lobby.getName());
				}
			}
			
			// Return list of LOBBY CLASSES
			else if (command == CommandUtil.cmd_mlobby && (subcommand.getRootArgument() == CommandUtil.arg_create && argument == CommandUtil.arg_lobbytype && args.length - argument_index == 2))
			{
				int i = Manhunt.getRegisteredLobbyClasses().size();
				for (int j = 0; j < i; j++)
				{
					list.add(j + "");
				}
			}
			
			// Return list of ONLINE PLAYERS
			else if ((command == CommandUtil.cmd_mlobby || command == CommandUtil.cmd_mmap || command == CommandUtil.cmd_mpoint || command == CommandUtil.cmd_mworld) && (subcommand.getRootArgument() == CommandUtil.arg_tp && (argument == CommandUtil.arg_player && args.length - argument_index == 2 || args.length - subcommand_index == 2)))
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					list.add(player.getName());
				}
			}
			
			// Return list of WORLDS
			else if (command == CommandUtil.cmd_mmap && subcommand.getRootArgument() == CommandUtil.arg_list && argument == CommandUtil.arg_world && args.length - argument_index == 2
					|| command == CommandUtil.cmd_mworld && subcommand.getRootArgument() == CommandUtil.arg_tp && argument == CommandUtil.arg_world && args.length - argument_index == 2
					|| command == CommandUtil.cmd_mworld && (subcommand.getRootArgument() == CommandUtil.arg_issues || subcommand.getRootArgument() == CommandUtil.arg_info) && (argument == CommandUtil.arg_world && args.length - argument_index == 2 || args.length - subcommand_index == 2))
			{
				for (World world : Manhunt.getWorlds())
				{
					list.add(world.getName());
				}
			}
			
			// Return list of MAPS
			else if (command == CommandUtil.cmd_mlobby && (subcommand.getRootArgument() == CommandUtil.arg_addmap && args.length - subcommand_index == 2
															|| subcommand.getRootArgument() == CommandUtil.arg_remmap && args.length - subcommand_index == 2)
					|| command == CommandUtil.cmd_mmap && subcommand.getRootArgument() == CommandUtil.arg_select && args.length - subcommand_index == 2)
			{
				for (World world : Manhunt.getWorlds())
				{
					for (Map map : world.getMaps())
					{
						list.add(map.getFullName());
					}
				}
			}
			
			// Return a list of ZONES
			else if (command == CommandUtil.cmd_mzone && subcommand.getRootArgument() == CommandUtil.arg_select && args.length - subcommand_index == 2)
			{
				if (CommandUtil.getSelectedMap(sender) != null)
				{
					for (Zone zone : CommandUtil.getSelectedMap(sender).getZones())
					{
						list.add(zone.getName());
					}
				}
			}
			
			// Return list of ZONE FLAGS
			else if (command == CommandUtil.cmd_mzone && subcommand.getRootArgument() == CommandUtil.arg_zoneflags && argument == CommandUtil.arg_zoneflags)
			{
				list.addAll(CommandUtil.arg_zoneflags.getParameters());
			}
			
			// Return list of POINTS
			else if (command == CommandUtil.cmd_mpoint && subcommand.getRootArgument() == CommandUtil.arg_select && args.length - subcommand_index == 2)
			{
				if (CommandUtil.getSelectedMap(sender) != null)
				{
					for (Spawn point : CommandUtil.getSelectedMap(sender).getPoints())
					{
						list.add(point.getName());
					}
				}
			}
			
			// Return list of POINT TYPES
			else if (command == CommandUtil.cmd_mpoint && subcommand.getRootArgument() == CommandUtil.arg_create && argument == CommandUtil.arg_pointtype && args.length - argument_index == 2)
			{
				list.addAll(CommandUtil.arg_pointtype.getParameters());
			}
			
			// Return list of LOADOUTS
			else if (command == CommandUtil.cmd_mlobby && (subcommand.getRootArgument() == CommandUtil.arg_addload && args.length - subcommand_index == 2 || subcommand.getRootArgument() == CommandUtil.arg_remload && args.length - subcommand_index == 2)
					|| command == CommandUtil.cmd_mloadout && subcommand.getRootArgument() == CommandUtil.arg_select && args.length - subcommand_index == 2)
			{
				for (Loadout loadout : Manhunt.getAllLoadouts())
				{
					list.add(loadout.getName());
				}
			}
			
			// Return list of TEAMS
			else if (command == CommandUtil.cmd_mlobby && (subcommand.getRootArgument() == CommandUtil.arg_addload || subcommand.getRootArgument() == CommandUtil.arg_remload) && argument == CommandUtil.arg_team)
			{
				list.addAll(CommandUtil.arg_team.getParameters());
			}
			
			// Return list of SETTINGS
			else if (command == CommandUtil.cmd_msettings && subcommand.getRootArgument() == CommandUtil.arg_set && args.length - subcommand_index == 2)
			{
				for (Setting setting : Manhunt.getSettings().getVisibleSettings())
				{
					list.add(setting.getLabel());
				}
				if (CommandUtil.getSelectedLobby(sender) != null)
				{
					for (Setting setting : CommandUtil.getSelectedLobby(sender).getSettings().getVisibleSettings())
					{
						list.add(setting.getLabel());
					}
				}
				if (CommandUtil.getSelectedLobby(sender) != null && CommandUtil.getSelectedLobby(sender).getType() == LobbyType.GAME)
				{
					for (Setting setting : ((GameLobby) CommandUtil.getSelectedLobby(sender)).getGameSettings().getVisibleSettings())
					{
						list.add(setting.getLabel());
					}
				}
			}
			
			// Return list of POTION EFFECT TYPES
			else if (command == CommandUtil.cmd_mloadout && (subcommand.getRootArgument() == CommandUtil.arg_addpotion || subcommand.getRootArgument() == CommandUtil.arg_rempotion) && (argument == CommandUtil.arg_potiontype && args.length - argument_index == 2 || args.length - subcommand_index == 2))
			{
				for (PotionEffectType type : PotionEffectType.values())
				{
					if (type != null)
					{
						list.add(type.getName().toLowerCase());
					}
				}
			}
			
			
		}
		
		
		// Remove anything that doesn't match
		for (int i = 0; i < list.size(); i++)
		{
			while (i < list.size() && !list.get(i).toLowerCase().startsWith(args[args.length-1].toLowerCase()))
			{
				list.remove(i);
			}
		}
		
		
		// If any elements contain spaces, wrap in quotes
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).contains(" "))
			{
				list.set(i, "\"" + list.get(i) + "\"");
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
