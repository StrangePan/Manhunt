package com.deaboy.manhunt.lobby;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.game.GameType;
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.map.*;
import com.deaboy.manhunt.settings.LobbySettings;
import com.deaboy.manhunt.settings.SettingManager;

public abstract class Lobby implements Closeable
{
	//////////////// Properties ////////////////
	private final long id;
	private String name;
	private LobbyType type;
	
	private Spawn spawn;
	private boolean open;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public Lobby(long id, String name, LobbyType type, World world, Location location)
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
		return true;
	}
	
	public void setSpawnRange(int range)
	{
		getSpawn().setRange(range);
	}
	public void setSpawnLocation(Location loc)
	{
		getSpawn().setLocation(loc);
	}
	
	protected void enable()
	{
		this.open = true;
	}
	protected void disable()
	{
		this.open = false;
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
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- INTERFACE ----------------//
	public abstract boolean playerJoinLobby(Player p);
	public abstract boolean playerLeaveLobby(Player p);
	public abstract boolean playerLeaveServer(Player p);
	
	
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
	public abstract int saveFiles();
	{
		List<String> mapnames;
		
		// Save name
		settings.LOBBY_NAME.setValue(this.name);
		
		// Save world
		settings.SPAWN_WORLD.setValue(this.world.getName());
		
		// Save type
		settings.LOBBY_TYPE.setValue(this.type.getName());
		
		// Save maps
		mapnames = new ArrayList<String>();
		for (String mapname : maps)
			mapnames.add(mapname);
		
		
		// Save spawn
		// Save open state
		
		// Save game
		
		settings.SPAWN_WORLD.setValue(world.getName());
		settings.SPAWN_X.setValue(spawn.getLocation().getX());
		settings.SPAWN_Y.setValue(spawn.getLocation().getY());
		settings.SPAWN_Z.setValue(spawn.getLocation().getZ());
		settings.SPAWN_PITCH.setValue(spawn.getLocation().getPitch());
		settings.SPAWN_YAW.setValue(spawn.getLocation().getYaw());
		settings.SPAWN_RANGE.setValue(spawn.getRange());
		settings.LOBBY_NAME.setValue(name);
		settings.MAPS.setValue(mapnames);
		settings.save();
	}
	public abstract int loadFiles();
	{
		Location loc;
		
		settings.load();
		
		// Load name
		this.name = settings.LOBBY_NAME.getValue();
		
		// Load world
		world = Manhunt.getWorld(settings.SPAWN_WORLD.getValue());
		if (world == null)
			world = Manhunt.getWorld(Bukkit.getWorlds().get(0));
		
		// Load type
		this.type = LobbyType.fromId(settings.LOBBY_TYPE.getValue());
		if (this.type == null)
			this.type = LobbyType.GAME;
		
		// Load maps 
		maps.clear();
		for (String mapname : settings.MAPS.getValue())
			if (Manhunt.getMap(mapname) != null)
				maps.add(mapname);
		
		// Load spawn
		loc = new Location(world.getWorld(), 0.0, 0.0, 0.0);
		loc.setX(settings.SPAWN_X.getValue());
		loc.setY(settings.SPAWN_Y.getValue());
		loc.setZ(settings.SPAWN_Z.getValue());
		loc.setPitch(settings.SPAWN_PITCH.getValue());
		loc.setYaw(settings.SPAWN_YAW.getValue());
		this.spawn = new ManhuntSpawn("spawn", SpawnType.OTHER, loc, settings.SPAWN_RANGE.getValue());
		
		// Load open state
		this.open = settings.LOBBY_OPEN.getValue();
		
		// Load game
		if (type == LobbyType.GAME)
		{
			GameType gametype = Manhunt.getGameTypeByClassCanonicalName(settings.GAME_TYPE.getValue());
			if (gametype == null)
				gametype = Manhunt.getGameTypeByClassCanonicalName(ManhuntGame.class.getCanonicalName());
			
			this.game = gametype.createInstance(this);
			if (game == null)
				game = new ManhuntGame(this);
		}
		
	}
	public abstract int deleteFiles();
	public abstract Lobby loadFromFile(File file);
	
	protected abstract void initializeSettings();
	public abstract SettingManager getSettings();
	
	@Override
	public void close()
	{
		clearPlayers();
	}
	
	
	
}
