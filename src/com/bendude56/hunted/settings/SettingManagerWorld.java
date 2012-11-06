package com.bendude56.hunted.settings;

import org.bukkit.World;

public class SettingManagerWorld extends SettingManagerBase implements SettingManager
{

	private static final long serialVersionUID = -6482647767320807514L;
	
	public final SettingLocation SPAWN_HUNTER;
	public final SettingLocation SPAWN_PREY;
	public final SettingLocation SPAWN_SETUP;
	public final SettingLocation SPAWN_LOBBY;
	
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
	
	public SettingManagerWorld( World world )
	{
		super( world.getWorldFolder().getPath() + "/manhunt.config" );
		
		addSetting(SPAWN_HUNTER =	new SettingLocation("hunterspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_PREY =		new SettingLocation("preyspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_SETUP =	new SettingLocation("setupspawn", world.getSpawnLocation()), true);
		addSetting(SPAWN_LOBBY =	new SettingLocation("lobbyspawn", world.getSpawnLocation()), true);
		
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
		
	}
	
}
