package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.GameLobby;

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
			if (scmd.containsArgument(CommandUtil.arg_info))
			{
				action |= infoloadout(sender ,scmd);
			}
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
			else if (scmd.containsArgument(CommandUtil.arg_lspotions))
			{
				action |= listpotionsloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_addpotion))
			{
				action |= addpotionloadout(sender, scmd);
			}
			else if (scmd.containsArgument(CommandUtil.arg_rempotion))
			{
				action |= removepotionloadout(sender, scmd);
			}
		}
		
		return action;
	}
	private static boolean infoloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "You must select a loadout first.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout>");
			return false;
		}
		
		sender.sendMessage(ChatManager.bracket1_ + "Loadout Info: " + loadout.getName() + ChatManager.bracket2_);
		sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY  + "Items: " + loadout.getContentCount() + "   Armor: " + loadout.getArmorContentCount());
		sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY  + "Random Items: " + loadout.getAllRandomItemStacks().size() + "   Potions: " + loadout.getEffects().size());
		
		String lobbies = new String();
		for (Lobby lobby : Manhunt.getLobbies())
		{
			if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).containsLoadout(loadout.getName()))
				lobbies += (lobbies.isEmpty() ? "" : ", ") + lobby.getName();
		}
		if (!lobbies.isEmpty())
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "Used in: " + lobbies);
		return true;
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
		loadouts = Manhunt.getAllLoadouts();
 
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
			boolean used = false;
			for (Lobby lobby : Manhunt.getLobbies())
			{
				if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).containsLoadout(loadout.getName()))
				{
					used = true;
					break;
				}
			}
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + loadout.getName() + "   " + ChatColor.GRAY + "Items: " + (loadout.getContentCount() + loadout.getArmorContentCount()) + (!used ? "  Unused" : ""));
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
		
		loadout = Manhunt.getLoadout(loadoutname);
		
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
		
		if (Manhunt.getLoadout(loadoutname) != null)
		{
			i = 0;
			while (Manhunt.getLoadout(loadoutname + i++) != null);
			loadoutname += i;
		}
		
		loadout = Manhunt.createLoadout(loadoutname, ((Player) sender).getInventory().getContents(), ((Player) sender).getInventory().getArmorContents());
		CommandUtil.setSelectedLoadout(sender, loadout);
		
		sender.sendMessage(ChatManager.leftborder + "Created loadout '" + ChatColor.GREEN + loadout.getName() + ChatManager.color + "'   " + ChatColor.GRAY + ChatColor.ITALIC + "File: " + loadout.getFilename());
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
		
		Manhunt.deleteLoadout(loadout.getName());
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
		loadout.save();
		sender.sendMessage(ChatManager.leftborder + "Loadout saved!");
		return true;
	}
	private static boolean listpotionsloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<PotionEffect> effects;
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to list potions.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
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
		else if (cmd.getArgument(CommandUtil.arg_lspotions).getParameter() != null)
		{
			 if (cmd.getArgument(CommandUtil.arg_lspotions).getParameter().equalsIgnoreCase("all"))
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
		effects = loadout.getEffects();
 
		if (!all)
		{
			if (page * perpage > effects.size() - 1 )
				page = (effects.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (effects.size() == 0)
			{
				sender.sendMessage("There are no potion effects to display.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + loadout.getName() + "'s Potion Effects " + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) effects.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all && effects.size() > perpage)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /mloadout -list [n] to get page n of loadouts");
			effects = effects.subList(page * perpage, Math.min( (page + 1) * perpage, effects.size() ));
		}
		
		for (PotionEffect effect : effects)
		{
			int duration = effect.getDuration() / (effect.getDuration() <= 0 ? 1 : 20);
			sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + effect.getType().getName() + " " + (effect.getAmplifier()+1) + "   " + ChatColor.GRAY + (duration < 0 ? "Auto" : duration/60 + ":" + (duration%60 < 10 ? "0" : "") + duration%60));
		}
		return true;
	}
	private static boolean addpotionloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		String type_string;
		PotionEffectType type;
		String duration_string;
		int duration;
		String amplifier_string;
		int amplifier;
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to add a potion to.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
		}
		
		amplifier_string = null;
		amplifier = 0;
		if (cmd.containsArgument(CommandUtil.arg_potiontype))
		{
			type_string = cmd.getArgument(CommandUtil.arg_potiontype).getParameter();
			if (cmd.getArgument(CommandUtil.arg_potiontype).getParameters().size() > 1)
			{
				amplifier_string = cmd.getArgument(CommandUtil.arg_potiontype).getParameters().get(1);
			}
		}
		else if (cmd.getArgument(CommandUtil.arg_addpotion).getParameter() != null)
		{
			type_string = cmd.getArgument(CommandUtil.arg_addpotion).getParameter();
			if (cmd.getArgument(CommandUtil.arg_addpotion).getParameters().size() > 2)
			{
				amplifier_string = cmd.getArgument(CommandUtil.arg_addpotion).getParameters().get(1);
			}
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Missing potion type.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_addpotion).getLabel() + " <potion type> [level] [duration]");
			return false;
		}
		type = PotionEffectType.getByName(type_string);
		if (type == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Invalid potion type " + type_string);
			return false;
		}
		if (amplifier_string != null)
		{
			try
			{
				amplifier = Integer.parseInt(amplifier_string);
				amplifier--;
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Invalid potion level " + amplifier_string);
				return false;
			}
		}
		
		duration = 60;
		if (cmd.containsArgument(CommandUtil.arg_duration))
		{
			duration_string = cmd.getArgument(CommandUtil.arg_duration).getParameter();
		}
		else if (cmd.getArgument(CommandUtil.arg_addpotion).getParameter() != null && cmd.getArgument(CommandUtil.arg_addpotion).getParameters().size() > 1)
		{
			duration_string = cmd.getArgument(CommandUtil.arg_addpotion).getParameters().get(cmd.containsArgument(CommandUtil.arg_potiontype) ? 0 : (cmd.getArgument(CommandUtil.arg_addpotion).getParameters().size() > 2 ? 2 : 1));
		}
		else
		{
			duration_string = null;
			duration = -1;
		}
		if (duration_string != null)
		{
			if (duration_string.equalsIgnoreCase("auto"))
			{
				duration = -1;
			}
			else try
			{
				duration = Integer.parseInt(duration_string);
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Invalid potion duration " + duration_string);
				return false;
			}
		}
		
		if (amplifier < 0)
		{
			amplifier = 0;
		}
		
		sender.sendMessage(ChatManager.leftborder + "Added potion effect. " + ChatColor.GRAY + type.getName() + ' ' + (amplifier+1) + ' ' + (duration <= 0 ? "Auto" : (duration/60 + ":" + (duration%60 < 10 ? "0" : "") + duration%60)));
		if (duration <= 0)
		{
			duration = -1;
		}
		else
		{
			duration *= 20;
		}
		
		loadout.addPotionEffect(type, duration, amplifier);
		loadout.save();
		return true;
	}
	private static boolean removepotionloadout(CommandSender sender, Subcommand cmd)
	{
		Loadout loadout;
		String type_string;
		PotionEffectType type;
		
		loadout = CommandUtil.getSelectedLoadout(sender);
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "Please select a loadout to remove a potion from.");
			sender.sendMessage(ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + CommandUtil.arg_select.getName() + " <loadout name>");
			return false;
		}
		
		if (cmd.containsArgument(CommandUtil.arg_potiontype))
		{
			type_string = cmd.getArgument(CommandUtil.arg_potiontype).getParameter();
		}
		else if (cmd.getArgument(CommandUtil.arg_rempotion).getParameter() != null)
		{
			type_string = cmd.getArgument(CommandUtil.arg_rempotion).getParameter();
		}
		else
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Missing potion type.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Example: /" + cmd.getLabel() + " -" + cmd.getArgument(CommandUtil.arg_rempotion).getLabel() + " <potion type>");
			return false;
		}
		type = PotionEffectType.getByName(type_string);
		if (type == null)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.RED + "Invalid potion type " + type_string);
			return false;
		}
		
		loadout.removePotionEffect(type);
		loadout.save();
		sender.sendMessage(ChatManager.leftborder + "Removed potion effect " + type_string);
		return true;
	}
	
	
}
