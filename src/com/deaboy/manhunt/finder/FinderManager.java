package com.deaboy.manhunt.finder;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class FinderManager implements Closeable
{
	//---------------- Properties ----------------//
	HashMap<String, Finder> finders;
	
	
	
	//---------------- Constructors ----------------//
	public FinderManager()
	{
		this.finders = new HashMap<String, Finder>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Initializes a new finder for the given player if one isn't already initialized.
	 * @param p
	 */
	public void startFinder(Player p, long lobby_id)
	{
		if (!finders.containsKey(p.getName()))
			finders.put(p.getName(), new Finder(p, lobby_id));
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
		return getFinder(p.getName());
	}
	
	private Finder getFinder(String name)
	{
		if (finders.containsKey(name))
			return finders.get(name);
		else
			return null;
	}

	/**
	 * Stops all finders.
	 */
	public void stopAllFinders()
	{
		for (Finder f : finders.values())
		{
			f.close();
		}
		finders.clear();
	}
	
	public void stopLobbyFinders(long lobby_id)
	{
		List<Finder> list = new ArrayList<Finder>();
		
		for (Finder f : finders.values())
		{
			if (f.getLobbyId() == lobby_id)
			{
				list.add(f);
			}
		}
		
		for (Finder f : list)
			stopFinder(f, true);
		
	}
	
	/**
	 * Stops a player's finder for whatever reason.
	 * @param p
	 */
	public void stopFinder(Player p, boolean ignoreUsed)
	{
		stopFinder(p.getName(), ignoreUsed);
	}
	
	public void stopFinder(String name, boolean ignoreUsed)
	{
		Finder f = getFinder(name);
		
		if (f == null)
			return;

		stopFinder(f, ignoreUsed);
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
			if (finders.containsValue(f))
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
