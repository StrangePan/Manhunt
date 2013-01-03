package com.bendude56.hunted.map;

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
		this(loc, range, null, null);
	}
	
	public ManhuntSpawn(Location loc, int range, Location protect_c1, Location protect_c2)
	{
		this(loc, range, protect_c1, protect_c2, null, null);
	}
	
	public ManhuntSpawn(Location loc, int range, Location protect_c1, Location protect_c2, Location boundary_c1, Location boundary_c2)
	{
		if (loc == null)
			throw new IllegalArgumentException("Spawn's Location cannot be null");
		
		if ((protect_c1 != null && protect_c1.getWorld() != loc.getWorld())
				|| (protect_c2 != null && protect_c2.getWorld() != loc.getWorld())
				|| (boundary_c1 != null && boundary_c1.getWorld() != loc.getWorld())
				|| (boundary_c2 != null && boundary_c2.getWorld() != loc.getWorld()))
			throw new IllegalArgumentException("All locations must be in the same world.");
		
		this.location = loc;
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
			throw new IllegalArgumentException("Location cannot be null");
		if (loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("New location must be in the same world");
		
		this.location = loc == null ? null : loc.clone();
	}
	
	@Override
	public void setRange(int range)
	{
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
