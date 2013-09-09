package com.deaboy.manhunt.timeline;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Spawn;

public class TeleportTeamAction implements Action
{

	private final long lobby_id;
	private final Team team;
	private final List<Spawn> locations;
	
	
	public TeleportTeamAction(long lobby_id, Team team, List<Spawn> spawns)
	{
		this.locations = new ArrayList<Spawn>(spawns);
		this.lobby_id = lobby_id;
		this.team = team;
	}
	
	
	@Override
	public void execute()
	{
		if (!locations.isEmpty() && Manhunt.getLobby(lobby_id) instanceof GameLobby)
		{
			for (Player p : ((GameLobby) Manhunt.getLobby(lobby_id)).getOnlinePlayers(team))
			{
				if (p.isOnline())
				{
					p.teleport(ManhuntUtil.safeTeleport(locations.get((int) (Math.random() * locations.size())).getRandomLocation()));
					// TODO Make less random
				}
			}
		}
	}

}
