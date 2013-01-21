package com.deaboy.hunted.map.models;

import org.bukkit.World;

import com.deaboy.hunted.NewManhuntPlugin;
import com.deaboy.hunted.map.ManhuntZone;
import com.deaboy.hunted.map.Zone;
import com.deaboy.hunted.map.ZoneType;

public class SimpleZone
{
	//---------------- Properties ----------------//
	public String Version;
	public Integer Type;
	public SimpleLocation Corner1;
	public SimpleLocation Corner2;
	public Boolean IgnoreY;
	
	
	
	//---------------- Constructor ----------------//
	public SimpleZone()
	{
		this.Version = NewManhuntPlugin.getVersion();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Zone toZone(World world)
	{
		return new ManhuntZone(ZoneType.fromId(Type), Corner1.toLocation(world), Corner2.toLocation(world), IgnoreY);
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleZone fromZone(Zone zone)
	{
		SimpleZone model = new SimpleZone();
		model.Type = zone.getType().getId();
		model.Corner1 = SimpleLocation.fromLocation(zone.getCorner1());
		model.Corner2 = SimpleLocation.fromLocation(zone.getCorner2());
		model.IgnoreY = zone.getIgnoreY();
		return model;
	}
	
	
	
}
