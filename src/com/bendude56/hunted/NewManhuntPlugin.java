package com.bendude56.hunted;

import org.bukkit.plugin.java.JavaPlugin;

public class NewManhuntPlugin extends JavaPlugin
{
	//---------------- Declarations ----------------//
	
	private static NewManhuntPlugin instance;
	private static Manhunt manhunt;
	
	
	
	//-------- Required methods --------//
	@Override
	public void onEnable()
	{
		instance = this;
		manhunt = new Manhunt();
	}
	
	@Override
	public void onDisable()
	{
		instance = null;
		manhunt = null;
	}
	
	
	
	//-------- Getters --------//
	public static NewManhuntPlugin getInstance()
	{
		return instance;
	}
	
	
	
	//-------- Private Methods --------//
	@SuppressWarnings("unused")
	private static Manhunt getManhuntInstance()
	{
		return manhunt;
	}
	
	

	//-------- Public Interface Methods --------//
	
	
	
	
}
