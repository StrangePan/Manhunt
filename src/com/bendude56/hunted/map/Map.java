package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public interface Map
{
	//---------------- Getters ----------------//
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
	 * Ges an array of the various Setup Spawns.
	 * @return Array of the Setup Spawns
	 */
	public Spawn[] getSetupSpawns();
	
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
	 * Gets one of the map's boundary corners.
	 * @param index Which corner to return, either 1 or 2.
	 * @return
	 */
	public Location getMapBoundary(int index);
	
	/**
	 * Gets one of the map's setup boundary corners.
	 * @param index Which corner to return, either 1 or 2.
	 * @return
	 */
	public Location getSetupBoundary(int index);
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Replaces the Setup Spawn to the map at the given index.
	 * @param index The index of the spawn. Must be 0-4.
	 * @param spawn The Spawn to set.
	 */
	public void setSetupSpawn(int index, Spawn spawn);
	
	/**
	 * Replaces the Hunter Spawn to the map at the given index.
	 * @param index The index of the spawn. Must be 0-4.
	 * @param spawn The Spawn to set.
	 */
	public void setHunterSpawn(int index, Spawn spawn);
	
	/**
	 * Replaces the Prey Spawn at the given index.
	 * @param index The index to set the Spawn. Must be 0.4.
	 * @param spawn The Spawn to set.
	 */
	public void setPreySpawn(int index, Spawn spawn);
	
	/**
	 * Replaces the map boundary at the given index.
	 * @param index The index to replace. Must be 1 or 2.
	 * @param loc
	 */
	public void setMapBoundary(int index, Location loc);
	
	/**
	 * Replaces the map boundary at the given index.
	 * @param index The index to replace. Must be 1 or 2.
	 * @param loc
	 */
	public void setSetupBoundary(int index, Location loc);
}
