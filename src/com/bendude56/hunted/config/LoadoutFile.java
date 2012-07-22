package com.bendude56.hunted.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.inventory.ItemStack;

public class LoadoutFile {

	private ItemStack[] loadout;
	private final ItemStack[] defaultLoadout;
	public final String label;
	
	private String filename;
	
	public LoadoutFile(String name, String directory, ItemStack[] defaultLoadout){
		this.label = name;
		this.filename = directory + "/" + name + ".inv";
		this.defaultLoadout = defaultLoadout;
		
		load();
		save();
	}
	
	public ItemStack[] getLoadout()
	{
		return this.loadout.clone();
	}
	
	public void setLoadout(ItemStack[] loadout)
	{
		this.loadout = loadout;
		save();
	}

	public void save()
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(loadout);
			out.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean load()
	{
		ItemStack[] loadout;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			loadout = (ItemStack[])in.readObject();
			in.close();
			
			this.loadout = loadout;
			
			return true;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			this.loadout = this.defaultLoadout;
			return false;
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			this.loadout = this.defaultLoadout;
			return false;
		}
	}

	public void delete()
	{
		File file = new File(filename);
		file.delete();
	}

}
