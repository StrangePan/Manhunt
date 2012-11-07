package com.bendude56.hunted;

import java.util.HashMap;

import org.bukkit.World;

import com.bendude56.hunted.lobby.Lobby;
import com.bendude56.hunted.lobby.ManhuntLobby;
import com.bendude56.hunted.settings.ManhuntSettings;

public class Manhunt
{
	
	//-------- Constants --------//
	
	public static final String extension_properties = ".properties";
	public static final String extension_loadout = ".dat";
	
	public static final String filename_settings = "config"; 
	
	public static final String dirname_plugins = "plugins";
	public static final String dirname_main = "Manhunt";
	public static final String dirname_loadouts = "loadouts";
	public static final String dirname_world = "Manhunt";
	
	public static final String path_maindir = dirname_plugins + "/" + dirname_main;
	public static final String path_settings = path_maindir + "/" + filename_settings + extension_properties;
	public static final String path_loadouts = path_maindir + "/" + dirname_loadouts;
	
	
	//-------- Local variables ---------//
	
	private HashMap<World, Lobby> lobbies;
	private ManhuntSettings settings;
	private static Manhunt instance;
	
	
	
	//-------- Methods --------//
	
	public Manhunt()
	{
		this.lobbies = new HashMap<World, Lobby>();
		this.settings = new ManhuntSettings(path_settings);
		
		instance = this;
	}
	
	public static ManhuntSettings getSettings()
	{
		return instance.settings;
	}
	
	public static Lobby getLobby(World w)
	{
		if (instance.lobbies.containsKey(w))
		{
			return instance.lobbies.get(w);
		}
		else
		{
			return null;
		}
	}
	
	public static void createLobby(World w)
	{
		if (instance.lobbies.containsKey(w))
			return;
		
		instance.lobbies.put(w, new ManhuntLobby(w));
	}
	
}
