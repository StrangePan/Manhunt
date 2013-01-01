package com.bendude56.hunted.map.models;

import java.util.ArrayList;

import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.map.World;

public class SimpleWorld
{
	//---------------- Properties ----------------//
	public String version;
	public String name;
	public SimpleSpawn spawn;
	public ArrayList<SimpleMap> maps;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleWorld()
	{
		this.version = NewManhuntPlugin.getVersion();
		this.maps = new ArrayList<SimpleMap>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public void toManhuntWorld(World world)
	{
		
	}
	
	public static SimpleWorld fromManhuntWorld(World world)
	{
		SimpleWorld model = new SimpleWorld();
		
		model.name = world.getWorld().getName();
		model.spawn = SimpleSpawn.fromSpawn(world.getSpawn());
		
		for (Map map : world.getMaps())
			model.maps(SimpleMap.fromMap(map));
		
		
		return model;
	}
	
}
