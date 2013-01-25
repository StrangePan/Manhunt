package com.deaboy.hunted.map.models;

import org.bukkit.World;

import com.deaboy.hunted.NewManhuntPlugin;
import com.deaboy.hunted.map.ManhuntSpawn;
import com.deaboy.hunted.map.Spawn;

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
