package com.bendude56.hunted.loadouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;
import org.jnbt.TagType;

import com.bendude56.hunted.ManhuntPlugin;
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
			net.minecraft.server.ItemStack mcstack = net.minecraft.server.ItemStack.a(nbttag);
			ItemStack stack = new CraftItemStack(mcstack);
			
			contents[item.Slot] = stack;
		}
		
		for (SimpleItem item : sloadout.armor)
		{
			CompoundTag ctag = CompoundTag.fromObject(item);
			NBTTagCompound nbttag = ctag.toNBTTag();
			net.minecraft.server.ItemStack mcstack = net.minecraft.server.ItemStack.a(nbttag);
			ItemStack stack = new CraftItemStack(mcstack);
			
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
				ManhuntPlugin.getInstance().log(Level.SEVERE,
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
			ManhuntPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
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
			
			NBTTagCompound nbttag = (new CraftItemStack(contents[i])).getHandle().getTag();
			
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
			
			NBTTagCompound nbttag = (new CraftItemStack(armor[i])).getHandle().getTag();

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
				ManhuntPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
				ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
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
			ManhuntPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
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
