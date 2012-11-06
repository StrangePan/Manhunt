package com.bendude56.hunted.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntPlugin.ManhuntMode;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.settings.OldSettingsManager;
import com.bendude56.hunted.teams.PlayerState.PlayerStateType;

public class TeamManager
{
	ManhuntPlugin plugin;
	
	private HashMap<String, Team> teams = new HashMap<String, Team>();
	private HashMap<String, Team> savedTeams = new HashMap<String, Team>();
	private List<PlayerState> playerStates = new ArrayList<PlayerState>();
	private List<PlayerState> tempStates = new ArrayList<PlayerState>();

	public TeamManager(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
		refreshPlayers();
	}

	public void refreshPlayers()
	{
		if (!teams.isEmpty() && teams.equals(savedTeams))
		{
			return;
		}
		
		teams.clear();
		playerStates.clear();
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			addPlayer(p);
		}
	}
	
	public void randomizeTeams()
	{
		List<Player> hunters = new ArrayList<Player>();
		List<Player> prey = new ArrayList<Player>();
		List<Player> spectators = getTeamPlayers(Team.SPECTATORS);
		
		while (spectators.size() > 0)
		{
			Player p = spectators.get(new Random().nextInt(spectators.size()));
			
			if (prey.size() == 0 || (double) hunters.size() / (double) prey.size() > 2.5)
			{
				prey.add(p);
			}
			else
			{
				hunters.add(p);
			}
			
			spectators.remove(p);
		}
		
		/*
		if (spectators.size() == 0)
		{
			return;
		}
		
		int preyCount = (int) ((Math.floor((double)spectators.size() / (double)4) + 1));
		if (preyCount < 1) preyCount = 1;
		
		while (preyCount > 0 && spectators.size() > 0)
		{
			while(true)
			{
				int index = new Random().nextInt(spectators.size());
				if (prey.contains(spectators.get(index)))
				{
					continue;
				}
				else
				{
					prey.add(spectators.get(index));
					break;
				}
			}
			preyCount--;
		}
		
		for (Player p : spectators)
		{
			if (!prey.contains(p))
			{
				hunters.add(p);
			}
		}
		*/
		
		for (Player p : prey)
		{
			changePlayerTeam(p, Team.PREY);
			p.sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.PREY.getColor() + Team.PREY.getName(true));
		}
		for (Player p : hunters)
		{
			changePlayerTeam(p, Team.HUNTERS);
			p.sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true));
		}
	}
	
	public void removeOfflinePlayers()
	{
		List<String> names = new ArrayList<String>();
		for (String s : teams.keySet())
		{
			names.add(s);
		}
		for (String name : names)
		{
			if (Bukkit.getPlayer(name) == null)
			{
				if (plugin.gameIsRunning())
				{
					plugin.getGame().onPlayerForfeit(name);
				}
				else
				{
					teams.remove(name);
				}
			}
		}
	}
	
	public void saveTeamLists()
	{
		savedTeams.clear();
		
		for (String key : teams.keySet())
		{
			savedTeams.put(key, teams.get(key));
		}
	}
	
	private void putPlayerTeam(String p, Team t)
	{
		teams.put(p, t);
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
		OldSettingsManager settings = plugin.getSettings();
		
		if (teams.containsKey(p.getName()))
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
			if (settings.MANHUNT_MODE.value == ManhuntMode.PUBLIC)
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
		if (teams.containsKey(s))
		{
			teams.remove(s);
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
		if (teams.containsKey(s))
		{
			return teams.get(s);
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
		for (String name : teams.keySet())
		{
			if (teams.get(name) == team)
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
		
		for (String name : teams.keySet())
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

	public PlayerState getPlayerState(Player p, PlayerStateType t)
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

	public void restoreTemporaryState(Player p)
	{
		PlayerState state = getTemporaryPlayerState(p);
		
		if (state != null)
		{
			state.restorePlayer(p);
		}
	}

	public PlayerState getTemporaryPlayerState(Player p)
	{
		for (PlayerState state : tempStates)
		{
			if (state.getName().equals(p.getName()))
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

		if (p.isDead() && p.isOnline())
		{
			tempStates.add(originalState);
		}
		else
		{
			originalState.restorePlayer(p);
		}
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
		
		if (p.isDead() && p.isOnline())
		{
			tempStates.add(manhuntState);
		}
		else
		{
			manhuntState.restorePlayer(p);
		}
	}
	
	/**
	 * Restores all saved player states and clears the list.
	 */
	public void restoreAllOriginalPlayerStates()
	{
		List<PlayerState> states = new ArrayList<PlayerState>();
		states.addAll(playerStates);
		
		for (PlayerState state : states)
		{
			if (state.getType() == PlayerStateType.ORIGINAL)
			{
				Player p = Bukkit.getPlayerExact(state.getName());
				
				if (p != null)
				{
					if (p.isDead() && p.isOnline())
					{
						tempStates.add(state);
					}
					else
					{
						restoreOriginalPlayerState(p);
					}
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
