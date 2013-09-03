package com.deaboy.manhunt.timeline;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;

public class UnlockTeamAction implements Action
{
	
	private final long lobby_id;
	private final Team team;
	
	
	public UnlockTeamAction(long lobby_id, Team team)
	{
		this.lobby_id = lobby_id;
		this.team = team;
	}
	
	@Override
	public void execute()
	{
		if (Manhunt.getLobby(lobby_id) != null)
		{
			if (Manhunt.getLobby(lobby_id).getType() == LobbyType.GAME)
			{
				for (String playername : ((GameLobby) Manhunt.getLobby(lobby_id)).getPlayerNames(team))
				{
					Manhunt.unlockPlayer(playername);
				}
			}
			else
			{
				for (String playername : Manhunt.getLobby(lobby_id).getPlayerNames())
				{
					Manhunt.unlockPlayer(playername);
				}
			}
		}
	}

}
