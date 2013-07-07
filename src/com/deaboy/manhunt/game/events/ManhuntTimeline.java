package com.deaboy.manhunt.game.events;

import java.util.Collections;
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
	private LinkedList<WorldEvent> worldevents;
	private int schedule = -1;
	private int event;
	private int worldevent;
	private long basetime;
	private long worldbasetime;
	
	
	//---------------- Constructors ----------------//
	public ManhuntTimeline(World world)
	{
		this.world = world;
		this.events = new LinkedList<Event>();
		this.worldevents = new LinkedList<WorldEvent>();
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
		Collections.sort(events);
		Collections.sort(worldevents);
	}
	
	
	//---------------- Public Methods ----------------//
	@Override
	public void registerEvent(Event event)
	{
		if (event == null)
			return;
		
		if (event instanceof WorldEvent && !worldevents.contains(event))
		{
			worldevents.add((WorldEvent) event);
		}
		else if (!events.contains(event))
		{
			events.add(event);
		}
	}
	@Override
	public void cancelEvent(Event event)
	{
		if (events.contains(event))
		{
			events.remove(event);
		}
		if (worldevents.contains(event))
		{
			worldevents.remove(event);
		}
	}
	@Override
	public void run()
	{
		basetime = new Date().getTime();
		worldbasetime = getWorld().getFullTime();
		worldevent = 0;
		event = 0;
		sortEvents();
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				if (event < events.size() && new Date().getTime() >= basetime + events.get(event).getTriggerTime())
				{
					events.get(event).execute();
					event++;
				}
				if (worldevent < worldevents.size() && worldevents.get(worldevent).getWorld().getFullTime() >= worldbasetime + worldevents.get(worldevent).getTriggerTime())
				{
					worldevents.get(worldevent).execute();
					worldevent++;
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
