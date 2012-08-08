package com.bendude56.hunted.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntPlugin.ManhuntMode;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.settings.Setting;

public class CommandsSettings
{
	public static void onCommandSettings(CommandSender sender, String[] args)
	{
		List<Setting<?>> settings = ManhuntPlugin.getInstance().getSettings().getAllSettings();
		
		String SYNTAX = ChatColor.RED + "Proper syntax is: /m settings [page]";
		
		int page; //between 1 and max_pages
		int per_page = 6; //settings displayed per page
		int max_pages = (int) Math.ceil(settings.size() / per_page);
		
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
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Settings (" + page + "/" + max_pages + ")" + ChatManager.bracket2_);
		
		settings = settings.subList((page-1) * per_page, page * per_page > settings.size() ? settings.size() - 1 : page * per_page);
		
		for (Setting<?> setting : settings)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.BLUE + setting.label + " " + setting.formattedValue() + ChatColor.WHITE + ": " + setting.message());
		}
		
		sender.sendMessage(ChatManager.divider);
	}

	public static void onCommandSet(CommandSender sender, String[] args)
	{
		String SYNTAX_ERROR = ChatColor.RED + "Proper syntax is: /m set <setting> <value>";
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 3)
		{
			sender.sendMessage(SYNTAX_ERROR);
			return;
		}
		
		Setting<?> setting = ManhuntPlugin.getInstance().getSettings().getSetting(args[1]);
		
		if (setting == null)
		{
			sender.sendMessage(ChatColor.RED + "That setting does not exist.");
			return;
		}
		
		if (setting.parseValue(args[2]))
			sender.sendMessage(ChatManager.leftborder + ChatColor.BLUE + setting.label + " " + setting.formattedValue() + " " + setting.message());
		else
			sender.sendMessage(ChatColor.RED + args[2] + "is an invalid setting for \"" + setting.label + "\"");
	}

	public static void onCommandSetworld(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		Player player;
		
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (player.getWorld() != ManhuntPlugin.getInstance().getWorld())
		{
			ManhuntPlugin.getInstance().setWorld(player.getWorld());
			sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "The Manhunt world has been changed" + ChatManager.bracket2_);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You are already in the Manhunt world!");
		}
	}

	public static void onCommandSetmode(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m setmode <mode>";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		ManhuntMode mode = ManhuntMode.fromString(args[1]);
		
		if (mode == null)
		{
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is an invalid mode.");
			return;
		}
		
		plugin.setMode(mode);
		
		sender.sendMessage(ChatManager.bracket1_ + "Manhunt mode set to " + mode + ChatManager.bracket2_);
	}

}
