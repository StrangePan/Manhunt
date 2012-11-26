package com.bendude56.hunted.game.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.lobby.Team;
import com.bendude56.hunted.map.Spawn;

public class TeleportTeamAction implements Action
{

	private final long lobby_id;
	private final Team team;
	private final Location[] locations;
	
	public TeleportTeamAction(long lobby_id, Team team, Spawn...spawns)
	{
		this(lobby_id, team, new Location[spawns.length]);
		
		for (int i = 0; i < spawns.length; i++)
		{
			locations[i] = spawns[i].getLocation();
		}
		
	}
	
	public TeleportTeamAction(long lobby_id, Team team, Location...locations)
	{
		this.lobby_id = lobby_id;
		this.team = team;
		this.locations = locations;
	}
	
	@Override
	public void execute()
	{
		for (Player p : Manhunt.getLobby(lobby_id).getPlayers(team))
		{
			if (p.isOnline())
			{
				p.teleport(locations[((int) Math.random()) % locations.length]);
			}
		}
	}

}
