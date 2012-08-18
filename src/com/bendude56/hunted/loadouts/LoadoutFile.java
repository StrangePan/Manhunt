package com.bendude56.hunted.loadouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.ManhuntPlugin;

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
		
		ItemStack[] contents = new ItemStack[36];
		ItemStack[] armor = new ItemStack[4];
		
		for (Object object : keySet())
		{
			String slot = (String) object;
			
			try
			{
				String property = getProperty(slot);
				
				String[] parts = property.split(";");
				
				int amount = Integer.parseInt(parts[0]);
				int type = Integer.parseInt(parts[1]);
				short durability = Short.parseShort(parts[2]);
				
				Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
				
				if (parts.length > 3)
				{
					for (String enchantment : parts[3].split(":"))
					{
						enchantments.put(Enchantment.getById(Integer.parseInt(enchantment.split(",")[0])), Integer.parseInt(enchantment.split(",")[1]));
					}
				}
				
				ItemStack item = new ItemStack(Material.getMaterial(type));
				item.setAmount(amount);
				item.setDurability(durability);
				item.addEnchantments(enchantments);
				
				try
				{
					if (slot.startsWith("a"))
					{
						armor[Integer.parseInt(slot.substring(1))] =  item;
					}
					else if (slot.startsWith("c"))
					{
						contents[Integer.parseInt(slot.substring(1))] = item;
					}
				}
				catch (Exception e) {}
				
			}
			catch (Exception e) {}
			
		}
		
		this.loadout.setContents(contents, armor);
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
				ManhuntPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "\"!");
				return;
			}
		}
		try {
			FileInputStream stream = new FileInputStream(file);
			load(stream);
			stream.close();
		} catch (IOException e) {
			ManhuntPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public void save()
	{
		ItemStack[] contents = loadout.getContents();
		ItemStack[] armour = loadout.getArmor();
		
		clear();
		for (Integer slot = 0; slot < contents.length; slot++)
		{
			String property = "";
			
			try
			{
				property += contents[slot].getAmount();
				property += ";";
				property += contents[slot].getTypeId();
				property += ";";
				property += contents[slot].getDurability();
				property += ";";
				for (Enchantment enchantment : contents[slot].getEnchantments().keySet())
				{
					property += enchantment.getId();
					property += ",";
					property += contents[slot].getEnchantments().get(enchantment);
					property += ":";
				}
			}
			catch (Exception e) {}
			
			put("c" + slot.toString(), property);
		}
		for (Integer slot = 0; slot < armour.length; slot++)
		{
			String property = "";
			
			try
			{
				property += armour[slot].getAmount();
				property += ";";
				property += armour[slot].getTypeId();
				property += ";";
				property += armour[slot].getDurability();
				property += ";";
				for (Enchantment enchantment : armour[slot].getEnchantments().keySet())
				{
					property += enchantment.getId();
					property += ",";
					property += armour[slot].getEnchantments().get(enchantment);
					property += ":";
				}
			}
			catch (Exception e) {}
			
			put("a" + slot.toString(), property);
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
				ManhuntPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
				ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			FileOutputStream stream = new FileOutputStream(file);
			store(stream, "- Manhunt " + loadout.name + " Loadout -");
			stream.close();
		} catch (IOException e) {
			ManhuntPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.name + "!\"");
			ManhuntPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public boolean delete()
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
