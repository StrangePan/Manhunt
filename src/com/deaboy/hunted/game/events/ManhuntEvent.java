package com.deaboy.hunted.game.events;

import java.util.ArrayList;
import java.util.List;

public class ManhuntEvent implements Event
{
	
	//---------------- Properties ----------------//
	private List<Action> actions;
	private Long time;
	private boolean expired;
	
	
	//---------------- Constructors ----------------//
	public ManhuntEvent(Long time)
	{
		this.actions = new ArrayList<Action>();
		this.time = time;
		this.expired = false;
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public List<Action> getActions()
	{
		return actions;
	}
	
	@Override
	public Long getTriggerTime()
	{
		return time;
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setTriggerTime(Long time)
	{
		this.time = time;
	}
	
	@Override
	public void setExpired(boolean expired)
	{
		this.expired = expired;
	}
	
	
	//---------------- Public Methods ----------------//
	@Override
	public void addAction(Action action)
	{
		if (!actions.contains(action))
			actions.add(action);
	}
	
	@Override
	public void removeAction(Action action)
	{
		if (actions.contains(action))
			actions.remove(action);
	}
	
	@Override
	public void clearActions()
	{
		this.actions.clear();
	}
	
	@Override
	public void execute()
	{
		for (Action action : actions)
		{
			action.execute();
		}
		expired = true;
	}
	
	@Override
	public boolean isExpired()
	{
		return expired;
	}
	
	
}
