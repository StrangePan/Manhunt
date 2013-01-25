package com.deaboy.manhunt.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.LobbySettings;

public class GameLobby extends Lobby
{
	//---------------- Properties ----------------//
	private HashMap<String, Team> teams;
	private HashMap<String, World> worlds;
	private String world;
	private String map;
	private Game game;
	private LobbySettings settings;
	
	
	
	//---------------- Constructors ----------------//
	public GameLobby(long id, String name, World world)
	{
		super(id, name, LobbyType.GAME, world);
		
		this.teams = new HashMap<String, Team>();
		this.worlds = new HashMap<String, World>();
		this.settings = new LobbySettings(world);
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the a list of Worlds this Lobby may spawn. Each
	 * lobby may spawn multiple maps, so this method returns
	 * what worlds it spawns.
	 * @return ArrayList of Worlds in this Lobby
	 */
	public List<World> getWorlds()
	{
		return new ArrayList<World>(this.worlds.values());
	}
	
	/**
	 * Gets the current map of this lobby.
	 * @return
	 */
	public Map getCurrentMap()
	{
		return worlds.get(world).getMap(map);
	}
	
	/**
	 * Gets the maps currently loaded by the Lobby
	 * @return ArrayList of Maps in this Lobby.
	 */
	public List<Map> getMaps()
	{
		List<Map> maps = new ArrayList<Map>();
		for (World world : getWorlds())
			maps.addAll(world.getMaps());
		return maps;
	}
	
	@Override
	public List<Player> getOnlinePlayers()
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String key : teams.keySet())
		{
			p = Bukkit.getPlayer(key);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	/**
	 * Gets a list of Players on a given team.
	 * @param teams The Team(s) to get.
	 * @return ArrayList of Players on the given team(s).
	 */
	public List<Player> getOnlinePlayers(Team...teams)
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String name : getPlayerNames(teams))
		{
			p = Bukkit.getPlayer(name);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	@Override
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(this.teams.keySet());
	}
	
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
	 * Gets the Team of the given Player.
	 * @param p The Player to get the Team of.
	 * @return The Team of the given Player.
	 */
	public Team getPlayerTeam(Player p)
	{
		return getPlayerTeam(p.getName());
	}
	
	public Team getPlayerTeam(String name)
	{
		return teams.get(name);
	}
	
	/**
	 * Gets the WorldSettings for this Lobby.
	 * @return The WorldSettings of this Lobby.
	 */
	public LobbySettings getSettings()
	{
		return settings;
	}
	
	/**
	 * Gets the game of this lobby. This includes access to various
	 * things, such as the game stage.
	 * @return
	 */
	public Game getGame()
	{
		return game;
	}
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Adds a player to the Lobby and to the given team.
	 * @param name The name of the player to add.
	 * @param t The Team to put the Player on
	 */
	@Override
	public void addPlayer(String name)
	{
		if (!teams.containsKey(name))
			teams.put(name, Team.SPECTATORS);
	}
	
	@Override
	public void removePlayer(String name)
	{
		if (teams.containsKey(name))
			teams.remove(name);
	}
	
	/**
	 * Changes the given Player's Team.
	 * @param name The name of the player to change teams.
	 * @param t The Team to put the Player in.
	 */
	public boolean setPlayerTeam(String name, Team t)
	{
		if (teams.containsKey(name))
			teams.put(name, t);
		else
			return false;
		return true;
	}
	
	/**
	 * Sets the team of all players in the lobby.
	 * @param t The new Team for all players.
	 */
	public void setAllPlayerTeam(Team t)
	{
		for (String key : teams.keySet())
			teams.put(key, t);
	}
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Broadcasts a message to all players on a given team.
	 * @param message The message to broadcast.
	 * @param teams The team(s) to broadcast to.
	 */
	public void broadcast(String message, Team ... teams)
	{
		for (Player p : getOnlinePlayers(teams))
			p.sendMessage(message);
	}
	
	/**
	 * Starts the game for this lobby.
	 */
	public void startGame()
	{
		// TODO To be determined
	}
	
	/**
	 * Stops the Manhunt game
	 */
	public void stopGame()
	{
		// TODO To be determined
	}
	
	public void clearPlayers()
	{
		this.teams.clear();
	}
	
	public void close()
	{}
	
}
