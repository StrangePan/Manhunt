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
public class ManhuntMainLobby implements MainLobby
{
	
	//---------------- Properties ----------------//
	private List<Player> players;
	private Spawn spawn;
	private boolean enabled;
	
	//---------------- Constructors ----------------//
	public ManhuntMainLobby(Location loc)
	{
		this(new ManhuntSpawn(loc));
	}
	
	public ManhuntMainLobby(Spawn spawn)
	{
		this.spawn = spawn;
		
		players = new ArrayList<Player>();
		
		this.enabled = true;
	}
	
	
	//---------------- Public Methods ----------------//
	
	//------------ Getters ------------//
	@Override
	public Spawn getSpawn()
	{
		return spawn;
	}
	
	@Override
	public World getWorld()
	{
		return spawn.getLocation().getWorld();
	}
	
	@Override
	public Location getLocation()
	{
		return spawn.getLocation();
	}
	
	@Override
	public List<Player> getPlayers()
	{
		return players;
	}
	
	@Override
	public boolean isEnabled()
	{
		return enabled;
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
	
	@Override
	public void enable()
	{
		enabled = true;
	}
	
	@Override
	public void disable()
	{
		enabled = false;
	}
	

}
