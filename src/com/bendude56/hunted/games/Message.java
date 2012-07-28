package com.bendude56.hunted.games;

import java.util.HashMap;

import com.bendude56.hunted.teams.TeamManager.Team;

public class Message {

	private HashMap<Team, String> messages = new HashMap<Team, String>();
	
	public Message(String message)
	{
		this(message, message, message);
	}
	
	public Message(String hunters, String prey, String spectators)
	{
		messages.put(Team.HUNTERS, hunters);
		messages.put(Team.PREY, prey);
		messages.put(Team.SPECTATORS, spectators);
	}
	
	public String getMessage(Team team)
	{
		return messages.get(team);
	}
	
}
