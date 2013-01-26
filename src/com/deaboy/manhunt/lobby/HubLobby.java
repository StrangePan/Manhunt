package com.deaboy.manhunt.lobby;

import java.util.List;

import org.bukkit.entity.Player;

import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.World;

public class HubLobby extends Lobby
{

	//---------------- Constructors ----------------//
	public HubLobby(long id, String name, World world)
	{
		super(id, name, LobbyType.HUB, world);
	}
	
	

	//---------------- Getters ----------------//
	@Override
	public Map getCurrentMap()
	{
		return null;
	}
	
	@Override
	public Game getGame()
	{
		return null;
	}
	
	@Override
	public List<Player> getOnlinePlayers(Team...teams)
	{
		return getOnlinePlayers();
	}
	
	@Override
	public List<String> getPlayerNames(Team...teams)
	{
		return getPlayerNames();
	}

	

	//---------------- Setters ----------------//
	@Override
	public boolean addPlayer(String name)
	{
		return (addPlayer(name, Team.NONE));
	}
	
	@Override
	protected boolean addPlayer(String name, Team team)
	{
		return addPlayer(name);
	}
	
	@Override
	public void setPlayerTeam(Player player, Team team)
	{
		return;
	}
	
	@Override
	public void setPlayerTeam(String name, Team team)
	{
		return;
	}
	
	@Override
	public void setAllPlayerTeams(Team team)
	{
		return;
	}
	
	@Override
	public boolean gameIsRunning()
	{
		return false;
	}
	
	@Override
	public boolean setCurrentMap(Map map)
	{
		return false;
	}

	@Override
	public void distributeTeams()
	{
		return;
	}

	@Override
	public void startGame()
	{
		return;
	}

	@Override
	public void stopGame()
	{
		return;
	}
	
	@Override
	public void close()
	{
		super.close();
	}
	
}
