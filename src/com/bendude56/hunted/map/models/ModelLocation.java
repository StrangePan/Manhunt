package com.bendude56.hunted.map.models;

import org.bukkit.Location;
import org.bukkit.World;

public class ModelLocation
{
	public Double x;
	public Double y;
	public Double z;
	public Float yaw;
	public Float pitch;
	
	
	
	//---------------- Public Methods ----------------//
	public Location toLocation(World world)
	{
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public void fromLocation(Location loc)
	{
		this.x = loc.getX();
		this.y = loc.getY();
		this.z = loc.getZ();
		this.yaw = loc.getYaw();
		this.pitch = loc.getPitch();
	}
	
}
