package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.*;

public abstract class Lobby implements Closeable
{
	//---------------- Properties ----------------//
	private final long id;
	private String name;
	private final LobbyType type;
	private final String world_name;
	private boolean enabled;
	
	
	
	//---------------- Constructors ----------------//
	public Lobby(long id, String name, LobbyType type, World world)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.world_name = world.getName();
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the long ID of this lobby.
	 * @return This lobby's ID.
	 */
	public long getId()
	{
		return id;
	}
	
	/**
	 * Gets the name of this lobby.
	 * @return This lobby's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets this Lobby's Spawn object.
	 * @return This Lobby's Spawn.
	 */
	public Spawn getSpawn()
	{
		return Manhunt.getWorld(world_name).getSpawn();
	}
	
	/**
	 * Gets this Lobby's main World.
	 * @return This Lobby's World.
	 */
	public World getWorld()
	{
		return Manhunt.getWorld(world_name);
	}
	
	/**
	 * Get this Lobby's main spawn point.
	 * @return This Lobby's spawn location
	 */
	public Location getSpawnLocation()
	{
		return Manhunt.getWorld(world_name).getSpawnLocation();
	}
	
	/**
	 * Gets a list of this Lobby's Players.
	 * @return ArrayList of Players in this Lobby
	 */
	public abstract List<Player> getOnlinePlayers();
	
	/**
	 * Gets a list of this Lobby's players' names.
	 * @return ArrayList of Strings of players' names in this lobby.
	 */
	public abstract List<String> getPlayerNames();
	
	/**
	 * Checks to see if the Lobby is currently enabled.
	 * @return True if the Lobby is enabled, false if not.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public LobbyType getType()
	{
		return type;
	}
	
	
	//---------------- Setters ----------------//
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Adds a player to the Lobby via their name.
	 * @param p The Player to add.
	 */
	public abstract void addPlayer(String name);
	
	/**
	 * Removes a Player from the lobby via their name.
	 * @param p The Player to remove.
	 */
	public abstract void removePlayer(String name);
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Sends a message to all of this Lobby's Players.
	 * @param message The message to broadcast.
	 */
	public void broadcast(String message)
	{
		for (Player p : getOnlinePlayers())
		{
			p.sendMessage(message);
		}
	}
	
	/**
	 * Clears all Players from this Lobby.
	 */
	public abstract void clearPlayers();
	
	/**
	 * Enables the Lobby.
	 */
	public void enable()
	{
		this.enabled = true;
	}
	
	/**
	 * Disables the Lobby.
	 */
	public void disable()
	{
		this.enabled = false;
	}
	
	
}
