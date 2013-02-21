package com.deaboy.manhunt.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class ManhuntMap implements Map
{//-------------------- Properties --------------------//
	private String name;
	private World world;
	private Location spawn;
	private List<Spawn> setup;
	private List<Spawn> hunter;
	private List<Spawn> prey;
	private List<Zone> zones;
	
	
	//-------------------- Constructors --------------------//
	/**
	 * Initializes a new ManhuntMap at the given World's spawn location.
	 * @param world The World of the new Map.
	 */
	public ManhuntMap(String name, World world)
	{
		this(name, world.getSpawnLocation());
	}
	
	/**
	 * Initializes a new ManhuntMap with the given spawn location.
	 * @param loc The spawn location of the new map.
	 */
	public ManhuntMap(String name, Location loc)
	{
		this.name = name;
		this.spawn = loc;
		this.setup = new ArrayList<Spawn>();
		this.hunter = new ArrayList<Spawn>();
		this.prey = new ArrayList<Spawn>();
	}
	
	
	//-------------------- Public Methods --------------------//
	
	//---------------- Getters ----------------//
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getFullName()
	{
		return getWorld().getName() + "." + getName();
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawn.clone();
	}

	@Override
	public World getWorld()
	{
		return world;
	}
	
	@Override
	public List<Spawn> getSetupSpawns()
	{
		return this.setup;
	}
	
	@Override
	public List<Spawn> getHunterSpawns()
	{
		return this.hunter;
	}
	
	@Override
	public List<Spawn> getPreySpawns()
	{
		return this.prey;
	}
	
	@Override
	public List<Zone> getZones()
	{
		return this.zones;
	}
	
	@Override
	public List<Zone> getZones(ZoneType ... types)
	{
		List<Zone> zones = new ArrayList<Zone>();
		for (Zone zone : this.zones)
		{
			for (ZoneType type : types)
			{
				if (zone.getType() == type)
				{
					zones.add(zone);
					break;
				}
			}
		}
		return zones;
	}
	
	
	
	//---------------- Setters ----------------//
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public void setSpawn(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.spawn = loc.clone();
	}
	
	@Override
	public void addSetupSpawn(Spawn spawn)
	{
		if (spawn != null && !setup.contains(spawn) && spawn.getWorld() == getWorld())
			setup.add(spawn);
	}
	
	@Override
	public void addHunterSpawn(Spawn spawn)
	{
		if ( spawn != null && !hunter.contains(spawn) &&spawn.getWorld() == getWorld())
			hunter.add(spawn);
	}
	
	@Override
	public void addPreySpawn(Spawn spawn)
	{
		if (spawn != null && !prey.contains(spawn) && spawn.getWorld() == getWorld())
			prey.add(spawn);
	}
	
	@Override
	public void removeSetupSpawn(Spawn spawn)
	{
		if (setup.contains(spawn))
			setup.remove(spawn);
	}
	
	@Override
	public void removeHunterSpawn(Spawn spawn)
	{
		if (hunter.contains(spawn))
			hunter.remove(spawn);
	}
	
	@Override
	public void removePreySpawn(Spawn spawn)
	{
		if (prey.contains(spawn))
			prey.remove(spawn);
	}
	
	@Override
	public void clearSetupSpawns()
	{
		this.setup.clear();
	}
	
	@Override
	public void clearHunterSpawns()
	{
		this.hunter.clear();
	}
	
	@Override
	public void clearPreySpawns()
	{
		this.prey.clear();
	}
	
	@Override
	public void addZone(Zone zone)
	{
		if (zone.getWorld() != getWorld())
			throw new IllegalArgumentException("The zone must be in the same world as the map.");
		if (!zones.contains(zone))
			zones.add(zone);
	}
	
	@Override
	public void removeZone(Zone zone)
	{
		if (zones.contains(zone))
			zones.remove(zone);
	}
	
	@Override
	public void clearZones()
	{
		this.zones.clear();
	}
	
	
	
	
	
}
