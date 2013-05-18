package com.deaboy.manhunt.map;

import org.bukkit.Location;

public class ManhuntZone extends Zone
{
	//---------------- Constructors ----------------//
	public ManhuntZone(String name, Location primaryCorner, Location secondaryCorner)
	{
		this(name, primaryCorner, secondaryCorner, false, 0);
	}
	
	public ManhuntZone(String name, Location primaryCorner, Location secondaryCorner, boolean ignoreY)
	{
		this(name, primaryCorner, secondaryCorner, ignoreY, 0);
	}
	
	public ManhuntZone(String name, Location primaryCorner, Location secondaryCorner, boolean ignoreY, int flags)
	{
		super(name, primaryCorner, secondaryCorner, ignoreY, flags);
	}
	
	
	
}
