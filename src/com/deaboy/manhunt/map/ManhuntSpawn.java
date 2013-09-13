package com.deaboy.manhunt.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntSpawn implements Spawn
{
	//---------------- Properties ----------------//
	private String name;
	private final SpawnType type;
	private Location location;
	private int range;
	
	
	
	//---------------- Constructors ----------------//
	public ManhuntSpawn(String name, SpawnType type, Location loc)
	{
		this(name, type, loc, 0);
	}
	
	public ManhuntSpawn(String name, SpawnType type, Location loc, int range)
	{
		if (name == null || type == null || loc == null)
		{
			throw new IllegalArgumentException("Arguments must not be null.");
		}
		
		this.name = name;
		this.type = type;
		this.location = loc.clone();
		if (range < 0)
			this.range = 0;
		else
			this.range = range;
	}
	
	
	
	//---------------- Getters ----------------//
	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public SpawnType getType()
	{
		return this.type;
	}
	
	@Override
	public Location getLocation()
	{
		return location.clone();
	}
	
	@Override
	public World getWorld()
	{
		return location.getWorld();
	}
	
	@Override
	public int getRange()
	{
		return range;
	}
	
	
	
	//---------------- Setters ----------------//
	@Override
	public void setName(String name)
	{
		if (name == null)
			return;
		this.name = name;
	}
	
	@Override
	public void setLocation(Location loc)
	{
		if (loc == null)
			throw new IllegalArgumentException("Location cannot be null.");
		if (loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("New location must be in the same world.");
		
		this.location = loc.clone();
	}
	
	@Override
	public void setRange(int range)
	{
		if (range < 0)
			this.range = 0;
		else
			this.range = range;
	}
	
	
	
	//---------------- Public Methods ----------------//
	@Override
	public Location getRandomLocation()
	{
		if (range == 0)
			return location.clone();
		else
		{
			Location loc = location.clone();
			double angle = Math.random() * 2 * Math.PI;
			double distance = Math.random() * range;
			
			loc.setX(loc.getX() + Math.cos(angle) * distance);
			loc.setZ(loc.getZ() + Math.sin(angle) * distance);
			
			return loc;
		}
	}
	
	
	
}
