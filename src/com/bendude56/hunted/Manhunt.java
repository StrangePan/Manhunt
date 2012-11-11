package com.bendude56.hunted;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.bendude56.hunted.lobby.GameLobby;
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
	
	private HashMap<World, GameLobby> lobbies;
	private ManhuntSettings settings;
	private static Manhunt instance;
	
	
	
	//-------- Constructor --------//
	
	public Manhunt()
	{
		this.lobbies = new HashMap<World, GameLobby>();
		this.settings = new ManhuntSettings(path_settings);
		
		instance = this;
		
		loadLobbiesFromFile();
	}
	
	
	//-------- Public static methods --------//
	
	public static ManhuntSettings getSettings()
	{
		return getInstance().settings;
	}
	
	public static GameLobby getLobby(World w)
	{
		if (getInstance().lobbies.containsKey(w))
		{
			return getInstance().lobbies.get(w);
		}
		else
		{
			return null;
		}
	}
	
	public static void createLobby(World w)
	{
		if (getInstance().lobbies.containsKey(w))
			return;
		
		getInstance().lobbies.put(w, new ManhuntGameLobby(w));
	}
	
	private static Manhunt getInstance()
	{
		return instance;
	}
	
	
	//-------- Private methods --------//
	
	private void loadLobbiesFromFile()
	{
		List<String> worlds = settings.WORLDS.getValue();
		World world;
		File file;
		
		for (String s : worlds)
		{
			world = Bukkit.getWorld(s);
			
			if (Bukkit.getWorld(s) == null)
			{
				file = new File(s);
				if (file.exists() && file.isDirectory())
				{
					world = Bukkit.createWorld(WorldCreator.name(s));
				}
				else
				{
					continue;
				}
			}
			
			this.lobbies.put(world, new ManhuntGameLobby(world));
		}
	}
	
	
	private GameLobby openLobby(World world)
	{
		if (lobbies.containsKey(world) || world == null)
			return null;
		
		GameLobby lobby = new ManhuntGameLobby(world);
		
		lobbies.put(world, lobby);
		return lobby;
	}
	
	
	private void closeLobby(World world)
	{
		if (lobbies.containsKey(world) || world == null)
			return;
		
		getLobby(world).close();
		
		lobbies.remove(world);
	}
	
	
	//-------- Public Interface Methods --------//
	
	
	public static GameLobby addLobby(World world)
	{
		GameLobby lobby = getInstance().openLobby(world);
		
		if (lobby != null)
		{
			if (!getSettings().WORLDS.getValue().contains(world.getName()))
			{
				getSettings().WORLDS.add(world.getName());
			}
		}
		
		return lobby;
	}
	
	
	public static void removeLobby(World world)
	{
		if (getSettings().WORLDS.getValue().contains(world.getName()))
		{
			getSettings().WORLDS.remove(world.getName());
		}
		
		getInstance().closeLobby(world);
	}
	
	
	
	
}
