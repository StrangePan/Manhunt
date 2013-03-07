package com.deaboy.manhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.game.events.ManhuntTimeline;
import com.deaboy.manhunt.game.events.Timeline;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;

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
		// Initiate timeline
		
		timeline = ManhuntTimeline.newStandardTimeline(this.getLobby().getId());
		timeline.run();
		
		startListening();
	}

	@Override
	public void stopGame()
	{
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
	
	
	
	//---------------- Listeners/Events ----------------//
	
}

