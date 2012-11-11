package com.bendude56.hunted.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.settings.WorldSettings;

public class ManhuntGameLobby implements GameLobby
{

	private final World world;
	
	private WorldSettings settings;
	
	private HashMap<Player, Team> players;
	
	
	public ManhuntGameLobby(World world)
	{
		this.world = world;
		
		this.settings = new WorldSettings(world);
		
		this.players = new HashMap<Player, Team>();
	}
	

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public void addPlayer(Player p, Team t)
	{
		this.players.put(p, t);
	}

	@Override
	public void removePlayer(Player p)
	{
		if (this.players.containsKey(p))
			this.players.remove(p);
	}

	@Override
	public void setPlayerTeam(Player p, Team t) throws Exception
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
	public Team getPlayerTeam(Player p)
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
		
		for (Player p : this.players.keySet())
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
		
		for (Player p : this.players.keySet())
		{
			for (Team t: teams)
			{
				if (this.players.get(p) == t)
				{
					players.add((OfflinePlayer) p);
					break;
				}
			}
		}
		
		return players;
	}

	@Override
	public void messagePlayers(String message, Team ... teams)
	{
		for (Player p : getPlayers(teams))
		{
			if (p.isOnline())
				p.sendMessage(message);
		}
	}
	
	@Override
	public void messagePlayers(String message)
	{
		for (Player p : players.keySet())
		{
			if (p.isOnline())
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
		
	}
	
	@Override
	public WorldSettings getSettings()
	{
		return settings;
	}
	
	
	
	
}
