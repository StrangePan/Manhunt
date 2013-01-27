package com.deaboy.manhunt.game.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Spawn;

public class TeleportTeamAction implements Action
{

	private final long lobby_id;
	private final Team team;
	private final List<Location> locations;
	
	public TeleportTeamAction(long lobby_id, Team team, List<Spawn> spawns)
	{
		this.locations = new ArrayList<Location>();
		
		this.lobby_id = lobby_id;
		this.team = team;
		for (Spawn spawn : spawns)
			this.locations.add(spawn.getLocation());
		
	}
	
	@Override
	public void execute()
	{
		for (Player p : Manhunt.getLobby(lobby_id).getOnlinePlayers(team))
		{
			if (p.isOnline())
			{
				p.teleport(locations.get(((int) Math.random()) % locations.size()));
			}
		}
	}

}
