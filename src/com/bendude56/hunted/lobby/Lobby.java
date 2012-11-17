package com.bendude56.hunted.lobby;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.map.*;

public interface Lobby
{
	
	//---------------- Getters ----------------//
	/**
	 * Gets this Lobby's Spawn object.
	 * @return This Lobby's Spawn.
	 */
	public Spawn getSpawn();
	
	/**
	 * Gets this Lobby's main World.
	 * @return This Lobby's World.
	 */
	public World getWorld();
	
	/**
	 * Get this Lobby's main spawn point.
	 * @return This Lobby's spawn location
	 */
	public Location getLocation();
	
	/**
	 * Gets a list of this Lobby's Players.
	 * @return ArrayList of Players in this Lobby
	 */
	public List<Player> getPlayers();
	
	/**
	 * Checks to see if the Lobby is currently enabled.
	 * @return True if the Lobby is enabled, false if not.
	 */
	public boolean isEnabled();
	
	
	//---------------- Setters ----------------//
	/**
	 * Adds a Player to the Lobby.
	 * @param p The Player to add.
	 */
	public void addPlayer(Player p);
	
	/**
	 * Removes a Player from the lobby.
	 * @param p The Player to remove.
	 */
	public void removePlayer(Player p);
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Sends a message to all of this Lobby's Players.
	 * @param message The message to broadcast.
	 */
	public void broadcast(String message);
	
	/**
	 * Clears all Players from this Lobby.
	 */
	public void clearPlayers();
	
	/**
	 * Enables the Lobby.
	 */
	public void enable();
	
	/**
	 * Disables the Lobby.
	 */
	public void disable();
	
	
}
