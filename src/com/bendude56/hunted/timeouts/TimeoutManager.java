package com.bendude56.hunted.timeouts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.games.Game;

/**
 * This class handles all player timeouts, including
 * automatically forfeiting them.
 * @author Deaboy
 *
 */
public class TimeoutManager {
	private Game game;
	
	private List<Timeout> timeouts = new ArrayList<Timeout>();
	
	public TimeoutManager(Game game)
	{
		this.game = game;
	}
	
	/**
	 * This method starts a timeout countdown for the player.
	 * Time is automatically set by the value in the settings file.
	 * @param p
	 */
	public Timeout startTimeout(Player p)
	{
		Long boot_tick = HuntedPlugin.getInstance().getWorld().getFullTime();
		boot_tick += HuntedPlugin.getInstance().getSettings().OFFLINE_TIMEOUT.value * 20;
		
		Timeout timeout = getTimeout(p.getName());
		
		if (timeout != null)
		{
			stopTimeout(timeout);
		}
		
		timeout = new Timeout(p.getName(), boot_tick, this);
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
	 * @param p
	 */
	public void stopTimeout(String p)
	{
		Timeout t = getTimeout(p);
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
	
	public Game getGame()
	{
		return game;
	}

	public void close()
	{
		game = null;
		stopAllTimeouts();
	}

}
