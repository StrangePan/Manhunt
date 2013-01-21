package com.deaboy.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntSpawn implements Spawn
{
	//---------------- Properties ----------------//
	private Location location;
	private int range;
	
	
	
	//---------------- Constructors ----------------//
	public ManhuntSpawn(Location loc)
	{
		this(loc, 0);
	}
	
	public ManhuntSpawn(Location loc, int range)
	{
		this.location = loc.clone();
		if (range < 0)
			this.range = 0;
		else
			this.range = range;
	}
	
	
	
	//---------------- Getters ----------------//
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
			
			loc.setX(Math.cos(angle) * distance);
			loc.setZ(Math.sin(angle) * distance);
			
			return loc;
		}
	}
	
	
	
}
