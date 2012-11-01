package com.bendude56.hunted.game.events;

import java.util.List;

import org.bukkit.World;

public interface Event
{
	/**
	 * Adds an action to be executed with this Event.
	 * @param action The Action to execute
	 */
	public void addAction(Action action);
	
	/**
	 * Removes an Action from this Event's Action list.
	 * @param action The Action to remove
	 */
	public void removeAction(Action action);
	
	/**
	 * Clears all Actions from this Event's Action list.
	 */
	public void removeAllActions();
	
	/**
	 * Sets the trigger time for this Event
	 * @param time The time measured in world ticks 
	 * @param world The world this event is monitoring
	 */
	public void setTriggerTime(Long time, World world);
	
	/**
	 * Sets the trigger time for this event
	 * @param time The time measured in milliseconds
	 */
	public void setTriggerTime(Long time);
	
	/**
	 * Gets a list of the Actions for this event
	 * @return An ArrayList of Actions
	 */
	public List<Action> getActions();
	
	/**
	 * Returns the time the event is going to trigger.
	 * @return The Long value of time.
	 */
	public Long getTriggerTime();
	
	/**
	 * The world this Event is counting on. Null if the event
	 * is watching the real time.
	 * @return The World the event is watching.
	 */
	public World getWorld();
	
	/**
	 * Executes all of the action's events.
	 */
	public void execute();
	
	/**
	 * Executes the event if all conditions are met.
	 */
	public void executeIfReady();
	
	/**
	 * Checks to see if the event is ready to execute.
	 * @return True if the event is ready, false if not.
	 */
	public boolean isReady();
	
	/**
	 * Returns whether or not this event has expired.
	 * @return True if this event has executed, false if not.
	 */
	public boolean isExpired();
}
