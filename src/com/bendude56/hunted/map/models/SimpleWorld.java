package com.bendude56.hunted.map.models;

import java.util.ArrayList;

import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.map.World;
import com.bendude56.hunted.map.Spawn;

public class SimpleWorld
{
	//---------------- Properties ----------------//
	public String Version;
	public String Name;
	public SimpleSpawn Spawn;
	public ArrayList<SimpleMap> Maps;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleWorld()
	{
		this.Version = NewManhuntPlugin.getVersion();
		this.Maps = new ArrayList<SimpleMap>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public void toManhuntWorld(World world)
	{
		Spawn spawn = Spawn.toSpawn(world.getWorld());
		world.getSpawn().setLocation(spawn.getLocation());
		world.getSpawn().setRange(spawn.getRange());
		
		world.clearMaps();
		for (SimpleMap map : Maps)
		{
			Map m = map.toMap(world.getWorld());
			world.addMap(m.getName(), m);
		}
		
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleWorld fromManhuntWorld(World world)
	{
		SimpleWorld model = new SimpleWorld();
		
		model.Name = world.getWorld().getName();
		model.Spawn = SimpleSpawn.fromSpawn(world.getSpawn());
		
		for (Map map : world.getMaps())
			model.Maps.add(SimpleMap.fromMap(map));
		
		return model;
	}
	
}
