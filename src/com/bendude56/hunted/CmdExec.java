package com.bendude56.hunted;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CmdExec implements CommandExecutor {
	private HuntedPlugin plugin;
	
	public CmdExec(HuntedPlugin instance) {
		plugin = instance;
		plugin.getCommand("manhunt").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd,
			String[] args) {
		sender.sendMessage("TEST");
		return false;
	}

}
