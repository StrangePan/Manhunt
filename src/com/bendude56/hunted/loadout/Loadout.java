package com.bendude56.hunted.loadout;

import org.bukkit.inventory.ItemStack;

public class Loadout {
	private ItemStack[] itemstack;

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
	
	public void setContents(ItemStack[] itemstack)
	{
		this.itemstack = itemstack.clone();
	}
	
	public ItemStack[] getContents()
	{
		return itemstack.clone();
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
