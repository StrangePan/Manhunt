package com.deaboy.manhunt.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;

import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.map.World;

public class GameLobby extends Lobby
{
	
	public GameLobby(long id, String name, World world)
	{
		super(id, name, LobbyType.GAME, world);
	}
	
	@Override
	public boolean addPlayer(String name)
	{
		if (gameIsRunning())
		{
			return addPlayer(name, Team.SPECTATORS);
		}
		else
		{
			return addPlayer(name, Team.STANDBY);
		}
	}
	
	@Override
	public void distributeTeams()
	{
		List<String> hunters = new ArrayList<String>();
		List<String> prey = new ArrayList<String>();
		List<String> standby = getPlayerNames(Team.STANDBY);
		double ratio = getSettings().TEAM_RATIO.getValue();
		
		String name;

		while (standby.size() > 0)
		{
			name = standby.get(new Random().nextInt(standby.size()));

			if (prey.size() == 0 || (double) hunters.size() / (double) prey.size() > ratio)
			{
				prey.add(name);
			}
			else
			{
				hunters.add(name);
			}

			standby.remove(name);
		}

		for (String p : prey)
		{
			setPlayerTeam(p, Team.PREY);
			Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.PREY.getColor() + Team.PREY.getName(true));
		}
		for (String p : hunters)
		{
			setPlayerTeam(p, Team.HUNTERS);
			Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true));
		}
	}
	
	@Override
	public void startGame()
	{
		if (gameIsRunning())
			return;
		
		getGame().importPlayers(this);
		getGame().distributeTeams();
		getGame().startGame();
		
	}
	
	@Override
	public void stopGame()
	{
		if (!gameIsRunning())
			return;
		
		getGame().stopGame();
	}
	
	public void close()
	{
		super.close();
	}
	
}
