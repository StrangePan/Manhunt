package com.bendude56.hunted.map.models;

import java.util.ArrayList;

import org.bukkit.World;

import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.map.ManhuntMap;
import com.bendude56.hunted.map.Map;

public class SimpleMap
{
	//---------------- Properties ----------------//
	public String Version;
	
	public String name;
	public SimpleLocation spawn;
	public ArrayList<SimpleSpawn> HunterSpawns;
	public ArrayList<SimpleSpawn> PreySpawns;
	public ArrayList<SimpleSpawn> SetupSpawns;
	
	
	
	//---------------- Constructors ----------------//
	public SimpleMap()
	{
		Version = NewManhuntPlugin.getVersion();
		HunterSpawns = new ArrayList<SimpleSpawn>();
		PreySpawns = new ArrayList<SimpleSpawn>();
		SetupSpawns = new ArrayList<SimpleSpawn>();
	}
	
	
	
	//---------------- Public Methods ----------------//
	public Map toMap(World world)
	{
		Map map = new ManhuntMap();
		
		
	}
}
