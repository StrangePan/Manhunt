package com.deaboy.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

public class ManhuntPlugin extends JavaPlugin
{
	//---------------- Declarations ----------------//
	
	private static ManhuntPlugin instance;
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
		Manhunt.saveSettings();
		getManhuntInstance().close();
		instance = null;
		manhunt = null;
	}
	
	
	
	//-------- Getters --------//
	public static ManhuntPlugin getInstance()
	{
		return instance;
	}
	
	
	
	//-------- Private Methods --------//
	private static Manhunt getManhuntInstance()
	{
		return manhunt;
	}
	
	

	//-------- Public Interface Methods --------//
	public static String getVersion()
	{
		return getInstance().getDescription().getVersion();
	}
	
	
	
}
