package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.io.File;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.manhunt.Manhunt;

public class LobbyClass implements Closeable
{
	////////////////Properties ////////////////
	private final Class<? extends Lobby> lobbyClass;
	private final long id;
	private final String name;
	private final LobbyType type;
	private JavaPlugin plugin;
	
	
	//////////////// Constructor ////////////////
	public LobbyClass(long id, String name, LobbyType type, Class<? extends Lobby> lobbyclass, JavaPlugin plugin)
	{
		try
		{
			lobbyclass.getConstructor(long.class, String.class, Location.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("Lobby class must implement constructor(long, String, Location)");
		}
		try
		{
			lobbyclass.getConstructor(File.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("Lobby class must implement constructor(File)");
		}
		
		this.lobbyClass = lobbyclass;
		this.name = name;
		this.id = id;
		this.type = type;
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
	public LobbyType getLobbyType()
	{
		return type;
	}
	public Class<? extends Lobby> getLobbyClass()
	{
		return lobbyClass;
	}
	public JavaPlugin getPlugin()
	{
		return plugin;
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	public Lobby createInstance(long id, String name, Location loc)
	{
		try
		{
			return (Lobby) lobbyClass.getConstructor(Lobby.class).newInstance(id, name, loc);
		}
		catch (Exception e)
		{
			Manhunt.log(e);
			return null;
		}
	}
	public Lobby createInstance(File file)
	{
		try
		{
			return (Lobby) lobbyClass.getConstructor(File.class).newInstance(file);
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
