package com.deaboy.manhunt.game;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.game.events.BroadcastAction;
import com.deaboy.manhunt.game.events.Event;
import com.deaboy.manhunt.game.events.LogAction;
import com.deaboy.manhunt.game.events.ManhuntEvent;
import com.deaboy.manhunt.game.events.ManhuntTimeline;
import com.deaboy.manhunt.game.events.RunnableAction;
import com.deaboy.manhunt.game.events.TeleportTeamAction;
import com.deaboy.manhunt.game.events.Timeline;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.SpawnType;

public class ManhuntGame extends Game implements Listener
{
	//---------------- Properties ----------------//
	Timeline timeline;
	
	
	
	//---------------- Constructors ----------------//
	public ManhuntGame(Lobby lobby)
	{
		super(lobby);
		
		
	}
	
	
	
	//---------------- Public methods ----------------//
	@Override
	public void startGame()
	{
		this.setMap(getLobby().getCurrentMap());
		setStage(GameStage.PREGAME);
		// Initiate timeline
		
		timeline = generateTimeline();
		timeline.run();
		
		startListening();
	}

	@Override
	public void stopGame()
	{
		setStage(GameStage.INTERMISSION);
		// Send players to spawn
		
		timeline.stop();
		
		for (Player p : getOnlinePlayers(Team.HUNTERS, Team.PREY, Team.SPECTATORS))
		{
			p.teleport(getLobby().getRandomSpawnLocation());
		}
		
		stopListening();
	}

	@Override
	public void startListening()
	{
		Bukkit.getPluginManager().registerEvents(this, ManhuntPlugin.getInstance());
	}

	@Override
	public void stopListening()
	{
		HandlerList.unregisterAll(this);
	}
	
	@Override
	public void close()
	{
		super.close();
	}

	@Override
	public boolean addPlayer(String name)
	{
		if (containsPlayer(name))
			return false;
		else
			addPlayer(name, Team.SPECTATORS);
		return true;
	}
	
	
	
	//---------------- Timeline ----------------//
	private Timeline generateTimeline()
	{
		// Create the new timeline and placeholder event
		ManhuntTimeline timeline = new ManhuntTimeline();
		Event event;
		long time = 0L;
		long lobby_id = getLobby().getId();
		
		
		// Start defining events and actions
		event = new ManhuntEvent(time += 1000);
		event.addAction(new BroadcastAction(lobby_id, "Prepapre for Teleport!", Team.PREY, Team.HUNTERS));
		timeline.registerEvent(event);
		
		/*
		 * 
		 */
		
		event = new ManhuntEvent(time += 5000);
		event.addAction(new TeleportTeamAction(lobby_id, Team.PREY, getMap().getPoints(SpawnType.PREY)));
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, getMap().getPoints(SpawnType.SETUP)));
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
		event.addAction(new RunnableAction(new Runnable(){ public void run(){ setStage(GameStage.SETUP); }}));
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
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, getMap().getPoints(SpawnType.HUNTER)));
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
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, getMap().getPoints(SpawnType.HUNTER)));
		event.addAction(new RunnableAction(new Runnable(){ public void run(){ setStage(GameStage.HUNT); }}));
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

