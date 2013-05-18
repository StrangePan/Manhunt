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
	public List<SimpleSpawn> HunterSpawns;
	public List<SimpleSpawn> PreySpawns;
	public List<SimpleSpawn> SetupSpawns;
	public List<SimpleZone> Zones;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleMap()
	{
		this.Version = ManhuntPlugin.getVersion();
		this.HunterSpawns = new ArrayList<SimpleSpawn>();
		this.PreySpawns = new ArrayList<SimpleSpawn>();
		this.SetupSpawns = new ArrayList<SimpleSpawn>();
		this.Zones = new ArrayList<SimpleZone>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Map toMap(World world)
	{
		Map map = new ManhuntMap(Name, world);
		
		map.setName(Name);
		map.setSpawn(Spawn.toLocation(world));
		for (SimpleSpawn spawn : HunterSpawns)
			map.addHunterSpawn(spawn.toSpawn(world));
		for (SimpleSpawn spawn : PreySpawns)
			map.addPreySpawn(spawn.toSpawn(world));
		for (SimpleSpawn spawn : SetupSpawns)
			map.addSetupSpawn(spawn.toSpawn(world));
		for (SimpleZone zone : Zones)
		{
			Zone z = zone.toZone(world);
			map.createZone(z.getName(), z.getPrimaryCorner(), z.getSecondaryCorner());
		}
		
		return map;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleMap fromMap(Map map)
	{
		SimpleMap model = new SimpleMap();
		model.Name = map.getName();
		model.Spawn = SimpleLocation.fromLocation(map.getSpawnLocation());
		for (Spawn spawn : map.getHunterSpawns())
			model.HunterSpawns.add(SimpleSpawn.fromSpawn(spawn));
		for (Spawn spawn : map.getPreySpawns())
			model.PreySpawns.add(SimpleSpawn.fromSpawn(spawn));
		for (Spawn spawn : map.getSetupSpawns())
			model.SetupSpawns.add(SimpleSpawn.fromSpawn(spawn));
		for (Zone zone : map.getZones())
			model.Zones.add(SimpleZone.fromZone(zone));
		return model;
	}
	
	
	
	
}
