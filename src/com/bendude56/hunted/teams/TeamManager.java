package com.bendude56.hunted.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.settings.SettingsManager;

public class TeamManager implements ITeamManager {

	private HashMap<String, Team> players = new HashMap<String, Team>();
	
	private List<String> creativePlayers = new ArrayList<String>();

	public boolean addPlayer(Player p, Team t)
	{
		if (!players.containsKey(p.getName()))
		{
			players.put(p.getName(), t);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean addPlayer(Player p)
	{
		SettingsManager settings = HuntedPlugin.getInstance().getSettings();
		
		if (!players.containsKey(p.getName()))
		{
			if (p.getWorld() != HuntedPlugin.getInstance().getWorld())
			{
				return addPlayer(p, Team.NONE);
			}
			else if (settings.PUBLIC_MODE.value)
			{
				return addPlayer(p, Team.SPECTATORS);
			}
			else if (settings.AUTO_JOIN.value)
			{
				return addPlayer(p, Team.HUNTERS);
			}
			else
			{
				return addPlayer(p, Team.SPECTATORS);
			}
		}
		return false;
	}

	public boolean removePlayer(String s)
	{
		if (players.containsKey(s))
		{
			players.remove(s);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean setTeam(Player p, Team t)
	{
		return setTeam(p.getName(), t);
	}

	public boolean setTeam(String s, Team t)
	{
		if (players.containsKey(s))
		{
			players.put(s, t);
			return true;
		}
		else
		{
			return false;
		}
	}

	public Team getTeamOf(Player p)
	{
		return getTeamOf(p.getName());
	}

	public Team getTeamOf(String s)
	{
		return players.get(s);
	}

	public List<Player> getTeam(Team team)
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

	public void sendMessageToTeam(Team team, String message)
	{
		for (Player p : getTeam(team))
		{
			p.sendMessage(message);
		}
	}

	//SIMPLE LIST SYSTEM FOR REMEMBERING WHO WAS IN CREATIVE MODE BEFORE THE GAME STARTED
	public void addCreativePlayer(Player p)
	{
		String name = p.getName();
		
		if (!creativePlayers.contains(name))
		{
			creativePlayers.add(name);
		}
	}

	/**
	 * Will collect all players who are in creative mode in the Manhunt world and set them to survival.
	 */
	public void collectAllCreativePlayers()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (p.getWorld() == HuntedPlugin.getInstance().getWorld() && p.getGameMode() == GameMode.CREATIVE && !creativePlayers.contains(p.getName()))
			{
				creativePlayers.add(p.getName());
				p.setGameMode(GameMode.SURVIVAL);
			}
		}
	}
	
	/**
	 * Takes all saved "creative" players, sets their game mode to Creative, then clears the list.
	 */
	public void restoreAllCreativePlayers()
	{
		for (String name : creativePlayers)
		{
			if (Bukkit.getPlayer(name) != null)
			{
				Bukkit.getPlayer(name).setGameMode(GameMode.CREATIVE);
			}
		}
		creativePlayers.clear();
	}

	public List<Player> getAllCreativePlayers()
	{
		List<Player> results = new ArrayList<Player>();
		
		for (String name : creativePlayers)
		{
			Player player = Bukkit.getPlayer(name);
			
			if (player != null && !results.contains(player))
			{
				results.add(player);
			}
		}
		
		return results;
	}

	public boolean wasCreative(Player p)
	{
		return (creativePlayers.contains(p.getName()));
	}
	
	public List<Player> clearCreativePlayers()
	{
		List<Player> clone = getAllCreativePlayers();
		creativePlayers.clear();
		return clone;
	}
	
	public void removeCreativePlayer(Player p)
	{
		removeCreativePlayer(p.getName());
	}
	
	public void removeCreativePlayer(String s)
	{
		if (creativePlayers.contains(s))
		{
			creativePlayers.remove(s);
		}
	}

	public enum Team
	{
		HUNTERS, PREY, SPECTATORS, NONE;
	}

}
