package com.bendude56.hunted.map.models;

import org.bukkit.World;

import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.map.ManhuntSpawn;
import com.bendude56.hunted.map.Spawn;

public class SimpleSpawn
{
	//---------------- Properties ----------------//
	public SimpleLocation Location;
	public Integer Range;
	
	public SimpleLocation Boundary1;
	public SimpleLocation Boundary2;
	public SimpleLocation Protection1;
	public SimpleLocation Protection2;
	
	public String Version;
	
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
		spawn.setBoundaryCorner1(Boundary1.toLocation(world));
		spawn.setBoundaryCorner2(Boundary2.toLocation(world));
		spawn.setProtectionCorner1(Protection1.toLocation(world));
		spawn.setProtectionCorner2(Protection2.toLocation(world));
		
		return spawn;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleSpawn fromSpawn(Spawn spawn)
	{
		SimpleSpawn model = new SimpleSpawn();
		
		model.Location = SimpleLocation.fromLocation(spawn.getLocation());
		model.Range = spawn.getRange();
		
		model.Boundary1 = SimpleLocation.fromLocation(spawn.getBoundaryCorner1());
		model.Boundary2 = SimpleLocation.fromLocation(spawn.getBoundaryCorner2());
		model.Protection1 = SimpleLocation.fromLocation(spawn.getProtectionCorner1());
		model.Protection2 = SimpleLocation.fromLocation(spawn.getProtectionCorner2());
		
		return model;
	}
	
	
	
}
