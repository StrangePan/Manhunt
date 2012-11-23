package com.bendude56.hunted;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.MainLobby;
import com.bendude56.hunted.lobby.ManhuntGameLobby;
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
	
	private MainLobby mainlobby;
	private HashMap<String, GameLobby> lobbies;
	private ManhuntSettings settings;
	private static Manhunt instance;
	
	
	
	//-------- Constructor --------//
	
	public Manhunt()
	{
		this.lobbies = new HashMap<String, GameLobby>();
		this.settings = new ManhuntSettings(path_settings);
		
		instance = this;
		
		loadLobbiesFromFile();
	}
	
	
	//-------- Public static methods --------//
	public static ManhuntSettings getSettings()
	{
		return getInstance().settings;
	}
	
	public static GameLobby getLobby(String label)
	{
		if (getInstance().lobbies.containsKey(label))
		{
			return getInstance().lobbies.get(label);
		}
		else
		{
			return null;
		}
		
	}
	
	public static GameLobby getLobby(World world)
	{
		for (GameLobby lobby : getInstance().lobbies.values())
		{
			if (lobby.getWorlds().contains(world))
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static void createLobby(String label, World w)
	{
		if (getInstance().lobbies.containsKey(label))
			return;
		
		getInstance().lobbies.put(label, new ManhuntGameLobby(w));
	}
	
	private static Manhunt getInstance()
	{
		return instance;
	}
	
	
	//-------- Private methods --------//
	
	private void loadLobbiesFromFile()
	{
		// TODO Finish this method
	}
	
	
	private GameLobby openLobby(World world)
	{
		// TODO Finish this method
		return null;
	}
	
	
	private void closeLobby(World world)
	{
		// TODO Finish this method
	}
	
	
	//-------- Public Interface Methods --------//
	
	
	public static GameLobby addLobby(World world)
	{
		// TODO Finish this method
		return null;
	}
	
	
	public static void removeLobby(World world)
	{
		// TODO Finish this method
	}
	
	
	
	
}
