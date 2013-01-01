package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntSpawn implements Spawn
{
	
	
	//---------------- Properties ----------------//
	private Location location;
	private int range;
	
	private Location boundary1;
	private Location boundary2;
	private Location protection1;
	private Location protection2;
	
	
	
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
		
		this.protection1 = protect_c1;
		this.protection2 = protect_c2;
		
		this.boundary1 = boundary_c1;
		this.boundary2 = boundary_c2;
		
	}
	
	
	
	//---------------- Public Methods ----------------//
	
	//------------ Getters ------------//
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
	
	@Override
	public Location getProtectionCorner1()
	{
		return protection1;
	}
	
	@Override
	public Location getProtectionCorner2()
	{
		return protection2;
	}
	
	@Override 
	public Location getBoundaryCorner1()
	{
		return boundary1;
	}
	
	@Override 
	public Location getBoundaryCorner2()
	{
		return boundary2;
	}
	
	//------------ Setters ------------//
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
	
	@Override
	public void setProtectionCorner1(Location loc)
	{
		if (loc != null && loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		this.protection1 = loc == null ? null : loc.clone();
	}
	
	@Override
	public void setProtectionCorner2(Location loc)
	{
		if (loc != null && loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		this.protection2 = loc == null ? null : loc.clone();
	}
	
	@Override
	public void setBoundaryCorner1(Location loc)
	{
		if (loc != null && loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		this.boundary1 = loc == null ? null : loc.clone();
	}
	
	@Override
	public void setBoundaryCorner2(Location loc)
	{
		if (loc != null && loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		this.boundary2 = loc == null ? null : loc.clone();
	}
	
	
}
