package com.bendude56.hunted.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntPlugin.ManhuntMode;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.game.GameUtil;
import com.bendude56.hunted.lobby.Lobby;
import com.bendude56.hunted.settings.OldSetting;
import com.bendude56.hunted.settings.Setting;
import com.bendude56.hunted.teams.TeamManager.Team;

public class CommandsSettings
{
	public static void onCommandSettings(CommandSender sender, Arguments args)
	{
		Lobby lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
		
		if (args.contains("list") || args.contains("ls"))
		{
			List<Setting> settings= new ArrayList<Setting>();
			
			if (lobby == null)
			{
				// TODO Tell sender to select a lobby to see it's settings
			}
			else
			{
				// TODO FIGURE OUT SETTINGS GOD DANG IT
			}
		}
		
	}

	public static void onCommandSet(CommandSender sender, String[] args)
	{
		String SYNTAX_ERROR = ChatColor.RED + "Proper syntax is: /m set <setting> <value>";
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 3)
		{
			sender.sendMessage(SYNTAX_ERROR);
			return;
		}
		
		OldSetting<?> setting = ManhuntPlugin.getInstance().getSettings().getSetting(args[1]);
		
		if (setting == null)
		{
			sender.sendMessage(ChatColor.RED + "That setting does not exist.");
			return;
		}
		
		if (setting.parseValue(args[2]))
		{
			sender.sendMessage(ChatManager.leftborder + ChatColor.BLUE + setting.label + " " + setting.formattedValue() + " " + setting.message());
			
			if (args[1].equalsIgnoreCase("intermission") && ManhuntPlugin.getInstance().getSettings().MANHUNT_MODE.value == ManhuntMode.PUBLIC)
			{
				ManhuntPlugin.getInstance().startIntermission(true);
			}
		}
		else
			sender.sendMessage(ChatColor.RED + args[2] + "is an invalid setting for \"" + setting.label + "\"");
	}

	public static void onCommandSetworld(CommandSender sender, String[] args)
	{
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		Player player;
		
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}
		else
		{
			sender.sendMessage(CommandUtil.IS_SERVER);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (player.getWorld() != ManhuntPlugin.getInstance().getWorld())
		{
			ManhuntPlugin.getInstance().setWorld(player.getWorld());
			sender.sendMessage(ChatManager.bracket1_ + ChatColor.GREEN + "The Manhunt world has been changed" + ChatManager.bracket2_);
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "You are already in the Manhunt world!");
		}
	}

	public static void onCommandSetmode(CommandSender sender, String[] args)
	{
		String SYNTAX = ChatColor.RED + "Proper syntax is /m setmode <mode>";
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		if (!sender.isOp())
		{
			sender.sendMessage(CommandUtil.NO_PERMISSION);
			return;
		}
		
		if (ManhuntPlugin.getInstance().gameIsRunning())
		{
			sender.sendMessage(CommandUtil.GAME_RUNNING);
			return;
		}
		
		if (args.length != 2)
		{
			sender.sendMessage(SYNTAX);
			return;
		}
		
		ManhuntMode mode = ManhuntMode.fromString(args[1]);
		
		if (mode == null)
		{
			sender.sendMessage(ChatColor.RED + "'" + args[1] + "' is an invalid mode.");
			return;
		}
		
		plugin.setMode(mode);
		
		GameUtil.broadcast(ChatManager.bracket1_ + "Manhunt mode set to " + ChatColor.BLUE + mode + ChatManager.bracket2_, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
	}

}
