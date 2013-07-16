package com.deaboy.manhunt.lobby;

import org.bukkit.Location;

import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.settings.GameLobbySettings;

public class ManhuntGameLobby extends GameLobby
{
	//////////////// PROPERTIES ////////////////
	private GameLobbySettings settings;
	
	
	//////////////// CONSTRUCTORS /////////////////
	public ManhuntGameLobby(String name, long id, Location loc)
	{
		super(id, name, loc);
		this.settings = new GameLobbySettings();
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- INTERFACE ----------------//
	@Override
	public boolean playerJoinLobby(String p)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean playerLeaveLobby(String p)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean playerLeaveServer(String p)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean forfeitPlayer(String name)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean playerChangeTeam(String player, Team team)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean registerMap(Map map)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean unregisterMap(Map map)
	{
		// TODO Write this
		return true;
	}
	
	
	//---------------- GAMES ----------------//
	@Override
	public boolean startGame()
	{
		
		return true;
	}
	@Override
	public boolean endGame()
	{
		
		stopGame();
		return true;
	}
	@Override
	public boolean cancelGame()
	{
		
		stopGame();
		return true;
	}
	private void stopGame()
	{
		
	}
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public GameLobbySettings getSettings()
	{
		return this.settings;
	}
	
	
	//---------------- FILES ----------------//
	protected void initializeSettings()
	{
		
	}
	
	
	//---------------- CLOSE ----------------//
	@Override
	public void close()
	{
		super.close();
	}
}
