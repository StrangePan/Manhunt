package com.bendude56.hunted.game;

import org.bukkit.World;

import com.bendude56.hunted.game.events.GameTimeline;
import com.bendude56.hunted.game.events.Timeline;
import com.bendude56.hunted.map.Map;

public class ManhuntGame implements Game
{
	//---------------- Properties ----------------//
	private Map map;
	private Timeline timeline;
	
	
	//---------------- Constructors ----------------//
	public ManhuntGame()
	{
		createTimeline();
	}
	
	
	//---------------- Private Methods ----------------//
	private void createTimeline()
	{
		timeline = new GameTimeline();
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