package com.deaboy.manhunt.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;

public abstract class MapCommands
{
	
	public static boolean mmaps(CommandSender sender, String args[])
	{
		final String spacing = "   ";
		
		// Check permissions
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return false;
		}
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatManager.bracket1_ + "All Loaded Manhunt Maps" + ChatManager.bracket2_);
			if (Manhunt.getWorlds().size() == 0)
			{
				sender.sendMessage(ChatColor.RED + "  No worlds loaded into Manhunt.");
			}
			for (World world : Manhunt.getWorlds())
			{
				sender.sendMessage(ChatColor.GOLD + world.getName());
				if (world.getMaps().size() == 0)
					sender.sendMessage(spacing + ChatColor.GRAY + "[NONE]");
				else
					for (Map map : world.getMaps())
						sender.sendMessage(spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
			}
		}
		else
		{
			sender.sendMessage(ChatManager.bracket1_ + "Loaded Manhunt Maps" + ChatManager.bracket2_);
			for (String wname : args)
			{
				World world = Manhunt.getWorld(wname);
				if (world == null)
				{
					sender.sendMessage(ChatColor.RED + wname + " is not a valid world.");
				}
				else
				{
					sender.sendMessage(ChatColor.GOLD + world.getName());
					if (world.getMaps().size() == 0)
						sender.sendMessage(spacing + ChatColor.GRAY + "[NONE]");
					else
						for (Map map : world.getMaps())
							sender.sendMessage(spacing + ChatColor.WHITE + world.getName() + "." + map.getName());
				}
			}
		}
		
		return true;
	}
}
