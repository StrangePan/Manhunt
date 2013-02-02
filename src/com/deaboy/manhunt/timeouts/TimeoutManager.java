package com.deaboy.manhunt.timeouts;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.entity.Player;

/**
 * This class handles all player timeouts, including
 * automatically forfeiting them.
 * @author Deaboy
 *
 */
public class TimeoutManager
{
	//---------------- Properties ----------------//
	private HashMap<String, Timeout> timeouts;
	
	
	
	//--------------- Constructor ----------------//
	public TimeoutManager()
	{
		this.timeouts = new HashMap<String, Timeout>();
	}
	
	
	
	//---------------- Functions ----------------//
	/**
	 * This method starts a timeout countdown for the player.
	 * Time given is measured in milliseconds and is set at
	 * the current system time + the given time.
	 * @param p The player to start the timeout for.
	 * @param delay The milliseconds from the current time
	 * to eject the player.
	 */
	public Timeout startTimeout(Player p, long lobby_id, long delay)
	{
		Timeout timeout = getTimeout(p.getName());
		
		if (timeout != null)
		{
			stopTimeout(timeout);
		}
		
		timeout = new Timeout(p.getName(), lobby_id, new Date().getTime() + delay);
		timeouts.put(p.getName(), timeout);
		
		return timeout;
	}

	/**
	 * Gets the timeout object by a player's name
	 * @param p
	 * @return Returns null if it doesn't exist.
	 */
	private Timeout getTimeout(String p)
	{
		if (timeouts.containsKey(p))
			return timeouts.get(p);
		else
			return null;
	}
	
	public boolean hasTimeout(Player p)
	{
		return hasTimeout(p.getName());
	}
	
	public boolean hasTimeout(String name)
	{
		return timeouts.containsKey(name);
	}
	
	/**
	 * Cancels a player's timeout if it exists.
	 * @param p
	 */
	public void stopTimeout(Player p)
	{
		stopTimeout(p.getName());
	}
	
	/**
	 * Cancels a player's timeout if it exists.
	 * @param name
	 */
	public void stopTimeout(String name)
	{
		Timeout t = getTimeout(name);
		if (t == null)
			return;
		
		stopTimeout(t);
		timeouts.remove(name);
	}
	
	/**
	 * Tells the timeout to shutdown
	 * @param t
	 */
	protected void stopTimeout(Timeout t)
	{
		t.close();
	}
	
	/**
	 * Stops all timeouts.
	 */
	public void stopAllTimeouts()
	{
		for (Timeout t : timeouts.values())
		{
			stopTimeout(t);
		}
		timeouts.clear();
	}

}
