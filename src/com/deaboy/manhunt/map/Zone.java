package com.deaboy.manhunt.map;

import org.bukkit.Location;

public abstract class Zone
{
	//---------------- Properties ----------------/
	private int flags;
	private String name;
	private Location corner1;
	private Location corner2;
	private boolean ignoreY;
	
	
	
	//---------------- Constructors ----------------//
	public Zone(String name, Location primaryCorner, Location secondaryCorner, boolean ignoreY, int flags)
	{
		if (primaryCorner == null || primaryCorner == null)
			throw new IllegalArgumentException("Both locations must not be null.");
		if (primaryCorner.getWorld() != secondaryCorner.getWorld())
			throw new IllegalArgumentException("Both locations must be in the same world.");
		
		this.flags = flags;
		this.name = name;
		this.corner1 = primaryCorner.clone();
		this.corner2 = secondaryCorner.clone();
		this.ignoreY = ignoreY;
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the name for this zone.
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Gets the primary corner of this zone.
	 * @return
	 */
	public Location getPrimaryCorner()
	{
		return this.corner1.clone();
	}
	
	/**
	 * Gets the second corner of this zone.
	 * @return
	 */
	public Location getSecondaryCorner()
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
	 * Changes the name of this zone.
	 * @param name The new name for the zone.
	 */
	public void setName(String name)
	{
		if (name != null)
			this.name = name;
	}
	
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
	
	/**
	 * Sets a flag's value on a Zone.
	 * @param flag The flag to change.
	 * @param val The value of the flag. true for on, false for off.
	 */
	public void setFlag(ZoneFlag flag, boolean val)
	{
		if (flag == null)
		{
			throw new IllegalArgumentException("Argument 'flag' cannot be null.");
		}
		flags |= 1 << flag.ordinal();
	}
	
	/** 
	 * Sets this zone's falgs using an integer.
	 * @param flagCode The integer code for zone flags.
	 */
	public void setFlags(int flagCode)
	{
		this.flags = flagCode;
	}
	
	/**
	 * Checks whether a flag was set on this Zone.
	 * @param flag The flag to check for.
	 * @return True if the flag is set, false if not.
	 */
	public boolean checkFlag(ZoneFlag flag)
	{
		if (flag == null)
		{
			throw new IllegalArgumentException("Argument 'flag' canot be null.");
		}
		return (flags & 1 << flag.ordinal()) != 0;
	}
	
	/**
	 * Gets an integer containing the state of this zone's flags.
	 * @return integer code representing the set and unset flags.
	 */
	public int getFlags()
	{
		return flags;
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
	public boolean containsLocation(Location loc)
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