package com.deaboy.manhunt.game;

import org.bukkit.Bukkit;
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
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
	
	
	//////////////// CONSTRUCTORS ////////////////
	public ManhuntGameListener(ManhuntGame game)
	{
		if (game == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		
		this.game = game;
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- EVENTS ----------------//
	public void startListening()
	{
		Bukkit.getPluginManager().registerEvents(this, ManhuntPlugin.getInstance());
		this.map = game.getMap();
		this.lobby = game.getLobby();
	}
	public void stopListening()
	{
		HandlerList.unregisterAll(this);
		this.lobby = null;
		this.map = null;
	}
	
	private boolean checkBaseActionValidityCancellable(Cancellable e, Player p)
	{
		if (e.isCancelled())
		{
			return false;
		}
		else if (!lobby.containsPlayer(p))
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
		for (Zone zone : map.getZones(ZoneFlag.BUILD))
		{
			if (zone.containsLocation(loc))
				return;
		}
		for (Zone zone : map.getZones(ZoneFlag.NO_BUILD))
		{
			if (zone.containsLocation(loc))
			{
				e.setCancelled(true);
				return;
			}
		}
		
		
		if (game.getStage() == GameStage.SETUP && team == Team.HUNTERS)
		{
			for (Zone zone : map.getZones(ZoneFlag.SETUP))
			{
				if (zone.containsLocation(loc))
					return;
			}
			e.setCancelled(true);
			return;
		}
		else
		{
			for (Zone zone : map.getZones(ZoneFlag.SETUP))
			{
				if (zone.containsLocation(loc))
				{
					e.setCancelled(true);
					return;
				}
			}
			for (Zone zone : map.getZones(ZoneFlag.BOUNDARY))
			{
				if (zone.containsLocation(loc))
					return;
			}
		}
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
	
	@EventHandler
	public final void onFinderActivate(PlayerInteractEvent e)
	{
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
			Manhunt.startFinderFor(e.getPlayer(), 8000L, game.getSettings().FINDER_COOLDOWN.getValue()*1000L, Material.getMaterial(game.getSettings().FINDER_ITEM.getValue()), 4, 0, true);
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
		if (e.isCancelled() || !checkBaseActionValidity(e, e.getPlayer()))
			return;
		
		Team player_team = lobby.getPlayerTeam(e.getPlayer());
		
		if (player_team == Team.STANDBY || player_team == null)
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
		
		if (e.getEntity().getType() == EntityType.PLAYER)
		{
			damagee = (Player) e.getEntity();
		}
		else
		{
			return;
		}
		
		if (!checkBaseActionValidityCancellable(e, damagee))
			return;
		
		if (game.getStage() == GameStage.INTERMISSION || game.getStage() == GameStage.PREGAME)
		{
			e.setCancelled(true);
			return;
		}
		
		if (e.getDamager() instanceof Projectile) // Deal with projectiles
		{
			if (((Projectile) e.getDamager()).getShooter().getType() == EntityType.PLAYER)
			{
				damager = ((Player) ((Projectile) e.getDamager()).getShooter());
			}
			else
			{
				return;
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
			return;
		}
		
		damager_team = lobby.getPlayerTeam(damager);
		damagee_team = lobby.getPlayerTeam(damagee);
		
		if (damager_team == null)
			return;
		if (damagee_team == null)
			return;
		
		if (damager_team != Team.HUNTERS && damager_team != Team.PREY || damagee_team != Team.HUNTERS && damager_team != Team.PREY)
		{
			e.setCancelled(true);
			return;
		}
		
		if (game.getStage() == GameStage.SETUP)
		{
			if (damagee_team == Team.HUNTERS)
			{
				e.setCancelled(true);
				return;
			}
		}
		
		if (damager_team == damagee_team && lobby.getSettings().FRIENDLY_FIRE.getValue() == false)
		{
			e.setCancelled(true);
			return;
		}
		
		if (game.getSettings().INSTANT_DEATH.getValue())
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
		
		game.testGame();
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
			for (Zone zone : map.getZones(ZoneFlag.NO_MOBS))
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
		
		if (e.getTarget().getType() == EntityType.PLAYER)
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
	
	
	
	public void close()
	{
		stopListening();
		this.game = null;
	}
}
