package com.bendude56.hunted.game.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.bendude56.hunted.ManhuntPlugin;

public class GameTimeline implements Timeline
{
	
	private List<Event> events;
	
	Integer schedule;
	Event current_event_world = null;
	Event current_event_time = null;
	
	public GameTimeline()
	{
		this.events = new ArrayList<Event>();
	}
	
	@Override
	public void registerEvent(Event event)
	{
		if (!this.events.contains(event))
			this.events.add(event);
	}
	
	@Override
	public void cancelEvent(Event event)
	{
		if (this.events.contains(event))
		{
			this.events.remove(event);
		}
	}
	
	@Override
	public List<Event> getRegisteredEvents()
	{
		return new ArrayList<Event>(this.events);
	}

	
	@Override
	public void run()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				timelineStep();
			}
		}, 0, 0);
	}
	
	@Override
	public void stop()
	{
		Bukkit.getScheduler().cancelTask(schedule);
	}
	
	
	private void timelineStep()
	{
		for (Event event : events)
		{
			event.executeIfReady();
		}
	}

}
