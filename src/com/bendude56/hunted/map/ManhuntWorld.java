package com.bendude56.hunted.map;

import java.util.HashMap;
import java.util.List;

public class ManhuntWorld implements World
{

	//---------------- Constants ----------------//
	private final org.bukkit.World world;
	private Spawn spawn;
	private HashMap<String, Map> maps;
	
	
	//---------------- Constructors ----------------//
	public ManhuntWorld(org.bukkit.World world)
	{
		if (world == null)
		{
			throw new IllegalArgumentException("Argument cannor be null.");
		}
		
		this.world = world;
		this.spawn = new ManhuntSpawn(world.getSpawnLocation());
		this.maps = new HashMap<String, Map>();
		
		load();
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public org.bukkit.World getWorld()
	{
		return world;
	}
	
	@Override
	public Spawn getLobbySpawn()
	{
		return spawn;
	}

	@Override
	public List<Map> getMaps()
	{
		return (List<Map>) maps.values();
	}

	@Override
	public Map getMap(String label)
	{
		if (maps.containsKey(label))
		{
			return maps.get(label);
		}
		else
		{
			return null;
		}
	}
	

	
	//---------------- Setters----------------//
	@Override
	public void addMap(String label, Map map)
	{
		if (!maps.containsKey(label))
		{
			maps.put(label, map);
		}
	}

	@Override
	public void clearMaps()
	{
		maps.clear();
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

}
