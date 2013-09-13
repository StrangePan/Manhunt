package com.deaboy.manhunt.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.finder.FinderUtil;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneFlag;

public class ManhuntGameListener implements GameEventListener, Listener
{
	//////////////// PROPERTIES ////////////////
	private ManhuntGame game;
	private GameLobby lobby;
	private Map map;
	
	private HashMap<String, Integer> boundary_state;
	private int schedule;
	private List<Zone> boundary;
	private List<Zone> setup;
	private List<Zone> nobuild;
	private List<Zone> build;
	private List<Zone> nomobs; 
	
	private HashMap<String, Location> locations;
	
	
	
	//////////////// CONSTRUCTORS ////////////////
	public ManhuntGameListener(ManhuntGame game)
	{
		if (game == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		
		this.game = game;
		
		this.boundary_state = new HashMap<String, Integer>();
		this.boundary = new ArrayList<Zone>();
		this.setup = new ArrayList<Zone>();
		this.nobuild = new ArrayList<Zone>();
		this.build = new ArrayList<Zone>();
		this.nomobs = new ArrayList<Zone>();
		
		this.locations = new HashMap<String, Location>();
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- EVENTS ----------------//
	public void startListening()
	{
		Bukkit.getPluginManager().registerEvents(this, ManhuntPlugin.getInstance());
		this.map = game.getMap();
		this.lobby = game.getLobby();

		this.boundary = map.getZones(ZoneFlag.BOUNDARY);
		this.setup = map.getZones(ZoneFlag.SETUP);
		this.nobuild = map.getZones(ZoneFlag.NO_BUILD);
		this.build = map.getZones(ZoneFlag.BUILD);
		this.nomobs = map.getZones(ZoneFlag.NO_MOBS);
		
		startMonitoringBoundaries();
	}
	public void stopListening()
	{
		HandlerList.unregisterAll(this);
		stopMonitoringBoundaries();
		this.lobby = null;
		this.map = null;
		this.boundary.clear();
		this.setup.clear();
		this.nobuild.clear();
		this.build.clear();
		this.nomobs.clear();
	}
	
	private boolean checkBaseActionValidityCancellable(Cancellable e, Player p)
	{
		if (e.isCancelled())
		{
			return false;
		}
		else if (lobby == null || !lobby.containsPlayer(p))
		{
			return false;
		}
		else if (p.getWorld() != map.getWorld().getWorld() || !game.isRunning())
		{
			e.setCancelled(true);
			return false;
		}
		else
		{
			return true;
		}
	}
	private boolean checkBaseActionValidity(Event e, Player p)
	{
		if (!lobby.containsPlayer(p) || !game.isRunning() || p.getWorld() != map.getWorld().getWorld())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	private void checkBlockActionValidity(Cancellable e, Player p, Location loc)
	{
		Team team;
		
		team = lobby.getPlayerTeam(p);
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		if (game.getStage() != GameStage.HUNT && game.getStage() != GameStage.SETUP)
		{
			e.setCancelled(true);
			return;
		}
		
		// If inside a BUILD zone,  then all's good.
		for (Zone zone : this.build)
		{
			if (zone.containsLocation(loc))
				return;
		}
		for (Zone zone : this.nobuild)
		{
			if (zone.containsLocation(loc))
			{
				e.setCancelled(true);
				return;
			}
		}
		
		/*
		if (game.getStage() == GameStage.SETUP && team == Team.HUNTERS)
		{
			for (Zone zone : this.setup)
			{
				if (zone.containsLocation(loc))
					return;
			}
			e.setCancelled(true);
			return;
		}
		else
		{
			for (Zone zone : this.setup)
			{
				if (zone.containsLocation(loc))
				{
					e.setCancelled(true);
					return;
				}
			}
			for (Zone zone : this.boundary)
			{
				if (zone.containsLocation(loc))
					return;
			}
		}
		*/
	}
	
	@EventHandler
	public final void onBlockPlace(BlockPlaceEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockPlaced().getLocation());
	}
	@EventHandler
	public final void onBlockBreak(BlockBreakEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		checkBlockActionValidity(e, e.getPlayer(), e.getBlock().getLocation());
	}
	@EventHandler
	public final void onBucketFill(PlayerBucketFillEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getLocation());
	}
	@EventHandler
	public final void onBucketEmpty(PlayerBucketEmptyEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public final void onFinderActivate(PlayerInteractEvent e)
	{
		if (e.getAction() == Action.RIGHT_CLICK_AIR && e.isCancelled())
			e.setCancelled(false);
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		if (e.getItem() == null
				|| e.getItem().getTypeId() != game.getSettings().FINDER_ITEM.getValue()
				|| e.getAction() != Action.RIGHT_CLICK_AIR
				&& e.getAction() != Action.RIGHT_CLICK_BLOCK
				&& e.getAction() != Action.LEFT_CLICK_AIR
				&& e.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		if (!game.getSettings().PREY_FINDER.getValue())
			return;
		if (lobby.getPlayerTeam(e.getPlayer()) != Team.HUNTERS
				&& lobby.getPlayerTeam(e.getPlayer()) != Team.PREY)
			return;
		
		if (!Manhunt.finderExistsFor(e.getPlayer()))
		{
			Manhunt.startFinderFor(e.getPlayer(), 160, game.getSettings().FINDER_COOLDOWN.getValue()*20, Material.getMaterial(game.getSettings().FINDER_ITEM.getValue()), 4, 0, true);
			FinderUtil.sendMessageFinderInitialize(e.getPlayer());
		}
		else
		{
			Manhunt.sendFinderTimeRemaining(e.getPlayer());
		}
	}
	@EventHandler
	public final void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if (!checkBaseActionValidity(e, e.getPlayer()))
			return;
		
		e.setRespawnLocation(lobby.getRandomSpawnLocation());
	}
	@EventHandler
	public final void onPlayerTeleport(PlayerTeleportEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		if (e.getCause() == TeleportCause.COMMAND)
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public final void onPlayerInteract(PlayerInteractEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
			return;
		
		Team team = lobby.getPlayerTeam(e.getPlayer());
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);;
			return;
		}
	}
	@EventHandler
	public final void onEntityDamage(EntityDamageEvent e)
	{
		Player player;
		Team team;
		
		if (e.getEntity() instanceof Player && !checkBaseActionValidityCancellable(e, (Player) e.getEntity()))
			return;
		
		if (e.isCancelled())
			return;
		if (e.getEntity().getType() != EntityType.PLAYER)
			return;
		if (e.getCause() == DamageCause.ENTITY_ATTACK)
			return;
		
		player = (Player) e.getEntity();
		team = game.getLobby().getPlayerTeam(player);
		
		if (!game.getLobby().containsPlayer(player))
		{
			return;
		}
		if (game.getStage() == GameStage.PREGAME  || game.getStage() == GameStage.INTERMISSION || game.getStage() == GameStage.SETUP && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public final void onEntityDamageByEntity(EntityDamageByEntityEvent e)
	{
		Player damager;
		Player damagee;
		Team damager_team;
		Team damagee_team;
		
		if (e.isCancelled())
			return;
		if (!game.isRunning() || game.getWorld() != e.getEntity().getWorld())
			return;
		
		if (e.getEntity().getType() == EntityType.PLAYER)
		{
			damagee = ((Player) e.getEntity());
		}
		else
		{
			damagee = null;
		}
		if (e.getDamager() instanceof Projectile) // Deal with projectiles
		{
			if (((Projectile) e.getDamager()).getShooter().getType() == EntityType.PLAYER)
			{
				damager = ((Player) ((Projectile) e.getDamager()).getShooter());
			}
			else
			{
				damager = null;
			}
		}
		else if (e.getDamager().getType() == EntityType.PLAYER)
		{
			damager = (Player) e.getDamager();
			if (!lobby.containsPlayer(damager))
			{
				e.setCancelled(true);
				return;
			}
		}
		else
		{
			damager = null;
		}
		
		damager_team = lobby.getPlayerTeam(damager);
		damagee_team = lobby.getPlayerTeam(damagee);
		
		if (damagee_team == null && damager_team == null)
		{
			return;
		}
		
		if (damager_team == null && (game.getStage() == GameStage.HUNT || game.getStage() == GameStage.SETUP && damagee_team == Team.PREY))
		{
			return;
		}
		
		if (damager_team != Team.HUNTERS && damager_team != Team.PREY || damagee_team != Team.HUNTERS && damagee_team != Team.PREY && damagee_team != null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (game.getStage() == GameStage.PREGAME)
		{
			e.setCancelled(true);
			return;
		}
		if (game.getStage() == GameStage.SETUP)
		{
			if (damagee_team != null && damager_team != null && damagee_team != damager_team)
			{
				e.setCancelled(true);
				return;
			}
		}
		
		if (damager != damagee && damager_team == damagee_team && game.getSettings().FRIENDLY_FIRE.getValue() == false)
		{
			e.setCancelled(true);
			return;
		}
		
		if (game.getSettings().INSTANT_DEATH.getValue() && damager != damagee)
		{
			damagee.setHealth(0.0);
		}
	}
	@EventHandler
	public final void onPlayerDeath(PlayerDeathEvent e)
	{
		Player damagee;
		Player damager;
		Team damagee_team;
		Team damager_team;

		if (!checkBaseActionValidity(e, e.getEntity()))
			return;
		
		else if (map.getWorld().getWorld() != e.getEntity().getWorld())
			return;
		
		damagee = e.getEntity();
		damager = e.getEntity().getKiller();
		damagee_team = lobby.getPlayerTeam(damagee);
		damager_team = lobby.getPlayerTeam(damager);
		
		e.setDeathMessage(null);
		
		if (damager == null || damager_team == null)
		{
			lobby.broadcast(ChatManager.leftborder + damagee_team.getColor() + damagee.getDisplayName() + ChatManager.color +
					" has died and is eliminated!");
		}
		else
		{
			lobby.broadcast(ChatManager.leftborder + damagee_team.getColor() + damagee.getDisplayName() + ChatManager.color +
					" was killed by " + damager_team.getColor() + damager.getName() + ChatManager.color + " and is eliminated!");
		}
		
		game.getLobby().playerChangeTeam(damagee.getName(), Team.STANDBY);
		game.testGame();
	}
	@EventHandler
	public final void onPlayerGainExp(PlayerExpChangeEvent e)
	{
		if (!checkBaseActionValidity(e, e.getPlayer()))
		{
			e.setAmount(0);
			return;
		}
		
		Team team;
		
		if (game.getSettings().PREY_FINDER.getValue())
		{
			e.setAmount(0);
			return;
		}
		
		team = lobby.getPlayerTeam(e.getPlayer());
		if (game.getStage() != GameStage.HUNT && game.getStage() != GameStage.SETUP)
		{
			e.setAmount(0);
			return;
		}
		else if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setAmount(0);
			return;
		}
	}
	@EventHandler
	public final void onPlayerPickupItem(PlayerPickupItemEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
		{
			return;
		}
		
		Team team = lobby.getPlayerTeam(e.getPlayer());
		if (game.getStage() != GameStage.HUNT && game.getStage() != GameStage.SETUP)
		{
			e.setCancelled(true);
			return;
		}
		else if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public final void onPlayerDropItem(PlayerDropItemEvent e)
	{
		if (!checkBaseActionValidityCancellable(e, e.getPlayer()))
		{
			return;
		}
		
		Team team = lobby.getPlayerTeam(e.getPlayer());
		if (game.getStage() != GameStage.HUNT && game.getStage() != GameStage.SETUP)
		{
			e.setCancelled(true);
			return;
		}
		else if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public final void onPlayerLaunchProjectile(ProjectileLaunchEvent e)
	{
		Player player;
		Team team;
		
		if (e.getEntity().getShooter().getType() == EntityType.PLAYER)
		{
			player = ((Player) e.getEntity().getShooter());
			team = lobby.getPlayerTeam(player);
		}
		else
		{
			return;
		}
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		else if (game.getStage() != GameStage.HUNT && game.getStage() != GameStage.SETUP)
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public final void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if (e.isCancelled() || e.getLocation().getWorld() != map.getWorld().getWorld())
			return;
		
		if (!game.isRunning())
		{
			e.setCancelled(true);
			return;
		}
		
		if (ManhuntUtil.isPassive(e.getEntity()))
		{
			if (!lobby.getSettings().PASSIVE_MOBS.getValue())
			{
				e.setCancelled(true);
				return;
			}
		}
		else
		{
			if (!lobby.getSettings().HOSTILE_MOBS.getValue())
			{
				e.setCancelled(true);
				return;
			}
			for (Zone zone : this.nomobs)
			{
				if (zone.containsLocation(e.getLocation()))
				{
					e.setCancelled(true);
					return;
				}
			}
		}
		
	}
	@EventHandler
	public final void onMobTargetEvent(EntityTargetEvent e)
	{
		Player target;
		Team target_team;
		
		if (e.isCancelled())
			return;
		if (e.getEntity().getWorld() != map.getWorld().getWorld())
			return;
		if (!game.isRunning())
		{
			e.setCancelled(true);
			return;
		}
		
		if (e.getTarget() == null)
		{
			return;
		}
		else if (e.getTarget().getType() == EntityType.PLAYER)
		{
			target = (Player) (e.getTarget());
		}
		else
		{
			return;
		}
		
		if (game.getStage() == GameStage.INTERMISSION || game.getStage() == GameStage.PREGAME)
		{
			e.setCancelled(true);
			return;
		}
		
		target_team = lobby.getPlayerTeam(target);
		
		if (target_team != Team.HUNTERS && target_team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		if (game.getStage() == GameStage.SETUP && target_team == Team.HUNTERS)
		{
			e.setCancelled(true);
			return;
		}
		
	}
	
	
	private void startMonitoringBoundaries()
	{
		this.boundary_state.clear();
		this.locations.clear();
		for (String playername : lobby.getPlayerNames(Team.HUNTERS, Team.PREY))
		{
			this.boundary_state.put(playername, 0);
		}
		if (this.boundary.size() != 0 || this.setup.size() != 0)
		{
			this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
			{
				public void run()
				{
					checkBoundaries();
				}
			}, 20, 20);
		}
	}
	private void stopMonitoringBoundaries()
	{
		Bukkit.getScheduler().cancelTask(this.schedule);
		this.boundary_state.clear();
		this.locations.clear();
	}
	private void checkBoundaries()
	{
		if (!game.isRunning())
		{
			return;
		}
		
		int i = 0;
		for (final Player player : lobby.getOnlinePlayers(Team.HUNTERS, Team.PREY))
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(ManhuntPlugin.getInstance(), new Runnable()
			{
				public void run()
				{
					checkPlayer(player);
				}
			}, i);
		}
	}
	private void checkPlayer(Player player)
	{
		boolean within;
		
		if (!game.isRunning() || player == null || !player.isOnline() || Manhunt.playerIsLocked(player.getName()) || player.isDead())
		{
			return;
		}
		
		within = true;
		if (this.setup.size() > 0 && game.getStage() == GameStage.SETUP && lobby.getPlayerTeam(player) == Team.HUNTERS)
		{
			within = false;
			for (Zone zone : this.setup)
			{
				if (zone.containsLocation(player.getLocation()))
				{
					within = true;
					break;
				}
			}
		}
		else if (this.boundary.size() > 0 && (game.getStage() == GameStage.SETUP || game.getStage() == GameStage.HUNT) && (lobby.getPlayerTeam(player) == Team.HUNTERS || lobby.getPlayerTeam(player) == Team.PREY))
		{
			within = false;
			for (Zone zone : this.boundary)
			{
				if (zone.containsLocation(player.getLocation()))
				{
					within = true;
					break;
				}
			}
		}
		else
		{
			return;
		}
		
		
		if (within)
		{
			this.locations.put(player.getName(), player.getLocation());
		}
		else if (this.locations.containsKey(player.getName()))
		{
			player.teleport(this.locations.get(player.getName()));
			player.sendMessage(ChatColor.RED + "You've hit a map boundary!");
		}
		
		
		
		/*
		if (!this.boundary_state.containsKey(player.getName()))
		{
			this.boundary_state.put(player.getName(), 0);
		}
		if (within)
		{
			if (this.boundary_state.get(player.getName()) != 0)
			{
				if (this.boundary_state.get(player.getName()) > 0)
				{
					player.sendMessage(ChatColor.GREEN + "You have returned to the game area.");
					this.boundary_state.put(player.getName(), -this.boundary_state.get(player.getName()));
				}
				this.boundary_state.put(player.getName(), this.boundary_state.get(player.getName()) + 1);
			}
		}
		else
		{
			if (this.boundary_state.get(player.getName()) < 0)
			{
				this.boundary_state.put(player.getName(), -this.boundary_state.get(player.getName()));
				player.sendMessage(ChatColor.RED + "You have left the game area! Return immediately!");
			}
			this.boundary_state.put(player.getName(), this.boundary_state.get(player.getName()) + 1);
			
			if (boundary_state.get(player.getName()) == 5)
			{
				player.sendMessage(ChatColor.RED + "Return to the game area, or I will resort to violence!");
			}
			else if (boundary_state.get(player.getName()) >= 10 && boundary_state.get(player.getName()) < 15)
			{
				if (boundary_state.get(player.getName()) == 10)
				{
					player.sendMessage(ChatColor.RED + "Take this!");
				}
				player.damage(1);
			}
			else if (boundary_state.get(player.getName()) >= 15 && boundary_state.get(player.getName()) < 20)
			{
				if (boundary_state.get(player.getName()) == 15)
				{
					player.sendMessage(ChatColor.RED + "And that!");
				}
				player.damage(2);
			}
			else if (boundary_state.get(player.getName()) >= 20)
			{
				player.sendMessage(ChatColor.RED + "I warned you...");
				player.setHealth(0);
			}
			else if (boundary_state.get(player.getName()) >= 21)
			{
				lobby.playerForfeit(player.getName());
			}
		}
		*/
	}
	
	
	public void close()
	{
		stopListening();
		this.game = null;
	}
}
