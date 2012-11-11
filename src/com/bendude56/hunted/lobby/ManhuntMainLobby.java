package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ManhuntMainLobby implements Lobby
{
	//-------- Properties --------//
	
	private List<Player> players;
	private Location spawnpoint;
	
	
	
	//-------- Constructors --------//
	
	public ManhuntMainLobby()
	{
		this.players = new ArrayList<Player>();
	}
	
	
	
	//-------- Public Methods --------//
	
	@Override
	public void addPlayer(Player p)
	{
		if (!this.players.contains(p))
		{
			this.players.add(p);
		}
	}

	@Override
	public void removePlayer(Player p)
	{
		if (this.players.contains(p))
		{
			this.players.remove(p);
		}
	}

	@Override
	public List<Player> getPlayers()
	{
		return this.players;
	}

	@Override
	public World getWorld()
	{
		return this.spawnpoint.getWorld();
	}

	@Override
	public Location getLocation()
	{
		return this.spawnpoint.clone();
	}

	@Override
	public void setLocation(Location location)
	{
		this.spawnpoint = location.clone();
	}

	@Override
	public void messagePlayers(String message)
	{
		for (Player p : this.players)
		{
			p.sendMessage(message);
		}
	}

	@Override
	public void clearPlayers()
	{
		this.players.clear();
	}

	@Override
	public void close()
	{
		clearPlayers();
	}

}
