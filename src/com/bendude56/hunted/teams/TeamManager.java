package com.bendude56.hunted.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.settings.SettingsManager;

public class TeamManager
{
	ManhuntPlugin plugin;
	
	private HashMap<String, Team> players = new HashMap<String, Team>();
	private HashMap<String, PlayerState> playerStates = new HashMap<String, PlayerState>();

	public TeamManager(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
		refreshPlayers();
	}

	public void refreshPlayers()
	{
		players.clear();
		playerStates.clear();
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			addPlayer(p);
		}
	}

	private void putPlayerTeam(String p, Team t)
	{
		players.put(p, t);
	}

	/**
	 * This method handles players joining the game, either by
	 * logging in to the server or switching worlds. Only
	 * handles players in context of the TeamManager, does not
	 * deal with any other aspects of the game.
	 * @param p The player to add.
	 */
	public void addPlayer(Player p)
	{
		SettingsManager settings = plugin.getSettings();
		
		if (players.containsKey(p.getName()))
		{
			return;
		}
		
		if (p.getWorld() != plugin.getWorld())
		{
			putPlayerTeam(p.getName(), Team.NONE);
		}
		else if (plugin.gameIsRunning())
		{
			putPlayerTeam(p.getName(), Team.SPECTATORS);
		}
		else
		{
			if (settings.PUBLIC_MODE.value)
			{
				putPlayerTeam(p.getName(), Team.SPECTATORS);
			}
			else if (settings.AUTO_JOIN.value)
			{
				putPlayerTeam(p.getName(), Team.HUNTERS);
			}
			else
			{
				putPlayerTeam(p.getName(), Team.SPECTATORS);
			}
		}
	}

	/**
	 * This method changes a player's team. Player must
	 * already be added, using the addPlayer(Player p) method.
	 * @param p
	 * @param t
	 */
	public void changePlayerTeam(Player p, Team t)
	{
		if (!playerStates.containsKey(p.getName()))
		{
			putPlayerTeam(p.getName(), t);
		}
	}

	/**
	 * This method simply deletes a player from the team manager.
	 * Used only after the player times out.
	 * @param s
	 */
	public void deletePlayer(String s)
	{
		if (players.containsKey(s))
		{
			players.remove(s);
		}
	}

	/**
	 * Returns the Team the player is on
	 * @param p The Player in question
	 * @return
	 */
	public Team getTeamOf(Player p)
	{
		return getTeamOf(p.getName());
	}

	/**
	 * Returns the Team the player is on
	 * @param s The name of the player in question
	 * @return
	 */
	public Team getTeamOf(String s)
	{
		return players.get(s);
	}

	/**
	 * Returns a List<String> of the names of players who
	 * belong to the specified team.
	 * @param team 
	 * @return
	 */
	public List<String> getTeamNames(Team team)
	{
		List<String> results = new ArrayList<String>();
		for (String name : players.keySet())
		{
			if (players.get(name) == team)
			{
				results.add(name);
			}
		}
		return results;
	}

	/**
	 * Returns a List<Player> of players who belong to the
	 * given team.
	 * @param team
	 * @return
	 */
	public List<Player> getTeamPlayers(Team team)
	{
		List<Player> results = new ArrayList<Player>();
		
		for (String name : players.keySet())
		{
			Player player = Bukkit.getPlayer(name);
			if (player != null)
			{
				if (getTeamOf(name) == team)
				{
					results.add(player);
				}
			}
		}
		
		return results;
		
	}

	/**
	 * Simply returns a list of all Players who are playing
	 * @param spectators True if you wish to include spectators
	 * @return
	 */
	public List<Player> getAllPlayers(boolean spectators) {
		List<Player> players = new ArrayList<Player>();
		players.addAll(getTeamPlayers(Team.HUNTERS));
		players.addAll(getTeamPlayers(Team.PREY));
		if (spectators)
			players.addAll(getTeamPlayers(Team.SPECTATORS));
		return players;
	}

	//PLAYER STATES
	/**
	 * Saves a snapshot of a player.
	 * @param p
	 */
	public void savePlayerState(Player p)
	{
		if (playerStates.containsKey(p.getName()))
		{
			return;
		}
		
		PlayerState state = new PlayerState(p);
		
		playerStates.put(p.getName(), state);
	}

	/**
	 * Returns true if a player's state is saved.
	 * @param p
	 * @return
	 */
	public boolean stateIsSaved(Player p)
	{
		return playerStates.containsKey(p.getName());
	}

	/**
	 * Restores a single player's state.
	 * @param p
	 */
	public void restorePlayerState(Player p)
	{
		restorePlayerState(p.getName());
	}

	private void restorePlayerState(String name)
	{
		if (!playerStates.containsKey(name))
		{
			return;
		}
		
		PlayerState state = playerStates.get(name);
		
		playerStates.remove(name);
		
		state.restorePlayer();
	}

	/**
	 * Restores all saved player states and clears the list.
	 */
	public void restoreAllPlayerStates()
	{
		HashMap<String, PlayerState> states = new HashMap<String, PlayerState>();
		states.putAll(playerStates);
		
		playerStates.clear();
		
		for (String name : states.keySet())
		{
			restorePlayerState(name);
		}
	}

	//TEAM ENUM
	public enum Team
	{
		HUNTERS, PREY, SPECTATORS, NONE;
		
		public Team fromString(String team)
		{
			if (team.equalsIgnoreCase("hunter") || team.equalsIgnoreCase("hunters"))
				return HUNTERS;
			if (team.equalsIgnoreCase("prey"))
				return PREY;
			if (team.equalsIgnoreCase("spectator") || team.equalsIgnoreCase("spectators"))
				return SPECTATORS;
			if (team.equalsIgnoreCase("none") || team.equalsIgnoreCase("null"))
				return NONE;
			return null;
		}
		
		public ChatColor getColor()
		{
			return TeamUtil.getTeamColor(this);
		}
		
		public String getName(boolean plural)
		{
			return TeamUtil.getTeamName(this, plural);
		}
	}

}
