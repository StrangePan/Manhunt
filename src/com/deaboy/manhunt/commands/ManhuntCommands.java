package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.game.GameClass;
import com.deaboy.manhunt.lobby.LobbyClass;

public abstract class ManhuntCommands
{
	
	public static boolean mlobbyclasses(CommandSender sender, String[] args)
	{
		List<LobbyClass> classes;
		final int perpage = 8;
		int page;
		
		classes = Manhunt.getRegisteredLobbyClasses();
		if (args.length == 0)
		{
			page = 1;
		}
		else
		{
			if (args[0].equalsIgnoreCase("all"))
			{
				page = 0;
			}
			try
			{
				page = Integer.parseInt(args[0]);
				if (page < 1)
				{
					page = 1;
				}
				if (page > (classes.size()/perpage+1))
				{
					page = (classes.size()/perpage+1);
				}
			}
			catch(NumberFormatException e)
			{
				page = 1;
			}
		}
		page--;
		
		sender.sendMessage(ChatManager.bracket1_ + "Lobbies registerd with Manhunt" + (page == -1 ? " (all)" : classes.size() > perpage ? " (page " + page+1 + '/' + (classes.size()/perpage+1) + ")" : "") + ChatManager.bracket2_);
		if (classes.size() > perpage)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Use /mlobbyclasses [n] to view page n of classes.");
			classes = classes.subList(page*perpage, Math.min((page+1)*perpage, classes.size()));
		}
		else if (classes.size() == 0)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  There are no registered lobby classes with Manhunt.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  This is a serious issue. Please ensure Manhunt is up-to-date.");
		}
		
		for (LobbyClass lobbyclass : classes)
		{
			sender.sendMessage(ChatManager.leftborder + lobbyclass.getId() + ". " + lobbyclass.getName() + ChatColor.GRAY + "   Plugin: " + lobbyclass.getPlugin().getName());
		}
		
		
		return true;
	}
	public static boolean mgameclasses(CommandSender sender, String[] args)
	{
		List<GameClass> classes;
		final int perpage = 8;
		int page;
		
		classes = Manhunt.getRegisteredGameClasses();
		if (args.length == 0)
		{
			page = 1;
		}
		else
		{
			if (args[0].equalsIgnoreCase("all"))
			{
				page = 0;
			}
			try
			{
				page = Integer.parseInt(args[0]);
				if (page < 1)
				{
					page = 1;
				}
				if (page > (classes.size()/perpage+1))
				{
					page = (classes.size()/perpage+1);
				}
			}
			catch(NumberFormatException e)
			{
				page = 1;
			}
		}
		page--;
		
		sender.sendMessage(ChatManager.bracket1_ + "Games registerd with Manhunt" + (page == -1 ? " (all)" : classes.size() > perpage ? " (page " + page+1 + '/' + (classes.size()/perpage+1) + ")" : "") + ChatManager.bracket2_);
		if (classes.size() > perpage)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  Use /mgameclasses [n] to view page n of classes.");
			classes = classes.subList(page*perpage, Math.min((page+1)*perpage, classes.size()));
		}
		else if (classes.size() == 0)
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  There are no registered game classes with Manhunt.");
			sender.sendMessage(ChatManager.leftborder + ChatColor.GRAY + "  This is a serious issue. Please ensure Manhunt is up-to-date.");
		}
		
		for (GameClass gameclass : classes)
		{
			sender.sendMessage(ChatManager.leftborder + gameclass.getId() + ". " + gameclass.getName() + ChatColor.GRAY + "   Plugin: " + gameclass.getPlugin().getName());
		}
		
		
		return true;
	}
	
}
