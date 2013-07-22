package com.deaboy.manhunt.settings;

import org.bukkit.Bukkit;

public class LobbySettings extends SettingsPack
{
	public final IntegerSetting 	MODE;
	public final StringSetting		LOBBY_NAME;
	public final StringSetting		LOBBY_CLASS;
	public final BooleanSetting		LOBBY_OPEN;
	public final IntegerSetting		MAX_PLAYERS;
	
	public final LocationSetting	SPAWN_LOCATION;
	public final IntegerSetting		SPAWN_RANGE;
	
	
	public LobbySettings()
	{
		addSetting(MODE =			new IntegerSetting("mode", 0, "The mode the plugin is running in.", ""), false);
		addSetting(LOBBY_NAME =		new StringSetting("lobbyname", "", ""), false);
		addSetting(LOBBY_CLASS =	new StringSetting("lobbyclass", "", ""), false);
		addSetting(LOBBY_OPEN =		new BooleanSetting("open", true, "", ""), false);
		addSetting(MAX_PLAYERS =	new IntegerSetting("maxplayers", 16, "Maximum players allowed in lobby.", "No player cap."), true);
		
		addSetting(SPAWN_LOCATION =	new LocationSetting("spawn", Bukkit.getWorlds().get(0).getSpawnLocation()), false);
		addSetting(SPAWN_RANGE =	new IntegerSetting("spawnrange", 0, "Number of blocks around spawn players will spawn.", "Players will spawn on the spawn point."), true);
		
	}
	
}
