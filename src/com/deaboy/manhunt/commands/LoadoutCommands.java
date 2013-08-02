package com.deaboy.manhunt.commands;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.loadouts.Loadout;

public class LoadoutCommands
{
	
	
	public static boolean mloadout(CommandSender sender, Command cmd)
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
				action |= listloadouts(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_select))
			{
				action |= selectloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_create))
			{
				action |= createloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_delete))
			{
				action |= deleteloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_load))
			{
				action |= loadloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_save))
			{
				action |= saveloadout(sender, scmd);
			}
		}
		
		return action;
	}
	private static boolean listloadouts(CommandSender sender, Subcommand cmd)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<Loadout> loadouts;
		
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
		loadouts = Manhunt.getLoadouts().getAllLoadouts();
 
		if (!all)
		{
			if (page * perpage > loadouts.size() - 1 )
				page = (loadouts.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (loadouts.size() == 0)
			{
				sender.sendMessage("There are no loadouts to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Manhunt Loadouts " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) loadouts.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all && loadouts.size() > perpage)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /mloadout -list [n] to get page n of loadouts");
			loadouts = loadouts.subList(page * perpage, Math.min( (page + 1) * perpage, loadouts.size() ));
		}
		
		for (Loadout loadout: loadouts)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + loadout.getName() + "    " + ChatColor.GRAY + loadout.getEffects().size() + " potion effects.");
		}
		return true;
	}
	private static boolean selectloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		String loadoutname = cmd.getArgument(CommandUtil.arg_select).getParameter();
		
		if (loadoutname == null)
		{
			sender.sendMessage(ChatColor.RED + "You must include the name of the lobby you want to select.");
			sender.sendMessage(ChatColor.GRAY + " /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_select).getLabel() + " <loadout name>");
			return false;
		}
		
		loadout = Manhunt.getLoadouts().getLoadout(loadoutname);
		
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "No loadout exists by that name.");
			sender.sendMessage(ChatColor.GRAY + "Use \"/" + cmd.getLabel() + " -" + CommandUtil.arg_list.getName() + "\" to view a list of loadouts.");
			return false;
		}
		
		CommandUtil.setSelectedLoadout(sender, loadout);
		sender.sendMessage(ChatManager.leftborder + "Selected loadout '" + loadout.getName() + "'.");
		return true;
	}
	private static boolean createloadout(CommandSender sender, Subcommand cmd)
	{
		String loadoutname;
		String filename;
		Loadout loadout;
		int i;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_name) && cmd.getArgument(CommandUtil.arg_name).getParameter() != null)
		{
			loadoutname = cmd.getArgument(CommandUtil.arg_name).getParameter();
		}
		else if (cmd.getArgument(CommandUtil.arg_create).getParameter() != null)
		{
			loadoutname = cmd.getArgument(CommandUtil.arg_create).getParameter();
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You must name your new loadout.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + cmd.getArgument(CommandUtil.arg_create).getLabel() + " <loadout name>");
			return false;
		}
		
		if (Manhunt.getLoadouts().getLoadout(loadoutname) != null)
		{
			i = 0;
			while (Manhunt.getLoadouts().getLoadout(loadoutname + i++) != null);
			loadoutname += i;
		}
		
		filename = loadoutname;
		if (new File(Manhunt.path_loadouts + '/' + filename + Manhunt.extension_loadouts).exists())
		{
			i = 0;
			while (new File(Manhunt.path_loadouts + '/' + filename + i++ + Manhunt.extension_loadouts).exists());
			filename += i;
		}
		filename = filename + Manhunt.extension_loadouts;
		
		loadout = new Loadout(loadoutname, filename, ((Player) sender).getInventory().getContents(), ((Player) sender).getInventory().getArmorContents());
		Manhunt.getLoadouts().addLoadout(loadout);
		
		sender.sendMessage(ChatManager.leftborder + "Created loadout '" + ChatColor.GREEN + "'   " + ChatColor.GRAY + ChatColor.ITALIC + "file: " + filename);
		return true;
	}
	private static boolean deleteloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to delete.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
		}
		
		Manhunt.getLoadouts().deleteLoadout(loadout.getName());
		sender.sendMessage(ChatManager.leftborder + "Deleted loadout '" + loadout.getName() + "'.");
		return true;
	}
	private static boolean loadloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to load.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
		}
		
		loadout.applyToPlayer((Player) sender);
		sender.sendMessage(ChatManager.leftborder + "Loadout loaded!");
		return true;
	}
	private static boolean saveloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return false;
		}
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to save.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
		}
		
		loadout.setContents(((Player) sender).getInventory().getContents(), ((Player) sender).getInventory().getArmorContents());
		sender.sendMessage(ChatManager.leftborder + "Loadout saved!");
		return true;
	}
	
	
}
