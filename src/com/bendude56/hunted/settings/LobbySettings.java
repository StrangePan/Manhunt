package com.bendude56.hunted.settings;

import org.bukkit.World;

public class LobbySettings extends SettingManagerBase implements SettingManager
{

	private static final long serialVersionUID = -6482647767320807514L;
	
	public final SettingInteger MODE;
	public final SettingBoolean USE_AMBER;
	
	public final SettingLocation SPAWN_HUNTER;
	public final SettingLocation SPAWN_PREY;
	public final SettingLocation SPAWN_SETUP;
	public final SettingLocation SPAWN_LOBBY;
	
	public final SettingInteger SPAWN_RANGE_HUNTER;
	public final SettingInteger SPAWN_RANGE_PREY;
	public final SettingInteger SPAWN_RANGE_LOBBY;
	
	public final SettingLocation BOUNDARY_WORLD1;
	public final SettingLocation BOUNDARY_WORLD2;
	public final SettingLocation BOUNDARY_SETUP1;
	public final SettingLocation BOUNDARY_SETUP2;
	
	public final SettingLocation PROTECTION_HUNTER1;
	public final SettingLocation PROTECTION_HUNTER2;
	public final SettingLocation PROTECTION_PREY1;
	public final SettingLocation PROTECTION_PREY2;
	public final SettingLocation PROTECTION_SETUP1;
	public final SettingLocation PROTECTION_SETUP2;
	public final SettingLocation PROTECTION_LOBBY1;
	public final SettingLocation PROTECTION_LOBBY2;

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
		
		addSetting(SPAWN_HUNTER =	new SettingLocation("hunterspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_PREY =		new SettingLocation("preyspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_SETUP =	new SettingLocation("setupspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_LOBBY =	new SettingLocation("lobbyspawn", world.getSpawnLocation()), true);
		
		addSetting(SPAWN_RANGE_HUNTER =	new SettingInteger("hunterspawnrange", 0, "The range around which the hunters can spawn.", "The hunters will spawn exactly on the point."), true);
		addSetting(SPAWN_RANGE_PREY =	new SettingInteger("preyspawnrange", 0, "The range around which the prey can spawn.", "The prey will spawn exactly on the point."), true);
		addSetting(SPAWN_RANGE_LOBBY =	new SettingInteger("lobbyspawnrange", 0, "The range around which the players will spawn.", "The prey will spawn exactly on the point."), true);
		
		addSetting(BOUNDARY_WORLD1 =new SettingLocation("worldboundary1", world.getSpawnLocation()), true);
		addSetting(BOUNDARY_WORLD2 =new SettingLocation("worldboundary2", world.getSpawnLocation()), true);
		addSetting(BOUNDARY_SETUP1 =new SettingLocation("setupboundary1", world.getSpawnLocation()), true);
		addSetting(BOUNDARY_SETUP2 =new SettingLocation("setupboundary2", world.getSpawnLocation()), true);
		
		addSetting(PROTECTION_HUNTER1 =	new SettingLocation("hunterprotection1", world.getSpawnLocation()), true);
		addSetting(PROTECTION_HUNTER2 =	new SettingLocation("hunterprotection2", world.getSpawnLocation()), true);
		addSetting(PROTECTION_PREY1 =	new SettingLocation("preyprotection1", world.getSpawnLocation()), true);
		addSetting(PROTECTION_PREY2 =	new SettingLocation("preyprotection2", world.getSpawnLocation()), true);
		addSetting(PROTECTION_SETUP1 =	new SettingLocation("setupprotection1", world.getSpawnLocation()), true);
		addSetting(PROTECTION_SETUP2 =	new SettingLocation("seutpprotection2", world.getSpawnLocation()), true);
		addSetting(PROTECTION_LOBBY1 =	new SettingLocation("lobbyprotection1", world.getSpawnLocation()), true);
		addSetting(PROTECTION_LOBBY2 =	new SettingLocation("lobbyprotection2", world.getSpawnLocation()), true);
		
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
