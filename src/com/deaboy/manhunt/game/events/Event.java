package com.deaboy.manhunt.game.events;

import java.util.List;

public interface Event
{
	
	//---------------- Getters ----------------//
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
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets the trigger time for this event
	 * @param time The time measured in milliseconds
	 */
	public void setTriggerTime(Long time);
	
	/**
	 * Sets whether or not this event is expired.
	 * @param expired
	 */
	public void setExpired(boolean expired);
	
	/**
	 * Resets the event, including it's expiration time.
	 */
	public void reset();
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Adds an action to be executed with this Event.
	 * @param action The Action to add
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
	public void clearActions();

	/**
	 * Executes all of the action's events.
	 */
	public void execute();

	/**
	 * Returns whether or not this event has expired.
	 * @return True if this event has executed, false if not.
	 */
	public boolean isExpired();
}
