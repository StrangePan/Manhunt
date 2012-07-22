package com.bendude56.hunted.config;

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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.HuntedPlugin;

public class LoadoutFile extends Properties {

	private static final long serialVersionUID = 6896429730869946565L;
	private HashMap<Integer, ItemStack> loadout = new HashMap<Integer, ItemStack>();
	
	public final String label;
	private final String filename;
	private final String directory;
	
	public LoadoutFile(String name, String directory){
		this.label = name;
		this.directory = directory;
		this.filename = name + ".inv";
		
		load();
	}
	
	public LoadoutFile(String name, String directory, Inventory inv){
		this.label = name;
		this.directory = directory;
		this.filename = name + ".inv";

		setLoadout(inv);
	}

	public void setLoadout(Inventory inv)
	{
		loadout.clear();
		
		int slot = 103;
		while (slot >= 0)
		{
			try
			{
				loadout.put(slot, inv.getItem(slot));
			}
			catch (Exception e){}
			slot--;
		}
		
		save();
	}

	@SuppressWarnings("unchecked")
	public void setLoadout(HashMap<Integer, ItemStack> inv)
	{
		try
		{
			this.loadout = (HashMap<Integer, ItemStack>) inv.clone();
		}
		catch (Exception e)
		{
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, ItemStack> getHashmap()
	{
		try
		{
			return (HashMap<Integer, ItemStack>) loadout.clone();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void fillInventory(Inventory inv)
	{
		inv.clear();
		
		for (Integer slot : loadout.keySet())
		{
			inv.setItem(slot, loadout.get(slot));
		}
	}

	private void loadFile() {
		File file = new File(directory + "/" + filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + label + "\"!");
				return;
			}
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + label + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
		parseItemStack();
		save();
	}
	
	private void saveFile() {
		File file = new File(directory + "/" + filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + label + "!\"");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file),
					"- Manhunt " + label + " Loadout -");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + label + "!\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
	}

	public void save()
	{
		this.clear();

		for (Integer slot : loadout.keySet())
		{
			this.put(slot.toString(), convertToString(loadout.get(slot)));
		}
		
		saveFile();
	}
	
	public void load()
	{
		loadFile();
		
		this.loadout = parseItemStack();
		
		save();
	}

	public String convertToString(ItemStack stack)
	{
		String string = "";
		
		if (stack != null)
		{
			string += stack.getAmount();
			string += ";";
			string += stack.getTypeId();
			string += ";";
			string += stack.getDurability();
			string += ";";
			for (Enchantment enchantment : stack.getEnchantments().keySet())
			{
				string += enchantment.getId();
				string += "=";
				string += stack.getEnchantments().get(enchantment);
				string += ",";
			}
		}
		
		return string;
	}

	public HashMap<Integer, ItemStack> parseItemStack()
	{
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		
		Integer slot = 103;
		while (slot >= 0)
		{
			String string = getProperty(slot.toString());
			if (string == null)
			{
				items.put(slot, null);
			}
			else
			{
				try
				{
					String[] parts = string.split(";");
				
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
					
					items.put(slot, item);
				}
				catch (Exception e)
				{
					items.put(slot, null);
				}
			}
			slot--;
		}
		return items;
	}

	public void delete() {
		File file = new File(directory + "/" + filename);
		
		if (file.exists())
			file.delete();
	}

}
