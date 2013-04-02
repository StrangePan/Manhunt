package com.deaboy.manhunt.map;

import java.util.ArrayList;
import java.util.HashMap;
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
		this.setup = new ArrayList<Spawn>();
		this.hunter = new ArrayList<Spawn>();
		this.prey = new ArrayList<Spawn>();
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
	public List<Zone> getZones(ZoneType ... types)
	{
		List<Zone> zones = new ArrayList<Zone>();
		for (Zone zone : this.zones.values())
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
	public Zone createZone(ZoneType type, Location corner1, Location corner2)
	{
		return createZone(type, new String(), corner1, corner2);
	}
	
	@Override
	public Zone createZone(ZoneType type, String name, Location corner1, Location corner2)
	{
		Zone zone;
		
		if (type == null)
			return null;
		if (corner1 == null)
			return null;
		if (corner2 == null)
			return null;
		if (name == null)
			name = "";
		if (corner1.getWorld() != corner2.getWorld())
			return null;
		if (name.contains(" "))
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
		
		zone = new ManhuntZone(type, name, corner1, corner2);
		
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
