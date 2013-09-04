package com.deaboy.manhunt.map;

import java.util.List;

import org.bukkit.Location;

public interface World
{
	//---------------- Getters ----------------//
	/**
	 * Gets this world's name.
	 * @return The name of this world.
	 */
	public String getName();
	
	/**
	 * Gets this world's Bukkit world.
	 * @return This world's linked world.
	 */
	public org.bukkit.World getWorld();
	
	/**
	 * Gets this world's main spawn. If there is no spawn set up in
	 * this world, it will return the world's default spawn.
	 * @return This world's spawn data.
	 */
	public Spawn getSpawn();
	
	/**
	 * Gets the spawn location of this world.
	 * @return
	 */
	public Location getSpawnLocation();
	
	/**
	 * Gets all the maps set up in the world.
	 * @return List of Maps in this world.
	 */
	public List<Map> getMaps();
	
	/**
	 * Gets the map with the given label. If no map by that name
	 * exists, it will return null.
	 * @param label The name of the desired map.
	 * @return The map if it exists, null if not.
	 */
	public Map getMap(String label);
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets the world's spawn point.
	 * @param loc The new location of the world.
	 */
	public void setSpawnLocation(Location loc);
	
	/**
	 * Adds a map to this world.
	 * @param map The map to add to the world.
	 */
	public void addMap(String label, Map map);
	
	/**
	 * Deletes a map from this world.
	 * @param map
	 */
	public void deleteMap(String label);
	
	/**
	 * Deletes a map from this world.
	 * @param map
	 */
	public void deleteMap(Map map);
	
	/**
	 * Removes all maps from this world.
	 */
	public void clearMaps();
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Saves this world to file. 
	 */
	public void save();
	
	/**
	 * Loads this world from file.
	 */
	public void load();
	
	
	
}
