package com.bendude56.hunted.map;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntMap implements Map
{
	//-------------------- Constants --------------------//
	private static final int MAX_SETUP = 5;
	private static final int MAX_HUNTERS = 5;
	private static final int MAX_PREY = 5;
	
	//-------------------- Properties --------------------//
	private Spawn spawn;
	private Spawn[] setup;
	private Spawn[] hunter;
	private Spawn[] prey;
	private Location boundary_world1;
	private Location boundary_world2;
	private Location boundary_setup1;
	private Location boundary_setup2;
	
	
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
		this.setup = new Spawn[MAX_SETUP];
		this.hunter = new Spawn[MAX_HUNTERS];
		this.prey = new Spawn[MAX_PREY];
	}
	
	
	//-------------------- Public Methods --------------------//
	
	//---------------- Getters ----------------//
	@Override
	public Spawn getSpawn()
	{
		return spawn;
	}

	@Override
	public World getWorld()
	{
		return getLocation().getWorld();
	}

	@Override
	public Location getLocation()
	{
		return spawn.getLocation();
	}
	
	@Override
	public Spawn[] getSetupSpawns()
	{
		return setup;
	}
	
	@Override
	public Spawn[] getHunterSpawns()
	{
		return hunter;
	}
	
	@Override
	public Spawn[] getPreySpawns()
	{
		return prey;
	}
	
	@Override
	public Location getMapBoundary(int index)
	{
		if (index == 1)
			return (boundary_world1 == null ? null : boundary_world1.clone());
		else if (index == 2)
			return (boundary_world2 == null ? null : boundary_world2.clone());
		else
			throw new IllegalArgumentException("index must be either 1 or 2");
	}
	
	@Override
	public Location getSetupBoundary(int index)
	{
		if (index == 1)
			return (boundary_setup1 == null ? null : boundary_setup1.clone());
		else if (index == 2)
			return (boundary_setup2 == null ? null : boundary_setup2.clone());
		else
			throw new IllegalArgumentException("index must be either 1 or 2");
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setSetupSpawn(int index, Spawn spawn)
	{
		if (index < 0 || index >= MAX_SETUP)
			throw new IllegalArgumentException("index must be between 0 and 4 inclusive");
		
		setup[index] = spawn;
	}
	
	@Override
	public void setHunterSpawn(int index, Spawn spawn)
	{
		if (index < 0 || index >= MAX_HUNTERS)
			throw new IllegalArgumentException("index must be between 0 and 4 inclusive");
		
		hunter[index] = spawn;
	}
	
	@Override
	public void setPreySpawn(int index, Spawn spawn)
	{
		if (index < 0 || index >= MAX_PREY)
			throw new IllegalArgumentException("index must be between 0 and 4 inclusive");
		
		prey[index] = spawn;
	}
	
	@Override
	public void setMapBoundary(int index, Location loc)
	{
		if (index == 1)
			boundary_world1 = (loc == null ? null : loc.clone());
		else if (index == 2)
			boundary_world2 = (loc == null ? null : loc.clone());
		else
			throw new IllegalArgumentException("index must be between either 1 or 2.");
	}
	
	@Override
	public void setSetupBoundary(int index, Location loc)
	{
		if (index == 1)
			boundary_setup1 = (loc == null ? null : loc.clone());
		else if (index == 2)
			boundary_setup2 = (loc == null ? null : loc.clone());
		else
			throw new IllegalArgumentException("index must be between either 1 or 2.");
	}
	
	
}
