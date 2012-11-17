package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntSpawn implements Spawn
{
	
	
	//---------------- Properties ----------------//
	private Location location;
	private int range;
	
	private Location[] protection;
	private Location[] border;
	
	private static final int max_protections = 2;
	private static final int max_borders = 2;
	
	
	
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
	
	public ManhuntSpawn(Location loc, int range, Location protect_c1, Location protect_c2, Location border_c1, Location border_c2)
	{
		if (loc == null)
			throw new IllegalArgumentException("Spawn's Location cannot be null");
		
		if ((protect_c1 != null && protect_c1.getWorld() != loc.getWorld())
				|| (protect_c2 != null && protect_c2.getWorld() != loc.getWorld())
				|| (border_c1 != null && border_c1.getWorld() != loc.getWorld())
				|| (border_c2 != null && border_c2.getWorld() != loc.getWorld()))
			throw new IllegalArgumentException("All locations must be in the same world.");
		
		this.location = loc;
		this.range = range;
		
		this.protection = new Location[max_protections];
		this.protection[0] = protect_c1;
		this.protection[1] = protect_c2;
		
		this.border = new Location[max_borders];
		this.border[0] = border_c1;
		this.border[1] = border_c2;
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
	public Location getProtectionCorner(int corner)
	{
		if (corner < 0 || corner >= max_protections)
			return null;
		
		return protection[corner].clone();
	}
	
	@Override 
	public Location getBorderCorner(int corner)
	{
		if (corner < 0 || corner >= max_borders)
			return null;
		
		return border[corner].clone();
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
	public void setProtectionCorner(int index, Location loc)
	{
		if (loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		if (index < 0 || index >= max_protections)
			throw new IllegalArgumentException("Index must be either 0 or 1");
		
		this.protection[index] = loc == null ? null : loc.clone();
	}
	
	@Override
	public void setBorderCorner(int index, Location loc)
	{
		if (loc.getWorld() != location.getWorld())
			throw new IllegalArgumentException("Location must be in the same world");
		
		if (index < 0 || index >= max_borders)
			throw new IllegalArgumentException("Index must be either 0 or 1");
		
		this.border[index] = loc == null ? null : loc.clone();
	}
	
	
}
