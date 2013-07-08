package com.deaboy.manhunt.game;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.finder.Finder;
import com.deaboy.manhunt.finder.FinderUtil;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.ManhuntSpawn;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.SpawnType;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneFlag;

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
	
	public List<Spawn> getPoints(SpawnType type)
	{
		List<Spawn> points = new ArrayList<Spawn>();
		points.addAll(getMap().getPoints(type));
		if (points.isEmpty())
			points.add(new ManhuntSpawn("", type, getMap().getSpawnLocation()));
		return points;
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
	 * Sets the stage of the game.
	 * @param stage
	 */
	protected void setStage(GameStage stage)
	{
		if (stage != null)
		{
			this.stage = stage;
			if (getLobby() != null)
				Manhunt.log(Level.INFO, getLobby().getName() + " has entered the " + stage.getName() + " stage.");
		}
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
		if (getPlayerTeam(player) == team)
			player.sendMessage(ChatManager.bracket1_ + "You've been moved to team " + team.getColor() + team.getName(true) + ChatManager.bracket2_);
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
	 * Officially ends the game and takes actions based on the state of the game at the given time.
	 * Announces winners and activates any award systems there may be.
	 */
	public abstract void endGame();
	
	/**
	 * Activates the game's listeners.
	 */
	public abstract void startListening();
	
	/**
	 * Deactivates the game's listeners.
	 */
	public abstract void stopListening();
	
	/**
	 * Closes a game and frees up resources.
	 */
	public void close()
	{
	}
	
	/**
	 * Distributes players in standby into appropriate teams
	 */
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
	
	/**
	 * Forfeits a player from the game.
	 * @param name
	 */
	public void forfeitPlayer(String name)
	{
		Player p;
		Team team;
		
		if (!isRunning())
			return;
		if (!containsPlayer(name))
			return;
		
		p = Bukkit.getPlayerExact(name);
		team = getPlayerTeam(name);
		
		if (p == null)
		{
			removePlayer(name);
		}
		else
		{
			setPlayerTeam(name, Team.SPECTATORS);
			if (p.getWorld() == getWorld())
			{
				p.teleport(getLobby().getSpawnLocation());
			}
		}
		
		
		getLobby().broadcast(team.getColor() + name + ChatManager.color + " has " + ChatColor.RED + " forfeit the game.");
		
		testGame();
	}
	
	/**
	 * Imports all players from the given lobby.
	 * Makes life easier rather than adding each player in one at a time.
	 * @param lobby
	 */
	public void importPlayers(Lobby lobby)
	{
		this.teams.clear();
		
		for (String name : lobby.getPlayerNames())
			this.teams.put(name, lobby.getPlayerTeam(name));
	}
	
	public void testGame()
	{
		if (getPlayerNames(Team.HUNTERS).isEmpty() || getPlayerNames(Team.PREY).isEmpty())
			endGame();
	}
	
	
	
	//---------------- Listeners ----------------//
	@EventHandler(priority = EventPriority.LOW)
	public final void onFinderActivate(PlayerInteractEvent e)
	{
		if (e.getPlayer().getWorld() != getWorld())
			return;
		if (!containsPlayer(e.getPlayer()))
			return;
		if (!isRunning())
			return;
		if (e.getItem() == null
				|| e.getItem().getTypeId() != getLobby().getSettings().FINDER_ITEM.getValue()
				|| e.getAction() != Action.RIGHT_CLICK_AIR
				&& e.getAction() != Action.RIGHT_CLICK_BLOCK
				&& e.getAction() != Action.LEFT_CLICK_AIR
				&& e.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		if (!getLobby().getSettings().PREY_FINDER.getValue())
			return;
		if (getPlayerTeam(e.getPlayer()) != Team.HUNTERS
				&& getPlayerTeam(e.getPlayer()) != Team.PREY)
			return;
		
		Finder finder = Manhunt.getFinder(e.getPlayer());
		
		if (finder == null)
		{
			Manhunt.startFinder(e.getPlayer(), lobby_id);
			FinderUtil.sendMessageFinderInitialize(e.getPlayer());
		}
		else if (finder.isUsed())
		{
			finder.sendTimeLeft();
		}
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public final void onSignInteract(PlayerInteractEvent e)
	{
		// TODO Sign interact event! :D
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public final void validateBlockPlace(BlockPlaceEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockPlaced().getLocation());
	}
	
	
	@EventHandler(priority = EventPriority.LOW)
	public final void validateBlockBreak(BlockBreakEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlock().getLocation());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void validateBucketFill(PlayerBucketFillEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getLocation());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void validateBucketEmpty(PlayerBucketEmptyEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void basePlayerTeleport(PlayerTeleportEvent e)
	{
		if (!isRunning())
			return;
		if (e.isCancelled())
			return;
		
		if (e.getCause() == TeleportCause.COMMAND)
		{
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void basePlayerRespawn(PlayerRespawnEvent e)
	{
		if (!isRunning())
			return;
		
		e.setRespawnLocation(getLobby().getSpawnLocation());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void baseEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		Player damager;
		Player damagee;
		Team damager_team;
		Team damagee_team;

		if (!isRunning())
			return;
		if (e.isCancelled())
			return;
		if (e.getEntity().getWorld() != getWorld())
			return;
		if (e.getDamager().getWorld() != getWorld())
			return;
		if (e.getEntity().getType() != EntityType.PLAYER)
			return;
		if (e.getDamager() instanceof Projectile) // Deal with projectiles
		{
			if (((Projectile) e.getDamager()).getShooter().getType() != EntityType.PLAYER)
				return;
			else
				damager = ((Player) ((Projectile) e.getDamager()).getShooter());
		}
		else if (e.getDamager().getType() != EntityType.PLAYER)
			return;
		
		damager = (Player) e.getDamager();
		damagee = (Player) e.getEntity();
		damager_team = getPlayerTeam(damager);
		damagee_team = getPlayerTeam(damagee);
		
		if (damager_team == null)
			return;
		if (damagee_team == null)
			return;
		
		if (getStage() == GameStage.INTERMISSION)
		{
			e.setCancelled(true);
			return;
		}
		if (getStage() == GameStage.PREGAME)
		{
			e.setCancelled(true);
			return;
		}
		if (getStage() == GameStage.SETUP)
		{
			if (damagee_team == Team.HUNTERS || damager_team == Team.HUNTERS && damagee_team == Team.PREY)
			{
				e.setCancelled(true);
				return;
			}
		}
		
		if (damager_team != Team.HUNTERS && damager_team != Team.PREY
			|| damagee_team != Team.HUNTERS && damagee_team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		
		
		if (damager_team == damagee_team && getLobby().getSettings().FRIENDLY_FIRE.getValue() == false)
		{
			e.setCancelled(true);
			return;
		}
		
		if (getLobby().getSettings().INSTANT_DEATH.getValue())
		{
			e.setDamage(damagee.getHealth());
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void basePlayerDeath(PlayerDeathEvent e)
	{
		Player damagee;
		Player damager;
		Team damagee_team;
		Team damager_team;

		if (!isRunning())
			return;
		if (!containsPlayer(e.getEntity()))
			return;
		else if (getWorld() != e.getEntity().getWorld())
			return;
		
		damagee = e.getEntity();
		damager = e.getEntity().getKiller();
		damagee_team = getPlayerTeam(damagee);
		damager_team = getPlayerTeam(damager);
		
		e.setDeathMessage(null);
		
		if (damager != null && damager_team != null)
		{
			getLobby().broadcast(ChatManager.leftborder + damagee_team.getColor() + damagee.getDisplayName() + ChatManager.color +
					" was killed by " + damager_team.getColor() + damager.getName() + ChatManager.color + " and is eliminated!");
		}
		else
		{
			getLobby().broadcast(ChatManager.leftborder + damagee_team.getColor() + damagee.getDisplayName() + ChatManager.color +
					" has died and is eliminated!");
		}
		
		testGame();
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void basePlayerInteract(PlayerInteractEvent e)
	{
		if (!isRunning())
			return;
		if (e.isCancelled())
			return;
		if (e.getPlayer().getWorld() != getWorld())
			return;
		if (!containsPlayer(e.getPlayer()))
			return;
		
		
		Team player_team = getPlayerTeam(e.getPlayer());
		
		if (player_team != Team.HUNTERS && player_team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void baseCreatureSpawn(CreatureSpawnEvent e)
	{
		if (e.isCancelled() || e.getLocation().getWorld() != getWorld())
			return;
		
		if (!isRunning())
		{
			e.setCancelled(true);
			return;
		}
		if (e.getLocation().getWorld() != getWorld())
			return;
		
		if (ManhuntUtil.isHostile(e.getEntity()))
		{
			for (Zone zone : getMap().getZones(ZoneFlag.NO_MOBS))
			{
				if (zone.containsLocation(e.getLocation()))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public final void baseMobTargetEvent(EntityTargetEvent e)
	{
		Player target;
		Team target_team;
		
		if (e.isCancelled())
			return;
		if (e.getEntity().getWorld() != getWorld())
			return;
		if (!isRunning())
		{
			e.setCancelled(true);
			return;
		}
		
		if (e.getTarget().getType() == EntityType.PLAYER)
			target = (Player) (e.getTarget());
		else
			return;
		
		target_team = getPlayerTeam(target);
		
		if (target_team != Team.HUNTERS && target_team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		if (getStage() == GameStage.SETUP)
		{
			if (target_team == Team.HUNTERS)
			{
				e.setCancelled(true);
				return;
			}
		}
		else if (getStage() != GameStage.HUNT)
		{
			e.setCancelled(true);
		}
		
		
	}
	
	
	
	//////// Universal method for block-editing events
	private void checkBlockActionValidity(Cancellable e, Player p, Location loc)
	{
		Team team;
		
		if (e.isCancelled())
			return;
		if (!isRunning())
			return;
		if (p.getWorld() != getWorld())
			return;
		
		team = getPlayerTeam(p);
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		
		if (getStage() == GameStage.SETUP && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		else if (getStage() != GameStage.HUNT)
		{
			e.setCancelled(true);
			return;
		}
		
		
		// Take map zones into account
		for (Zone zone : getMap().getZones(ZoneFlag.BUILD, ZoneFlag.NO_BUILD))
		{
			if ((!e.isCancelled() || !zone.checkFlag(ZoneFlag.NO_BUILD)) && zone.containsLocation(loc))
			{
				if (zone.checkFlag(ZoneFlag.BUILD))
				{
					e.setCancelled(false);
					break;
				}
				else
				{
					e.setCancelled(true);
				}
			}
		}
		if (e.isCancelled())
			return;
		
	}
	
	
	
	
}
