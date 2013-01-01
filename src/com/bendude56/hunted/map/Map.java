package com.bendude56.hunted.map;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public interface Map
{
	//---------------- Getters ----------------//
	/**
	 * Gets the name of this map.
	 * @return
	 */
	public String getName();
	
	/**
	 * Gets the main spawn point for the Map.
	 * @return The Spawn for this Map.
	 */
	public Location getSpawnLocation();
	
	/**
	 * Gets the main World of this Map.
	 * @return The World of this Map.
	 */
	public World getWorld();
	
	/**
	 * Gets a list of the various Setup Spawns.
	 * @return Array of the Setup Spawns
	 */
	public List<Spawn> getSetupSpawns();
	
	/**
	 * Gets a list of the various Hunter Spawns.
	 * @return Array of the Hunter Spawns.
	 */
	public List<Spawn> getHunterSpawns();
	
	/**
	 * Gets a list of the various Prey Spawns.
	 * @return Array of the Prey Spawns.
	 */
	public List<Spawn> getPreySpawns();
	
	/**
	 * Gets one of the map's boundary corners.
	 * @return
	 */
	public Location getMapBoundary1();
	
	/**
	 * Gets one of the map's boundary corners.
	 * @return
	 */
	public Location getMapBoundary2();
	
	/**
	 * Gets one of the map's setup boundary corners.
	 * @return
	 */
	public Location getSetupBoundary1();
	
	/**
	 * Gets one of the map's setup boundary corners.
	 * @return
	 */
	public Location getSetupBoundary2();
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Changes the name of the map.
	 * @param name
	 */
	public void setName(String name);
	
	/**
	 * Changes the spawn location of this map.
	 * @param loc
	 */
	public void setSpawn(Location loc);
	
	/**
	 * Adds a setup spawn to the map.
	 * @param spawn The Spawn to set.
	 */
	public void addSetupSpawn(Spawn spawn);
	
	/**
	 * Adds a hunter spawn to the map.
	 * @param spawn The Spawn to set.
	 */
	public void addHunterSpawn(Spawn spawn);
	
	/**
	 * Adds a prey spawn to the map.
	 * @param spawn The Spawn to set.
	 */
	public void addPreySpawn(Spawn spawn);
	
	/**
	 * Removes a setup spawn from the map.
	 * @param spawn
	 */
	public void removeSetupSpawn(Spawn spawn);
	
	/**
	 * Removes a hunter spawn from the map.
	 * @param spawn
	 */
	public void removeHunterSpawn(Spawn spawn);
	
	/**
	 * Removes a prey spawn from the map.
	 * @param spawn
	 */
	public void removePreySpawn(Spawn spawn);
	
	/**
	 * Sets the map's first boundary corner.
	 * @param loc
	 */
	public void setMapBoundary1(Location loc);
	
	/**
	 * Sets the map's second boundary corner.
	 * @param loc
	 */
	public void setMapBoundary2(Location loc);
	
	/**
	 * Sets the map's first setup boundary.
	 * @param loc
	 */
	public void setSetupBoundary1(Location loc);
	
	/**
	 * Sets the map's second setup boundary.
	 * @param loc
	 */
	public void setSetupBoundary2(Location loc);
	
	
}
