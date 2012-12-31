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
	private HashMap<Long, Loadout> loadouts;
	
	
	
	//---------------- Constructors ----------------//
	public LoadoutManager()
	{
		loadouts = new HashMap<Long, Loadout>();
	}
	
	
	
	//---------------- Getters ----------------//
	public Loadout getLoadout(Long id)
	{
		return loadouts.get(id);
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
		long id = 0;
		
		while (loadouts.containsKey(id))
			id++;
		
		loadouts.put(id, loadout);
	}
	
	public boolean deleteLoadout(long id)
	{
		if (loadouts.containsKey(id))
		{
			Loadout loadout = getLoadout(id);
			loadouts.remove(id);
			return loadout.delete();
		}
		else
		{
			return true;
		}
	}
	
	
}
