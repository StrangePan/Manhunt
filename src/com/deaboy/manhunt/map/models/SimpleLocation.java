package com.deaboy.manhunt.map.models;

import org.bukkit.Location;

import com.deaboy.manhunt.NewManhuntPlugin;
import com.deaboy.manhunt.map.World;

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
		return new Location(world.getWorld(), x, y, z, yaw, pitch);
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
