package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ManhuntLobby implements Lobby
{

	private final World world;
	
	private HashMap<OfflinePlayer, Team> players;
	
	public ManhuntLobby(World world)
	{
		this.world = world;
		
		this.players = new HashMap<OfflinePlayer, Team>();
	}
	

	@Override
	public World getWorld()
	{
		return world;
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
	public List<Player> getPlayers(Team ... teams)
	{
		List<Player> players = new ArrayList<Player>();
		
		for (OfflinePlayer p : this.players.keySet())
		{
			if (!p.isOnline())
				continue;
			
			for (Team t: teams)
			{
				if (this.players.get(p) == t)
				{
					players.add(p.getPlayer());
					break;
				}
			}
		}
		
		return players;
	}

	@Override
	public List<OfflinePlayer> getOfflinePlayers(Team ... teams)
	{
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		
		for (OfflinePlayer p : this.players.keySet())
		{
			for (Team t: teams)
			{
				if (this.players.get(p) == t)
				{
					players.add(p);
					break;
				}
			}
		}
		
		return players;
	}

	@Override
	public void messageTeams(String message, Team ... teams)
	{
		for (Player p : getPlayers(teams))
		{
			p.sendMessage(message);
		}
	}

	@Override
	public void clear()
	{
		this.players.clear();
	}

}
