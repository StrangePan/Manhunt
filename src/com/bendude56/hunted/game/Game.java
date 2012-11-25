package com.bendude56.hunted.game;

import org.bukkit.World;

import com.bendude56.hunted.map.Map;

public interface Game
{
	//------------ Getters ------------//
	public World getWorld();
	
	public Map getMap();
	
	
	//------------ Setters ------------//
	public void setMap(Map map);
	
	
	//------------ Public Methods ------------//
	public void startGame();
	
	public void stopGame();
	
	
	
	
}
