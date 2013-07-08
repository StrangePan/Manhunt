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
	
	
	//---------------- Public Methods ----------------//
	@Override
	public ManhuntWorldEvent addAction(Action action)
	{
		super.addAction(action);
		return this;
	}
	@Override
	public ManhuntWorldEvent removeAction(Action action)
	{
		super.removeAction(action);
		return this;
	}
	@Override
	public ManhuntWorldEvent clearActions()
	{
		super.clearActions();
		return this;
	}
	
	
}


