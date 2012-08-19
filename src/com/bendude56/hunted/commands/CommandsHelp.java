package com.bendude56.hunted.commands;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.teams.TeamUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

public class CommandsHelp
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
			sender.sendMessage(cmdColor + "/m startgame" + cmdDesc + "Starts a Manhunt game.");
			sender.sendMessage(cmdColor + "/m stopgame" + cmdDesc + "Starts a Manhunt game.");
			sender.sendMessage(cmdColor + "/m help [page]" + cmdDesc + "Displays a list of Manhunt commands.");
			sender.sendMessage(cmdColor + "/m rules" + cmdDesc + "Displays the rules of Manhunt.");
			sender.sendMessage(cmdColor + "/m info" + cmdDesc + "Displays info about the Manhunt plugin.");
			sender.sendMessage(cmdColor + "/m status" + cmdDesc + "Gives info on the currently running game.");
		}
		if (page == 2)
		{
			sender.sendMessage(cmdColor + "/m list" + cmdDesc + "Lists the current players and their teams.");
			sender.sendMessage(cmdColor + "/m hunter [player]" + cmdDesc + "Puts you or a player on the " + TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, false) + cmdDesc + " team");
			sender.sendMessage(cmdColor + "/m prey [player]" + cmdDesc + "Puts you or a player on the " + TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, false) + cmdDesc + " team");
			sender.sendMessage(cmdColor + "/m spectator [player]" + cmdDesc + "Makes you or a player a " + TeamUtil.getTeamColor(Team.SPECTATORS) + TeamUtil.getTeamName(Team.SPECTATORS, false) + cmdDesc + ".");
			sender.sendMessage(cmdColor + "/m lock" + cmdDesc + "Locks the teams and the world.");
			sender.sendMessage(cmdColor + "/m quit" + cmdDesc + "Lets you quit the Manhunt game.");
			sender.sendMessage(cmdColor + "/m kick [player]" + cmdDesc + "Kicks a player from the game.");
		}
		if (page == 3)
		{
			sender.sendMessage(cmdColor + "/m settings [page]" + cmdDesc + "Lists the current Manhunt settings.");
			sender.sendMessage(cmdColor + "/m set <setting> <value>" + cmdDesc + "Changes a Manhunt setting.");
			sender.sendMessage(cmdColor + "/m spawn [spawn] [player]" + cmdDesc + "Teleports a player to a spawn.");
			sender.sendMessage(cmdColor + "/m setspawn <spawn>" + cmdDesc + "Changes the given spawnpoint.");
			sender.sendMessage(cmdColor + "/m setworld" + cmdDesc + "Changes the Manhunt World to the current one.");
			sender.sendMessage(cmdColor + "/m setmode [mode]" + cmdDesc + "Sets Manhunt's privacy mode.");
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
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		String header = ChatManager.color + ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Rules" + ChatManager.color + ChatManager.bracket2_;
		ChatColor color = ChatColor.WHITE;
		String pre = ChatManager.leftborder + color;
		String hunters = Team.HUNTERS.getColor() + Team.HUNTERS.getName(true) + color;
		String prey = Team.PREY.getColor() + Team.PREY.getName(true) + color;
		
		sender.sendMessage(header);
		
		sender.sendMessage(pre + "Players are divided into two teams: " + hunters + " and " + prey + ".");
		sender.sendMessage(pre + "The " + prey + " have " + plugin.getSettings().SETUP_TIME.value + " minutes to prepare,");
		if (plugin.getSettings().DAY_LIMIT.value > 0)
		{
			sender.sendMessage(pre + " and the " + hunters + " have " + ChatColor.BLUE + plugin.getSettings().DAY_LIMIT.value + " days " + color + "to kill them.");
		}
		else
		{
			sender.sendMessage(pre + " and the " + hunters + " have " + ChatColor.BLUE + " unlimited days " + color + "to kill them.");
		}
		if (plugin.getSettings().PREY_FINDER.value)
		{
			sender.sendMessage(pre + ChatColor.GREEN + "ProTip: " + color + "Right-click with a compass to find out where the nearest enemy is.");
		}
		if (plugin.getSettings().NORTH_COMPASS.value)
		{
			sender.sendMessage(pre + ChatColor.GREEN + "ProTip: " + color + "The compass always points true north.");
		}
		sender.sendMessage(pre + "Type " + ChatColor.GREEN + "/m settings" + color + " for detailed setting info.");
		sender.sendMessage(ChatManager.divider);
	}
	
	public static void onCommandInfo(CommandSender sender)
	{
		PluginDescriptionFile desc = ManhuntPlugin.getInstance().getDescription();
		sender.sendMessage(ChatManager.divider);
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + desc.getName() + " " + desc.getVersion() + ChatManager.bracket2_);
		sender.sendMessage(ChatManager.leftborder + ChatColor.WHITE + "Written by Deaboy. " + ChatColor.UNDERLINE + "http://youtube.com/fearofmobs");
		sender.sendMessage(ChatManager.divider);
	}
	
	public static void onCommandStatus(CommandSender sender)
	{
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		String pre = ChatManager.leftborder + ChatManager.color;
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "Manhunt Game Status" + ChatManager.bracket2_);
		
		String stage;
		Long time;
		Long endTime;
		
		if (plugin.gameIsRunning())
		{
			stage = plugin.getGame().getStage().name();
			time = plugin.getWorld().getFullTime();
			endTime = plugin.getGame().getStageStopTick(plugin.getGame().getStage());
		}
		else if (plugin.getIntermission() != null)
		{
			stage = "INTERMISSION";
			time = new Date().getTime()/50;
			endTime = plugin.getIntermission().getRestartTime()/50;
		}
		else
		{
			sender.sendMessage(pre + ChatColor.RED + "There are no Manhunt games running.");
			return;
		}
		
		int hours = (int) Math.floor((endTime - time)/72000);
		int minutes = (int) Math.floor(((endTime - time) - (hours*72000))/1200);
		int seconds = (int) Math.floor(((endTime - time) - (hours*72000) - (minutes*1200))/20);
		
		sender.sendMessage(pre + "Time left in the " + ChatColor.BLUE + stage + ChatManager.color + " stage: " + ChatColor.BLUE + String.format("%d:%02d:%02d", hours, minutes, seconds));
		sender.sendMessage(TeamUtil.getTeamColor(Team.HUNTERS) + TeamUtil.getTeamName(Team.HUNTERS, true) + ": " + plugin.getTeams().getTeamNames(Team.HUNTERS).size()
				+ "  " + TeamUtil.getTeamColor(Team.PREY) + TeamUtil.getTeamName(Team.PREY, true) + ": " + plugin.getTeams().getTeamNames(Team.PREY).size()
				+ "  " + TeamUtil.getTeamColor(Team.SPECTATORS) + TeamUtil.getTeamName(Team.SPECTATORS, true) + ": " + plugin.getTeams().getTeamNames(Team.SPECTATORS).size());
	}
	
}
