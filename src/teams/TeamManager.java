package teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.config.SettingsManager;

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

	//SIMPLE LIST SYSTEM FOR REMEMBERING WHO WAS IN CREATIVE MODE BEFORE THE GAME
	public void addCreativePlayer(Player p)
	{
		String name = p.getName();
		
		if (!creativePlayers.contains(name))
		{
			creativePlayers.add(name);
		}
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
