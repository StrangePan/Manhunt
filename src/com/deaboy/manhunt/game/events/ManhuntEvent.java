package com.deaboy.manhunt.game.events;

import java.util.ArrayList;
import java.util.List;


public class ManhuntEvent implements Event
{
	//---------------- Properties ----------------//
	private List<Action> actions;
	private Long delay;
	private boolean expired;
	
	
	//---------------- Constructors ----------------//
	/**
	 * Creates a new Event and requires two arguments:
	 * @param time The time in ticks to activate.
	 */
	public ManhuntEvent(Long time)
	{
		this.delay = time;
		this.actions = new ArrayList<Action>();
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
		return delay;
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setTriggerTime(Long time)
	{
		this.delay = time;
	}
	@Override
	public void setExpired(boolean expired)
	{
		this.expired = false;
	}
	@Override
	public void reset()
	{
		this.expired = false;
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
	@Override
	public int compareTo(Event arg0)
	{
		return (int) (this.getTriggerTime() - arg0.getTriggerTime());
	}
	
	
}



