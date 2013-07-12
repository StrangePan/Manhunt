package com.deaboy.manhunt.lobby;

import java.io.File;

import org.bukkit.Location;

import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.SettingManager;

public abstract class GameLobby extends Lobby
{
	//////////////// PROPERTIES ////////////////
	private Game game;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public GameLobby(long id, String name, World world, Location loc)
	{
		super(id, name, LobbyType.GAME, world, loc);
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- GAMES ----------------//
	@Override
	public boolean gameIsRunning()
	{
		if (this.game != null)
		{
			return this.game.isRunning();
		}
		else
		{
			return false;
		}
	}
	protected Game getGame()
	{
		return this.game;
	}
	@Override
	public abstract boolean startGame();
	@Override
	public abstract boolean endGame();
	@Override
	public abstract boolean cancelGame();
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public abstract SettingManager getSettings();
	@Override
	protected abstract void initializeSettings();
	
	
	//---------------- FILES ----------------//
	@Override
	public abstract int saveFiles();
	@Override
	public abstract int loadFiles();
	@Override
	public abstract int deleteFiles();
	@Override
	public abstract Lobby loadFromFile(File file);
	
}
