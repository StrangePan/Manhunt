package com.bendude56.hunted.finder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.bendude56.hunted.games.Game;

public class FinderManager
{
	private Game game;

	List<Finder> finders = new ArrayList<Finder>();

	public FinderManager(Game game)
	{
		this.game = game;
	}

	/**
	 * Initializes a new finder for the given player if one isn't already initialized.
	 * @param p
	 */
	public void startFinder(Player p)
	{
		if (getFinder(p) == null)
		{
			finders.add(new Finder(p, this));
		}
		else
		{
			getFinder(p).sendTimeLeft();
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
			if (f.player_name == p.getName())
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
	public void stopFinder(Player p)
	{
		Finder f = getFinder(p);
		if (f != null)
		{
			stopFinder(f);
		}
	}

	/**
	 * Stops a finder, protected to be used only by the Finders package
	 * @param f
	 */
	protected void stopFinder(Finder f)
	{
		f.close();
		if (finders.contains(f))
		{
			finders.remove(f);
		}
	}

	public Game getGame()
	{
		return game;
	}

	/**
	 * Closes the class.
	 */
	public void close()
	{
		stopAllFinders();
		game = null;
	}

}
