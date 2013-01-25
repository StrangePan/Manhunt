package com.deaboy.hunted.finder;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class FinderManager implements Closeable
{
	List<Finder> finders = new ArrayList<Finder>();

	/**
	 * Initializes a new finder for the given player if one isn't already initialized.
	 * @param p
	 */
	public void startFinder(Player p)
	{
		if (getFinder(p) == null)
		{
			finders.add(new Finder(p));
		}
	}

	/**
	 * Check if the player has a finder that is still valid.
	 * @param p the player in question
	 * @return Returns true if the player has a finder that is still valid, false if the finder is invalid or does not exist.
	 */
	public boolean verifyFinder(Player p)
	{
		Finder f = getFinder(p);
		if (f == null)
		{
			return false;
		}
		else
		{
			return f.checkValidity();
		}
	}

	/**
	 * Returns a Finder object from the player.
	 * @param p
	 * @return
	 */
	private Finder getFinder(Player p)
	{
		for (Finder f : finders)
		{
			if (f.getPlayerName() == p.getName())
			{
				return f;
			}
		}
		return null;
	}

	/**
	 * Stops all finders.
	 */
	public void stopAllFinders()
	{
		for (Finder f : finders)
		{
			f.close();
		}
		finders.clear();
	}

	/**
	 * Stops a player's finder for whatever reason.
	 * @param p
	 */
	public void stopFinder(Player p, boolean ignoreUsed)
	{
		Finder f = getFinder(p);
		if (f != null)
		{
			stopFinder(f, ignoreUsed);
		}
	}

	/**
	 * Stops a finder, protected to be used only by the Finders package
	 * @param f
	 */
	protected void stopFinder(Finder f, boolean ignoreUsed)
	{
		if (ignoreUsed || !f.isUsed())
		{
			f.close();
			if (finders.contains(f))
			{
				finders.remove(f);
			}
		}
	}

	/**
	 * Closes the class.
	 */
	public void close()
	{
		stopAllFinders();
	}

}
