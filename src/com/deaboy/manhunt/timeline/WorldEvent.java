package com.deaboy.manhunt.timeline;

import org.bukkit.World;

public interface WorldEvent extends Event
{
	//---------------- Getters ----------------//
	/**
	 * Gets the world for the event.
	 * @return The world of this event.
	 */
	public World getWorld();
	
	
	//---------------- Public Methods ----------------//
	@Override
	public WorldEvent addAction(Action action);
	@Override
	public WorldEvent removeAction(Action action);
	@Override
	public WorldEvent clearActions();
	
}
