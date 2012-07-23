package com.bendude56.hunted.loadout;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.HuntedPlugin;

public class LoadoutManager {

	private HashMap<String, Loadout> loadouts = new HashMap<String, Loadout>();

	private final String loadouts_directory = "plugins/Manhunt/Loadouts";
	private final String world_loadouts_directory = HuntedPlugin.getInstance().getWorld().getName() + "/Manhunt";

	private final Loadout HUNTER_LOADOUT;
	private final Loadout PREY_LOADOUT;

	public LoadoutManager()
	{
		HUNTER_LOADOUT = new Loadout("hunter_loadout", world_loadouts_directory);
		PREY_LOADOUT = new Loadout("prey_loadout", world_loadouts_directory);
	}

	public Loadout getHunterLoadout()
	{
		if (getLoadout(HuntedPlugin.getInstance().getSettings().HUNTER_LOADOUT_CURRENT.value) == null)
			return HUNTER_LOADOUT;
		else
			return getLoadout(HuntedPlugin.getInstance().getSettings().HUNTER_LOADOUT_CURRENT.value);
	}
	
	public Loadout getPreyLoadout()
	{
		if (getLoadout(HuntedPlugin.getInstance().getSettings().PREY_LOADOUT_CURRENT.value) == null)
			return PREY_LOADOUT;
		else
			return getLoadout(HuntedPlugin.getInstance().getSettings().PREY_LOADOUT_CURRENT.value);
	}
	
	public void addLoadout(String name, ItemStack[] items)
	{
		if (!loadouts.containsKey(name))
		{
			loadouts.put(name, new Loadout(name, loadouts_directory));
		}
	}
	
	public Loadout getLoadout(String name)
	{
		return loadouts.get(name);
	}

	public Collection<Loadout> getAllLoadouts()
	{
		return loadouts.values();
	}

	public void deleteLoadout(String name)
	{
		if (loadouts.containsKey(name))
		{
			getLoadout(name).delete();
			loadouts.remove(name);
		}
	}
	
}
