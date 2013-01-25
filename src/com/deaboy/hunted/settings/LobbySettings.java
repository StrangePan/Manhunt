package com.deaboy.hunted.settings;

import org.bukkit.World;

public class LobbySettings extends SettingManagerBase implements SettingManager
{
	private static final long serialVersionUID = -6482647767320807514L;
	
	public final SettingInteger MODE;
	public final SettingBoolean USE_AMBER;

	public final SettingInteger OFFLINE_TIMEOUT;
	public final SettingInteger FINDER_COOLDOWN;
	public final SettingBoolean ALL_TALK;
	public final SettingBoolean TEAM_HATS;
	public final SettingBoolean PREY_FINDER;
	public final SettingBoolean FRIENDLY_FIRE;
	public final SettingBoolean INSTANT_DEATH;
	
	public final SettingBoolean HOSTILE_MOBS;
	public final SettingBoolean PASSIVE_MOBS;
	
	public LobbySettings( World world )
	{
		super( world.getWorldFolder().getPath() + "/manhunt.config" );
		
		addSetting(MODE =		new SettingInteger("mode", 0, "The mode the plugin is running in.", ""), false);
		addSetting(USE_AMBER =		new SettingBoolean("useamber", true, "Manhunt will record/restore the world with Amber.", "Manhunt will not restore the world."), true);
		
		addSetting(OFFLINE_TIMEOUT =	new SettingInteger("timeout", 30, "Seconds before players are disqualified.", "Players will be immediately disqualified."), true);
		addSetting(FINDER_COOLDOWN =	new SettingInteger("findercooldown", 60, "Seconds until the Prey Finder is recharged.", "The Prey Finder has no cooldown."), true);
		addSetting(ALL_TALK =		new SettingBoolean("alltalk", false, "Teams can communicate with each other.", "Teams cannot see each other's chat."), true);
		addSetting(TEAM_HATS =		new SettingBoolean("teamhats", true, "Teams will have identifying hats.", "Teams do not have identifying hats."), true);
		addSetting(PREY_FINDER =	new SettingBoolean("preyfinder", true, "Players can use the PreyFinder.", "The PreyFinder is disabled."), true);
		addSetting(FRIENDLY_FIRE =	new SettingBoolean("friendlyfire", false, "Teammates can damage each other.", "Teammates cannot kill each other."), true);
		addSetting(INSTANT_DEATH =	new SettingBoolean("insantdeath", false, "Every attack is a one-hit-kill.", "Attack damage is normal."), true);
		
		addSetting(HOSTILE_MOBS =	new SettingBoolean("hostilemobs", true, "Hostile mobs are enabled.", "Hostile mobs are disabled."), true);
		addSetting(PASSIVE_MOBS =	new SettingBoolean("passivemobs", true, "Passive mobs are enabled.", "Passive mobs are disabled."), true);
	}
	
}
