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
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.map.*;
import com.deaboy.manhunt.settings.LobbySettings;

public abstract class Lobby implements Closeable
{
	//---------------- Properties ----------------//
	private final long id;
	private String name;
	private final World world;
	private final LobbyType type;
	
	private HashMap<String, Team> teams;
	private HashMap<String, World> worlds;
	private Map current_map;
	private boolean enabled;
	
	private Game game;
	private LobbySettings settings;
	
	
	
	//---------------- Constructors ----------------//
	public Lobby(long id, String name, LobbyType type, World world)
	{
		this.id = id;
		this.name = name;
		this.world = world;
		this.type = type;
		
		this.teams = new HashMap<String, Team>();
		this.worlds = new HashMap<String, World>();
		this.current_map = null;
		this.enabled = true;
		
		
		this.worlds.put(world.getName(), world);
		
		
		this.settings = new LobbySettings(world);
		
		if (type == LobbyType.GAME)
		{
			this.game = Manhunt.getGameTypeByClassCanonicalName(settings.GAME_TYPE.getValue()).createInstance(this);
			if (game == null)
				game = new ManhuntGame(this);
		}
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
		return world.getSpawn();
	}
	
	/**
	 * Gets this Lobby's main World.
	 * @return This Lobby's World.
	 */
	public World getWorld()
	{
		return world;
	}
	
	/**
	 * Gets the Manhunt worlds associated with this lobby.
	 * @return
	 */
	public List<World> getWorlds()
	{
		return new ArrayList<World>(worlds.values());
	}
	
	public Map getCurrentMap()
	{
		return current_map;
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
		return world.getSpawnLocation();
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
	
	public Team getPlayerTeam(Player p)
	{
		return getPlayerTeam(p.getName());
	}
	
	public Team getPlayerTeam(String name)
	{
		if (teams.containsKey(name))
			return teams.get(name);
		else
			return null;
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
	
	//////// ABSTRACT
	/**
	 * Adds a player to the Lobby via their name.
	 * @param p The Player to add.
	 */
	public abstract boolean addPlayer(String name);
	
	protected boolean addPlayer(String name, Team team)
	{
		if (teams.containsKey(name))
			return false;
		else
			teams.put(name, team);
		return true;
	}
	
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
	
	public boolean gameIsRunning()
	{
		if (game == null)
			return false;
		else
			return game.isRunning();
	}
	
	/**
	 * Sets the current map for the lobby. If the map is
	 * not in a valid world, will return false and not make the assignment.
	 * @param map
	 * @return
	 */
	public boolean setCurrentMap(Map map)
	{
		if (!worlds.containsValue(map.getWorld()))
			return false;
		else if (!map.getWorld().getMaps().contains(map))
			return false;
		else
			current_map = map;
		return true;
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
	
	public void broadcast(String message, Team...teams)
	{
		for (Player p : getOnlinePlayers(teams))
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
	
	public void forfeitPlayer(String name)
	{
		if (!gameIsRunning())
			return;
		if (!containsPlayer(name))
			return;
		
		Player p = Bukkit.getPlayerExact(name);
		
		if (p == null)
		{
			removePlayer(name);
		}
		
		if (getGame() != null)
			getGame().forfeitPlayer(name);
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
	
	@Override
	public void close()
	{
		clearPlayers();
		current_map = null;
	}
	
	
	
	//---------------- Public ABSTRACT Methods ----------------//
	public abstract void distributeTeams();
	
	public abstract void startGame();
	
	public abstract void stopGame();
	
	
	
}
