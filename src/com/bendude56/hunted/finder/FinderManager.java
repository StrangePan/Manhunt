package com.bendude56.hunted.finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class FinderManager implements IFinderManager {

	HashMap<String, Finder> finders = new HashMap<String, Finder>();

	public void registerNewFinder(Player p)
	{
		removeFinder(p.getName());
		finders.put(p.getName(), new Finder(p));
	}

	public List<Finder> getExpiredFinders()
	{
		List<Finder> expired = new ArrayList<Finder>();
		
		for (Finder finder : finders.values())
		{
			if (finder.isExpired())
			{
				expired.add(finder);
			}
		}
		
		return expired;
	}

	public boolean removeFinder(Finder f)
	{
		if (finders.containsValue(f.player))
		{
			return removeFinder(f.player);
		}
		else
		{
			return false;
		}
	}

	public boolean removeFinder(String s)
	{
		if (finders.containsKey(s))
		{
			finders.remove(s);
			return true;
		}
		else
		{
			return false;
		}
	}

}
