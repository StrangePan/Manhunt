package com.deaboy.manhunt.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
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

import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.NewManhuntPlugin;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneType;

public class ManhuntGame extends Game implements Listener
{
	//---------------- Properties ----------------//
	
	
	
	//---------------- Constructors ----------------//
	public ManhuntGame(Lobby lobby)
	{
		super(lobby);
	}
	
	
	
	//---------------- Public methods ----------------//
	@Override
	public void startGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startListening()
	{
		Bukkit.getPluginManager().registerEvents(this, NewManhuntPlugin.getInstance());
	}

	@Override
	public void stopListening()
	{
		HandlerList.unregisterAll(this);
	}
	
	@Override
	public void close()
	{
		super.close();
	}

	@Override
	public boolean addPlayer(String name)
	{
		if (containsPlayer(name))
			return false;
		else
			addPlayer(name, Team.SPECTATORS);
		return true;
	}
	
	
	
	//---------------- Listeners/Events ----------------//
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
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
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if (!isRunning())
			return;
		
		e.setRespawnLocation(getLobby().getSpawnLocation());
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
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

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
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
		
		
		
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
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
			// TODO Take into account interactable signs
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockPlaced().getLocation());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlock().getLocation());
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e)
	{
		checkBlockActionValidity(e, e.getPlayer(), e.getBlockClicked().getLocation());
	}
	
	//////// Universal method for block-editing events
	@EventHandler
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
		for (Zone zone : getMap().getZones(ZoneType.BUILD, ZoneType.NO_BUILD))
		{
			if ((!e.isCancelled() || zone.getType() != ZoneType.NO_BUILD) && zone.containsLocation(loc))
			{
				if (zone.getType() == ZoneType.BUILD)
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

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if (e.isCancelled())
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
			for (Zone zone : getMap().getZones(ZoneType.NO_MOBS))
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
	public void onMobTargetEvent(EntityTargetEvent e)
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
	
	
	
}
