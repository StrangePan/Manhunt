package com.bendude56.hunted.lobby;

import org.bukkit.Location;
import org.bukkit.World;

import com.bendude56.hunted.map.Spawn;

public abstract class ManhuntLobby implements Lobby
{

	private final Long id;
	private Spawn spawn;
	private boolean enabled;
	
	private static long next_id = 0;
	
	public ManhuntLobby(Spawn spawn)
	{
		this.spawn = spawn;
		this.id = next_id++;
		this.enabled = true;
	}
	
	@Override
	public long getId()
	{
		return id;
	}

	@Override
	public Spawn getSpawn()
	{
		return spawn;
	}

	@Override
	public World getWorld()
	{
		return spawn.getWorld();
	}

	@Override
	public Location getSpawnLocation()
	{
		return spawn.getLocation();
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	@Override
	public void enable()
	{
		this.enabled = true;
	}

	@Override
	public void disable()
	{
		this.enabled = false;
	}

}
