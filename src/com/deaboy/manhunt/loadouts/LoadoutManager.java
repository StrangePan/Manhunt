package com.deaboy.manhunt.loadouts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.deaboy.manhunt.Manhunt;

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
	
	
	
	//---------------- Public Methods ----------------//
	public void loadLoadoutFiles()
	{
		File file = new File(Manhunt.path_loadouts);
		
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		if (!file.isDirectory())
			Manhunt.log(Level.SEVERE, "There was a problem loading the Manhunt loadouts.");
		
		for (File f : file.listFiles())
		{
			if (f.getName().endsWith(Manhunt.extension_loadouts))
			{
				Loadout loadout = new Loadout("", f.getName());
				loadout.load();
				loadout.setName(loadout.getName().replaceAll(" ", "_"));
				
				if (this.loadouts.containsKey(loadout.getName()))
				{
					int i = 2;
					while (loadouts.containsKey(loadout.getName() + "_" + i))
						i++;
					
					Manhunt.log(Level.SEVERE, "A loadout with the name \"" + loadout.getName() + "\" already exists.\n" +
							"Renaming loadout in file \"" + file.getName() + "\" to \"" + loadout.getName() + "_" + i + "\"");
					
					loadout.setName(loadout.getName() + "_" + i);
				}
				
				addLoadout(loadout);
			}
		}
	}
	
	
	
	public void saveAllLoadouts()
	{
		for (Loadout loadout : getAllLoadouts())
			loadout.save();
	}
	
	
	
}
