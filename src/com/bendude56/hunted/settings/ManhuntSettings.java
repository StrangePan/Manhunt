package com.bendude56.hunted.settings;

import org.bukkit.Bukkit;

public class ManhuntSettings extends SettingManagerBase implements SettingManager
{
	private static final long serialVersionUID = -2749093656484939858L;
	
	public final SettingInteger MODE;
	
	public final SettingListString WORLDS;

	public final SettingBoolean HANDLE_CHAT;
	public final SettingBoolean FRIENDLY_FIRE;
	public final SettingBoolean INSTANT_DEATH;
	public final SettingBoolean TEAM_HATS;
	public final SettingBoolean PREY_FINDER;
	public final SettingBoolean USE_AMBER;
	
	public final SettingInteger OFFLINE_TIMEOUT;
	public final SettingInteger TIME_LIMIT;
	public final SettingInteger TIME_INTERMISSION;
	public final SettingInteger TIME_SETUP;  
	
	public ManhuntSettings( String filepath )
	{
		super( filepath );
		
		addSetting(MODE =		new SettingInteger("mode", 0, "The mode the plugin is running in.", ""), false);
		
		addSetting(WORLDS =		new SettingListString("worlds", "The list of Worlds Manhunt will run in.", Bukkit.getWorlds().get(0).getName()), false);
		
		addSetting(HANDLE_CHAT =	new SettingBoolean("handlechat", true, "Manhunt will handle chat events.", "Manhunt will ignore chat events."), true);
		addSetting(FRIENDLY_FIRE =	new SettingBoolean("friendlyfire", false, "Teammates can damage each other.", "Teammates cannot kill each other."), true);
		addSetting(INSTANT_DEATH =	new SettingBoolean("insantdeath", false, "Every attack is a one-hit-kill.", "Attack damage is normal."), true);
		addSetting(TEAM_HATS =		new SettingBoolean("teamhats", true, "Teams will have identifying hats.", "Teams do not have identifying hats."), true);
		addSetting(PREY_FINDER =	new SettingBoolean("preyfinder", true, "Players can use the PreyFinder.", "The PreyFinder is disabled."), true);
		addSetting(USE_AMBER =		new SettingBoolean("useamber", true, "Manhunt will record/restore the world with Amber.", "Manhunt will not restore the world."), true);
		
		addSetting(OFFLINE_TIMEOUT =	new SettingInteger("timeout", 30, "Seconds before players are disqualified.", "Players will be immediately disqualified."), true);
		addSetting(TIME_LIMIT =			new SettingInteger("timelimit", 60, "Minutes that the hunt will last.", "The game will never end."), true );
		addSetting(TIME_INTERMISSION =	new SettingInteger("intermission", 3, "Minutes between Manhunt games.", ""), true);
		addSetting(TIME_SETUP =			new SettingInteger("setuptime", 10, "Minutes the prey have to prepare.", "There is no setup time."), true);
		
	}
	
}
