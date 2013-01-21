package com.deaboy.hunted.map.models;

import java.util.ArrayList;

import org.bukkit.World;

import com.deaboy.hunted.NewManhuntPlugin;
import com.deaboy.hunted.map.ManhuntMap;
import com.deaboy.hunted.map.Map;
import com.deaboy.hunted.map.Spawn;
import com.deaboy.hunted.map.Zone;

public class SimpleMap
{
	//---------------- Properties ----------------//
	public String Version;
	
	public String Name;
	public SimpleLocation Spawn;
	public ArrayList<SimpleSpawn> HunterSpawns;
	public ArrayList<SimpleSpawn> PreySpawns;
	public ArrayList<SimpleSpawn> SetupSpawns;
	public ArrayList<SimpleZone> Zones;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleMap()
	{
		this.Version = NewManhuntPlugin.getVersion();
		this.HunterSpawns = new ArrayList<SimpleSpawn>();
		this.PreySpawns = new ArrayList<SimpleSpawn>();
		this.SetupSpawns = new ArrayList<SimpleSpawn>();
		this.Zones = new ArrayList<SimpleZone>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Map toMap(World world)
	{
		Map map = new ManhuntMap(Name, world);
		
		for (SimpleSpawn spawn : HunterSpawns)
			map.addHunterSpawn(spawn.toSpawn(world));
		for (SimpleSpawn spawn : PreySpawns)
			map.addPreySpawn(spawn.toSpawn(world));
		for (SimpleSpawn spawn : SetupSpawns)
			map.addSetupSpawn(spawn.toSpawn(world));
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
