package com.deaboy.manhunt.map;

import org.bukkit.Location;

public abstract class Zone
{
	//---------------- Properties ----------------/
	private final ZoneType type;
	private Location corner1;
	private Location corner2;
	private boolean ignoreY;
	
	
	
	//---------------- Constructors ----------------//
	public Zone(ZoneType type, Location corner1, Location corner2, boolean ignoreY)
	{
		if (corner1 == null || corner1 == null)
			throw new IllegalArgumentException("Both locations must not be null.");
		if (corner1.getWorld() != corner2.getWorld())
			throw new IllegalArgumentException("Both locations must be in the same world.");
		
		this.type = type;
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.ignoreY = ignoreY;
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the type for this zone.
	 * @return
	 */
	public ZoneType getType()
	{
		return this.type;
	}
	
	/**
	 * Gets the first corner of this zone.
	 * @return
	 */
	public Location getCorner1()
	{
		return this.corner1.clone();
	}
	
	/**
	 * Gets the second corner of this zone.
	 * @return
	 */
	public Location getCorner2()
	{
		return this.corner2.clone();
	}
	
	/**
	 * Gets whether or not the zone ignores the y-coordinate
	 * in its calculations.
	 * @return
	 */
	public boolean getIgnoreY()
	{
		return this.ignoreY;
	}
	
	/**
	 * Gets the world of this zone. Usually used for validation.
	 * @return
	 */
	public org.bukkit.World getWorld()
	{
		return corner1.getWorld();
	}
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Changes the first corner of the zone to a clone of
	 * the given location. 
	 * @param corner1 The corner's new location.
	 */
	public void setCorner1(Location corner1)
	{
		if (corner1 == null)
			throw new IllegalArgumentException("The location cannot be null.");
		if (corner1.getWorld() != getWorld())
			throw new IllegalArgumentException("The location must be in the same world as the zone.");
		
		this.corner1 = corner1.clone();
	}
	
	/**
	 * Changes the second corner of the zone to a clone of
	 * the given location.
	 * @param corner2 The corner's new location.
	 */
	public void setCorner2(Location corner2)
	{
		if (corner2 == null)
			throw new IllegalArgumentException("The location cannot be null.");
		if (corner2.getWorld() != getWorld())
			throw new IllegalArgumentException("The location must be in the same world as the zone.");
		
		this.corner2 = corner2.clone();
	}
	
	/**
	 * Sets whether or not the zone ignores the y-coordinate in
	 * its calculations.
	 * @param ignore True will make the zone ignore the y-corrdinate,
	 * False will make the zone take the y-coordinate into account.
	 */
	public void setIgnoreY(boolean ignore)
	{
		this.ignoreY = ignore;
	}
	
	
	
	//---------------- Public Methods ----------------//
	/**
	 * Gets the 2-dimensional area of the zone, ignoring the y-coordinate,
	 * regardless of whether the zone is set to ignore the y-coordinate in
	 * its calculations.
	 * @return
	 */
	public double getArea()
	{
		return Math.abs((corner1.getX() - corner2.getX())
				* (corner1.getZ() - corner2.getZ()));
	}
	
	/**
	 * Gets the 3-dimensional volume of the zone, taking into account
	 * the y-coordinate into account regardless of whether the zone is
	 * set to ignore the y-coordinate in its calculations.
	 * @return
	 */
	public double getVolume()
	{
		return Math.abs(getArea() * (corner1.getY() - corner2.getY()));
	}
	
	/**
	 * Checks to see if the given location lies within the zone's
	 * boundaries. Will ignore the y-coordinate if the zone is set so.
	 * @param loc The location to check.
	 * @return True if the locatio's coordinate falls within the zone's
	 * boundaries, or false if it doesn't.
	 */
	public boolean envelopes(Location loc)
	{
		return (loc.getWorld() == getWorld()
				&&((loc.getX() < corner1.getX() && loc.getX() > corner2.getX())
				|| (loc.getX() > corner1.getX() && loc.getX() < corner2.getX()))
				&& ((loc.getZ() < corner1.getZ() && loc.getZ() > corner2.getZ())
				|| (loc.getZ() > corner1.getZ() && loc.getZ() < corner2.getZ()))
				&& ignoreY ? true :
					((loc.getY() < corner1.getY() && loc.getY() > corner2.getY())
					|| (loc.getY() > corner1.getY() && loc.getY() < corner2.getY())));
	}
	
	/**
	 * Generates random location that lies within the zone's range.
	 * If the zone is set to ignore the y-coordinate, this method
	 * will return any y-coordinate between 0 and the world's max height.
	 * @return
	 */
	public Location getRandomLocation()
	{
		Location loc = corner1.clone();
		
		loc.setX(loc.getX() + Math.random() * (corner1.getX() - corner2.getX()));
		loc.setZ(loc.getZ() + Math.random() * (corner1.getZ() - corner2.getZ()));
		if (ignoreY)
			loc.setY(Math.random() * getWorld().getMaxHeight());
		else
			loc.setY(loc.getY() + Math.random() * (corner1.getY() - corner2.getY()));
		
		return loc;
	}
	
	
	
}