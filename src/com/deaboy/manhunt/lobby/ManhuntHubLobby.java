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
	public ManhuntHubLobby(long id, File file)
	{
		super(id, file);
		this.settings = new LobbySettings();
	}
	public ManhuntHubLobby(long id, File file, String name, Location loc)
	{
		super(id, file, name, loc);
		this.settings = new LobbySettings();
	}
	
	
	///////////////// PUBLIC METHODS /////////////////
	//---------------- Interface ----------------//
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
	
	
	//---------------- Settings -----------------//
	@Override
	public LobbySettings getSettings()
	{
		return this.settings;
	}
	
	
}
