package com.deaboy.manhunt.game;

import org.bukkit.event.Listener;

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
	public void startGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startListening()
	{
		//Bukkit.getPluginManager().registerEvents(this, NewManhuntPlugin.getInstance());
	}

	@Override
	public void stopListening()
	{
		//HandlerList.unregisterAll(this);
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
