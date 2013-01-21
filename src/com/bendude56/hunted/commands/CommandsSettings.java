package com.bendude56.hunted.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.Lobby;
import com.bendude56.hunted.settings.Setting;

public class CommandsSettings
{
	public static void onCommandSettings(CommandSender sender, Arguments args)
	{
		Argument list = new Argument("list", "ls");
		Argument page = new Argument("page", "pg", "p");
		
		Lobby lobby;
		List<Setting> settings;
		int pg;
		int max_per_pg = 8;
		
		
		lobby = Manhunt.getCommandHelper().getSelectedLobby(sender);
		
		settings = new ArrayList<Setting>();
		settings.addAll(Manhunt.getSettings().getVisibleSettings());
		if (lobby != null && lobby instanceof GameLobby)
			settings.addAll(((GameLobby) lobby).getSettings().getVisibleSettings());
		
		
		if (args.contains(list))
		{
			
			if (args.contains(page))
				pg = args.getInteger(page);
			else
				pg = 1;
			
			pg--;
			
			if (pg != 0 && (pg < 0 || pg > settings.size() / max_per_pg))
			{
				// TODO Tell sender that their pages are out of range.
			}
			else
			{
				settings = settings.subList(pg * max_per_pg, Math.min((pg + 1) * max_per_pg, settings.size() - 1));
			}
			
			
			
			for (Setting setting : settings)
			{
				// TODO Send player info on each setting.
			}
			
			
			
		}
		
		
		
		for (Setting setting : settings)
		{
			if (args.contains(setting.getLabel()))
			{
				Object value;
				
				switch (setting.getType())
				{
				case INTEGER:
					value = args.getInteger(setting.getLabel());
					break;
				case BOOLEAN:
					value = args.getBoolean(setting.getLabel());
					break;
				case STRING:
					value = args.getString(setting.getLabel());
					break;
				default:
					value = null;
					break;
				}
				
				if (value == null)
				{
					// TODO Tell sender about invalid value
				}
				else
				{
					setting.setValue(value);
					// TODO Tell sender about confirmed change
				}
			}
		}
		
		
	}
}
