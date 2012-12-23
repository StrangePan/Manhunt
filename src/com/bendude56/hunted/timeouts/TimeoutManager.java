package com.bendude56.hunted.timeouts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.entity.Player;

import com.bendude56.hunted.Manhunt;

/**
 * This class handles all player timeouts, including
 * automatically forfeiting them.
 * @author Deaboy
 *
 */
public class TimeoutManager
{
	private List<Timeout> timeouts = new ArrayList<Timeout>();
	
	/**
	 * This method starts a timeout countdown for the player.
	 * Time given is measured in milliseconds and is set at
	 * the current system time + the given time.
	 * @param p The player to start the timeout for.
	 * @param time The milliseconds from the current time
	 * to eject the player.
	 */
	public Timeout startTimeout(Player p, long time)
	{
		Timeout timeout = getTimeout(p.getName());
		
		if (timeout != null)
		{
			stopTimeout(timeout);
		}
		
		timeout = new Timeout(p.getName(), Manhunt.getLobby(p).getId(), new Date().getTime() + time);
		timeouts.add(timeout);
		
		return timeout;
	}

	/**
	 * Gets the timeout object by a player's name
	 * @param p
	 * @return Returns null if it doesn't exist.
	 */
	private Timeout getTimeout(String p)
	{
		for (Timeout timeout : timeouts)
		{
			if (timeout.player_name.equalsIgnoreCase(p))
			{
				return timeout;
			}
		}
		return null;
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
		if (t != null)
		{
			stopTimeout(t);
		}
	}
	
	/**
	 * Tells the timeout to shutdown
	 * @param t
	 */
	protected void stopTimeout(Timeout t)
	{
		t.close();
		
		if (timeouts.contains(t));
		{
			timeouts.remove(t);
		}
	}
	
	/**
	 * Stops all timeouts.
	 */
	public void stopAllTimeouts()
	{
		for (Timeout t : timeouts)
		{
			t.close();
		}
		timeouts.clear();
	}

}
