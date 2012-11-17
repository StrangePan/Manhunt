package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public interface Map
{
	
	/**
	 * Gets the main spawn point for the Map.
	 * @return The Spawn for this Map.
	 */
	public Spawn getSpawn();
	
	/**
	 * Gets the main World of this Map.
	 * @return The World of this Map.
	 */
	public World getWorld();
	
	/**
	 * Gets the main spawn Location of this Map.
	 * @return The spawn point of this Map.
	 */
	public Location getLocation();
	
	/**
	 * Gets an array of the various Hunter Spawns.
	 * @return Array of the Hunter Spawns.
	 */
	public Spawn[] getHunterSpawns();
	
	/**
	 * Gets an array of the various Prey Spawns.
	 * @return Array of the Prey Spawns.
	 */
	public Spawn[] getPreySpawns();
	
	/**
	 * Adds a new Hunter Spawn to the map at the given index.
	 * @param spawn The Spawn to add.
	 */
	public void setHunterSpawn(int index, Spawn spawn);
	
	/**
	 * Adds a new Prey Spawn to the map at the given index.
	 * @param index The index to set the Spawn.
	 * @param spawn The Spawn to add.
	 */
	public void setPreySpawn(int index, Spawn spawn);
	
	
}
