package com.bendude56.hunted.game.events;

import java.util.List;

public interface Timeline
{
	/**
	 * Registers a new event with this timeline.
	 */
	public void registerEvent(Event event);
	
	/**
	 * Removes a registered event from this timeline
	 */
	public void cancelEvent(Event event);
	
	/**
	 * Returns a list of the registered events for this timelnie
	 */
	public List<Event> getRegisteredEvents();
	
	/**
	 * Runs the timeline.
	 */
	public void run();
	
	/**
	 * Stops this timeline from executing it's events
	 */
	public void stop();
}
