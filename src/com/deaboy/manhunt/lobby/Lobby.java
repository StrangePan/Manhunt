package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.map.*;
import com.deaboy.manhunt.settings.LobbySettings;
import com.deaboy.manhunt.settings.SettingsFile;

public abstract class Lobby implements Closeable
{
	//////////////// Properties ////////////////
	private final long id;
	private String name;
	private LobbyType type;
	
	private Spawn spawn;
	private boolean open;
	private SettingsFile file;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public Lobby(long id, String name, LobbyType type, Location location)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		
		this.spawn = new ManhuntSpawn("spawn", SpawnType.OTHER, location);
		this.open = true;
	}
	
	
	//////////////// SETTERS ////////////////
	public boolean setName(String name)
	{
		if (name.trim().isEmpty())
			return false;
		
		if (Manhunt.getLobby(name) != null)
			return false;
		
		this.name = name;
		getSettings().LOBBY_NAME.setValue(name);
		return true;
	}
	
	public void setSpawnRange(int range)
	{
		getSpawn().setRange(range);
		getSettings().SPAWN_RANGE.setValue(range);
	}
	public void setSpawnLocation(Location loc)
	{
		getSpawn().setLocation(loc);
		getSettings().SPAWN_LOCATION.setValue(loc);
	}
	
	public void enable()
	{
		this.open = true;
		getSettings().LOBBY_OPEN.setValue(true);
	}
	public void disable()
	{
		this.open = false;
		getSettings().LOBBY_OPEN.setValue(false);
	}
	public void setMaxPlayers(int num)
	{
		getSettings().MAX_PLAYERS.setValue(num);
	}
	
	
	//////////////// GETTERS ////////////////
	public long getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	private Spawn getSpawn()
	{
		return spawn;
	}
	public World getWorld()
	{
		return Manhunt.getWorld(spawn.getWorld());
	}
	
	public Location getSpawnLocation()
	{
		return spawn.getLocation();
	}
	public Location getRandomSpawnLocation()
	{
		return spawn.getRandomLocation();
	}
	public int getSpawnRange()
	{
		return getSpawn().getRange();
	}
	
	public boolean isEnabled()
	{
		return open;
	}
	public LobbyType getType()
	{
		return type;
	}
	
	public int getMaxPlayers()
	{
		return getSettings().MAX_PLAYERS.getValue();
	}
	public boolean allowAllPlayers()
	{
		return getSettings().MAX_PLAYERS.getValue() < 0;
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- INTERFACE ----------------//
	public abstract boolean playerJoinLobby(String p);
	public abstract boolean playerLeaveLobby(String p);
	public abstract boolean playerLeaveServer(String p);
	
	
	//---------------- PLAYERS ----------------//
	protected abstract boolean addPlayer(Player player);
	protected abstract boolean addPlayer(String name);
	public abstract boolean containsPlayer(Player player);
	public abstract boolean containsPlayer(String name);
	protected abstract boolean removePlayer(Player player);
	protected abstract boolean removePlayer(String name);
	protected abstract void clearPlayers();
	protected abstract void clearOfflinePlayers();
	
	public abstract List<String> getPlayerNames();
	public abstract List<String> getOnlinePlayerNames();
	public abstract List<String> getOfflinePlayerNames();
	public abstract List<Player> getOnlinePlayers();
	public abstract List<OfflinePlayer> getOfflinePlayers();
	
	
	public abstract void broadcast(String message);
	
	
	//---------------- Files and Config ----------------//
	public int saveFiles()
	{
		this.file.clearPacks();
		if (this instanceof GameLobby)
		{
			this.file.addPack(((GameLobby) this).getGame().getSettings());
		}
		this.file.addPack(getSettings());
		this.file.save();
		return 0;
	}
	public int loadFiles()
	{
		this.file.clearPacks();
		if (this instanceof GameLobby)
		{
			this.file.addPack(((GameLobby) this).getGame().getSettings());
		}
		this.file.addPack(getSettings());
		this.file.load();
		this.file.loadPacks();
		
		this.name = getSettings().LOBBY_NAME.getValue();
		this.spawn.setLocation(getSettings().SPAWN_LOCATION.getValue());
		this.spawn.setRange(getSettings().SPAWN_RANGE.getValue());
		this.open = getSettings().LOBBY_OPEN.getValue();
		return 0;
	}
	public int deleteFiles()
	{
		if (this.file.getFile().delete())
			return 0;
		else
			return 1;
	}
	
	public abstract LobbySettings getSettings();
	
	@Override
	public void close()
	{
		clearPlayers();
	}
	
	
	
}
