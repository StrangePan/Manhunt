package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.map.*;
import com.deaboy.manhunt.settings.LobbySettings;

public class Lobby implements Closeable
{
	//---------------- Properties ----------------//
	private final long id;
	private String name;
	private final World world;
	private LobbyType type;
	
	private HashMap<String, Team> teams;
	private List<String> maps;
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
		this.maps = new ArrayList<String>();
		this.current_map = null;
		this.enabled = true;
		
		// TODO Load up maps from file, or import maps from world
		
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
	private Spawn getSpawn()
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
		Map map;
		List<World> worlds = new ArrayList<World>();
		
		for (String mapname : maps)
		{
			map = Manhunt.getMap(mapname);
			if (map != null)
				worlds.add(map.getWorld());
		}
		
		return worlds;
	}
	
	public List<Map> getMaps()
	{
		List<Map> maplist = new ArrayList<Map>();
		Map map;
		
		for (String mapname : maps)
		{
			map = Manhunt.getMap(mapname);
			if (map != null)
				maplist.add(map);
		}
		
		return maplist;
	}
	
	public Map getCurrentMap()
	{
		switch (type)
		{
		case HUB:
			return null;
		case GAME:
			return current_map;
		default:
			return null;
		}
	}
	
	/**
	 * Get this Lobby's main spawn point.
	 * @return This Lobby's spawn location
	 */
	public Location getSpawnLocation()
	{
		return world.getSpawnLocation();
	}
	
	public Location getRandomSpawnLocation()
	{
		return getSpawn().getRandomLocation();
	}
	
	public int getSpawnRange()
	{
		return getSpawn().getRange();
	}
	
	public void setSpawnRange(int range)
	{
		getSpawn().setRange(range);
	}
	
	public void setSpawnLocation(Location loc)
	{
		getSpawn().setLocation(loc);
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
	
	private Game getGame()
	{
		switch (type)
		{
		case HUB:
			return null;
			
		case GAME:
			return this.game;
			
		default:
			return null;
		}
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
	public boolean addPlayer(String name)
	{
		switch (type)
		{
		case HUB:
			return addPlayer(name, Team.NONE);
			
		case GAME:
			if (gameIsRunning())
				return addPlayer(name, Team.SPECTATORS);
			else
				return addPlayer(name, Team.STANDBY);
			
		default:
			return false;
		}
	}
	
	private boolean addPlayer(String name, Team team)
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
		setPlayerTeam(player.getName(), team);
	}
	
	public void setPlayerTeam(String name, Team team)
	{
		switch (type)
		{
		case HUB:
			if (containsPlayer(name))
				this.teams.put(name, Team.NONE);
			return;
			
		case GAME:
			if (team == null)
				throw new IllegalArgumentException("Argument cannot be null");
			if (containsPlayer(name))
				this.teams.put(name, team);
			return;
			
		default:
			return;
		}
	}
	
	public void setAllPlayerTeams(Team team)
	{
		switch (type)
		{
		case HUB:
			for (String key : teams.keySet())
				teams.put(key, Team.NONE);
			
		case GAME:
			if (team == null)
				throw new IllegalArgumentException("Argument cannot be null");
			for (String key : teams.keySet())
				teams.put(key, team);
			return;
			
		default:
			return;
		}
	}
	
	public boolean gameIsRunning()
	{
		switch (type)
		{
		case HUB:
			return false;
			
		case GAME:
			if (game == null)
				return false;
			else
				return game.isRunning();
			
		default:
			return false;
		}
	}
	
	public boolean setCurrentMap(Map map)
	{
		return setCurrentMap(map.getFullName());
	}
	
	/**
	 * Sets the current map for the lobby. If the map is
	 * not in a valid world, will return false and not make the assignment.
	 * @param map
	 * @return
	 */
	public boolean setCurrentMap(String mapname)
	{
		switch (type)
		{
		case HUB:
			return false;
			
		case GAME:
			if (!maps.contains(mapname))
				return false;
			else if (Manhunt.getMap(mapname) == null)
				return false;
			else
				current_map = Manhunt.getMap(mapname);
			return true;
			
		default:
			return false;
		}
	}
	
	public void setLobbyType(LobbyType type)
	{
		if (type == null)
			return;
		if (gameIsRunning())
			return;
		
		switch  (type)
		{
		case HUB:
			setAllPlayerTeams(Team.NONE);
			break;
		case GAME:
			setAllPlayerTeams(Team.STANDBY);
			break;
		default:
			break;
		}
		
		this.type = type;
	}
	
	public boolean addWorld(World world)
	{
		if (world == null)
			return false;
		if (world.getMaps().isEmpty())
			return false;
		boolean success = false;
		
		for (Map map : world.getMaps())
			if (addMap(map))
				success = true;
		
		return success;
	}
	
	public boolean addWorld(String worldname)
	{
		return addWorld(Manhunt.getWorld(worldname));
	}
	
	public boolean addMap(Map map)
	{
		return addMap(map.getFullName());
	}
	
	private boolean addMap(String mapname)
	{
		if (maps.contains(mapname))
			return false;
		else if (Manhunt.getMap(mapname) == null)
			return false;
		else
			maps.add(mapname);
		return true;
	}
	
	public boolean removeMap(Map map)
	{
		return removeMap(map.getFullName());
	}
	
	private boolean removeMap(String mapname)
	{
		if (!maps.contains(mapname))
			return false;
		else
			maps.remove(mapname);
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
	
	public void distributeTeams()
	{
		switch (type)
		{
		case HUB:
			return;
			
		case GAME:
			List<String> hunters = new ArrayList<String>();
			List<String> prey = new ArrayList<String>();
			List<String> standby = getPlayerNames(Team.STANDBY);
			double ratio = getSettings().TEAM_RATIO.getValue();
			
			String name;

			while (standby.size() > 0)
			{
				name = standby.get(new Random().nextInt(standby.size()));

				if (prey.size() == 0 || (double) hunters.size() / (double) prey.size() > ratio)
				{
					prey.add(name);
				}
				else
				{
					hunters.add(name);
				}

				standby.remove(name);
			}

			for (String p : prey)
			{
				setPlayerTeam(p, Team.PREY);
				Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.PREY.getColor() + Team.PREY.getName(true));
			}
			for (String p : hunters)
			{
				setPlayerTeam(p, Team.HUNTERS);
				Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true));
			}
			
		default:
			return;
		}
	}
	
	public Map chooseMap()
	{
		Map map;
		
		if (gameIsRunning())
			return null;
		if (maps.isEmpty())
			return null;
		
		switch (type)
		{
		case HUB:
			return null;
		
		case GAME:
			Collections.sort(maps, new Comparator<String>() {
				public int compare(String s1, String s2) {
					return s1.compareTo(s2);
				}
			} );
			
			map = Manhunt.getMap(maps.get((int) (Math.random()*maps.size())));
			
			setCurrentMap(map);
			return map;
			
		default:
			return null;
		}
	}
	
	public void startGame()
	{
		switch (type)
		{
		case HUB:
			return;
			
		case GAME:
			if (gameIsRunning())
				return;
			
			if (chooseMap() == null)
				return;
			
			getGame().importPlayers(this);
			getGame().distributeTeams();
			getGame().startGame();
			return;
			
		default:
			return;
		}
	}
	
	public void stopGame()
	{
		switch (type)
		{
		case HUB:
			return;
			
		case GAME:
			if (!gameIsRunning())
				return;
			
			getGame().stopGame();
			return;
			
		default:
			return;
		}
	}
	
	@Override
	public void close()
	{
		clearPlayers();
		current_map = null;
	}
	
	
	
}
