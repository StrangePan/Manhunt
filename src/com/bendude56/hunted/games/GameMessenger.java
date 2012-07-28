package com.bendude56.hunted.games;

import java.util.HashMap;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.teams.TeamManager;
import com.bendude56.hunted.teams.TeamManager.Team;

public class GameMessenger {

	int stage = 0;
	
	HashMap<Integer, Message> messages = new HashMap<Integer, Message>();
	
	public GameMessenger()
	{
		//Save a Game in order to get the game's time and stuff. 
	}
	
	public void updateMessages()
	{
		//Check the stage against a predefined time. If true, broadcast message.
	}
	
	private void broadcastMessage(Message message)
	{
		TeamManager teams = HuntedPlugin.getInstance().getTeams();
		
		if (!message.getMessage(Team.HUNTERS).isEmpty())
		{
			teams.sendMessageToTeam(Team.HUNTERS, message.getMessage(Team.HUNTERS));
		}
		if (!message.getMessage(Team.PREY).isEmpty())
		{
			teams.sendMessageToTeam(Team.PREY, message.getMessage(Team.PREY));
		}
		if (!message.getMessage(Team.SPECTATORS).isEmpty())
		{
			teams.sendMessageToTeam(Team.SPECTATORS, message.getMessage(Team.SPECTATORS));
		}
		if (!message.getMessage(Team.NONE).isEmpty())
		{
			teams.sendMessageToTeam(Team.NONE, message.getMessage(Team.NONE));
		}
		
		stage++;
	}
}
