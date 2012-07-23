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
		List<ItemStack> armour = new ArrayList<ItemStack>();
		
		for (Object object : keySet())
		{
			String slot = (String) object;
			
			Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
			
			try
			{
				String property = getProperty(slot);
				
				String[] parts = property.split(";");
				
				int amount = Integer.parseInt(parts[0]);
				int type = Integer.parseInt(parts[1]);
				short durability = Short.parseShort(parts[2]);
				
				for (String enchantment : parts[3].split(","))
				{
					enchantments.put(Enchantment.getById(Integer.parseInt(enchantment.split("=")[0])), Integer.parseInt(enchantment.split("=")[1]));
				}
				
				ItemStack item = new ItemStack(Material.getMaterial(type));
				item.setAmount(amount);
				item.setDurability(durability);
				item.addEnchantments(enchantments);
				try
				{
					if (slot.startsWith("a"))
					{
						armour.add(Integer.parseInt(slot.substring(1)), item);
					}
					else if (slot.startsWith("c"))
					{
						contents.add(Integer.parseInt(slot.substring(1)), item);
					}
				}
				catch (Exception e) {}
				
			}
			catch (Exception e) {}
			
		}
		
		ItemStack[] c = new ItemStack[contents.size()];
		for (int i = 0; i < c.length ; i++)
		{
			c[i] = contents.get(i);
		}
		ItemStack[] a = new ItemStack[armour.size()];
		for (int i = 0; i < a.length ; i++)
		{
			a[i] = armour.get(i);
		}
		
		this.loadout.setContents(c, a);
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
		ItemStack[] armour = loadout.getArmour();
		
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
					property += "=";
					property += contents[slot].getEnchantments().get(enchantment);
					property += ",";
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
					property += "=";
					property += armour[slot].getEnchantments().get(enchantment);
					property += ",";
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
