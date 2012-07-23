package com.bendude56.hunted.loadout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.HuntedPlugin;

public class LoadoutFile extends Properties {

	private static final long serialVersionUID = 1007071207737225723L;

	private Loadout loadout;
	
	public LoadoutFile(Loadout loadout)
	{
		this.loadout = loadout;
	}
	
	public void load()
	{
		loadFile();
		
		List<ItemStack> contents = new ArrayList<ItemStack>();
		
		for (Object object : keySet())
		{
			try
			{
				String slot = (String) object;
				String property = getProperty(slot);
				
				String[] parts = property.split(";");
				
				int amount = Integer.parseInt(parts[0]);
				int type = Integer.parseInt(parts[1]);
				short durability = Short.parseShort(parts[2]);
				
				Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
				for (String enchantment : parts[3].split(","))
				{
					enchantments.put(Enchantment.getById(Integer.parseInt(enchantment.split("=")[0])), Integer.parseInt(enchantment.split("=")[1]));
				}
				
				ItemStack item = new ItemStack(Material.getMaterial(type));
				item.setAmount(amount);
				item.setDurability(durability);
				item.addEnchantments(enchantments);
				
				contents.add(Integer.parseInt(slot), item);
			}
			catch (Exception e) {}
		}
		
		this.loadout.setContents((ItemStack[]) contents.toArray());
	}
	
	private void loadFile()
	{
		File file = new File(loadout.fullpath);
		File dir = new File(loadout.directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "\"!");
				return;
			}
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public void save()
	{
		ItemStack[] contents = loadout.getContents();
		
		clear();
		for (Integer slot = 0; slot < contents.length; slot++)
		{
			String property = "";
			
			property += contents[slot].getAmount();
			property += ";";
			property += contents[slot].getTypeId();
			property += ";";
			property += contents[slot].getDurability();
			property += ";";
			for (Enchantment enchantment : contents[slot].getEnchantments().keySet())
			{
				property += enchantment.getId();
				property += "=";
				property += contents[slot].getEnchantments().get(enchantment);
				property += ",";
			}
			
			put(slot.toString(), property);
		}
		
		saveFile();
	}
	
	private void saveFile()
	{
		File file = new File(loadout.fullpath);
		File dir = new File(loadout.directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file),
					"- Manhunt " + loadout.name + " Loadout -");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public boolean delete()
	{
		File file = new File(loadout.fullpath);
		if (file.exists())
		{
			file.delete();
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
