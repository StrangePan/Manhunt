package com.bendude56.hunted.finder;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class FinderManager
{

	HashMap<String, Finder> finders = new HashMap<String, Finder>();

	public void registerNewFinder(Player p)
	{
		finders.put(p.getName(), new Finder(p));
	}

}
