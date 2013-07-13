package com.deaboy.manhunt.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.SettingManager;

public abstract class GameLobby extends Lobby
{
	//////////////// PROPERTIES ////////////////
	private Game game;
	private List<String> maps;
	private Map current_map;
	private HashMap<String, Team> teams;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public GameLobby(long id, String name, World world, Location loc)
	{
		super(id, name, LobbyType.GAME, world, loc);
		this.game = null;
		this.maps = new ArrayList<String>();
		this.current_map = null;
		this.teams = new HashMap<String, Team>();
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- INTERFACE ----------------//
	public abstract boolean forfeitPlayer(String name);
	public abstract boolean playerChangeTeam(Player p, Team team);
	
	
	//---------------- PLAYERS ----------------//
	protected boolean addPlayer(Player player, Team team)
	{
		if (player != null)
		{
			return addPlayer(player.getName(), team);
		}
		else
		{
			return false;
		}
	}
	protected boolean addPlayer(String name, Team team)
	{
		if (team != null && !teams.containsKey(name))
		{
			this.teams.put(name, team);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	protected boolean addPlayer(Player player)
	{
		if (player != null)
		{
			return addPlayer(player.getName());
		}
		else
		{
			return false;
		}
	}
	@Override
	protected boolean addPlayer(String name)
	{
		return addPlayer(name, Team.STANDBY);
	}
	@Override
	public boolean containsPlayer(Player player)
	{
		if (player != null)
		{
			return containsPlayer(player.getName());
		}
		else
		{
			return false;
		}
	}
	@Override
	public boolean containsPlayer(String name)
	{
		return this.teams.containsKey(name);
	}
	@Override
	protected boolean removePlayer(Player player)
	{
		if (player != null)
		{
			return removePlayer(player.getName());
		}
		else
		{
			return false;
		}
	}
	@Override
	protected boolean removePlayer(String name)
	{
		if (this.teams.containsKey(name))
		{
			this.teams.remove(name);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	protected void clearPlayers()
	{
		this.teams.clear();
	}
	protected void clearOfflinePlayers()
	{
		for (String name : getOfflinePlayerNames())
		{
			this.teams.remove(name);
		}
	}
	
	@Override
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(this.teams.keySet());
	}
	public List<String> getPlayerNames(Team...teams)
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.teams.keySet())
		{
			if (arrContains(this.teams.get(name), teams))
				names.add(name);
		}
		return names;
	}
	@Override
	public List<String> getOnlinePlayerNames()
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) != null)
				names.add(name);
		}
		return names;
	}
	public List<String> getOnlinePlayerNames(Team...teams)
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) != null && arrContains(this.teams.get(name), teams))
				names.add(name);
		}
		return names;
	}
	@Override
	public List<String> getOfflinePlayerNames()
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) == null)
				names.add(name);
		}
		return names;
	}
	public List<String> getOfflinePlayerNames(Team...teams)
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) == null && arrContains(this.teams.get(name), teams))
				names.add(name);
		}
		return names;
	}
	@Override
	public List<Player> getOnlinePlayers()
	{
		List<Player> players = new ArrayList<Player>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) != null)
				players.add(Bukkit.getPlayerExact(name));
		}
		return players;
	}
	public List<Player> getOnlinePlayers(Team...teams)
	{
		List<Player> players = new ArrayList<Player>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) != null && arrContains(this.teams.get(name), teams))
				players.add(Bukkit.getPlayerExact(name));
		}
		return players;
	}
	@Override
	public List<OfflinePlayer> getOfflinePlayers()
	{
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) == null)
				players.add(Bukkit.getOfflinePlayer(name));
		}
		return players;
	}
	public List<OfflinePlayer> getOfflinePlayers(Team...teams)
	{
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (String name : this.teams.keySet())
		{
			if (Bukkit.getPlayerExact(name) == null && arrContains(this.teams.get(name), teams))
				players.add(Bukkit.getOfflinePlayer(name));
		}
		return players;
	}
	
	
	@Override
	public void broadcast(String message)
	{
		Player player;
		for (String name : this.teams.keySet())
		{
			player = Bukkit.getPlayerExact(name);
			if (player != null)
				player.sendMessage(message);
		}
	}
	public void broadcast(String message, Team...teams)
	{
		Player player;
		for (String name : this.teams.keySet())
		{
			if (arrContains(this.teams.get(name), teams))
			{
				player = Bukkit.getPlayerExact(name);
				if (player != null)
				{
					player.sendMessage(message);
					break;
				}
			}
		}
	}
	
	private boolean arrContains(Object o, Object[] a)
	{
		for (Object b : a)
			if (o == b)
				return true;
		return false;
	}
	
	
	//---------------- TEAMS -----------------//
	protected void setPlayerTeam(Player player, Team team)
	{
		if (player != null)
			setPlayerTeam(player.getName(), team);
	}
	protected void setPlayerTeam(String name, Team team)
	{
		if (team != null && this.teams.containsKey(name))
			this.teams.put(name, team);
	}
	protected void setAllPlayerTeams(Team team)
	{
		if (team != null)
		{
			for (String name : this.teams.keySet())
			{
				this.teams.put(name, team);
			}
		}
	}
	protected void distributeTeams()
	{
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
			changePlayerTeam(p, Team.PREY);
		}
		for (String p : hunters)
		{
			changePlayerTeam(p, Team.HUNTERS);
		}
	}
	
	
	//---------------- GAMES ----------------//
	protected Game getGame()
	{
		return this.game;
	}
	public boolean gameIsRunning()
	{
		if (this.game != null)
		{
			return this.game.isRunning();
		}
		else
		{
			return false;
		}
	}
	public abstract boolean startGame();
	public abstract boolean endGame();
	public abstract boolean cancelGame();
	
	
	//---------------- MAPS ----------------//
	protected boolean addMap(Map map)
	{
		if (map != null && !this.maps.contains(map.getName()))
		{
			this.maps.add(map.getFullName());
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean containsMap(Map map)
	{
		if (map != null)
		{
			return containsMap(map.getName());
		}
		else
		{
			return false;
		}
	}
	public boolean containsMap(String name)
	{
		return this.maps.contains(name);
	}
	protected boolean removeMap(Map map)
	{
		if (map != null)
		{
			return removeMap(map.getName());
		}
		else
		{
			return false;
		}
	}
	protected boolean removeMap(String name)
	{
		if (this.maps.contains(name))
		{
			this.maps.remove(name);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected boolean setCurrentMap(String fullmapname)
	{
		if (this.maps.contains(fullmapname))
		{
			this.current_map = Manhunt.getMap(fullmapname);
			return true;
		}
		else
		{
			return false;
		}
	}
	public String chooseMap()
	{
		String map;
		
		if (gameIsRunning())
			return null;
		if (maps.isEmpty())
			return null;
		
		Collections.sort(maps, new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		} );
		
		map = maps.get((int) (Math.random()*maps.size()));
		setCurrentMap(map);
		return map;
	}
	public Map getCurrentMap()
	{
		return current_map;
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
		
		if (!worlds.contains(getWorld()))
			worlds.add(getWorld());
		
		return worlds;
	}
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public abstract SettingManager getSettings();
	@Override
	protected abstract void initializeSettings();
	
	
	//---------------- FILES ----------------//
	@Override
	public abstract int saveFiles();
	@Override
	public abstract int loadFiles();
	@Override
	public abstract int deleteFiles();
	@Override
	public abstract Lobby loadFromFile(File file);
	
}
