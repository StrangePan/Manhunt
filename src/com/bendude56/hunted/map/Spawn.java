package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public interface Spawn
{
	//---------------- Getters ----------------//
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
	 * Gets the spawn's first protection corner location.
	 * @return The location of the protection boundary around the spawn.
	 */
	public Location getProtectionCorner1();
	
	/**
	 * Gets the spawn's second protection corner location.
	 * @return The location of the protection boundary around the spawn.
	 */
	public Location getProtectionCorner2();
	
	/**
	 * Gets the spawn's first boundary corner location.
	 * @return The location of the boundary around the spawn.
	 */
	public Location getBoundaryCorner1();
	
	/**
	 * Gets the spawn's second boundary corner location.
	 * @return The location of the boundary around the spawn.
	 */
	public Location getBoundaryCorner2();
	
	
	
	//---------------- Setters ----------------//
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
	 * Sets the spawn's first protection corner to the given location.
	 * @param loc The corner's new location.
	 */
	public void setProtectionCorner1(Location loc);
	
	/**
	 * Sets the spawn's second protection corner to the given location.
	 * @param loc The corner's new location.
	 */
	public void setProtectionCorner2(Location loc);
	
	/**
	 * Sets the spawn's first boundary corner to the given location.
	 * @param loc The corner's new location.
	 */
	public void setBoundaryCorner1(Location loc);
	
	/**
	 * Sets the spawn's second boundary corner to the given location.
	 * @param loc The corner's new location.
	 */
	public void setBoundaryCorner2(Location loc);
	
	
}
