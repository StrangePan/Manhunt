package com.deaboy.manhunt.map.models;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.map.ManhuntZone;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.World;

public class SimpleZone
{
	//---------------- Properties ----------------//
	public String Version;
	public int Flags;
	public String Name;
	public SimpleLocation PrimaryCorner;
	public SimpleLocation SecondaryCorner;
	public byte IgnoreY;
	
	
	
	//---------------- Constructor ----------------//
	public SimpleZone()
	{
		this.Version = ManhuntPlugin.getVersion();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Zone toZone(World world)
	{
		Zone zone = new ManhuntZone(Name, PrimaryCorner.toLocation(world), SecondaryCorner.toLocation(world), (char) IgnoreY != 0);
		zone.setFlags(Flags);
		return zone;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleZone fromZone(Zone zone)
	{
		SimpleZone model = new SimpleZone();
		model.Flags = zone.getFlags();
		model.Name = zone.getName();
		model.PrimaryCorner = SimpleLocation.fromLocation(zone.getPrimaryCorner());
		model.SecondaryCorner = SimpleLocation.fromLocation(zone.getSecondaryCorner());
		model.IgnoreY = zone.getIgnoreY() ? (byte) (char) 1 : (byte) (char) 0;
		return model;
	}
	
	
	
}
