package com.deaboy.manhunt.lobby;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	public boolean playerJoinLobby(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean playerLeaveLobby(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean playerLeaveServer(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LobbySettings getSettings()
	{
		return this.settings;
	}
	
	
	//---------------- Files ----------------//
	public ManhuntHubLobby fromFile(File file)
	{
		// TODO WRITE THIS
	}
	
}
