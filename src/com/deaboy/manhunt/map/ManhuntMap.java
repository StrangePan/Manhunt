package com.deaboy.manhunt.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

public class ManhuntMap implements Map
{
	//-------------------- Properties --------------------//
	private String name;
	private World world;
	private Location spawn;
	private HashMap<String, Spawn> spawns;
	private HashMap<String, Zone> zones;
	
	
	//-------------------- Constructors --------------------//
	/**
	 * Initializes a new ManhuntMap at the given World's spawn location.
	 * @param world The World of the new Map.
	 */
	public ManhuntMap(String name, World world)
	{
		this(name, world.getSpawnLocation(), world);
	}
	
	/**
	 * Initializes a new ManhuntMap with the given spawn location.
	 * @param loc The spawn location of the new map.
	 */
	public ManhuntMap(String name, Location loc, World world)
	{
		this.name = name;
		this.world = world;
		this.spawn = loc;
		this.spawns = new HashMap<String, Spawn>();
		this.zones = new HashMap<String, Zone>();
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
	public Spawn getSpawn(String name)
	{
		if (this.spawns.containsKey(name))
		{
			return this.spawns.get(name);
		}
		else
		{
			return null;
		}
	}
	@Override
	public List<Spawn> getSpawns()
	{
		return (new ArrayList<Spawn>(this.spawns.values()));
	}
	@Override
	public List<Spawn> getSpawns(SpawnType type)
	{
		List<Spawn> spawns = new ArrayList<Spawn>();
		for (Spawn spawn : this.spawns.values())
			if (spawn.getType() == type)
				spawns.add(spawn);
		return spawns;
	}
	
	@Override
	public Zone getZone(String name)
	{
		if (zones.containsKey(name))
			return zones.get(name);
		else
			return null;
	}
	@Override
	public List<Zone> getZones()
	{
		return new ArrayList<Zone>(this.zones.values());
	}
	@Override
	public List<Zone> getZones(ZoneFlag ... flags)
	{
		List<Zone> zones = new ArrayList<Zone>();
		for (Zone zone : this.zones.values())
		{
			for (ZoneFlag flag : flags)
			{
				if (zone.checkFlag(flag))
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
	public boolean addSpawn(Spawn spawn)
	{
		if (!this.spawns.containsKey(spawn.getName()) && !this.spawns.containsValue(spawn) && spawn.getWorld() == this.world)
		{
			this.spawns.put(spawn.getName(), spawn);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public Spawn createSpawn(String name, SpawnType type, Location location)
	{
		return createSpawn(name, type, location, 0);
	}
	@Override
	public Spawn createSpawn(String name, SpawnType type, Location location, int range)
	{
		Spawn spawn;
		
		if (name == null || this.spawns.containsKey(name))
			return null;
		
		if (location == null || location.getWorld() != this.world.getWorld())
			return null;
		
		if (range < 0)
			return null;
		
		if (type == null)
			return null;
		
		spawn = new ManhuntSpawn(name, type, location, range);
		
		this.spawns.put(name, spawn);
		return spawn;
	}
	public void removeSpawn(String name)
	{
		if (this.spawns.containsKey(name))
		{
			this.spawns.remove(name);
		}
	}
	public void clearSpawns()
	{
		this.spawns.clear();
	}
	
	@Override
	public boolean addZone(Zone zone)
	{
		if (!this.zones.containsKey(zone.getName()) && !this.zones.containsValue(zone) && zone.getWorld() == this.world)
		{
			this.zones.put(zone.getName(), zone);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public Zone createZone(String name, Location corner1, Location corner2, List<ZoneFlag> flags)
	{
		ZoneFlag[] f = new ZoneFlag[flags.size()];
		for (int i = 0; i < flags.size(); i++)
			f[i] = flags.get(i);
		return createZone(name, corner1, corner2, f);
	}
	@Override
	public Zone createZone(String name, Location corner1, Location corner2, ZoneFlag...flags)
	{
		Zone zone;
		
		if (corner1 == null)
			return null;
		if (corner2 == null)
			return null;
		if (name == null || name.isEmpty())
			return null;
		if (corner1.getWorld() != corner2.getWorld())
			return null;
		
		
		
		if (name.isEmpty())
		{
			int i = 1;
			while (getZone("zone" + i) != null)
				i++;
			name = "zone" + i;
		}
		else
		{
			if (zones.containsKey(name))
				return null;
		}
		
		zone = new ManhuntZone(name, corner1, corner2);
		
		for (ZoneFlag flag : flags)
			zone.setFlag(flag, true);
		
		zones.put(name, zone);
		
		return zone;
	}
	@Override
	public void removeZone(String name)
	{
		if (zones.containsKey(name))
			zones.remove(name);
	}
	@Override
	public void clearZones()
	{
		this.zones.clear();
	}
	
	
	
	
	
}
