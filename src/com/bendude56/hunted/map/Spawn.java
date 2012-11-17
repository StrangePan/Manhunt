package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public interface Spawn
{
	/**
	 * Gets the spawn point for this Spawn.
	 * @return The spawn point.
	 */
	public Location getLocation();
	
	/**
	 * Gets the World for this spawn.
	 * @return The World.
	 */
	public World getWorld();
	
	/**
	 * Gets the range players can spawn around the spawn point.
	 * @return The range around the spawn point players can spawn.
	 */
	public int getRange();
	
	/**
	 * Gets a corner of the protection boundary around the spawn.
	 * @param corner The corner to get, either 0 or 1.
	 * @return The corner of the protection boundary around the spawn.
	 */
	public Location getProtectionCorner(int corner);
	
	/**
	 * Gets a corner of the border around spawn.
	 * The border confines players to a given area around spawn.
	 * @param corner The corner to get, either 0 or 1.
	 * @return The corner of the border around spawn.
	 */
	public Location getBorderCorner(int corner);
	
	
	/**
	 * Sets the spawn's location.
	 * @param loc The location to change the spawn's location to.
	 */
	public void setLocation(Location loc);
	
	/**
	 * Sets the range around the spawn point players will spawn.
	 * @param range The new range for this Spawn.
	 */
	public void setRange(int range);
	
	/**
	 * Sets the specified protection boundary corner to the given location.
	 * @param corner The corner to change.
	 * @param loc The corner's new location.
	 */
	public void setProtectionCorner(int corner, Location loc);
	
	/**
	 * Sets the specified border corner to the given location.
	 * @param corner The corner to change.
	 * @param loc The corner's new location.
	 */
	public void setBorderCorner(int corner, Location loc);
	
	
}
