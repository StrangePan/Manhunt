package com.deaboy.manhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;

public class ManhuntGame extends Game implements Listener
{
	//---------------- Properties ----------------//
	
	
	
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
		
		startListening();
	}

	@Override
	public void stopGame()
	{
		// Cancel timeline
		// Send players to spawn
		
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

