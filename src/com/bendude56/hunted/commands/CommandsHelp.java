package com.bendude56.hunted.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.chat.ChatManager;

public class CommandsHelp
{
	
	public static void onCommandInfo(CommandSender sender)
	{
		PluginDescriptionFile desc = NewManhuntPlugin.getInstance().getDescription();
		sender.sendMessage(ChatManager.divider);
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + desc.getName() + " " + desc.getVersion() + ChatManager.bracket2_);
		sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + "For more info, visit " + ChatColor.UNDERLINE + "http://bit.ly/Nb9sh0");
		sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + "Written by Deaboy. " + ChatColor.UNDERLINE + "http://youtube.com/fearofmobs");
		sender.sendMessage(ChatManager.divider);
	}
	
}
