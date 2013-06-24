package com.deaboy.manhunt.map.models;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.map.ManhuntSpawn;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.SpawnType;
import com.deaboy.manhunt.map.World;

public class SimpleSpawn
{
	//---------------- Properties ----------------//
	public String Version;
	
	public String Name;
	public int Type;
	public SimpleLocation Location;
	public int Range;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleSpawn()
	{
		Version = ManhuntPlugin.getVersion();
		this.Name = new String();
		this.Type = 0;
		this.Location = null;
		this.Range = 0;
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Spawn toSpawn(World world)
	{
		Spawn spawn = new ManhuntSpawn(Name, SpawnType.fromId(Type), Location.toLocation(world), Range);
		
		return spawn;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleSpawn fromSpawn(Spawn spawn)
	{
		SimpleSpawn model = new SimpleSpawn();
		
		model.Name = spawn.getName();
		model.Type = spawn.getType().getId();
		model.Location = SimpleLocation.fromLocation(spawn.getLocation());
		model.Range = spawn.getRange();
		
		return model;
	}
	
	
	
}
