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
	public ManhuntEvent addAction(Action action)
	{
		if (!actions.contains(action))
			actions.add(action);
		return this;
	}
	@Override
	public ManhuntEvent removeAction(Action action)
	{
		if (actions.contains(action))
			actions.remove(action);
		return this;
	}
	@Override
	public ManhuntEvent clearActions()
	{
		this.actions.clear();
		return this;
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



