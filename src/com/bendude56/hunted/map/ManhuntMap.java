package com.bendude56.hunted.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public class ManhuntMap implements Map
{//-------------------- Properties --------------------//
	private String name;
	private Location spawn;
	private List<Spawn> setup;
	private List<Spawn> hunter;
	private List<Spawn> prey;
	private Location boundary_world1;
	private Location boundary_world2;
	private Location boundary_setup1;
	private Location boundary_setup2;
	
	
	//-------------------- Constructors --------------------//
	/**
	 * Initializes a new ManhuntMap at the given World's spawn location.
	 * @param world The World of the new Map.
	 */
	public ManhuntMap(String name, World world)
	{
		this(name, world.getSpawnLocation());
	}
	
	/**
	 * Initializes a new ManhuntMap with the given spawn location.
	 * @param loc The spawn location of the new map.
	 */
	public ManhuntMap(String name, Location loc)
	{
		this.name = name;
		this.spawn = loc;
		this.setup = new ArrayList<Spawn>();
		this.hunter = new ArrayList<Spawn>();
		this.prey = new ArrayList<Spawn>();
	}
	
	
	//-------------------- Public Methods --------------------//
	
	//---------------- Getters ----------------//
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public Location getSpawnLocation()
	{
		return spawn;
	}

	@Override
	public World getWorld()
	{
		return getSpawnLocation().getWorld();
	}
	
	@Override
	public List<Spawn> getSetupSpawns()
	{
		return setup;
	}
	
	@Override
	public List<Spawn> getHunterSpawns()
	{
		return hunter;
	}
	
	@Override
	public List<Spawn> getPreySpawns()
	{
		return prey;
	}
	
	@Override
	public Location getMapBoundary1()
	{
		return boundary_world1.clone();
	}
	
	@Override
	public Location getMapBoundary2()
	{
		return boundary_world2.clone();
	}
	
	@Override
	public Location getSetupBoundary1()
	{
		return boundary_setup1.clone();
	}
	
	@Override
	public Location getSetupBoundary2()
	{
		return boundary_setup2.clone();
	}
	
	
	
	//---------------- Setters ----------------//
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public void setSpawn(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.spawn = loc.clone();
	}
	
	@Override
	public void addSetupSpawn(Spawn spawn)
	{
		if (spawn != null && !setup.contains(spawn) && spawn.getWorld() == getWorld())
			setup.add(spawn);
	}
	
	@Override
	public void addHunterSpawn(Spawn spawn)
	{
		if ( spawn != null && !hunter.contains(spawn) &&spawn.getWorld() == getWorld())
			hunter.add(spawn);
	}
	
	@Override
	public void addPreySpawn(Spawn spawn)
	{
		if (spawn != null && !prey.contains(spawn) && spawn.getWorld() == getWorld())
			prey.add(spawn);
	}
	
	@Override
	public void removeSetupSpawn(Spawn spawn)
	{
		if (setup.contains(spawn))
			setup.remove(spawn);
	}
	
	@Override
	public void removeHunterSpawn(Spawn spawn)
	{
		if (hunter.contains(spawn))
			hunter.remove(spawn);
	}
	
	@Override
	public void removePreySpawn(Spawn spawn)
	{
		if (prey.contains(spawn))
			prey.remove(spawn);
	}
	
	@Override
	public void setMapBoundary1(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.boundary_world1 = loc.clone();
	}
	
	@Override
	public void setMapBoundary2(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.boundary_world2 = loc.clone();
	}
	
	@Override
	public void setSetupBoundary1(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.boundary_setup1 = loc.clone();
	}
	
	@Override
	public void setSetupBoundary2(Location loc)
	{
		if (loc != null && loc.getWorld() == getWorld())
			this.boundary_setup2 = loc.clone();
	}
	
	
}
