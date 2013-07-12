package com.deaboy.manhunt.lobby;

import java.io.File;

import org.bukkit.Location;

import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.SettingManager;

public class ManhuntLobby extends GameLobby
{
	//////////////// PROPERTIES ////////////////
	
	
	//////////////// CONSTRUCTORS /////////////////
	public ManhuntLobby(String name, long id, World world, Location loc)
	{
		super(id, name, world, loc);
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- GAMES ----------------//
	public boolean startGame()
	{
		
		return true;
	}
	public boolean endGame()
	{
		
		return true;
	}
	public boolean cancelGame()
	{
		
		return true;
	}
	private void stopGame()
	{
		
	}
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public SettingManager getSettings()
	{
		
	}
	
	
	//---------------- FILES ----------------//
	@Override
	public int saveFiles()
	{
		
	}
	@Override
	public int loadFiles()
	{
		
	}
	@Override
	public int deleteFiles()
	{
		
	}
	@Override
	protected void initializeSettings()
	{
		
	}
	@Override
	public ManhuntLobby loadFromFile(File file)
	{
		
	}
	
	
	//---------------- CLOSE ----------------//
	@Override
	public void close()
	{
		super.close();
	}
}
