package com.deaboy.manhunt.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.map.ManhuntSpawn;
import com.deaboy.manhunt.map.Spawn;

/**
 * The main, central lobby for new Manhunt players, simply
 * keeps track of players and the main spawn point.
 * @author Deaboy
 *
 */
public class ManhuntMainLobby extends ManhuntLobby implements MainLobby
{
	
	//---------------- Properties ----------------//
	private List<String> players;
	
	
	//---------------- Constructors ----------------//
	public ManhuntMainLobby(World world)
	{
		this(world.getSpawnLocation());
	}
	
	public ManhuntMainLobby(Location loc)
	{
		this(new ManhuntSpawn(loc));
	}
	
	public ManhuntMainLobby(Spawn spawn)
	{
		super(spawn);
		
		players = new ArrayList<String>();
		
	}
	
	
	//---------------- Public Methods ----------------//
	
	//------------ Getters ------------//
	@Override
	public List<Player> getPlayers()
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String name : this.players)
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
		return players;
	}
	
	
	//------------ Setters ------------//
	@Override
	public void addPlayer(String name)
	{
		if (!players.contains(name))
		{
			players.add(name);
		}
	}
	
	@Override
	public void removePlayer(String name)
	{
		if (players.contains(name))
		{
			players.remove(name);
		}
	}
	
	
	//------------ Public Methods ------------//
	@Override
	public void broadcast(String message)
	{
		for (Player p : getPlayers())
		{
			if (p.isOnline())
				p.sendMessage(message);
		}
	}
	
	@Override
	public void clearPlayers()
	{
		players.clear();
	}
	
	
}
