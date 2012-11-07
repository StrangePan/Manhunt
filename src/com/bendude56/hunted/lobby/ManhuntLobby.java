package com.bendude56.hunted.lobby;

import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ManhuntLobby implements Lobby
{

	private final World world;
	private final String name;
	
	private HashMap<OfflinePlayer, Team> players;
	
	public ManhuntLobby(World world, String name)
	{
		this.world = world;
		this.name = name;
		
		this.players = new HashMap<OfflinePlayer, Team>();
	}
	
	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void addPlayer(OfflinePlayer p, Team t)
	{
		this.players.put(p, t);
	}

	@Override
	public void removePlayer(OfflinePlayer p)
	{
		if (this.players.containsKey(p))
			this.players.remove(p);
	}

	@Override
	public void setPlayerTeam(OfflinePlayer p, Team t) throws Exception
	{
		if (this.players.containsKey(p))
		{
			this.players.put(p, t);
		}
		else
		{
			throw new Exception("OfflinePlayer not found.");
		}
	}

	@Override
	public Team getPlayerTeam(OfflinePlayer p)
	{
		if (this.players.containsKey(p))
		{
			return this.players.get(p);
		}
		else
		{
			return null;
		}
	}

	@Override
	public List<Player> getPlayers(Team... teams)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void messageTeams(Team... teams) {
		// TODO Auto-generated method stub

	}

}
