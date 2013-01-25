package com.deaboy.hunted.loadouts;

import org.bukkit.inventory.ItemStack;

/**
 * Class dedicated to giving item stacks a random chance of appearing.
 * @author Deaboy
 *
 */
public class RandomStack
{
	//---------------- Constants ----------------//
	private static final double DEFAULT_CHANCE = 0.5; 
	
	//---------------- Properties ----------------//
	private double chance;
	private ItemStack item;
	
	
	
	//---------------- Constructors ----------------//
	public RandomStack(ItemStack item)
	{
		this(item, DEFAULT_CHANCE);
	}
	
	public RandomStack(ItemStack item, double chance)
	{
		this.item = item.clone();
		this.chance = chance;
	}
	
	
	
	//---------------- Getters ----------------//
	public ItemStack getItemStack()
	{
		return item.clone();
	}
	
	public double getChance()
	{
		return chance;
	}
	
	
	
	//---------------- Setters ----------------//
	public void setChance(double chance)
	{
		this.chance = chance;
	}
	
	
	
	//---------------- Public Methods ----------------//
	public boolean tryChance()
	{
		if (Math.random() < chance)
			return true;
		else
			return false;
	}
	
	
	
	
	
}
