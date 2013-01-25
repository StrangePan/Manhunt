package com.deaboy.manhunt.game;

import org.bukkit.World;

import com.deaboy.manhunt.map.Map;

public interface Game
{
	//------------ Getters ------------//
	/**
	 * Gets the world this game is taking place in. Will return null
	 * if the game is not running.
	 * @return The world this game is taking place in.
	 */
	public World getWorld();
	
	/**
	 * Gets the map the game is taking place in. Will return null
	 * if the game is not running.
	 * @return The Map the game is taking place in.
	 */
	public Map getMap();
	
	/**
	 * Checks to see if the game is currently running. Will return
	 * false if the game's stage is equal to INTERMISSION and true
	 * for everything else
	 * @return True if the game is running, false if not.
	 */
	public boolean isRunning();
	
	/**
	 * Gets the current stage of of the game. Will return an enum
	 * of type GameStage, which can be converted to an int or String.
	 * @return Enum of type GameStage representing the current stage of the game.
	 */
	public GameStage getStage();
	
	
	//------------ Setters ------------//
	/**
	 * Sets the map for the game. Will only work while the game is not
	 * running so as to prevent errors and unexpected behavior.
	 * @param map The map to base the game in.
	 */
	public void setMap(Map map);
	
	/**
	 * Changes the game's stage. This effects event behaviors and
	 * other in-game actions, and thus should be used with care.
	 * @param stage
	 */
	public void setStage(GameStage stage);
	
	
	//------------ Public Methods ------------//
	/**
	 * Starts the Manhunt game on the pre-programmed sequence. 
	 */
	public void startGame();
	
	/**
	 * Cancels any currently running Manhunt game.
	 */
	public void stopGame();
	
	
}
