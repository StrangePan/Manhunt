package com.bendude56.hunted.loadouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.server.v1_4_6.NBTTagCompound;

import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;
import org.jnbt.TagType;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.loadouts.models.SimpleItem;
import com.bendude56.hunted.loadouts.models.SimpleLoadout;

public class LoadoutFile
{
	
	public static void load(Loadout loadout)
	{
		if (new File(loadout.fullpath_inv).exists())
		{
			OldLoadoutFile oldfile = new OldLoadoutFile(loadout);
			oldfile.load();
			oldfile.delete();
			oldfile.close();
			return;
		}
		
		SimpleLoadout sloadout = loadFile(loadout);
		
		if (sloadout == null)
			return;
		
		ItemStack[] contents = new ItemStack[36];
		ItemStack[] armor = new ItemStack[4];
		
		for (SimpleItem item : sloadout.inventory)
		{
			CompoundTag ctag = CompoundTag.fromObject(item);
			NBTTagCompound nbttag = ctag.toNBTTag();
			net.minecraft.server.v1_4_6.ItemStack mcstack = net.minecraft.server.v1_4_6.ItemStack.a(nbttag);
			ItemStack stack = CraftItemStack.asBukkitCopy(mcstack);
			
			contents[item.Slot] = stack;
		}
		
		for (SimpleItem item : sloadout.armor)
		{
			CompoundTag ctag = CompoundTag.fromObject(item);
			NBTTagCompound nbttag = ctag.toNBTTag();
			net.minecraft.server.v1_4_6.ItemStack mcstack = net.minecraft.server.v1_4_6.ItemStack.a(nbttag);
			ItemStack stack = CraftItemStack.asBukkitCopy(mcstack);
			
			armor[item.Slot] = stack;
		}
		
		loadout.setContents(contents, armor);
	}
	
	private static SimpleLoadout loadFile(Loadout loadout)
	{
		File file = new File(loadout.fullpath);
		File dir = new File(loadout.directory);
		
		if (!dir.exists())
		{
			dir.mkdir();
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				Manhunt.log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "\"!");
				return null;
			}
		}
		try
		{
			NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
			Tag tag = stream.readTag();
			stream.close();
			
			if (tag.getTagType() != TagType.COMPOUND)
				return null;
			
			SimpleLoadout l = new SimpleLoadout();
			((CompoundTag) tag).toObject(l);
			
			return l;
		}
		catch (IOException e)
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			Manhunt.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static void save(Loadout loadout)
	{
		ItemStack[] contents = loadout.getContents();
		ItemStack[] armor = loadout.getArmorContents();
		SimpleLoadout sloadout = new SimpleLoadout();
		
		for (int i = 0; i < contents.length && i < 36; i++)
		{
			if (contents[i] == null)
				return;
			
			NBTTagCompound nbttag = (CraftItemStack.asNMSCopy(contents[i])).getTag();
			
			if (nbttag == null)
				return;
			
			CompoundTag ctag = CompoundTag.fromNBTTag(nbttag);
			SimpleItem sitem = new SimpleItem();
			ctag.toObject(sitem);
			sitem.Slot = (byte) i;
			
			sloadout.inventory.add(sitem);
		}
		
		for (int i = 0; i < armor.length && i < 4; i++)
		{
			if (contents[i] == null)
				return;

			NBTTagCompound nbttag = (CraftItemStack.asNMSCopy(contents[i])).getTag();

			if (nbttag == null)
				return;
			
			CompoundTag ctag = CompoundTag.fromNBTTag(nbttag);
			SimpleItem sitem = new SimpleItem();
			ctag.toObject(sitem);
			sitem.Slot = (byte) i;
			
			sloadout.inventory.add(sitem);
		}
		
		saveFile(loadout, sloadout);
	}
	
	private static void saveFile(Loadout loadout, SimpleLoadout sloadout)
	{
		File file = new File(loadout.fullpath);
		File dir = new File(loadout.directory);
		
		if (!dir.exists())
		{
			dir.mkdir();
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				Manhunt.log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
				Manhunt.log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try
		{
			NBTOutputStream stream = new NBTOutputStream(new FileOutputStream(file));
			stream.writeTag(CompoundTag.fromObject(sloadout));
			stream.close();
		}
		catch (IOException e)
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			Manhunt.log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public static boolean delete(Loadout loadout)
	{
		File file = new File(loadout.fullpath);
		if (file.exists())
		{
			return file.delete();
		}
		else
		{
			return true;
		}
	}
	
}
