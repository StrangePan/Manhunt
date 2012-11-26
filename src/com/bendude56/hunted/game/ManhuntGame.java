package com.bendude56.hunted.game;

import org.bukkit.World;

import com.bendude56.hunted.game.events.ManhuntTimeline;
import com.bendude56.hunted.game.events.Timeline;
import com.bendude56.hunted.map.Map;

public class ManhuntGame implements Game
{
	//---------------- Properties ----------------//
	private Map map;
	private Timeline timeline;
	private final long lobby_id;
	
	
	//---------------- Constructors ----------------//
	public ManhuntGame(long lobby_id)
	{
		this.lobby_id = lobby_id; 
		createTimeline();
	}
	
	
	//---------------- Private Methods ----------------//
	private void createTimeline()
	{
		timeline = ManhuntTimeline.newStandardTimeline(lobby_id);
	}
	
	
	//---------------- Getters ----------------//
	public World getWorld()
	{
		return map.getWorld();
	}
	
	public Map getMap()
	{
		return map;
	}
	
	//---------------- Setters ----------------//
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	
	//---------------- Public Methods ----------------//
	public void startGame()
	{
		timeline.run();
	}
	
	public void stopGame()
	{
		timeline.stop();
	}
	
}