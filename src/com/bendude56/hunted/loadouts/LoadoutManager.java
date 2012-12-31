package com.bendude56.hunted.loadouts;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LoadoutManager {

	private HashMap<String, Loadout> loadouts = new HashMap<String, Loadout>();
	//private HashMap<String, Loadout> tempLoadouts = new HashMap<String, Loadout>();

	private final String loadouts_directory = "plugins/Manhunt";
	private final String world_loadouts_directory = loadouts_directory;

	public final Loadout DEFAULT_HUNTER_LOADOUT;
	public final Loadout DEFAULT_PREY_LOADOUT;

	public LoadoutManager()
	{
		File hl = new File(world_loadouts_directory + "/" + "hunter_loadout.inv");
		if (hl.exists())
		{
			DEFAULT_HUNTER_LOADOUT = new Loadout("hunter_loadout", world_loadouts_directory);
		}
		else
		{
			ItemStack[] contents = new ItemStack[36];
			ItemStack[] armour = new ItemStack[4];
			contents[0] = new ItemStack(Material.STONE_SWORD, 1);
			contents[1] = new ItemStack(Material.BOW, 1);
			contents[2] = new ItemStack(Material.TORCH, 3);
			contents[3] = new ItemStack(Material.COOKED_CHICKEN, 3);
			contents[4] = new ItemStack(Material.ARROW, 64);
			contents[5] = new ItemStack(Material.COMPASS);
			armour[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
			armour[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			armour[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			armour[3] = new ItemStack(Material.LEATHER_HELMET, 1);
			
			DEFAULT_HUNTER_LOADOUT = new Loadout("hunter_loadout", world_loadouts_directory, contents, armour);
		}
		
		File pl = new File(world_loadouts_directory + "/" + "prey_loadout.inv");
		if (pl.exists())
		{
			DEFAULT_PREY_LOADOUT = new Loadout("prey_loadout", world_loadouts_directory);
		}
		else
		{
			ItemStack[] contents = new ItemStack[36];
			ItemStack[] armour = new ItemStack[4];
			contents[0] = new ItemStack(Material.STONE_SWORD, 1);
			contents[1] = new ItemStack(Material.BOW, 1);
			contents[2] = new ItemStack(Material.TORCH, 3);
			contents[3] = new ItemStack(Material.COOKED_CHICKEN, 1);
			contents[4] = new ItemStack(Material.ARROW, 64);
			
			DEFAULT_PREY_LOADOUT = new Loadout("prey_loadout", world_loadouts_directory, contents, armour);
		}
		
		File loadoutFolder = new File(loadouts_directory);
		if (loadoutFolder.exists())
		{
			File[] invFiles = loadoutFolder.listFiles();
			for (int i = 0; i < invFiles.length; i++)
			{
				if (invFiles[i].getName().endsWith(".inv"))
				{
					Loadout load = new Loadout(invFiles[i].getName().substring(0, invFiles[i].getName().length()-4), loadouts_directory);
					loadouts.put(load.name, load);
				}
			}
		}
			
	}
	
	public void addLoadout(String name, ItemStack[] contents, ItemStack[] armour)
	{
		if (!loadouts.containsKey(name))
		{
			loadouts.put(name, new Loadout(name, loadouts_directory, contents, armour));
		}
	}
	
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
/*
	public void saveLoadout(Player p)
	{
		if (!tempLoadouts.containsKey(p.getName()))
		{
			tempLoadouts.put(p.getName(), new Loadout(p.getInventory().getContents(), p.getInventory().getArmorContents()));
		}
	}

	public void restoreLoadout(Player p)
	{
		if (tempLoadouts.containsKey(p.getName()))
		{
			LoadoutUtil.setPlayerInventory(p, tempLoadouts.get(p.getName()));
			tempLoadouts.remove(p.getName());
		}
	}
*/
}
