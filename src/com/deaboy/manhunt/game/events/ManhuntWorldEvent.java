package com.deaboy.manhunt.game.events;

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
	
	
}


