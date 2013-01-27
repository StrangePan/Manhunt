package com.deaboy.manhunt.game;

import java.io.Closeable;

import org.bukkit.World;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.map.Map;

public abstract class Game implements Closeable
{
	//---------------- Properties ----------------//
	private final long lobby_id;
	private Map current_map;
	private GameStage stage;
	
	
	
	//---------------- Constructors ----------------//
	public Game(Lobby lobby)
	{
		this.lobby_id = lobby.getId();
		this.current_map = null;
		this.stage = GameStage.INTERMISSION;
	}
	
	
	
	//------------ Getters ------------//
	/**
	 * Gets the world this game is taking place in. Will return null
	 * if the game is not running.
	 * @return The world this game is taking place in.
	 */
	public World getWorld()
	{
		if (isRunning() && current_map != null)
			return current_map.getWorld().getWorld();
		else
			return null;
	}
	
	/**
	 * Gets the map the game is taking place in. Will return null
	 * if the game is not running.
	 * @return The Map the game is taking place in.
	 */
	public Map getMap()
	{
		return current_map;
	}
	
	/**
	 * Checks to see if the game is currently running. Will return
	 * false if the game's stage is equal to INTERMISSION and true
	 * for everything else
	 * @return True if the game is running, false if not.
	 */
	public boolean isRunning()
	{
		return (this.stage != GameStage.INTERMISSION);
	}
	
	/**
	 * Gets the current stage of of the game. Will return an enum
	 * of type GameStage, which can be converted to an int or String.
	 * @return Enum of type GameStage representing the current stage of the game.
	 */
	public GameStage getStage()
	{
		return this.stage;
	}
	
	/**
	 * Gets the lobby that this game belongs to.
	 * @return
	 */
	public Lobby getLobby()
	{
		return Manhunt.getLobby(lobby_id);
	}
	
	
	
	//------------ Setters ------------//
	/**
	 * Sets the map for the game. Will only work while the game is not
	 * running so as to prevent errors and unexpected behavior.
	 * @param map The map to base the game in.
	 */
	public void setMap(Map map)
	{
		this.current_map = map;
	}
	
	
	//------------ Public Methods ------------//
	/**
	 * Starts the Manhunt game on the pre-programmed sequence. 
	 */
	public abstract void startGame();
	
	/**
	 * Cancels any currently running Manhunt game.
	 */
	public abstract void stopGame();
	
	/**
	 * Activates the game's listeners.
	 */
	public abstract void startListening();
	
	/**
	 * Deactivates the game's listeners.
	 */
	public abstract void stopListening();
	
	
}
