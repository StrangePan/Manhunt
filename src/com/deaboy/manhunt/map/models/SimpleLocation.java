package com.deaboy.manhunt.map.models;

import org.bukkit.Location;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.map.World;

public class SimpleLocation
{
	public String version;
	
	public double x;
	public double y;
	public double z;
	public float yaw;
	public float pitch;
	
	
	//---------------- Constructors ----------------//
	public SimpleLocation()
	{
		this.version = ManhuntPlugin.getVersion();
		
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
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
