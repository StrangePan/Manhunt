package com.bendude56.hunted.loadout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.HuntedPlugin;

public class LoadoutManager {

	private HashMap<String, Loadout> loadouts = new HashMap<String, Loadout>();

	private final String loadouts_directory = "plugins/Manhunt";
	private final String world_loadouts_directory = HuntedPlugin.getInstance().getWorld().getName() + "/Manhunt";

	public final Loadout HUNTER_LOADOUT;
	public final Loadout PREY_LOADOUT;

	public LoadoutManager()
	{
		File hl = new File(world_loadouts_directory + "/" + "hunter_loadout.inv");
		if (hl.exists())
		{
			HUNTER_LOADOUT = new Loadout("hunter_loadout", world_loadouts_directory);
		}
		else
		{
			ItemStack[] contents = new ItemStack[36];
			/*for (int i = 0; i < contents.length; i++)
			{
				contents[i] = new ItemStack((Integer) null, (Integer) null, (Short) null);
			}*/
			ItemStack[] armour = new ItemStack[4];
			/*for (int i = 0; i < armour.length; i++)
			{
				armour[i] = new ItemStack((Integer) null, (Integer) null, (Short) null);
			}*/
			contents[0] = new ItemStack(Material.STONE_SWORD, 1);
			contents[1] = new ItemStack(Material.BOW, 1);
			// contents[2] = new ItemStack(Material.STONE_PICKAXE, 1);
			// contents[3] = new ItemStack(Material.STONE_SPADE, 1);
			// contents[4] = new ItemStack(Material.STONE_AXE, 1);
			contents[2] = new ItemStack(Material.TORCH, 3);
			contents[3] = new ItemStack(Material.COOKED_CHICKEN, 3);
			contents[4] = new ItemStack(Material.ARROW, 64);
			armour[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
			armour[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			armour[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			armour[3] = new ItemStack(Material.LEATHER_HELMET, 1);
			
			HUNTER_LOADOUT = new Loadout("hunter_loadout", world_loadouts_directory, contents, armour);
		}
		
		File pl = new File(world_loadouts_directory + "/" + "prey_loadout.inv");
		if (pl.exists())
		{
			PREY_LOADOUT = new Loadout("prey_loadout", world_loadouts_directory);
		}
		else
		{
			ItemStack[] contents = new ItemStack[36];
			/*for (int i = 0; i < contents.length; i++)
			{
				contents[i] = new ItemStack((Integer) null, (Integer) null, (Short) null);
			}*/
			ItemStack[] armour = new ItemStack[4];
			/*for (int i = 0; i < armour.length; i++)
			{
				armour[i] = new ItemStack((Integer) null, (Integer) null, (Short) null);
			}*/
			contents[0] = new ItemStack(Material.STONE_SWORD, 1);
			contents[1] = new ItemStack(Material.BOW, 1);
			// contents[2] = new ItemStack(Material.STONE_PICKAXE, 1);
			// contents[3] = new ItemStack(Material.STONE_SPADE, 1);
			// contents[4] = new ItemStack(Material.STONE_AXE, 1);
			contents[2] = new ItemStack(Material.TORCH, 3);
			contents[3] = new ItemStack(Material.COOKED_CHICKEN, 1);
			contents[4] = new ItemStack(Material.ARROW, 64);
			// armour[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
			// armour[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			// armour[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			// armour[3] = new ItemStack(Material.LEATHER_HELMET, 1);
			
			PREY_LOADOUT = new Loadout("prey_loadout", world_loadouts_directory, contents, armour);
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
	
}
