package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.settings.Setting;

public abstract class SettingCommands
{
	
	public static boolean msettings(CommandSender sender, Command cmd)
	{
		boolean action = false;

		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		for (Subcommand scmd : cmd.getSubcommands())
		{
			if (scmd.containsArgument(CommandUtil.arg_list))
			{
				action |= listsettings(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_set))
			{
				action |= setsetting(sender, scmd);
			}
		}
		
		return action;
	}
	public static boolean listsettings(CommandSender sender, Subcommand cmd)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<Setting> settings;
		String message;
		
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		
		// Get the page #
		if (cmd.containsArgument(CommandUtil.arg_page) && cmd.getArgument(CommandUtil.arg_page).getParameter() != null)
		{
			try
			{
				page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_page).getParameter());
			}
			catch (NumberFormatException e)
			{
				page = 1;
			}
		}
		else if (cmd.getArgument(CommandUtil.arg_list).getParameter() != null)
		{
			 if (cmd.getArgument(CommandUtil.arg_list).getParameter().equalsIgnoreCase("all"))
			 {
				 all = true;
				 page = 1;
			 }
			 else
			 {
				 try
				 {
					 page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_list).getParameter());
				 }
				 catch (Exception e)
				 {
					 page = 1;
				 }
			 }
		}
		else
		{
			page = 1;
		}
		
		page--;
		
		// Assemble list of settings
		settings = new ArrayList<Setting>();
		settings.addAll(Manhunt.getSettings().getVisibleSettings());
		if (CommandUtil.getSelectedLobby(sender) != null)
			settings.addAll(CommandUtil.getSelectedLobby(sender).getSettings().getVisibleSettings());
		if (CommandUtil.getSelectedLobby(sender).getType() == LobbyType.GAME)
			settings.addAll(((GameLobby) CommandUtil.getSelectedLobby(sender)).getGameSettings().getVisibleSettings());
		
		if (!all)
		{
			if (page * perpage > settings.size() - 1 )
				page = (settings.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
		}
		
		if (settings.size() == 0)
		{
			sender.sendMessage("There are no settings to display.");
			return true;
		}
		
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Settings " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) settings.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /msettings [n] to get page n of settings");
			settings = settings.subList(page * perpage, Math.min( (page + 1) * perpage, settings.size() ));
		}
		for (Setting setting : settings)
		{
			message = (ChatManager.leftborder + ChatColor.GRAY + (Manhunt.getSettings().getVisibleSettings().contains(setting) ? "[M] " : CommandUtil.getSelectedLobby(sender).getSettings().getVisibleSettings().contains(setting) ? "[L] " : "[G] ") + ChatManager.color + setting.getLabel() + " " + ChatColor.GREEN + "[" + setting.getValue().toString() + "]  " + ChatColor.WHITE + setting.getDescription());
			if (sender instanceof Player && message.length() > 65)
			{
				message = message.substring(0, 65) + "...";
			}
			sender.sendMessage(message);
		}
		return true;
		
	}
	public static boolean setsetting(CommandSender sender, Subcommand cmd)
	{
		List<Setting> settings;
		Setting setting;
		String name;
		String value;
		
		name = cmd.getArgument(CommandUtil.arg_set).getParameter();
		if (cmd.getArgument(CommandUtil.arg_set).getParameters().size() >= 2)
			value = cmd.getArgument(CommandUtil.arg_set).getParameters().get(1);
		else
			value = null;
		
		if (name == null || value == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid argument usage: " + CommandUtil.arg_set.getName());
			sender.sendMessage(ChatColor.GRAY + "  Usage: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_set).getLabel() + " <setting> <value>");
			return false;
		}
		
		
		settings = Manhunt.getSettings().getVisibleSettings();
		if (CommandUtil.getSelectedLobby(sender) != null)
			settings.addAll(CommandUtil.getSelectedLobby(sender).getSettings().getVisibleSettings());
		if (CommandUtil.getSelectedLobby(sender).getType() == LobbyType.GAME)
			settings.addAll(((GameLobby) CommandUtil.getSelectedLobby(sender)).getGameSettings().getVisibleSettings());
		
		
		setting = null;
		for (Setting s : settings)
		{
			if (s.getLabel().equalsIgnoreCase(name))
			{
				setting = s;
				break;
			}
		}

		if (setting == null)
		{
			sender.sendMessage(ChatColor.RED + "That setting does not exist, or is not visible.");
			return true;
		}
		
		if (setting.setValue(value))
		{
			sender.sendMessage(ChatColor.GOLD + setting.getLabel() + ChatColor.GREEN + " has been set to " + ChatColor.GOLD + "[" + setting.getValue() + "]");
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + setting.getDescription());
			return true;
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "Invalid value for that setting.");
			return true;
		}
		
	}
	
	
}
