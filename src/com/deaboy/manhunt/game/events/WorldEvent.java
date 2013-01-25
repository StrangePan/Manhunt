package com.deaboy.manhunt.game.events;

import org.bukkit.World;

public interface WorldEvent extends Event
{
	
	//---------------- Getters ----------------//
	/**
	 * Gets the world for the event.
	 * @return The world of this event.
	 */
	public World getWorld();
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets the world of the event.
	 * @param world The new world.
	 */
	public void setWorld(World world);
	
}
