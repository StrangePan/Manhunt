package com.bendude56.hunted.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.settings.SettingsManager;

public class TeamManager
{
	ManhuntPlugin plugin;
	
	private HashMap<String, Team> players = new HashMap<String, Team>();
	private HashMap<String, GameMode> gamemodes = new HashMap<String, GameMode>();

	public TeamManager(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
		refreshPlayers();
	}

	public void refreshPlayers()
	{
		players.clear();
		gamemodes.clear();
		
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
		if (!gamemodes.containsKey(p.getName()))
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
	 * Saves a player's game mode if it isn't already saved and sets
	 * their game mode to the one they're supposed to be in.
	 * @param p The player
	 */
	public void savePlayerGameMode(Player p)
	{
		GameMode oldMode, mode;
		Team team = getTeamOf(p);;
		if (!gamemodes.containsKey(p.getName()))
		{
			oldMode = p.getGameMode();
		}
		else
		{
			oldMode = null;
		}
		
		switch (team)
		{
			case HUNTERS:	mode = GameMode.SURVIVAL;
							break;
			case PREY:		mode = GameMode.SURVIVAL;
							break;
			case SPECTATORS:mode = GameMode.CREATIVE;
							break;
			default:		mode = p.getGameMode();
							break;
		}
		if (p.getGameMode() != mode)
		{
			p.setGameMode(mode);
		}
		if (!gamemodes.containsKey(p.getName()))
		{
			gamemodes.put(p.getName(), oldMode);
		}
	}

	/**
	 * Will collect the game modes of all players in the Manhunt world and
	 * change their game mode to the one they need to be in.
	 */
	public void saveAllGameModes()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getWorld() == plugin.getWorld())
			{
				savePlayerGameMode(p);
			}
		}
	}

	public boolean modeIsSaved(Player p)
	{
		return (gamemodes.containsKey(p.getName()));
	}

	/**
	 * Restores the game mode of a single player.
	 * Deletes their saved game mode.
	 */
	public void restorePlayerGameMode(Player p)
	{
		if (!modeIsSaved(p))
		{
			return;
		}
		
		GameMode mode = gamemodes.get(p.getName());
		gamemodes.remove(p.getName());
		if (p.getGameMode() != mode)
		{
			p.setGameMode(mode);
		}
	}

	/**
	 * Gives all players their saved game modes.
	 * Clears all saved game modes.
	 */
	public void restoreAllGameModes()
	{
		HashMap<String, GameMode> modes = new HashMap<String, GameMode>();
		modes.putAll(gamemodes);
		
		gamemodes.clear();
		
		for (String name : modes.keySet())
		{
			Player p = Bukkit.getPlayer(name);
			if (p != null)
			{
				if (p.getGameMode() != modes.get(name))
				{
					GameMode mode = modes.get(name);
					p.setGameMode(mode);
				}
			}
		}
	}

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
