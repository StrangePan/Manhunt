package com.bendude56.hunted.game.events;

import org.bukkit.World;

public class ManhuntWorldEvent extends ManhuntEvent implements WorldEvent
{

	//---------------- Properties ----------------//
	private World world;
	
	
	//---------------- Constructors ----------------//
	public ManhuntWorldEvent(World world, Long time)
	{
		super(time);
		this.world = world;
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public World getWorld()
	{
		return world;
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	
	//---------------- Public Methods ----------------//
	@Override
	public boolean isReady()
	{
		return world.getFullTime() >= getTriggerTime();
	}
	

}
