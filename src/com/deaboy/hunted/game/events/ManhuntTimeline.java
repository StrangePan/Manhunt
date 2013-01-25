package com.deaboy.hunted.game.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.deaboy.hunted.Manhunt;
import com.deaboy.hunted.NewManhuntPlugin;
import com.deaboy.hunted.lobby.Team;

public class ManhuntTimeline implements Timeline
{

	//---------------- Properties ----------------//
	private World world;
	private List<Event> events;
	private int schedule = -1;
	
	
	//---------------- Helper Variables ----------------//
	private int event;
	private Event[] event_array;
	
	
	//---------------- Constructors ----------------//
	public ManhuntTimeline()
	{
		this(null);
	}
	
	public ManhuntTimeline(World world)
	{
		this.world = world;
		this.events = new ArrayList<Event>();
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
		event_array = (Event[]) events.toArray();
		int min_index = 0;
		Event temp_event;
		
		for (int i = 0; i < event_array.length; i++)
		{
			for (int j = i; j < event_array.length + 2; j++)
			{
				if (event_array[j].getTriggerTime() < event_array[min_index].getTriggerTime())
				{
					min_index = j;
				}
			}
			temp_event = event_array[i];
			event_array[i] = event_array[min_index];
			event_array[min_index] = temp_event;
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
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(NewManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				if (new Date().getTime() >= event_array[event].getTriggerTime())
				{
					event_array[event].execute();
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
	
	
	//---------------- 	Public Static Constructors ----------------//
	
	public static ManhuntTimeline newStandardTimeline(long lobby_id)
	{
		// Create the new timeline and placeholder event
		ManhuntTimeline timeline = new ManhuntTimeline();
		Event event;
		long time = 0L;
		
		
		// Start defining events and actions
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "Prepapre for Teleport!", Team.PREY, Team.HUNTERS));
		timeline.registerEvent(event);
		
		/*
		 * 
		 */
		
		event = new ManhuntEvent(time += 5000);
		event.addAction(new TeleportTeamAction(lobby_id, Team.PREY, Manhunt.getLobby(lobby_id).getCurrentMap().getPreySpawns()));
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, Manhunt.getLobby(lobby_id).getCurrentMap().getHunterSpawns()));
		timeline.registerEvent(event);
		
		/*
		 * 
		 * 
		 * 
		 */
		
		event = new ManhuntEvent(time += 3000);
		event.addAction(new BroadcastAction(lobby_id, "Setup begins in 5...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "4...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "3...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "2...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "1...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "The game has begun!", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		event.addAction(new LogAction(Level.INFO, "A Manhunt game was started in lobby " + Manhunt.getLobby(lobby_id).getName()));
		timeline.registerEvent(event);
		
		/*
		 * 
		 * 
		 * 
		 * 
		 */
		
		event = new ManhuntEvent(time += Manhunt.getSettings().TIME_SETUP.getValue() * 60000 - 20 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "Only one day left in the hunt!", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		/*
		 * 
		 */
		
		event = new ManhuntEvent(time += 15 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "5 minutes left in the hunt!", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		/*
		 */
		
		event = new ManhuntEvent(time += 4 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "1 minute left in the hunt!", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 30000);
		event.addAction(new BroadcastAction(lobby_id, "30 seconds left!", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 20000);
		event.addAction(new BroadcastAction(lobby_id, "10...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "9...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "8...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "7...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "6...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "5...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "4...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "3...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "2...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "1...", Team.PREY, Team.HUNTERS, Team.SPECTATORS));
		timeline.registerEvent(event);
		
		
		
		
		
		
		return timeline;
	}

}
