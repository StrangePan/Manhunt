package com.deaboy.manhunt.game;

import java.io.Closeable;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.event.Listener;
import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.settings.SettingsPack;

public abstract class Game implements Closeable, Listener
{
	//////////////// Properties ////////////////
	private final long lobby_id;
	private Map current_map;
	private GameStage stage;
	
	//////////////// Constructors ////////////////
	public Game(GameLobby lobby)
	{
		this.lobby_id = lobby.getId();
		this.current_map = null;
		this.stage = GameStage.INTERMISSION;
	}
	
	
	//////////////// Getters ////////////////
	public World getWorld()
	{
		if (isRunning() && current_map != null)
			return current_map.getWorld().getWorld();
		else
			return null;
	}
	public Map getMap()
	{
		return current_map;
	}
	public boolean isRunning()
	{
		return (this.stage != GameStage.INTERMISSION);
	}
	public GameStage getStage()
	{
		return this.stage;
	}
	public GameLobby getLobby()
	{
		return (GameLobby) Manhunt.getLobby(lobby_id);
	}
	
	
	//------------ Setters ------------//
	public void setMap(Map map)
	{
		if (!isRunning())
		{
			this.current_map = map;
		}
	}
	protected void setStage(GameStage stage)
	{
		if (stage != null)
		{
			this.stage = stage;
			if (getLobby() != null)
				Manhunt.log(Level.INFO, getLobby().getName() + " has entered the " + stage.getName() + " stage.");
		}
	}
	
	
	//------------ Public Methods ------------//
	public abstract void startGame();
	public abstract void cancelGame();
	public abstract void endGame();
	public abstract void testGame();
	
	@Override
	public void close()
	{
		this.current_map = null;
	}
	
	public abstract void distributeTeams();
	public abstract void forfeitPlayer(String name);
	public abstract SettingsPack getSettings();
	
}
