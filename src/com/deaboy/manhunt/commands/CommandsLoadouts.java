package com.deaboy.manhunt.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.loadouts.LoadoutUtil;

public class CommandsLoadouts
{
	private static final String arg_list = "list";
	private static final String arg_ls = "ls";
	
	
	
	
	public static void onLoadoutCommand(CommandSender sender, Arguments args)
	{
		Loadout loadout;
		
		if (args.contains(arg_ls) || args.contains(arg_list))
		{
			
			// TODO send loadout list to sender
			
			
			return;
		}
	}
	
	
}
