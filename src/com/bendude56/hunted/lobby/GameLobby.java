package com.bendude56.hunted.lobby;

import java.util.List;

import org.bukkit.entity.Player;

import com.bendude56.hunted.game.Game;
import com.bendude56.hunted.map.Map;
import com.bendude56.hunted.map.World;
import com.bendude56.hunted.settings.LobbySettings;

public interface GameLobby extends Lobby
{
	
	//---------------- Getters ----------------//
	/**
	 * Gets the name of this lobby.
	 * @return Name of this lobby.
	 */
	public String getName();
	
	/**
	 * Gets the a list of Worlds this Lobby may spawn. Each
	 * lobby may spawn multiple maps, so this method returns
	 * what worlds it spawns.
	 * @return ArrayList of Worlds in this Lobby
	 */
	public List<World> getWorlds();
	
	/**
	 * Gets the current map of this lobby.
	 * @return
	 */
	public Map getCurrentMap();
	
	/**
	 * Gets the maps currently loaded by the Lobby
	 * @return ArrayList of Maps in this Lobby.
	 */
	public List<Map> getMaps();
	
	/**
	 * Gets a list of Players on a given team.
	 * @param teams The Team(s) to get.
	 * @return ArrayList of Players on the given team(s).
	 */
	public List<Player> getPlayers(Team...teams);
	
	/**
	 * Gets the Team of the given Player.
	 * @param p The Player to get the Team of.
	 * @return The Team of the given Player.
	 */
	public Team getPlayerTeam(Player p);
	
	/**
	 * Gets the WorldSettings for this Lobby.
	 * @return The WorldSettings of this Lobby.
	 */
	public LobbySettings getSettings();
	
	/**
	 * Gets the game of this lobby. This includes access to various
	 * things, such as the game stage.
	 * @return
	 */
	public Game getGame();
	
	
	
	//---------------- Setters ----------------//
	/**
	 * Sets this lobby's name.
	 * @param name The new name.
	 */
	public void setName(String name);
	
	/**
	 * Adds a player to the Lobby and to the given team.
	 * @param name The name of the player to add.
	 * @param t The Team to put the Player on
	 */
	public void addPlayer(String name, Team t);
	
	/**
	 * Changes the given Player's Team.
	 * @param name The name of the player to change teams.
	 * @param t The Team to put the Player in.
	 */
	public void setPlayerTeam(String name, Team t);
	
	/**
	 * Sets the team of all players in the lobby.
	 * @param t The new Team for all players.
	 */
	public void setAllPlayerTeam(Team t);
	
	//---------------- Public Methods ----------------//
	/**
	 * Broadcasts a message to all players on a given team.
	 * @param message The message to broadcast.
	 * @param teams The team(s) to broadcast to.
	 */
	public void broadcast(String message, Team ... teams);
	
	/**
	 * Starts the game for this lobby.
	 */
	public void startGame();
	
	/**
	 * Stops the Manhunt game
	 */
	public void stopGame();
	
}
