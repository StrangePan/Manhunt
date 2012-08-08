package com.bendude56.hunted.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.loadouts.Loadout;
import com.bendude56.hunted.loadouts.LoadoutUtil;

public class CommandsLoadouts
{
	public static void onCommandListinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m listinv [page]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		List<Loadout> loads = plugin.getLoadouts().getAllLoadouts();
		
		int page; //between 1 and max_pages
		int per_page = 6; //loadouts displayed per page
		int max_pages = (int) Math.ceil(loads.size() / per_page);
		
		if (args.length == 1)
		{
			page = 1;
		}
		else if (args.length == 2)
		{
			try
			{
				page = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				sender.sendMessage(SYNTAX);
				return;
			}
		}
		else
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (page > max_pages)
			page = max_pages;
		if (page < 1)
			page = 1;
		
		loads = loads.subList((page-1) * per_page, page * per_page > loads.size() ? loads.size() - 1 : page * per_page);

		if (loads.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "There are no saved loadouts!");
			return;
		}
		else
		{
			sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "Saved Loadouts (" + page + "/" + max_pages + ")" + ChatManager.bracket2_);
		}
		
		for (Loadout load : loads)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GREEN + load.name + (plugin.getSettings().HUNTER_LOADOUT_CURRENT.value.equals(load.name) ? " (" + ChatColor.DARK_RED + "Hunter" + ChatColor.GREEN + ")" : "") + ChatColor.GREEN + (plugin.getSettings().PREY_LOADOUT_CURRENT.value.equals(load.name) ? " (" + ChatColor.BLUE + "Prey" + ChatColor.GREEN + ")" : ""));
		}
		
		sender.sendMessage(ChatManager.divider);
	}

	public static void onCommandNewinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m newinv [name]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		Player p;
		
		if (sender instanceof Player)
		{
			p = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		if (args[1].equalsIgnoreCase("hunter") || args[1].equalsIgnoreCase("prey") || args[1].equalsIgnoreCase("default"))
		{
			sender.sendMessage(ChatColor.RED + "You may not use that name!");
			return;
		}
		
		Loadout loadout = plugin.getLoadouts().getLoadout(args[1]);
		
		if (loadout == null)
		{
			plugin.getLoadouts().addLoadout(args[1], p.getInventory().getContents(), p.getInventory().getArmorContents());
			sender.sendMessage(ChatColor.GREEN + "New loadout created with name " + args[1]);
		}
		else
		{
			plugin.getLoadouts().getLoadout(args[1]).setContents(p.getInventory().getContents(), p.getInventory().getArmorContents());
			p.sendMessage(ChatColor.GREEN + "The existing loadout " + loadout.name + " was overwritten.");
		}
	}

	public static void onCommandLoadinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m loadinv [name]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		Player p;
		
		if (sender instanceof Player)
		{
			p = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		Loadout loadout;
		
		if (args[1].equalsIgnoreCase("hunter"))
		{
			loadout = plugin.getLoadouts().getHunterLoadout();
		}
		else if (args[1].equalsIgnoreCase("prey"))
		{
			loadout = plugin.getLoadouts().getPreyLoadout();;
		}
		else
		{
			loadout = plugin.getLoadouts().getLoadout(args[1]);
		}
		
		if (loadout == null)
		{
			p.sendMessage(ChatColor.RED + "No loadout with that name exists.");
			return;
		}
		
		LoadoutUtil.clearInventory(p.getInventory());
		p.getInventory().setContents(loadout.getContents());
		p.getInventory().setArmorContents(loadout.getArmor());
		p.sendMessage(ChatColor.GREEN + "Loadout \"" + loadout.name + "\" has been loaded.");
	}

	public static void onCommandDelinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m loadinv [name]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		Loadout loadout = plugin.getLoadouts().getLoadout(args[1]);
		
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "No loadout with that name exists.");
			return;
		}
		else
		{
			if (plugin.getLoadouts().deleteLoadout(args[1]))
				sender.sendMessage(ChatColor.GREEN + "Loadout \"" + loadout.name + "\" was deleted.");
			else
				sender.sendMessage(ChatColor.RED + "Loadout \"" + loadout.name + "\" was NOT deleted.");
			return;
		}
	}

	public static void onCommandHunterinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m hunterinv [name]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		Loadout loadout;
		
		if (args[1].equalsIgnoreCase("default"))
		{
			loadout = plugin.getLoadouts().DEFAULT_HUNTER_LOADOUT;
		}
		else
		{
			loadout = plugin.getLoadouts().getLoadout(args[1]);
		}
		
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "No loadout with that name exists.");
			return;
		}
		else
		{
			plugin.getSettings().HUNTER_LOADOUT_CURRENT.setValue(loadout.name);
			if (loadout == plugin.getLoadouts().DEFAULT_HUNTER_LOADOUT)
				sender.sendMessage(ChatColor.GREEN + "The Hunter loadout has been reset to default.");
			else
				sender.sendMessage(ChatColor.GREEN + "'" + loadout.name + "' is now the current Hunter loadout.");
		}
	}

	public static void onCommandPreyinv(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m preyinv [name]";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		Loadout loadout;
		
		if (args[1].equalsIgnoreCase("default"))
		{
			loadout = plugin.getLoadouts().DEFAULT_PREY_LOADOUT;
		}
		else
		{
			loadout = plugin.getLoadouts().getLoadout(args[1]);
		}
		
		if (loadout == null)
		{
			sender.sendMessage(ChatColor.RED + "No loadout with that name exists.");
			return;
		}
		else
		{
			plugin.getSettings().PREY_LOADOUT_CURRENT.setValue(loadout.name);
			if (loadout == plugin.getLoadouts().DEFAULT_PREY_LOADOUT)
				sender.sendMessage(ChatColor.GREEN + "The Prey loadout has been reset to default.");
			else
				sender.sendMessage(ChatColor.GREEN + "'" + loadout.name + "' is now the current Prey loadout.");
		}
	}

}
