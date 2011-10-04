package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdExec implements CommandExecutor {
	private HuntedPlugin plugin;
	
	public CmdExec(HuntedPlugin instance) {
		plugin = instance;
		plugin.getCommand("manhunt").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd,
			String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments!");
		} else if (args[0].equalsIgnoreCase("world")) {
			if (sender instanceof Player) {
				((Player) sender).teleport(Bukkit.getServer().getWorld("manhunt").getSpawnLocation());
			} else {
				sender.sendMessage(ChatColor.RED + "You can't do that from the console!");
			}
		}
		return true;
	}

}
