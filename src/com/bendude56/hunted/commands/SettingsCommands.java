package com.bendude56.hunted.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.settings.Setting;

public class SettingsCommands
{
	public static void onCommandSettings(CommandSender sender, String[] args)
	{
		List<Setting<?>> settings = HuntedPlugin.getInstance().getSettings().getAllSettings();
		
		String syntax = "Proper syntax is: /m settings [page]";
		
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
				sender.sendMessage(ChatColor.RED + syntax);
				return;
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + syntax);
			return;
		}
		
		if (page > max_pages)
			page = max_pages;
		if (page < 1)
			page = 1;
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Settings (" + page + "/" + max_pages + ")" + ChatManager.bracket2_);
		
		settings = settings.subList((page-1) * per_page, page * max_pages > settings.size() ? settings.size() - 1 : page * max_pages);
		
		for (Setting<?> setting : settings)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.BLUE + setting.label + " " + setting.formattedValue() + ChatColor.WHITE + ": " + setting.message());
		}
		
		sender.sendMessage(ChatManager.divider);
	}

	

}
