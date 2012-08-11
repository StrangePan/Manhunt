package com.bendude56.hunted.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.settings.SettingsManager;
import com.bendude56.hunted.teams.PlayerState.PlayerStateType;

public class TeamManager
{
	ManhuntPlugin plugin;
	
	private HashMap<String, Team> players = new HashMap<String, Team>();
	private List<PlayerState> playerStates = new ArrayList<PlayerState>();

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
		putPlayerTeam(p.getName(), t);
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
		if (players.containsKey(s))
		{
			return players.get(s);
		}
		else
		{
			return null;
		}
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
	public void saveOriginalPlayerState(Player p)
	{
		if (getPlayerState(p, PlayerStateType.ORIGINAL) != null)
		{
			return;
		}
		
		playerStates.add(new PlayerState(p, PlayerStateType.ORIGINAL));
	}

	public void saveManhuntPlayerState(Player p)
	{
		if (getPlayerState(p, PlayerStateType.MANHUNT) != null)
		{
			return;
		}
		
		playerStates.add(new PlayerState(p, PlayerStateType.MANHUNT));
	}

	/**
	 * Returns true if a player's state is saved.
	 * @param p
	 * @return
	 */
	public boolean stateIsSaved(Player p)
	{
		return getPlayerState(p, PlayerStateType.ORIGINAL) != null;
	}

	private PlayerState getPlayerState(Player p, PlayerStateType t)
	{
		for (PlayerState state : playerStates)
		{
			if (state.getType() == t && state.getName().equals(p.getName()))
			{
				return state;
			}
		}
		return null;
	}

	/**
	 * Restores a single player's state.
	 * @param p
	 */
	public void restoreOriginalPlayerState(Player p)
	{
		PlayerState originalState = getPlayerState(p, PlayerStateType.ORIGINAL);
		
		if (originalState == null)
		{
			return;
		}
		playerStates.remove(originalState);
		
		originalState.restorePlayer();
	}

	public void restoreManhuntPlayerState(Player p)
	{
		PlayerState manhuntState = getPlayerState(p, PlayerStateType.MANHUNT);
		
		if (manhuntState == null)
		{
			manhuntState = new PlayerState(p, PlayerStateType.MANHUNT);
			manhuntState.clearInventory();
		}
		if (playerStates.contains(manhuntState))
		{
			playerStates.remove(manhuntState);
		}
		
		manhuntState.restorePlayer();
	}
	
	/**
	 * Restores all saved player states and clears the list.
	 */
	public void restoreAllOriginalPlayerStates()
	{
		List<PlayerState> states = playerStates;
		
		for (PlayerState state : states)
		{
			if (state.getType() == PlayerStateType.ORIGINAL)
			{
				Player p = Bukkit.getPlayerExact(state.getName());
				
				if (p != null)
				{
					restoreOriginalPlayerState(p);
				}
			}
		}
		
		playerStates.clear();
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
