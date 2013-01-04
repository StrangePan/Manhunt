package com.bendude56.hunted.loadouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadoutManager
{
	//---------------- Constants ----------------//
	public static final String DEFAULT_HUNTER_LOADOUT = "def_Hunter";
	public static final String DEFAULT_PREY_LOADOUT = "def_Prey";
	
	
	
	//---------------- Properties ----------------//
	private HashMap<String, Loadout> loadouts;
	
	
	
	//---------------- Constructors ----------------//
	public LoadoutManager()
	{
		loadouts = new HashMap<String, Loadout>();
	}
	
	
	
	//---------------- Getters ----------------//
	public Loadout getLoadout(String name)
	{
		return loadouts.get(name);
	}
	
	public List<Loadout> getAllLoadouts()
	{
		List<Loadout> results = new ArrayList<Loadout>();
		for (Loadout loadout : loadouts.values())
			results.add(loadout);
		return results;
	}
	
	
	
	//---------------- Setters ----------------//
	public void addLoadout(Loadout loadout)
	{
		if (!loadouts.containsKey(loadout.getName()) && !loadouts.containsValue(loadout))
			loadouts.put(loadout.getName(), loadout);
	}
	
	public boolean deleteLoadout(String name)
	{
		if (loadouts.containsKey(name))
		{
			Loadout loadout = getLoadout(name);
			loadouts.remove(name);
			return loadout.delete();
		}
		else
		{
			return true;
		}
	}
	
	
}
