package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.map.ManhuntSpawn;
import com.bendude56.hunted.map.Spawn;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.settings.WorldSettings;

public class ManhuntGameLobby implements GameLobby
{
	
	//---------------- Properties ----------------//
	String name;
	
	private Spawn spawn;
	private HashMap<Player, Team> players;
	private boolean enabled;
	
	private List<Map> maps;
	private WorldSettings settings;
	
	
	//---------------- Constructors ----------------//
	public ManhuntGameLobby(String name, World world)
	{
		this(name, world.getSpawnLocation());
	}
	
	public ManhuntGameLobby(String name, Location loc)
	{
		this.name = name;
		
		spawn = new ManhuntSpawn(loc);
		players = new HashMap<Player, Team>();
		enabled = true;
		
		maps = new ArrayList<Map>();
		settings = new WorldSettings(loc.getWorld());
	}
	
	
	//---------------- Public Methods ----------------//
	
	//------------ Getters ------------//
	public String getName()
	{
		return name;
	}
	
	public Spawn getSpawn()
	{
		return spawn;
	}
	
	public World getWorld()
	{
		return spawn.getWorld();
	}
	
	public List<World> getWorlds()
	{
		List<World> worlds = new ArrayList<World>();
		for (Map map : getMaps())
		{
			if (!worlds.contains(map.getWorld()))
				worlds.add(map.getWorld());
		}
		return worlds;
	}
	
	public List<Map> getMaps()
	{
		return maps;
	}
	
	public Location getLocation()
	{
		return spawn.getLocation();
	}
	
	public List<Player> getPlayers()
	{
		return new ArrayList<Player>(players.keySet());
	}
	
	public List<Player> getPlayers(Team...teams)
	{
		List<Player> plrs = new ArrayList<Player>();
		for (Player p : players.keySet())
		{
			for (Team t : teams)
			{
				if (players.get(p) == t)
				{
					plrs.add(p);
					break;
				}
			}
		}
		return plrs;
	}
	
	public Team getPlayerTeam(Player p)
	{
		if (players.containsKey(p))
			return players.get(p);
		else
			return null;
	}
	
	public WorldSettings getSettings()
	{
		return settings;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	//------------ Setters ------------//
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void addPlayer(Player p)
	{
		// TODO Finish
	}
	
	public void addPlayer(Player p, Team t)
	{
		if (!players.containsKey(p))
		{
			players.put(p, t);
		}
	}
	
	public void setPlayerTeam(Player p, Team t)
	{
		if (players.containsKey(p))
		{
			players.put(p, t);
		}
	}
	
	public void setAllPlayerTeam(Team t)
	{
		for (Player p : players.keySet())
		{
			players.put(p, t);
		}
	}
	
	public void removePlayer(Player p)
	{
		if (players.containsKey(p))
		{
			players.remove(p);
		}
	}
	
	
	//------------ Other ------------//
	public void broadcast(String message)
	{
		for (Player p : players.keySet())
		{
			if (p.isOnline())
			{
				p.sendMessage(message);
			}
		}
	}
	
	public void broadcast(String message, Team...teams)
	{
		for (Player p : players.keySet())
		{
			if (p.isOnline())
			{
				for (Team t : teams)
				{
					if (players.get(p) == t)
						p.sendMessage(message);
				}
			}
		}
	}
	
	public void clearPlayers()
	{
		players.clear();
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void disable()
	{
		enabled = false;
	}
	
}
