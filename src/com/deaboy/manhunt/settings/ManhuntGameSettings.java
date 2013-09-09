package com.deaboy.manhunt.settings;

import org.bukkit.Material;

public class ManhuntGameSettings extends SettingsPack
{
	//////////////// PROPERTIES ////////////////
	public final IntegerSetting 	FINDER_COOLDOWN;
	public final IntegerSetting		FINDER_ITEM;
	public final BooleanSetting 	INSTANT_DEATH;
	public final BooleanSetting 	TEAM_HATS;
	public final IntegerSetting 	TIME_SETUP;
	public final IntegerSetting 	TIME_LIMIT;
	public final BooleanSetting 	FRIENDLY_FIRE;
	public final BooleanSetting 	PREY_FINDER;
	public final DoubleSetting		TEAM_RATIO;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public ManhuntGameSettings()
	{
		addSetting(FINDER_COOLDOWN =new IntegerSetting("findercooldown", 60, "Seconds until the Prey Finder is recharged.", "The Prey Finder has no cooldown."), true);
		addSetting(FINDER_ITEM =	new IntegerSetting("finderitem", Material.COMPASS.getId(), "The item used as the Prey Finder.", ""), false);
		addSetting(INSTANT_DEATH =	new BooleanSetting("instantdeath", false, "Every attack is a one-hit-kill.", "Attack damage is normal."), true);
		addSetting(TEAM_HATS =		new BooleanSetting("teamhats", true, "Teams will have identifying hats.", "Teams do not have identifying hats."), true);
		addSetting(TIME_SETUP =		new IntegerSetting("setuptime", 10, "Minutes the prey have to prepare.", "There is no setup time."), true);
		addSetting(TIME_LIMIT =		new IntegerSetting("timelimit", 60, "Minutes that the hunt will last.", "The game will never end."), true );
		addSetting(FRIENDLY_FIRE =	new BooleanSetting("friendlyfire", false, "Teammates can damage each other.", "Teammates cannot kill each other."), true);
		addSetting(PREY_FINDER =	new BooleanSetting("preyfinder", true, "Players can use the PreyFinder.", "The PreyFinder is disabled."), true);
		addSetting(TEAM_RATIO =		new DoubleSetting("teamratio", 3.0, "The ratio of Hunters to Prey"), true);
		
	}
	
}
