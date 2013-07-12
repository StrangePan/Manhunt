package com.deaboy.manhunt.lobby;

import org.bukkit.Location;

import com.deaboy.manhunt.map.World;

public class ManhuntLobby extends Lobby
{
	//////////////// PROPERTIES ////////////////
	
	
	//////////////// CONSTRUCTORS /////////////////
	public ManhuntLobby(String name, long id, World world, Location loc)
	{
		super(id, name, LobbyType.GAME, world, loc);
	}
	
	
	
	
}
