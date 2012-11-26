package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.map.ManhuntSpawn;
import com.bendude56.hunted.map.Spawn;

/**
 * The main, central lobby for new Manhunt players, simply
 * keeps track of players and the main spawn point.
 * @author Deaboy
 *
 */
public class ManhuntMainLobby extends ManhuntLobby implements MainLobby
{
	
	//---------------- Properties ----------------//
	private List<Player> players;
	
	
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
		
		players = new ArrayList<Player>();
		
	}
	
	
	//---------------- Public Methods ----------------//
	
	//------------ Getters ------------//
	@Override
	public List<Player> getPlayers()
	{
		return players;
	}
	
	
	//------------ Setters ------------//
	@Override
	public void addPlayer(Player p)
	{
		if (!players.contains(p))
		{
			players.add(p);
		}
	}
	
	@Override
	public void removePlayer(Player p)
	{
		if (players.contains(p))
		{
			players.remove(p);
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
