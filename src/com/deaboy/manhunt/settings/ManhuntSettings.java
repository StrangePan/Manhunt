package com.deaboy.manhunt.settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ManhuntSettings extends SettingManagerBase implements SettingManager
{
	private static final long serialVersionUID = -2749093656484939858L;
	
	public final SettingListString WORLDS;
	
	public final SettingString DEFAULT_LOBBY;
	
	public final SettingBoolean HANDLE_WORLDS;
	public final SettingBoolean HANDLE_CHAT;
	public final SettingBoolean LOG_ERRORS;
	public final SettingBoolean DISPLAY_ERRORS;
	public final SettingBoolean OP_CONTROL;
	public final SettingBoolean CONTEXT_LIST;
	public final SettingBoolean CONTROL_XP;

	public final SettingInteger OFFLINE_TIMEOUT;
	public final SettingInteger FINDER_ITEM;
	public final SettingInteger TIME_LIMIT;
	public final SettingInteger TIME_INTERMISSION;
	public final SettingInteger TIME_SETUP;  
	
	public ManhuntSettings( String filepath )
	{
		super( filepath );
		
		addSetting(WORLDS =		new SettingListString("worlds", "The list of Worlds Manhunt will run in.", Bukkit.getWorlds().get(0).getName()), false);
		
		addSetting(DEFAULT_LOBBY =	new SettingString("defaultlobby", "default", "The default Manhunt lobby."), false);
		
		addSetting(HANDLE_WORLDS =	new SettingBoolean("handleworlds", false, "Manhunt will handle multi-world features.", "Another plugin must handle multi-world."), true);
		addSetting(HANDLE_CHAT =	new SettingBoolean("handlechat", true, "Manhunt will handle chat events.", "Manhunt will ignore chat events."), true);
		addSetting(LOG_ERRORS =		new SettingBoolean("logerrors", true, "Manhunt will log errors in a seperate file.", "Manhunt will not keep a seperate error log."), false);
		addSetting(DISPLAY_ERRORS =	new SettingBoolean("showerrors", false, "Manhunt will show full errors in the console.", "Manhunt will not show full errors in the console."), false);
		addSetting(OP_CONTROL =		new SettingBoolean("opcontrol", true, "Only ops have access to basic commands.", "Non-ops have access to basic, non-harmful commands."), true);
		addSetting(CONTEXT_LIST =	new SettingBoolean("contextlist", true, "Server list hides players in other games.", "Server list displays all players."), true);
		addSetting(CONTROL_XP =		new SettingBoolean("controlxp", true, "Manhunt will have its way with player XP levels.", "Manhunt will not touch player XP levels."), false);

		addSetting(OFFLINE_TIMEOUT =	new SettingInteger("timeout", 30, "Seconds before players are disqualified.", "Players will be immediately disqualified."), true);
		addSetting(FINDER_ITEM =		new SettingInteger("finderitem", Material.COMPASS.getId(), "The item used as the Prey Finder.", ""), false);
		addSetting(TIME_LIMIT =			new SettingInteger("timelimit", 60, "Minutes that the hunt will last.", "The game will never end."), true );
		addSetting(TIME_INTERMISSION =	new SettingInteger("intermission", 3, "Minutes between Manhunt games.", ""), true);
		addSetting(TIME_SETUP =			new SettingInteger("setuptime", 10, "Minutes the prey have to prepare.", "There is no setup time."), true);
		
	}
	
}
