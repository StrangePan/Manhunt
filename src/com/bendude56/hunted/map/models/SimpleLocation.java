package com.bendude56.hunted.map.models;

import org.bukkit.Location;
import org.bukkit.World;

import com.bendude56.hunted.NewManhuntPlugin;

public class SimpleLocation
{
	public String version;
	
	public Double x;
	public Double y;
	public Double z;
	public Float yaw;
	public Float pitch;
	
	
	//---------------- Constructors ----------------//
	public SimpleLocation()
	{
		this.version = NewManhuntPlugin.getVersion();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Location toLocation(World world)
	{
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleLocation fromLocation(Location loc)
	{
		SimpleLocation model = new SimpleLocation();
		
		model.x = loc.getX();
		model.y = loc.getY();
		model.z = loc.getZ();
		model.yaw = loc.getYaw();
		model.pitch = loc.getPitch();
		
		return model;
	}
	
}
