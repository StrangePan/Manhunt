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
	private HashMap<String, Spawn> points;
	private HashMap<String, Zone> zones;
	
	private List<Spawn> points_other;
	private List<Spawn> points_hunter;
	private List<Spawn> points_prey;
	private List<Spawn> points_setup;
	
	private List<Zone> zones_nobuild;
	private List<Zone> zones_boundary;
	private List<Zone> zones_build;
	private List<Zone> zones_nomobs;
	private List<Zone> zones_setup;
	
	
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
		this.points = new HashMap<String, Spawn>();
		this.zones = new HashMap<String, Zone>();
		
		points_other = new ArrayList<Spawn>();
		points_hunter = new ArrayList<Spawn>();
		points_prey = new ArrayList<Spawn>();
		points_setup = new ArrayList<Spawn>();
		
		zones_nobuild = new ArrayList<Zone>();
		zones_boundary = new ArrayList<Zone>();
		zones_build = new ArrayList<Zone>();
		zones_nomobs = new ArrayList<Zone>();
		zones_setup = new ArrayList<Zone>();
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
	public Spawn getPoint(String name)
	{
		if (this.points.containsKey(name))
		{
			return this.points.get(name);
		}
		else
		{
			return null;
		}
	}
	@Override
	public List<Spawn> getPoints()
	{
		return (new ArrayList<Spawn>(this.points.values()));
	}
	@Override
	public List<Spawn> getPoints(SpawnType type)
	{
		switch (type)
		{
		case OTHER:
			return points_other;
		case HUNTER:
			return points_hunter;
		case PREY:
			return points_prey;
		case SETUP:
			return points_setup;
		default:
			return null;
		}
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
		for (ZoneFlag flag : flags)
		{
			switch (flag)
			{
			case NO_BUILD:
				zones.addAll(zones_nobuild);
				break;
			case BOUNDARY:
				zones.addAll(zones_boundary);
				break;
			case BUILD:
				zones.addAll(zones_build);
				break;
			case NO_MOBS:
				zones.addAll(zones_nomobs);
				break;
			case SETUP:
				zones.addAll(zones_setup);
				break;
			default:
				break;
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
	public boolean addPoint(Spawn point)
	{
		if (!this.points.containsKey(point.getName()) && !this.points.containsValue(point) && point.getWorld() == this.getWorld().getWorld())
		{
			this.points.put(point.getName(), point);
			switch(point.getType())
			{
			case OTHER:
				points_other.add(point);
				break;
			case HUNTER:
				points_hunter.add(point);
				break;
			case PREY:
				points_prey.add(point);
				break;
			case SETUP:
				points_setup.add(point);
				break;
			default:
				break;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public Spawn createPoint(String name, SpawnType type, Location location)
	{
		return createPoint(name, type, location, 0);
	}
	@Override
	public Spawn createPoint(String name, SpawnType type, Location location, int range)
	{
		Spawn point;
		
		if (name == null || this.points.containsKey(name))
			return null;
		
		if (location == null || location.getWorld() != this.world.getWorld())
			return null;
		
		if (range < 0)
			return null;
		
		if (type == null)
			return null;
		
		point = new ManhuntSpawn(name, type, location, range);
		
		addPoint(point);
		return point;
	}
	public void removePoint(String name)
	{
		if (this.points.containsKey(name))
		{
			Spawn point = points.get(name);
			if (points_other.contains(point)) points_other.remove(point);
			if (points_hunter.contains(point)) points_hunter.remove(point);
			if (points_prey.contains(point)) points_prey.remove(point);
			if (points_setup.contains(point)) points_setup.remove(point);
			
			this.points.remove(name);
		}
	}
	public void clearPoints()
	{
		this.points.clear();
	}
	
	@Override
	public boolean addZone(Zone zone)
	{
		if (!this.zones.containsKey(zone.getName()) && !this.zones.containsValue(zone) && zone.getWorld() == this.getWorld().getWorld())
		{
			this.zones.put(zone.getName(), zone);
			for (ZoneFlag type : ZoneFlag.values())
			{
				switch (type)
				{
				case NO_BUILD:
					zones_nobuild.add(zone);
					break;
				case BOUNDARY:
					zones_boundary.add(zone);
					break;
				case BUILD:
					zones_build.add(zone);
					break;
				case NO_MOBS:
					zones_nomobs.add(zone);
					break;
				case SETUP:
					zones_setup.add(zone);
					break;
				default:
					break;
				}
			}
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
		
		addZone(zone);
		
		return zone;
	}
	@Override
	public void removeZone(String name)
	{
		if (zones.containsKey(name))
		{
			Zone zone = zones.get(name);
			if (zones_nobuild.contains(zone)) zones_nobuild.remove(zone);
			if (zones_boundary.contains(zone)) zones_boundary.remove(zone);
			if (zones_build.contains(zone)) zones_build.remove(zone);
			if (zones_nomobs.contains(zone)) zones_nomobs.remove(zone);
			if (zones_setup.contains(zone)) zones_setup.remove(zone);
			
			zones.remove(name);
		}
	}
	@Override
	public void clearZones()
	{
		this.zones.clear();
		
		this.zones_nobuild.clear();
		this.zones_boundary.clear();
		this.zones_build.clear();
		this.zones_nomobs.clear();
		this.zones_setup.clear();
	}
	
	
	
	//---------------- Issues ----------------//
	public boolean hasIssues()
	{
		boolean issue = false;
		for (Spawn point : getPoints())
		{
			switch (point.getType())
			{
			case OTHER:
				break;
			case PREY:
			case HUNTER:
				for (Zone zone : getZones(ZoneFlag.BOUNDARY))
				{
					if (zone.containsLocation(point.getLocation()))
					{
						issue = false;
						continue;
					}
					else
					{
						issue = true;
					}
				}
				break;
			case SETUP:
				for (Zone zone : getZones(ZoneFlag.SETUP))
				{
					if (zone.containsLocation(point.getLocation()))
					{
						issue = false;
						continue;
					}
					else
					{
						issue = true;
					}
				}
				break;
			default:
				break;
			}
			if (issue) return true;
		}
		return issue;
	}
	public List<String> getIssues()
	{
		List<String> issues;
		boolean issue;
		
		issues = new ArrayList<String>();
		issue = false;
		for (Spawn point : getPoints())
		{
			switch (point.getType())
			{
			case OTHER:
				break;
			case PREY:
			case HUNTER:
				for (Zone zone : getZones(ZoneFlag.BOUNDARY))
				{
					if (zone.containsLocation(point.getLocation()))
					{
						issue = false;
						continue;
					}
					else
					{
						issue = true;
					}
				}
				break;
			case SETUP:
				for (Zone zone : getZones(ZoneFlag.SETUP))
				{
					if (zone.containsLocation(point.getLocation()))
					{
						issue = false;
						continue;
					}
					else
					{
						issue = true;
					}
				}
				break;
			default:
				break;
			}
			
			if (issue)
			{
				switch(point.getType())
				{
				case PREY:
				case HUNTER:
					issues.add("Point " + point.getName() + " is not contained within a " + ZoneFlag.BOUNDARY.getName() + " zone");
					break;
				case SETUP:
					issues.add("Point " + point.getName() + " is not contained within a " + ZoneFlag.SETUP.getName() + " zone");
					break;
				default:
					break;
				}
			}
			
			return issues;
		}
		
		return issues;
	}
	
	
	
	
}
