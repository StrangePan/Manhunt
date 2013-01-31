package com.deaboy.manhunt.game;

import java.io.Closeable;

import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Lobby;

public class GameType implements Closeable
{
	//---------------- Properties ----------------//
	private final Class<? extends Game> gameClass;
	private final long id;
	private final String name;
	private JavaPlugin plugin;
	
	
	
	//---------------- Constructor ----------------//
	public GameType(Class<? extends Game> gameclass, long id, String name, JavaPlugin plugin)
	{
		this.gameClass = gameclass;
		this.name = name;
		this.id = id;
		this.plugin = plugin;
	}
	
	
	
	//---------------- Getters ----------------//
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
	
	public Game createInstance(Lobby lobby)
	{
		try
		{
			return (Game) gameClass.getConstructor(Lobby.class).newInstance(lobby);
		}
		catch (Exception e)
		{
			Manhunt.log(e);
			return null;
		}
	}
	
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	public void close()
	{
		this.plugin = null;
	}
	
	
}
