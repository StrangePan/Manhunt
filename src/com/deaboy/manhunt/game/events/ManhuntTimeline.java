package com.deaboy.manhunt.game.events;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.lobby.Team;

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
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, Manhunt.getLobby(lobby_id).getCurrentMap().getSetupSpawns()));
		timeline.registerEvent(event);
		
		/*
		 * 
		 * 
		 * 
		 */
		
		event = new ManhuntEvent(time += 3000);
		event.addAction(new BroadcastAction(lobby_id, "Setup begins in 5..."));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "4..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "3..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "2..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "1..."));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "GO! Use this time to prepare for the hunt!", Team.PREY));
		event.addAction(new BroadcastAction(lobby_id, "The prey are preparing for the hunt.", Team.HUNTERS));
		event.addAction(new BroadcastAction(lobby_id, "The hunt will begin at sundown."));
		timeline.registerEvent(event);
		
		/*
		 * 
		 * 
		 * 
		 */

		event = new ManhuntEvent(time += Manhunt.getSettings().TIME_SETUP.getValue() - 13000);
		event.addAction(new BroadcastAction(lobby_id, "Prepare for teleport!", Team.HUNTERS));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 5000);
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, Manhunt.getLobby(lobby_id).getCurrentMap().getHunterSpawns()));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 3000);
		event.addAction(new BroadcastAction(lobby_id, "The hunt begins in 5..."));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "4..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "3..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "2..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "1..."));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, Manhunt.getLobby(lobby_id).getCurrentMap().getHunterSpawns()));
		event.addAction(new BroadcastAction(lobby_id, "The hunt has begun! The game will end in " + Manhunt.getSettings().TIME_LIMIT.getValue() + " minutes."));
		event.addAction(new BroadcastAction(lobby_id, "Beware! The hunters have been released!", Team.PREY));
		event.addAction(new BroadcastAction(lobby_id, "Now's you're chance! Hunt the prey!", Team.HUNTERS));
		event.addAction(new LogAction(Level.INFO, "A Manhunt game was started in lobby " + Manhunt.getLobby(lobby_id).getName()));
		timeline.registerEvent(event);
		
		/*
		 * 
		 * 
		 * 
		 * 
		 */
		
		event = new ManhuntEvent(time += Manhunt.getSettings().TIME_LIMIT.getValue() * 60000 - 20 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "Only one day left in the hunt!"));
		timeline.registerEvent(event);
		
		/*
		 * 
		 */
		
		event = new ManhuntEvent(time += 15 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "5 minutes left in the hunt!"));
		timeline.registerEvent(event);
		
		/*
		 */
		
		event = new ManhuntEvent(time += 4 * 60000);
		event.addAction(new BroadcastAction(lobby_id, "1 minute left in the hunt!"));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 30000);
		event.addAction(new BroadcastAction(lobby_id, "30 seconds left in the hunt!"));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 20000);
		event.addAction(new BroadcastAction(lobby_id, "10..."));
		timeline.registerEvent(event);
		
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "9..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "8..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "7..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "6..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "5..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "4..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "3..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "2..."));
		timeline.registerEvent(event);

		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "1..."));
		timeline.registerEvent(event);
		
		
		
		
		
		
		return timeline;
	}

}
