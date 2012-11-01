package com.bendude56.hunted.game.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.World;

public class GameEvent implements Event
{

	private Long trigger_time;
	private World world;
	private List<Action> actions;
	private Boolean expired;
	
	/**
	 * Creates a new GameEvent that triggers on or after the
	 * given world's given full_time.
	 * @param trigger_time The time measured in game_ticks that this event
	 * will trigger
	 * @param world The world whose time to monitor
	 */
	public GameEvent(Long trigger_time, World world)
	{
		this.trigger_time = trigger_time;
		this.world = world;
		this.actions = new ArrayList<Action>();
		this.expired = false;
	}
	
	/**
	 * Creates a new GameEvent that triggers when or after
	 * the given real time.
	 * @param trigger_time The event's trigger_time measured in milliseconds
	 */
	public GameEvent(Long trigger_time)
	{
		this.trigger_time = trigger_time;
		this.world = null;
		this.actions = new ArrayList<Action>();
	}

	@Override
	public void addAction(Action action)
	{
		if (!this.actions.contains(action))
			this.actions.add(action);
	}

	@Override
	public void removeAction(Action action)
	{
		if (this.actions.contains(action))
			this.actions.remove(action);
	}
	
	@Override
	public void removeAllActions()
	{
		this.actions.clear();
	}

	@Override
	public void setTriggerTime(Long time, World world)
	{
		this.trigger_time = time;
		this.world = world;
	}

	@Override
	public void setTriggerTime(Long time)
	{
		this.setTriggerTime(time, null);
	}

	@Override
	public List<Action> getActions()
	{
		return new ArrayList<Action>(this.actions);
	}

	@Override
	public Long getTriggerTime()
	{
		return this.trigger_time;
	}

	@Override
	public World getWorld()
	{
		return this.world;
	}

	@Override
	public void execute()
	{
		expired = true;
		
		for (Action action : actions)
		{
			action.execute();
		}
	}

	@Override
	public void executeIfReady()
	{
		if (expired)
		{
			return;
		}
		
		long time_to_compare_to;
		
		if (world == null)
		{
			time_to_compare_to = new Date().getTime();
		}
		else
		{
			time_to_compare_to = world.getFullTime();
		}
		
		if (trigger_time > time_to_compare_to)
		{
			execute();
		}
			
	}
	
	@Override
	public boolean isReady()
	{
		
		long time_to_compare_to;
		
		if (world == null)
		{
			time_to_compare_to = new Date().getTime();
		}
		else
		{
			time_to_compare_to = world.getFullTime();
		}
		
		if (trigger_time > time_to_compare_to && !expired)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isExpired()
	{
		return this.expired;
	}

}
