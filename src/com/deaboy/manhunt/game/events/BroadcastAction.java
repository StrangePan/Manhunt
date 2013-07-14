package com.deaboy.manhunt.game.events;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;

public class BroadcastAction implements Action
{

	private long lobby_id;
	private String message;
	private Team[] teams;
	
	public BroadcastAction(long lobbyId, String message, Team...teams)
	{
		this.lobby_id = lobbyId;
		this.message = message;
		this.teams = teams;
	}
	
	@Override
	public void execute()
	{
		if (teams.length == 0 && !(Manhunt.getLobby(lobby_id) instanceof GameLobby))
			Manhunt.getLobby(lobby_id).broadcast(message);
		else
			((GameLobby) Manhunt.getLobby(lobby_id)).broadcast(message, teams);
	}

}
