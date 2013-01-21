package com.deaboy.hunted.game;

import org.bukkit.World;

import com.deaboy.hunted.game.events.ManhuntTimeline;
import com.deaboy.hunted.game.events.Timeline;
import com.deaboy.hunted.map.Map;

public class ManhuntGame implements Game
{
	//---------------- Properties ----------------//
	private Map map;
	private Timeline timeline;
	private final long lobby_id;
	private GameStage stage;
	
	
	//---------------- Constructors ----------------//
	public ManhuntGame(long lobby_id)
	{
		this.lobby_id = lobby_id; 
		createTimeline();
		stage = GameStage.INTERMISSION;
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
	
	public boolean isRunning()
	{
		switch (stage)
		{
		case PREGAME:
		case SETUP:
		case HUNT:
			return true;
		case INTERMISSION:
		default:
			return false;
		}
	}
	
	public GameStage getStage()
	{
		return stage;
	}
	
	//---------------- Setters ----------------//
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public void setStage(GameStage stage)
	{
		this.stage = stage;
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