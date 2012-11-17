package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntMap implements Map
{
	//-------------------- Constants --------------------//
	private static final int MAX_HUNTERS = 5;
	private static final int MAX_PREY = 5;
	
	//-------------------- Properties --------------------//
	private Spawn spawn;
	private Spawn[] hunter;
	private Spawn[] prey;
	
	
	//-------------------- Constructors --------------------//
	/**
	 * Initializes a new ManhuntMap at the given World's spawn location.
	 * @param world The World of the new Map.
	 */
	public ManhuntMap(World world)
	{
		this(new ManhuntSpawn(world.getSpawnLocation()));
	}
	
	/**
	 * Initializes a new MahuntMap at the given Location.
	 * @param loc The Location of the new Map.
	 */
	public ManhuntMap(Location loc)
	{
		this(new ManhuntSpawn(loc));
	}
	
	/**
	 * Initializes a new ManhuntMap with the given Spawn.
	 * @param spawn The Spawn of the new Map.
	 */
	public ManhuntMap(Spawn spawn)
	{
		this.spawn = spawn;
		hunter = new Spawn[MAX_HUNTERS];
		prey = new Spawn[MAX_PREY];
	}
	
	
	//-------------------- Public Methods --------------------//
	
	//---------------- Getters ----------------//
	public Spawn getSpawn()
	{
		return spawn;
	}
	
	public World getWorld()
	{
		return getLocation().getWorld();
	}
	
	public Location getLocation()
	{
		return spawn.getLocation();
	}
	
	public Spawn[] getHunterSpawns()
	{
		return hunter;
	}
	
	public Spawn[] getPreySpawns()
	{
		return prey;
	}
	
	
	//---------------- Setters ----------------//
	public void setHunterSpawn(int index, Spawn spawn) throws IllegalArgumentException
	{
		if (index < 0 || index >= MAX_HUNTERS)
			throw new IllegalArgumentException("index must be between 0 and 4 inclusive");
		
		hunter[index] = spawn;
	}
	
	public void setPreySpawn(int index, Spawn spawn) throws IllegalArgumentException
	{
		if (index < 0 || index >= MAX_PREY)
			throw new IllegalArgumentException("index must be between 0 and 4 inclusive");
		
		prey[index] = spawn;
	}
	
	
	
}
