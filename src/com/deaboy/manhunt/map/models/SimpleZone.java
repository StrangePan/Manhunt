package com.deaboy.manhunt.map.models;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.map.ManhuntZone;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneType;
import com.deaboy.manhunt.map.World;

public class SimpleZone
{
	//---------------- Properties ----------------//
	public String Version;
	public int Type;
	public String Name;
	public SimpleLocation Corner1;
	public SimpleLocation Corner2;
	public byte IgnoreY;
	
	
	
	//---------------- Constructor ----------------//
	public SimpleZone()
	{
		this.Version = ManhuntPlugin.getVersion();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Zone toZone(World world)
	{
		return new ManhuntZone(ZoneType.fromId(Type), Name, Corner1.toLocation(world), Corner2.toLocation(world), (char) IgnoreY != 0);
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleZone fromZone(Zone zone)
	{
		SimpleZone model = new SimpleZone();
		model.Type = zone.getType().getId();
		model.Name = zone.getName();
		model.Corner1 = SimpleLocation.fromLocation(zone.getCorner1());
		model.Corner2 = SimpleLocation.fromLocation(zone.getCorner2());
		model.IgnoreY = zone.getIgnoreY() ? (byte) (char) 1 : (byte) (char) 0;
		return model;
	}
	
	
	
}
