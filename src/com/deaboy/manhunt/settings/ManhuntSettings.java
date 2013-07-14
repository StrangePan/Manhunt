package com.deaboy.manhunt.settings;

import org.bukkit.Bukkit;

public class ManhuntSettings extends SettingsPack
{
	public final StringListSetting WORLDS;
	
	public final StringSetting DEFAULT_LOBBY;
	
	public final BooleanSetting LOAD_WORLDS;
	public final BooleanSetting HANDLE_CHAT;
	public final BooleanSetting LOG_ERRORS;
	public final BooleanSetting DISPLAY_ERRORS;
	public final BooleanSetting OP_CONTROL;
	public final BooleanSetting CONTEXT_LIST;
	public final BooleanSetting CONTROL_XP;
	public final IntegerSetting FORGET_PLAYER;
	
	public final IntegerSetting SELECTION_TOOL;
	
	public ManhuntSettings()
	{
		addSetting(WORLDS =		new StringListSetting("worlds", "The list of Worlds Manhunt will run in.", Bukkit.getWorlds().get(0).getName()), false);
		
		addSetting(DEFAULT_LOBBY =	new StringSetting("defaultlobby", "default", "The default Manhunt lobby."), false);
		
		addSetting(LOAD_WORLDS =	new BooleanSetting("loadworlds", false, "Manhunt will handle multi-world features.", "Another plugin must handle multi-world."), true);
		addSetting(HANDLE_CHAT =	new BooleanSetting("handlechat", true, "Manhunt will handle chat events.", "Manhunt will ignore chat events."), true);
		addSetting(LOG_ERRORS =		new BooleanSetting("logerrors", true, "Manhunt will log errors in a seperate file.", "Manhunt will not keep a seperate error log."), false);
		addSetting(DISPLAY_ERRORS =	new BooleanSetting("showerrors", false, "Manhunt will show full errors in the console.", "Manhunt will not show full errors in the console."), false);
		addSetting(OP_CONTROL =		new BooleanSetting("opcontrol", true, "Only ops have access to basic commands.", "Non-ops have access to basic, safe commands."), true);
		addSetting(CONTEXT_LIST =	new BooleanSetting("contextlist", true, "Server list hides players in other games.", "Server list displays all players."), true);
		addSetting(CONTROL_XP =		new BooleanSetting("controlxp", true, "Manhunt will have its way with player XP levels.", "Manhunt will not touch player XP levels."), false);
		addSetting(FORGET_PLAYER =	new IntegerSetting("forgetplayer", 300, "Seconds after logging off Manhunt will remember", ""), false);
		
		addSetting(SELECTION_TOOL =		new IntegerSetting("selectiontool", 271, "", ""), false);
		
	}
	
}
