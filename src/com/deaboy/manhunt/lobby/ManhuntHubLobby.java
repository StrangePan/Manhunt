package com.deaboy.manhunt.lobby;

import org.bukkit.Location;

import com.deaboy.manhunt.settings.LobbySettings;

public class ManhuntHubLobby extends HubLobby
{
	///////////////// PROPERTIES /////////////////
	private LobbySettings settings;
	
	
	///////////////// CONSTRUCTORS /////////////////
	public ManhuntHubLobby(long id, String name, Location loc)
	{
		super(id, name, loc);
		this.settings = new LobbySettings();
	}
	
	
	///////////////// PUBLIC METHODS /////////////////
	@Override
	public boolean playerJoinLobby(String p) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean playerLeaveLobby(String p) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean playerLeaveServer(String p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LobbySettings getSettings()
	{
		return this.settings;
	}
	
	
	
}
