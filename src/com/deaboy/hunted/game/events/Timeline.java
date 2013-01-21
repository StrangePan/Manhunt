package com.deaboy.hunted.game.events;

import java.util.List;

import org.bukkit.World;

public interface Timeline
{

	//---------------- Getters ----------------//
	/**
	 * Gets the current world for the timeline.
	 * @return The world associated with this timeline.
	 */
	public World getWorld();
	
	/**
	 * Returns a list of the registered events for this timeline.
	 * @return List of registered events.
	 */
	public List<Event> getRegisteredEvents();
	
	/**
	 * Returns whether or not the timeline is currently
	 * running or not.
	 * @return True if the timeline is running, false if not.
	 */
	public boolean isRunning();
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets the world for the current timeline. Only works if
	 * the timeline is not running.
	 * @param world The new world.
	 */
	public void setWorld(World world);
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Removes a registered event from this timeline.
	 * @param event The event to cancel.
	 */
	public void cancelEvent(Event event);
	
	/**
	 * Registers a new event with the event.
	 * @param event The event to register.
	 */
	public void registerEvent(Event event);
	
	/**
	 * Runs the timeline.
	 */
	public void run();
	
	/**
	 * Stops this timeline from executing it's events.
	 */
	public void stop();
}
