package com.bendude56.hunted;

import java.util.logging.Level;

import org.bukkit.Bukkit;
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
	private static Manhunt getManhuntInstance()
	{
		return manhunt;
	}
	
	

	//-------- Public Interface Methods --------//
	public static void log(String message)
	{
		Bukkit.getLogger().log(Level.INFO, "[Manhunt]  " + message);
	}
}
