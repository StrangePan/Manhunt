package com.deaboy.manhunt.map;

import org.bukkit.Location;

public class ManhuntZone extends Zone
{
	//---------------- Constructors ----------------//
	public ManhuntZone(ZoneType type, Location corner1, Location corner2)
	{
		this(type, corner1, corner2, false);
	}
	
	public ManhuntZone(ZoneType type, Location corner1, Location corner2, boolean ignoreY)
	{
		super(type, corner1, corner2, ignoreY);
	}
	
	
	
}
