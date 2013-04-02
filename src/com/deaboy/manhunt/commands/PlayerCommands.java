package com.deaboy.manhunt.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntMode;
import com.deaboy.manhunt.chat.ChatManager;

public abstract class PlayerCommands
{
	
	public static boolean manhuntmode(CommandSender sender, String[] args)
	{
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return true;
		}
		
		Player p;
		ManhuntMode m;
		
		if (args.length == 2)
		{
			p = Bukkit.getPlayerExact(args[2]);
		}
		else if (!(sender instanceof Player))
		{
			if (args.length < 2) sender.sendMessage(ChatColor.RED + "You must include a player's name!");
			Bukkit.dispatchCommand(sender, "help manhuntmode");
			return true;
		}
		else if (args.length == 1)
		{
			p = (Player) sender;
		}
		else
		{
			Bukkit.dispatchCommand(sender, "help manhuntmode");
			return true;
		}
		
		if (p == null)
		{
			sender.sendMessage(ChatColor.RED + "Player does not exist.");
			return true;
		}
		else if (!p.isOp())
		{
			sender.sendMessage(ChatColor.RED + "That person does not have permission!");
			return true;
		}
		
		m = ManhuntMode.fromName(args[0]);
		
		if (m == null)
		{
			sender.sendMessage(ChatColor.RED + "Invalid Manhunt mode.");
			sender.sendMessage(ChatManager.leftborder + "play, edit");
			return true;
		}
		
		Manhunt.setPlayerMode(p, m);
		p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Your Manhunt Mode is set to " + m.getId() + ": " + m.getName());
		Manhunt.log(p.getName() + " Manhunt mode set to " + m.getId() + ": " + m.getName());
		
		return true;
	}
	
}
