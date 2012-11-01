package com.bendude56.hunted.loadouts;

import org.bukkit.inventory.ItemStack;

public class Loadout
{
	private ItemStack[] contents;
	private ItemStack[] armor;

	public final String name;
	public final String directory;
	public final String filename;
	public final String fullpath;
	
	// -------- Legacy Filetype Paths -------- //
	public final String fullpath_inv;
	
	
	public Loadout(String name, String directory)
	{
		this.name = name;
		this.directory = directory;
		this.filename = name + ".dat";
		this.fullpath = this.directory + "/" + this.filename;
		
		this.fullpath_inv = this.directory + "/" + this.name + ".inv";
		
		this.contents = new ItemStack[36];
		this.armor = new ItemStack[4];
		
		load();
	}

	public Loadout(String name, String directory, ItemStack[] contents, ItemStack[] armor)
	{
		this.name = name;
		this.directory = directory;
		this.filename = name + ".dat";
		this.fullpath = this.directory + "/" + this.filename;

		this.fullpath_inv = this.directory + "/" + this.name + ".inv";
		
		setContents(contents, armor);
		
		save();
	}

	public Loadout(ItemStack[] contents, ItemStack[] armor)
	{
		this.name = null;
		this.directory = null;
		this.filename = null;
		this.fullpath = null;
		
		this.fullpath_inv = null;
		
		setContents(contents, armor);
	}

	public void setContents(ItemStack[] contents, ItemStack[] armor)
	{
		this.contents = new ItemStack[contents.length];
		this.armor = new ItemStack[armor.length];
		
		for (int i = 0; i < contents.length; i ++)
		{
			if (contents[i] != null)
			{
				this.contents[i] = contents[i].clone();
			}
		}
		for (int i = 0; i < armor.length; i ++)
		{
			if (armor[i] != null)
			{
				this.armor[i] = armor[i].clone();
			}
		}
		
		save();
	}
	
	public ItemStack[] getContents()
	{
		ItemStack[] contents = new ItemStack[this.contents.length];
		
		for (int i = 0; i < this.contents.length; i ++)
		{
			if (this.contents[i] != null)
			{
				contents[i] = this.contents[i].clone();
			}
		}
		
		return contents;
	}
	
	public ItemStack[] getArmorContents()
	{
		ItemStack[] armor = new ItemStack[this.armor.length];
		
		for (int i = 0; i < this.armor.length; i ++)
		{
			if (this.armor[i] != null)
			{
				armor[i] = this.armor[i].clone();
			}
		}
		
		return armor;
	}
	
	public void save()
	{
		if (name != null)
		{
			LoadoutFile.save(this);
		}
	}
	
	public void load()
	{
		if (name != null)
		{
			LoadoutFile.load(this);
		}
	}
	
	public boolean delete()
	{
		if (name != null)
		{
			return LoadoutFile.delete(this);
		}
		else
		{
			return false;
		}
	}
	
}
