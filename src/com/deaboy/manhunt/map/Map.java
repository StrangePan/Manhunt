package com.deaboy.manhunt.map;

import java.util.List;

import org.bukkit.Location;

public interface Map
{
	//---------------- Getters ----------------//
	/**
	 * Gets the name of this map.
	 * @return
	 */
	public String getName();
	/**
	 * Gets the full name of this map, formatted as such: <world name>.<map name>
	 * @return
	 */
	public String getFullName();

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
	 * Gets a spawn point based on the spawn's name.
	 * @param name The name of the spawn point.
	 * @return The spawn.
	 */
	public Spawn getSpawn(String name);
	/**
	 * Gets a list of all spawns associated with this map.
	 * @return A list of all spawn points in this map.
	 */
	public List<Spawn> getSpawns();
	/**
	 * Gets a list of all spawns with the specified type.
	 * @param type The type of spawn to search for.
	 * @return A list of all spawns with the specified type.
	 */
	public List<Spawn> getSpawns(SpawnType type);
	
	/**
	 * Gets a zone based on the zone's name.
	 * @param name The name of the zone to get.
	 * @return The zone with the given name or null if none exist.
	 */
	public Zone getZone(String name);
	/**
	 * Gets a list of all zones associated with this map.
	 * @return
	 */
	public List<Zone> getZones();
	/**
	 * Gets a list of the specified type(s) of zones associated
	 * with this maps.
	 * @param types The different types of zone(s) desired.
	 * @return
	 */
	public List<Zone> getZones(ZoneFlag ... types);
	
	
	
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
	 * Adds a pre-made spawn to the map.
	 * @param spawn The spawn to add.
	 */
	public boolean addSpawn(Spawn spawn);
	/**
	 * Creates and adds a spawn point to the map.
	 * @param name The name of the spawn.
	 * @param location The location of the spawn. Must be in the same world as the map.
	 * @param type The type of spawn.
	 * @return The newly created spawn or null if it didn't work.
	 */
	public Spawn createSpawn(String name, SpawnType type, Location location);
	/**
	 * Creates and adds a spawn point to the map.
	 * @param name The name of the spawn point.
	 * @param location The location of the spawn.
	 * @param range The range of the spawn.
	 * @param type The type of spawn.
	 * @return The newly created spawn or null if it didn't work.
	 */
	public Spawn createSpawn(String name, SpawnType type, Location location, int range);
	/**
	 * Deletes a spawn from the map.
	 * @param name The name of the spawn point to delete.
	 */
	public void removeSpawn(String name);
	/**
	 * Clears all spawn points from the map.
	 */
	public void clearSpawns();
	
	/**
	 * Adds a pre-made zone to the map.
	 * @param zone The zone to add
	 */
	public boolean addZone(Zone zone);
	/**
	 * Creates and adds a new zone to the map.
	 * @param type The type of zone
	 * @param corner1 The first corner of the zone
	 * @param corner2 The second corner of the zone
	 * @param flags Array of zone flags to apply to this zone
	 * @return The newly created zone or null if it didn't work.
	 */
	public Zone createZone(String name, Location corner1, Location corner2, ZoneFlag...flags);
	/**
	 * Creates and adds a new zone to the map.
	 * @param type The type of zone
	 * @param corner1 The first corner of the zone
	 * @param corner2 The second corner of the zone
	 * @param flags List of flags to apply to this zone
	 * @return The newly created zone or null if it didn't work.
	 */
	public Zone createZone(String name, Location corner1, Location corner2, List<ZoneFlag> flags);
	/**
	 * Removes a zone from the map.
	 * @param name The name of the zone to remove.
	 */
	public void removeZone(String name);
	/**
	 * Clears all zones from this map.
	 */
	public void clearZones();
	
	
		
}
