package com.bendude56.hunted.loadout;

import org.bukkit.inventory.ItemStack;

public class Loadout {
	private ItemStack[] contents;
	private ItemStack[] armour;

	public final String name;
	public final String directory;
	public final String filename;
	public final String fullpath;
	
	public Loadout(String name, String directory)
	{
		this.name = name;
		this.directory = directory;
		this.filename = name + ".inv";
		this.fullpath = this.directory + "/" + this.filename;
		
		load();
	}

	public Loadout(String name, String directory, ItemStack[] contents, ItemStack[] armour)
	{
		this.name = name;
		this.directory = directory;
		this.filename = name + ".inv";
		this.fullpath = this.directory + "/" + this.filename;
		
		setContents(contents, armour);
		
		save();
	}

	public void setContents(ItemStack[] contents, ItemStack[] armour)
	{
		this.contents = contents;
		this.armour = armour;
		
		save();
	}
	
	public ItemStack[] getContents()
	{
		return contents.clone();
	}
	
	public ItemStack[] getArmour()
	{
		return armour.clone();
	}
	
	public void save()
	{
		(new LoadoutFile(this)).save();
	}
	
	public void load()
	{
		(new LoadoutFile(this)).load();
	}
	
	public boolean delete() {
		return (new LoadoutFile(this)).delete();
	}
	
}
