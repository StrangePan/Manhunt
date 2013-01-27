package com.deaboy.manhunt.map.models;

import com.deaboy.manhunt.NewManhuntPlugin;
import com.deaboy.manhunt.map.ManhuntSpawn;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.World;

public class SimpleSpawn
{
	//---------------- Properties ----------------//
	public String Version;
	
	public SimpleLocation Location;
	public Integer Range;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleSpawn()
	{
		Version = NewManhuntPlugin.getVersion();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Spawn toSpawn(World world)
	{
		Spawn spawn = new ManhuntSpawn(Location.toLocation(world));
		
		spawn.setRange(Range);
		
		return spawn;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleSpawn fromSpawn(Spawn spawn)
	{
		SimpleSpawn model = new SimpleSpawn();
		
		model.Location = SimpleLocation.fromLocation(spawn.getLocation());
		model.Range = spawn.getRange();
		
		return model;
	}
	
	
	
}
