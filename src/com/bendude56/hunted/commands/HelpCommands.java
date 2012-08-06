package com.bendude56.hunted.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.games.Game.GameStage;
import com.bendude56.hunted.teams.TeamUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

public class HelpCommands
{
	
	public static void onCommandHelp(CommandSender sender, String[] args)
	{
		int page;
		int max_page = 4;
		
		if (args.length == 1)
		{
			page = 1;
		}
		else
		{
			try
			{
				page = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				page = 1;
			}
		}
		if (page > max_page)
		{
			page = max_page;
		}
		
		String header = ChatManager.color + ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Command Help (page " + page + "/" + max_page + ")" + ChatManager.color + ChatManager.bracket2_;
		String cmdColor = ChatManager.leftborder + ChatColor.RED;
		String cmdDesc = ChatColor.GREEN + " ";
		
		
		sender.sendMessage(header);
		if (page == 1)
		{
			sender.sendMessage(cmdColor + "/m startgame [now]" + cmdDesc + "Starts a Manhunt game.");
			sender.sendMessage(cmdColor + "/m stopgame [now]" + cmdDesc + "Starts a Manhunt game.");
			sender.sendMessage(cmdColor + "/m help [page]" + cmdDesc + "Displays a list of Manhunt commands.");
			sender.sendMessage(cmdColor + "/m rules" + cmdDesc + "Displays the rules of Manhunt.");
			sender.sendMessage(cmdColor + "/m info" + cmdDesc + "Displays info about the Manhunt plugin.");
			sender.sendMessage(cmdColor + "/m status" + cmdDesc + "Gives info on the currently running game.");
		}
		if (page == 2)
		{
			sender.sendMessage(cmdColor + "/m list" + cmdDesc + "Lists the current teams.");
			sender.sendMessage(cmdColor + "/m hunter [player]" + cmdDesc + "Puts you or a player on the " + TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, false) + cmdDesc + " team");
			sender.sendMessage(cmdColor + "/m prey [player]" + cmdDesc + "Puts you or a player on the " + TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, false) + cmdDesc + " team");
			sender.sendMessage(cmdColor + "/m spectator [player]" + cmdDesc + "Makes you or a player a " + TeamUtil.getTeamColor(Team.SPECTATORS) + TeamUtil.getTeamName(Team.SPECTATORS, false) + cmdDesc + ".");
			sender.sendMessage(cmdColor + "/m lock" + cmdDesc + "Locks the teams and the world.");
		}
		if (page == 3)
		{
			sender.sendMessage(cmdColor + "/m settings [page]" + cmdDesc + "Lists the current Manhunt settings.");
			sender.sendMessage(cmdColor + "/m set <setting> <value>" + cmdDesc + "Changes a Manhunt setting.");
			sender.sendMessage(cmdColor + "/m spawn [spawn] [player]" + cmdDesc + "Teleports a player to a spawn.");
			sender.sendMessage(cmdColor + "/m setspawn [spawn]" + cmdDesc + "Changes the given spawnpoint.");
			sender.sendMessage(cmdColor + "/m setworld" + cmdDesc + "Changes the Manhunt World to the current one.");
			sender.sendMessage(cmdColor + "/m mode [mode]" + cmdDesc + "Displays / changes Manhunt's privacy mode.");
		}
		if (page == 4)
		{
			sender.sendMessage(cmdColor + "/m listinv [page]" + cmdDesc + "Lists all saved loadouts.");
			sender.sendMessage(cmdColor + "/m newinv <name>" + cmdDesc + "Saves a new loadout with the given name.");
			sender.sendMessage(cmdColor + "/m loadinv <name>" + cmdDesc + "Loads the loadout with the given name.");
			sender.sendMessage(cmdColor + "/m delinv <name>" + cmdDesc + "Deletes the loadout with the given name.");
			sender.sendMessage(cmdColor + "/m hunterinv [name]" + cmdDesc + "Sets the " + TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, false) + " loadout.");
			sender.sendMessage(cmdColor + "/m preyinv [name]" + cmdDesc + "Sets the " + TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, false) + " loadout.");
		}
		sender.sendMessage(ChatManager.divider);
	}
	
	public static void onCommandRules(CommandSender sender)
	{
		String header = ChatManager.color + ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Rules" + ChatManager.color + ChatManager.bracket2_;
		ChatColor color = ChatColor.WHITE;
		String pre = ChatManager.leftborder + color;
		String hunters = TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, true) + color;
		String prey = TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, false) + color;
		
		sender.sendMessage(header);
		
		sender.sendMessage(pre + "Manhunt players are divided into two teams: " + hunters + " and " + prey + ".");
		sender.sendMessage(pre + "The rules are simple: The " + hunters + " must hunt the " + prey + ", who must survive.");
		sender.sendMessage(pre + "The " + prey + " have until sundown to prepare for the hunt.");
		sender.sendMessage(pre + "That's it! Type " + ChatColor.GREEN + "/m settings" + color + " for more info.");
		sender.sendMessage(ChatManager.divider);
	}
	
	public static void onCommandInfo(CommandSender sender)
	{
		PluginDescriptionFile desc = HuntedPlugin.getInstance().getDescription();
		sender.sendMessage(ChatManager.divider);
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + desc.getName() + " " + desc.getVersion() + ChatManager.bracket2_);
		sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + "Written by Deaboy. " + ChatColor.UNDERLINE + "http://youtube.com/fearofmobs");
		sender.sendMessage(ChatManager.divider);
	}
	
	public static void onCommandStatus(CommandSender sender)
	{
		HuntedPlugin plugin = HuntedPlugin.getInstance();
		String pre = ChatManager.leftborder + ChatManager.color;
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Game Status" + ChatManager.bracket2_);
		
		if (plugin.gameIsRunning())
		{
			GameStage stage = plugin.getGame().getStage();
			Long time = plugin.getWorld().getFullTime();
			Long nexttime = plugin.getGame().getStageStopTick(stage);
			
			int hours = (int) Math.floor((nexttime - time)/72000);
			int minutes = (int) Math.floor(((nexttime - time) - (hours*72000))/1200);
			int seconds = (int) Math.floor(((nexttime - time) - (hours*72000) - (minutes*1200))/20);
			
			sender.sendMessage(pre + "Time left in the " + stage + " stage: " + ChatColor.DARK_BLUE + hours + ":" + minutes + ":" + seconds);
			sender.sendMessage(TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, true) + ": " + plugin.getTeams().getTeamNames(Team.HUNTERS).size()
					+ "  " + TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, true) + ": " + plugin.getTeams().getTeamNames(Team.PREY).size()
					+ "  " + TeamUtil.getTeamColor(Team.SPECTATORS) + TeamUtil.getTeamName(Team.SPECTATORS, true) + ": " + plugin.getTeams().getTeamNames(Team.SPECTATORS).size());
		}
		else
		{
			sender.sendMessage(pre + ChatColor.RED + "There are no Manhunt games running.");
		}
	}
	
}
