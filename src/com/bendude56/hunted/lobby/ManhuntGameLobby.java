package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bendude56.hunted.game.Game;
import com.bendude56.hunted.game.ManhuntGame;
import com.bendude56.hunted.map.ManhuntSpawn;
import com.bendude56.hunted.map.Spawn;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.map.World;
import com.bendude56.hunted.settings.LobbySettings;

public class ManhuntGameLobby extends ManhuntLobby implements GameLobby
{
	
	//---------------- Properties ----------------//
	String name;
	
	private HashMap<String, Team> players;
	
	private Map current_map;
	private List<World> worlds;
	private LobbySettings settings;
	
	private Game game;
	
	
	//---------------- Constructors ----------------//
	public ManhuntGameLobby(String name, org.bukkit.World world)
	{
		this(name, world.getSpawnLocation());
	}
	
	public ManhuntGameLobby(String name, Location loc)
	{
		this(name, new ManhuntSpawn(loc));
	}
	
	public ManhuntGameLobby(String name, Spawn spawn)
	{
		super(spawn);
		
		this.name = name;
		
		players = new HashMap<String, Team>();
		
		worlds = new ArrayList<World>();
		settings = new LobbySettings(spawn.getWorld());
		
		this.game = new ManhuntGame(this.getId());
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public List<World> getWorlds()
	{
		return worlds;
	}
	
	@Override
	public Map getCurrentMap()
	{
		return current_map;
	}

	@Override
	public List<Map> getMaps()
	{
		List<Map> maps = new ArrayList<Map>();
		
		for (World world : worlds)
		{
			maps.addAll(world.getMaps());
		}
		
		return maps;
	}

	@Override
	public List<Player> getPlayers()
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String name : this.players.keySet())
		{
			p = Bukkit.getPlayer(name);
			if (p == null)
				players.add(p);
		}
		
		return players;
	}
	
	@Override
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(players.keySet());
	}

	@Override
	public List<Player> getPlayers(Team...teams)
	{
		List<Player> plrs = getPlayers();
		for (Player p : getPlayers())
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

	@Override
	public Team getPlayerTeam(Player p)
	{
		if (players.containsKey(p))
			return players.get(p);
		else
			return null;
	}

	@Override
	public LobbySettings getSettings()
	{
		return settings;
	}
	
	@Override
	public Game getGame()
	{
		return game;
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void addPlayer(String name)
	{
		setPlayerTeam(name, Team.SPECTATORS);
	}

	@Override
	public void addPlayer(String name, Team t)
	{
		if (!players.containsKey(name))
		{
			players.put(name, t);
		}
	}

	@Override
	public void setPlayerTeam(String name, Team t)
	{
		if (players.containsKey(name))
		{
			players.put(name, t);
		}
	}

	@Override
	public void setAllPlayerTeam(Team t)
	{
		for (String name : players.keySet())
		{
			players.put(name, t);
		}
	}

	@Override
	public void removePlayer(String name)
	{
		if (players.containsKey(name))
		{
			players.remove(name);
		}
	}
	
	
	//---------------- Public Methods ----------------//
	@Override
	public void broadcast(String message)
	{
		for (Player p : getPlayers())
		{
			if (p.isOnline())
			{
				p.sendMessage(message);
			}
		}
	}

	@Override
	public void broadcast(String message, Team...teams)
	{
		for (Player p : getPlayers())
		{
			if (p.isOnline())
			{
				for (Team t : teams)
				{
					if (players.get(p.getName()) == t)
						p.sendMessage(message);
				}
			}
		}
	}

	@Override
	public void clearPlayers()
	{
		players.clear();
	}
	
	@Override
	public void startGame()
	{
		List<Map> maps = getMaps();
		
		current_map = maps.get(((int) Math.random()) % maps.size());
	}
	
	@Override
	public void stopGame()
	{
		current_map = null;
	}
	
}
