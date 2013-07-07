package com.deaboy.manhunt.settings;

import org.bukkit.Material;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.lobby.LobbyType;

public class LobbySettings extends SettingManagerBase implements SettingManager
{
	private static final long serialVersionUID = -6482647767320807514L;
	
	public final SettingInteger MODE;
	public final SettingBoolean USE_AMBER;
	public final SettingString	LOBBY_NAME;
	public final SettingInteger LOBBY_TYPE;
	public final SettingBoolean	LOBBY_OPEN;
	public final SettingString	GAME_TYPE;
	public final SettingListString MAPS;

	public final SettingInteger OFFLINE_TIMEOUT;
	public final SettingInteger FINDER_ITEM;
	public final SettingInteger TIME_LIMIT;
	public final SettingInteger TIME_INTERMISSION;
	public final SettingInteger TIME_SETUP;
	
	public final SettingString	SPAWN_WORLD;
	public final SettingDouble	SPAWN_X;
	public final SettingDouble	SPAWN_Y;
	public final SettingDouble	SPAWN_Z;
	public final SettingFloat	SPAWN_PITCH;
	public final SettingFloat	SPAWN_YAW;
	public final SettingInteger	SPAWN_RANGE;
	
	public final SettingDouble	TEAM_RATIO;
	public final SettingInteger FINDER_COOLDOWN;
	public final SettingBoolean ALL_TALK;
	public final SettingBoolean TEAM_HATS;
	public final SettingBoolean PREY_FINDER;
	public final SettingBoolean FRIENDLY_FIRE;
	public final SettingBoolean INSTANT_DEATH;
	
	public final SettingBoolean HOSTILE_MOBS;
	public final SettingBoolean PASSIVE_MOBS;
	
	
	public LobbySettings( String filename, boolean literalpath )
	{
		super(literalpath ? filename : Manhunt.path_lobbies + "/" + filename + Manhunt.extension_lobbies);
		
		addSetting(MODE =			new SettingInteger("mode", 0, "The mode the plugin is running in.", ""), false);
		addSetting(USE_AMBER =		new SettingBoolean("useamber", true, "Manhunt will record/restore the world with Amber.", "Manhunt will not restore the world."), true);
		addSetting(LOBBY_NAME =		new SettingString("lobbyname", "", ""), false);
		addSetting(LOBBY_TYPE =		new SettingInteger("lobbytype", LobbyType.GAME.ordinal(), "", ""), false);
		addSetting(LOBBY_OPEN =		new SettingBoolean("open", true, "", ""), false);
		addSetting(GAME_TYPE =		new SettingString("gametype", ManhuntGame.class.getCanonicalName(), ""), false);
		addSetting(MAPS =			new SettingListString("maps", ""), false);

		addSetting(OFFLINE_TIMEOUT =	new SettingInteger("timeout", 30, "Seconds before players are disqualified.", "Players will be immediately disqualified."), true);
		addSetting(FINDER_ITEM =		new SettingInteger("finderitem", Material.COMPASS.getId(), "The item used as the Prey Finder.", ""), false);
		addSetting(TIME_LIMIT =			new SettingInteger("timelimit", 60, "Minutes that the hunt will last.", "The game will never end."), true );
		addSetting(TIME_INTERMISSION =	new SettingInteger("intermission", 3, "Minutes between Manhunt games.", ""), true);
		addSetting(TIME_SETUP =			new SettingInteger("setuptime", 10, "Minutes the prey have to prepare.", "There is no setup time."), true);
		
		addSetting(SPAWN_WORLD =	new SettingString("spawnworld", "", ""), false);
		addSetting(SPAWN_X = 		new SettingDouble("spawnx", 0.0, ""), false);
		addSetting(SPAWN_Y =		new SettingDouble("spawny", 64.0, ""), false);
		addSetting(SPAWN_Z =		new SettingDouble("spawnz", 0.0, ""), false);
		addSetting(SPAWN_PITCH =	new SettingFloat("spawnpitch", 0.0f, ""), false);
		addSetting(SPAWN_YAW =		new SettingFloat("spawnyaw", 0.0f, ""), false);
		addSetting(SPAWN_RANGE =	new SettingInteger("spawnrange", 0, "Number of blocks around spawn players will spawn.", "Players will spawn on the spawn point."), true);
		
		addSetting(TEAM_RATIO =		new SettingDouble("teamratio", 3.0, "The ratio of Hunters to Prey"), true);
		addSetting(FINDER_COOLDOWN =new SettingInteger("findercooldown", 60, "Seconds until the Prey Finder is recharged.", "The Prey Finder has no cooldown."), true);
		addSetting(ALL_TALK =		new SettingBoolean("alltalk", false, "Teams can communicate with each other.", "Teams cannot see each other's chat."), true);
		addSetting(TEAM_HATS =		new SettingBoolean("teamhats", true, "Teams will have identifying hats.", "Teams do not have identifying hats."), true);
		addSetting(PREY_FINDER =	new SettingBoolean("preyfinder", true, "Players can use the PreyFinder.", "The PreyFinder is disabled."), true);
		addSetting(FRIENDLY_FIRE =	new SettingBoolean("friendlyfire", false, "Teammates can damage each other.", "Teammates cannot kill each other."), true);
		addSetting(INSTANT_DEATH =	new SettingBoolean("insantdeath", false, "Every attack is a one-hit-kill.", "Attack damage is normal."), true);
		
		addSetting(HOSTILE_MOBS =	new SettingBoolean("hostilemobs", true, "Hostile mobs are enabled.", "Hostile mobs are disabled."), true);
		addSetting(PASSIVE_MOBS =	new SettingBoolean("passivemobs", true, "Passive mobs are enabled.", "Passive mobs are disabled."), true);
	}
	
}
