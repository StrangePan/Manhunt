package com.deaboy.manhunt.map.models;

import java.util.ArrayList;
import java.util.List;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.map.ManhuntMap;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.Zone;

public class SimpleMap
{
	//---------------- Properties ----------------//
	public String Version;
	
	public String Name;
	public SimpleLocation Spawn;
	public List<SimpleSpawn> Spawns;
	public List<SimpleZone> Zones;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleMap()
	{
		this.Version = ManhuntPlugin.getVersion();
		this.Name = new String();
		this.Spawn = null;
		this.Spawns = new ArrayList<SimpleSpawn>();
		this.Zones = new ArrayList<SimpleZone>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Map toMap(World world)
	{
		Map map = new ManhuntMap(Name, world);
		
		map.setSpawn(Spawn.toLocation(world));
		for (SimpleSpawn spawn : Spawns)
			map.addPoint(spawn.toSpawn(world));
		for (SimpleZone zone : Zones)
			map.addZone(zone.toZone(world));
		
		return map;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleMap fromMap(Map map)
	{
		SimpleMap model = new SimpleMap();
		model.Name = map.getName();
		model.Spawn = SimpleLocation.fromLocation(map.getSpawnLocation());
		for (Spawn spawn : map.getPoints())
			model.Spawns.add(SimpleSpawn.fromSpawn(spawn));
		for (Zone zone : map.getZones())
			model.Zones.add(SimpleZone.fromZone(zone));
		return model;
	}
	
	
	
	
}
