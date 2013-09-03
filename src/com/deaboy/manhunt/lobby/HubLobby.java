package com.deaboy.manhunt.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;

public abstract class HubLobby extends Lobby
{
	//////////////// PROPERTIES ////////////////
	private List<String> players;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public HubLobby(long id, File file)
	{
		super(id, file);
		this.players = new ArrayList<String>();
	}
	public HubLobby(long id, File file, String name, Location loc)
	{
		super(id, file, name, loc);
		this.players = new ArrayList<String>();
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- Players ----------------//
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
		if (!this.players.contains(name))
		{
			this.players.add(name);
			Manhunt.unlockPlayer(name);
			return true;
		}
		else
		{
			return false;
		}
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
		return this.players.contains(name);
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
		if (this.players.contains(name))
		{
			this.players.remove(name);
			Manhunt.unlockPlayer(name);
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
		this.players.clear();
	}
	@Override
	protected void clearOfflinePlayers()
	{
		for (String name : getOfflinePlayerNames())
		{
			this.players.remove(name);
		}
	}
	
	@Override
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(this.players);
	}
	@Override
	public List<String> getOnlinePlayerNames()
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.players)
		{
			if (Bukkit.getPlayerExact(name) != null)
			{
				names.add(name);
			}
		}
		return names;
	}
	@Override
	public List<String> getOfflinePlayerNames()
	{
		List<String> names = new ArrayList<String>();
		for (String name : this.players)
		{
			if (Bukkit.getPlayerExact(name) == null)
			{
				names.add(name);
			}
		}
		return names;
	}
	@Override
	public List<Player> getOnlinePlayers()
	{
		List<Player> players = new ArrayList<Player>();
		for (String name : this.players)
		{
			if (Bukkit.getPlayerExact(name) != null)
			{
				players.add(Bukkit.getPlayerExact(name));
			}
		}
		return players;
	}
	@Override
	public List<OfflinePlayer> getOfflinePlayers()
	{
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (String name : this.players)
		{
			if (Bukkit.getPlayerExact(name) == null)
			{
				players.add(Bukkit.getOfflinePlayer(name));
			}
		}
		return players;
	}
	
	@Override
	public void broadcast(String message)
	{
		for (String name : this.players)
		{
			if (Bukkit.getPlayerExact(name) != null)
			{
				Bukkit.getPlayerExact(name).sendMessage(message);
			}
		}
	}
	
	
	//---------------- Miscellaneous ----------------//
	public LobbyType getType()
	{
		return LobbyType.HUB;
	}
	
	
	//---------------- Settings ----------------//
	
	
}
