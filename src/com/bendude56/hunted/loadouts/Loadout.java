package com.bendude56.hunted.loadouts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Loadout
{
	private ItemStack[] contents;
	private ItemStack[] armor;
	
	private List<PotionEffect> effects;

	private String name;
	private final String filename;
	
	
	//---------------- Constructors ----------------//
	public Loadout(String name, String filename)
	{
		this(name, filename, new ItemStack[36], new ItemStack[4], new ArrayList<PotionEffect>());
	}

	public Loadout(String name, String filename, ItemStack[] contents, ItemStack[] armor)
	{
		this(name, filename, contents, armor, new ArrayList<PotionEffect>());
	}

	public Loadout(String name, String filename, ItemStack[] contents, ItemStack[] armor, List<PotionEffect> effects)
	{
		this.name = null;
		this.filename = filename;
		
		setContents(contents, armor);
		
		this.effects = effects;
	}

	
	
	//---------------- Setters ----------------//
	public void setName(String name)
	{
		this.name = name;
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
	}
	
	/**
	 * Adds an initial potion effect to the given loadout. If an effect
	 * of the given type already exists, will remove the old one, overwriting
	 * it with the new effect.
	 * @param type The type of effect
	 * @param duration How long (in ticks) the effect will last
	 * @param amplifier The strength of the effect
	 * @param ambient Whether the effect will create more, transluscent, particles or not.
	 */
	public void addPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient)
	{
		for (PotionEffect effect : effects)
		{
			if (effect.getType() == type)
			{
				effects.remove(effect);
				break;
			}
		}
		
		effects.add(new PotionEffect(type, duration, amplifier, ambient));
	}
	
	/**
	 * Clears the loadout's current effects and replaces them
	 * with a new set.
	 * @param effects
	 */
	public void setPotionEffects(List<PotionEffect> effects)
	{
		this.effects.clear();
		this.effects.addAll(effects);
	}
	
	/**
	 * Removes a potion effect from this loadout based on the effect type.
	 * @param type The type of effect to remove from the loadout.
	 */
	public void removePotionEffect(PotionEffectType type)
	{
		for (PotionEffect effect : effects)
		{
			if (effect.getType() == type)
			{
				effects.remove(effect);
				break;
			}
		}
	}
	
	/**
	 * Removes all potion effects from the loadout.
	 */
	public void clearPotionEffects()
	{
		this.effects.clear();
	}
	
	
	
	//---------------- Getters ----------------//
	public String getName()
	{
		return name;
	}
	
	public String getFilename()
	{
		return filename;
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
	
	public List<PotionEffect> getEffects()
	{
		return effects;
	}
	
	
	
	//---------------- Public Methods ----------------//
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
	
	
	
	//---------------- Public Static Methods ----------------//
	/**
	 * Creates a new loadout from the given file. The filename must be
	 * the plain filename of the loadout without the path. Extension is optional. 
	 * @param filename
	 * @return
	 */
	public static Loadout fromFile(String filename)
	{
		int end = filename.lastIndexOf('.');
		if (end == -1)
			end = filename.length();
		
		Loadout loadout = new Loadout(filename.substring(filename.lastIndexOf('/')+1, end), "");
		loadout.load();
		return loadout;
	}
	
}
