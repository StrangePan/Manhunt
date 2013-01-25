package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.map.*;
import com.deaboy.manhunt.settings.LobbySettings;

public abstract class Lobby implements Closeable
{
	//---------------- Properties ----------------//
	private final long id;
	private String name;
	private final String world_name;
	private final LobbyType type;
	
	private HashMap<String, Team> teams;
	private HashMap<String, World> worlds;
	private String current_map;
	private boolean enabled;
	
	private Game game;
	private LobbySettings settings;
	
	
	
	//---------------- Constructors ----------------//
	public Lobby(long id, String name, LobbyType type, World world)
	{
		this.id = id;
		this.name = name;
		this.world_name = world.getName();
		this.type = type;
		
		this.teams = new HashMap<String, Team>();
		this.worlds = new HashMap<String, World>();
		this.current_map = null;
		this.enabled = true;
		
		// TODO Get class for game, then instantiate it
		this.settings = new LobbySettings(world);
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the long ID of this lobby.
	 * @return This lobby's ID.
	 */
	public long getId()
	{
		return id;
	}
	
	/**
	 * Gets the name of this lobby.
	 * @return This lobby's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets this Lobby's Spawn object.
	 * @return This Lobby's Spawn.
	 */
	public Spawn getSpawn()
	{
		return Manhunt.getWorld(world_name).getSpawn();
	}
	
	/**
	 * Gets this Lobby's main World.
	 * @return This Lobby's World.
	 */
	public World getWorld()
	{
		return Manhunt.getWorld(world_name);
	}
	
	/**
	 * Gets the Manhunt worlds associated with this lobby.
	 * @return
	 */
	public List<World> getWorlds()
	{
		return new ArrayList<World>(worlds.values());
	}
	
	/**
	 * Gets the game of the lobby.
	 * @return
	 */
	public Game getGame()
	{
		return this.game;
	}
	
	/**
	 * Get this Lobby's main spawn point.
	 * @return This Lobby's spawn location
	 */
	public Location getSpawnLocation()
	{
		return Manhunt.getWorld(world_name).getSpawnLocation();
	}
	
	/**
	 * Gets a list of this Lobby's Players.
	 * @return ArrayList of Players in this Lobby
	 */
	public List<Player> getOnlinePlayers()
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String key : teams.keySet())
		{
			p = Bukkit.getPlayerExact(key);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	/**
	 * Gets a list of this Lobby's Player handles,
	 * filtered by team.
	 * If a player is offline, then they will not
	 * be included in the returned List.
	 * @param teams The teams whose members' names
	 * to return.
	 * @return
	 */
	public List<Player> getOnlinePlayers(Team...teams)
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String name : getPlayerNames(teams))
		{
			p = Bukkit.getPlayerExact(name);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	/**
	 * Gets a list of this Lobby's players' names.
	 * @return ArrayList of Strings of players' names in this lobby.
	 */
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(teams.keySet());
	}
	
	/**
	 * Gets a list of this Lobby's players' names,
	 * filtered by teams given in the parameters.
	 * @param teams The teams whose members' names
	 * to return.
	 * @return
	 */
	public List<String> getPlayerNames(Team...teams)
	{
		List<String> names = new ArrayList<String>();
		
		for (String key : this.teams.keySet())
		{
			for (Team team : teams)
			{
				if (this.teams.get(key) == team)
				{
					names.add(key);
					break;
				}
			}
		}
		
		return names;
	}
	
	/**
	 * Gets this lobby's settings.
	 * @return
	 */
	public LobbySettings getSettings()
	{
		return this.settings;
	}
	
	/**
	 * Checks to see if the Lobby is currently enabled.
	 * @return True if the Lobby is enabled, false if not.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Gets the type of this lobby.
	 * @return
	 */
	public LobbyType getType()
	{
		return type;
	}
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets the name of this lobby.
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Adds a player to the Lobby via their name.
	 * @param p The Player to add.
	 */
	public abstract void addPlayer(String name);
	
	/**
	 * Removes a Player from the lobby via their name.
	 * @param p The Player to remove.
	 */
	public void removePlayer(String name)
	{
		if (this.teams.containsKey(name))
			this.teams.remove(name);
	}
	
	/**
	 * Removes a player from the lobby.
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		if (this.teams.containsKey(player.getName()))
			this.teams.remove(player.getName());
	}
	
	public boolean containsPlayer(Player p)
	{
		return containsPlayer(p.getName());
	}
	
	public boolean containsPlayer(String name)
	{
		return this.teams.containsKey(name);
	}
	
	public void setPlayerTeam(Player player, Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		setPlayerTeam(player.getName(), team);
	}
	
	public void setPlayerTeam(String name, Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		if (containsPlayer(name))
			this.teams.put(name, team);
	}
	
	public void setAllPlayerTeams(Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		for (String key : teams.keySet())
			teams.put(key, team);
	}
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Sends a message to all of this Lobby's Players.
	 * @param message The message to broadcast.
	 */
	public void broadcast(String message)
	{
		for (Player p : getOnlinePlayers())
		{
			p.sendMessage(message);
		}
	}
	
	/**
	 * Clears all Players from this Lobby.
	 */
	public void clearPlayers()
	{
		this.teams.clear();
	}
	
	/**
	 * Enables the Lobby.
	 */
	public void enable()
	{
		this.enabled = true;
	}
	
	/**
	 * Disables the Lobby.
	 */
	public void disable()
	{
		this.enabled = false;
	}
	
	
	
	//---------------- Public ABSTRACT Methods ----------------//
	public abstract void bootPlayer(Player player);
	
	public abstract void bootPlayer(String name);
	
	
	
}
