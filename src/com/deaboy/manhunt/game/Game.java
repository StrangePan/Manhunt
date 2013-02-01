package com.deaboy.manhunt.game;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Map;

public abstract class Game implements Closeable, Listener
{
	//---------------- Properties ----------------//
	private final long lobby_id;
	private Map current_map;
	private GameStage stage;
	
	private HashMap<String, Team> teams;
	
	
	
	//---------------- Constructors ----------------//
	public Game(Lobby lobby)
	{
		this.lobby_id = lobby.getId();
		this.current_map = null;
		this.stage = GameStage.INTERMISSION;
		
		this.teams = new HashMap<String, Team>();
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

	/**
	 * Gets a list of this Game's Players.
	 * @return ArrayList of Players in this Lobby
	 */
	public List<Player> getOnlinePlayers()
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String key : teams.keySet())
		{
			p = Bukkit.getPlayerExact(key);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	/**
	 * Gets a list of this Game's Player handles,
	 * filtered by team.
	 * If a player is offline, then they will not
	 * be included in the returned List.
	 * @param teams The teams whose members' names
	 * to return.
	 * @return
	 */
	public List<Player> getOnlinePlayers(Team...teams)
	{
		List<Player> players = new ArrayList<Player>();
		Player p;
		
		for (String name : getPlayerNames(teams))
		{
			p = Bukkit.getPlayerExact(name);
			if (p != null)
				players.add(p);
		}
		
		return players;
	}
	
	/**
	 * Gets a list of this Game's players' names.
	 * @return ArrayList of Strings of players' names in this lobby.
	 */
	public List<String> getPlayerNames()
	{
		return new ArrayList<String>(teams.keySet());
	}
	
	/**
	 * Gets a list of this Game's players' names,
	 * filtered by teams given in the parameters.
	 * @param teams The teams whose members' names
	 * to return.
	 * @return
	 */
	public List<String> getPlayerNames(Team...teams)
	{
		List<String> names = new ArrayList<String>();
		
		for (String key : this.teams.keySet())
		{
			for (Team team : teams)
			{
				if (this.teams.get(key) == team)
				{
					names.add(key);
					break;
				}
			}
		}
		
		return names;
	}
	
	public Team getPlayerTeam(Player p)
	{
		return getPlayerTeam(p.getName());
	}
	
	public Team getPlayerTeam(String name)
	{
		if (teams.containsKey(name))
			return teams.get(name);
		else
			return null;
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
	
	/**
	 * Adds a player to the Lobby via their name.
	 * @param p The Player to add.
	 */
	public abstract boolean addPlayer(String name);
	
	protected boolean addPlayer(String name, Team team)
	{
		if (teams.containsKey(name))
			return false;
		else
			teams.put(name, team);
		return true;
	}
	
	/**
	 * Removes a Player from the game via their name.
	 * @param p The Player to remove.
	 */
	public void removePlayer(String name)
	{
		if (this.teams.containsKey(name))
			this.teams.remove(name);
	}
	
	/**
	 * Removes a player from the game.
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		if (this.teams.containsKey(player.getName()))
			this.teams.remove(player.getName());
	}
	
	public boolean containsPlayer(Player p)
	{
		return containsPlayer(p.getName());
	}
	
	public boolean containsPlayer(String name)
	{
		return this.teams.containsKey(name);
	}
	
	public void setPlayerTeam(Player player, Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		setPlayerTeam(player.getName(), team);
	}
	
	public void setPlayerTeam(String name, Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		if (containsPlayer(name))
			this.teams.put(name, team);
	}
	
	public void setAllPlayerTeams(Team team)
	{
		if (team == null)
			throw new IllegalArgumentException("Argument cannot be null");
		for (String key : teams.keySet())
			teams.put(key, team);
	}
	
	@SuppressWarnings("unchecked")
	public void loadTeams(HashMap<String, Team> teams)
	{
		teams = (HashMap<String, Team>) teams.clone();
		this.teams.clear();
		
		for (String key : teams.keySet())
		{
			this.teams.put(key, teams.get(key));
		}
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
	
	public void close()
	{
	}
	
	public void distributeTeams()
	{
		List<String> hunters = new ArrayList<String>();
		List<String> prey = new ArrayList<String>();
		List<String> standby = getPlayerNames(Team.STANDBY);
		double ratio = getLobby().getSettings().TEAM_RATIO.getValue();
		
		String name;

		while (standby.size() > 0)
		{
			name = standby.get(new Random().nextInt(standby.size()));

			if (prey.size() == 0 || (double) hunters.size() / (double) prey.size() > ratio)
			{
				prey.add(name);
			}
			else
			{
				hunters.add(name);
			}

			standby.remove(name);
		}

		for (String p : prey)
		{
			setPlayerTeam(p, Team.PREY);
			Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.PREY.getColor() + Team.PREY.getName(true));
		}
		for (String p : hunters)
		{
			setPlayerTeam(p, Team.HUNTERS);
			Bukkit.getPlayerExact(p).sendMessage(ChatManager.leftborder + "You have been moved to team " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true));
		}
	}
	
	
	
	//---------------- Listeners ----------------//
	@EventHandler(priority = EventPriority.LOW)
	public final void onFinderActivate(PlayerInteractEvent e)
	{
		if (e.getItem().getTypeId() != Manhunt.getSettings().FINDER_ITEM.getValue()
				|| e.getAction() != Action.RIGHT_CLICK_AIR
				&& e.getAction() != Action.RIGHT_CLICK_BLOCK
				&& e.getAction() != Action.LEFT_CLICK_AIR
				&& e.getAction() != Action.LEFT_CLICK_BLOCK)
		{
			return;
		}
		
		
		// TODO Handle finder events
		
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void onSignInteract(PlayerInteractEvent e)
	{
		// TODO Sign interact event! :D
	}

	@EventHandler(priority = EventPriority.LOW)
	public final void validateBlockPlace(BlockPlaceEvent e)
	{
		// TODO Check for valid placement
	}

	@EventHandler(priority = EventPriority.LOW)
	public final void validateBlockBreak(BlockBreakEvent e)
	{
		// TODO Check for valid breakage
	}

	@EventHandler(priority = EventPriority.LOW)
	public final void validateBucketFill(PlayerBucketFillEvent e)
	{
		// TODO Check for valid bucket filling
	}

	@EventHandler(priority = EventPriority.LOW)
	public final void validateBucketEmpty(PlayerBucketEmptyEvent e)
	{
		// TODO Check for valid bucket emptying
	}
	
	
	
	
	
}
