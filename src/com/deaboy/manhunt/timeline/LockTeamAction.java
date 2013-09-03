package com.deaboy.manhunt.timeline;

import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;

public class LockTeamAction implements Action
{
	
	private final long lobby_id;
	private final Team team;
	
	
	public LockTeamAction(long lobby_id, Team team)
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
				for (Player player : ((GameLobby) Manhunt.getLobby(lobby_id)).getOnlinePlayers(team))
				{
					Manhunt.lockPlayer(player);
				}
			}
			else
			{
				for (Player player : Manhunt.getLobby(lobby_id).getOnlinePlayers())
				{
					Manhunt.lockPlayer(player);
				}
			}
		}
	}

}
