package com.deaboy.manhunt.game;

import java.io.Closeable;

import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;

public class GameClass implements Closeable
{
	//////////////// Properties ////////////////
	private final Class<? extends Game> gameClass;
	private final long id;
	private final String name;
	private JavaPlugin plugin;
	
	
	//////////////// Constructor ////////////////
	public GameClass(long id, String name, Class<? extends Game> gameclass, JavaPlugin plugin)
	{
		try
		{
			gameclass.getConstructor(GameLobby.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("Game class must implement constructor(Lobby)");
		}
		
		this.gameClass = gameclass;
		this.name = name;
		this.id = id;
		this.plugin = plugin;
	}
	
	
	//////////////// Getters ////////////////
	public long getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public Class<? extends Game> getGameClass()
	{
		return gameClass;
	}
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	public Game createInstance(GameLobby lobby)
	{
		try
		{
			return (Game) gameClass.getConstructor(GameLobby.class).newInstance(lobby);
		}
		catch (Exception e)
		{
			Manhunt.log(e);
			return null;
		}
	}
	
	
	public void close()
	{
		this.plugin = null;
	}
	
	
}
