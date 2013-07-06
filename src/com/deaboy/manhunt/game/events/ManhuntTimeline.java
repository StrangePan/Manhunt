package com.deaboy.manhunt.game.events;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.deaboy.manhunt.ManhuntPlugin;

public class ManhuntTimeline implements Timeline
{

	//---------------- Properties ----------------//
	private World world;
	private LinkedList<Event> events;
	private int schedule = -1;
	private int event;
	private long basetime;
	
	
	//---------------- Constructors ----------------//
	public ManhuntTimeline()
	{
		this(null);
	}
	
	public ManhuntTimeline(World world)
	{
		this.world = world;
		this.events = new LinkedList<Event>();
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public World getWorld()
	{
		return world;
	}
	
	@Override
	public List<Event> getRegisteredEvents()
	{
		return events;
	}
	
	@Override
	public boolean isRunning()
	{
		return schedule == -1;
	}
	
	
	//---------------- Setters ----------------//
	@Override
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	
	//---------------- Private Methods ----------------//
	private void sortEvents()
	{
		int flag;
		Event temp;
		
		for (int i = 0; i < events.size()-1; i++)
		{
			flag = i;
			for (int j = i; j < events.size(); j++)
			{
				if (events.get(j).getTriggerTime() < events.get(flag).getTriggerTime())
					flag = j;
			}
			if (flag != i)
			{
				temp = events.get(i);
				events.set(i, events.get(flag));
				events.set(flag, temp);
			}
		}
	}
	
	
	//---------------- Public Methods ----------------//
	@Override
	public void registerEvent(Event event)
	{
		
		if (event == null)
			return;
		
		if (events.contains(event))
		return;
		
		events.add(event);
		
	}
	
	@Override
	public void cancelEvent(Event event)
	{
		if (!events.contains(event))
			return;
		
		events.remove(event);
	}

	@Override
	public void run()
	{
		event = 0;
		sortEvents();
		basetime = new Date().getTime();
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				if (new Date().getTime() >= basetime + events.get(event).getTriggerTime())
				{
					events.get(event).execute();
					event++;
				}
			}
		}, 0, 0);
	}

	@Override
	public void stop()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		schedule = -1;
	}
	
	

}
