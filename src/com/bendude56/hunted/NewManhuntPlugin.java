package com.bendude56.hunted;

import org.bukkit.plugin.java.JavaPlugin;

public class NewManhuntPlugin extends JavaPlugin
{
	//---------------- Declarations ----------------//
	
	private static NewManhuntPlugin instance;
	private static Manhunt manhunt;
	
	
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
	}
	
	public static NewManhuntPlugin getInstance()
	{
		return instance;
	}
	
	public static Manhunt getManhuntInstance()
	{
		return manhunt;
	}
}
