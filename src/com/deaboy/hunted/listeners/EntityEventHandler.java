package com.deaboy.hunted.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;


import com.deaboy.hunted.Manhunt;
import com.deaboy.hunted.ManhuntUtil;
import com.deaboy.hunted.game.GameStage;
import com.deaboy.hunted.lobby.GameLobby;
import com.deaboy.hunted.lobby.Team;

public class EntityEventHandler implements Listener
{
	/**
	 * Handles entity damage events. Prevents PvP and
	 * implements insta-kills.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e)
	{
		
		GameLobby lobby;
		
		lobby = Manhunt.getLobby(e.getEntity().getWorld());
		
		
		if (lobby == null)
			return;
		
		if (lobby.getGame().isRunning() && lobby.getGame().getStage() != GameStage.PREGAME)
		{
			Player p = null;
			Player p2 = null;
			Team t = null;
			Team t2 = null;
			
			if (e.getEntity() instanceof Player)
			{
				p = (Player) e.getEntity();
			}
			
			if (e instanceof EntityDamageByEntityEvent)
			{
				if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player)
				{
					p2 = (Player) ((EntityDamageByEntityEvent) e).getDamager();
				}
				else if (((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile)
				{
					if (((Projectile) (((EntityDamageByEntityEvent) e).getDamager())).getShooter() instanceof Player)
					{
						p2 = (Player) ((Projectile) (((EntityDamageByEntityEvent) e).getDamager())).getShooter();
					}
				}
			}
			
			if (p != null)
				t = lobby.getPlayerTeam(p);
			if (p2 != null)
				t2 = lobby.getPlayerTeam(p2);
			
			// Either player was a spectator.
			if (t == Team.SPECTATORS || t2 == Team.SPECTATORS)
			{
				e.setCancelled(true);
				
				// Move damagee aside if the projectile was shot at them.
				//   This *may* help with invisible spectators blocking shots.
				if (t == Team.SPECTATORS && (t2 == Team.HUNTERS || t2 == Team.PREY))
				{
					if (((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile)
					{
						Projectile projectile = (Projectile) ((EntityDamageByEntityEvent) e).getDamager();
						
						projectile.setBounce(false);
					}
				}
			}
			
			// A player attacked a team mate.
			if (t == t2 && p != null && p2 != null && lobby.getSettings().FRIENDLY_FIRE.getValue())
			{
				e.setCancelled(true);
			}
			
			// Player was damaged by another player.
			if (p != null && p2 != null)
			{
				GameStage stage = lobby.getGame().getStage();
				
				if (stage == GameStage.PREGAME)
				{
					e.setCancelled(true);
				}
				else if (stage == GameStage.SETUP)
				{
					if (t != t2) e.setCancelled(true);
				}
				else
				{
					if (t != t2 && lobby.getSettings().INSTANT_DEATH.getValue())
					{
						p.setHealth(0);
					}
				}
			}
			
		}
		else
		{
			if (e.getEntity() instanceof Player)
			{
				e.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * Handles Entity Target events. Ensures that mobs do not harass players
	 * when they shouldn't.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onEntityTarget(EntityTargetEvent e)
	{
		
		GameLobby lobby;
		
		lobby = Manhunt.getLobby(e.getEntity().getWorld());
		
		if (lobby == null)
			return;
		
		if (lobby.getGame().isRunning() && lobby.getGame().getStage() != GameStage.PREGAME)
		{
			if (e.getTarget() instanceof Player)
			{
				Player p = (Player) e.getTarget();
				Team t = lobby.getPlayerTeam(p);
				
				if (lobby.getGame().getStage() == GameStage.SETUP && t != Team.PREY)
				{
					e.setCancelled(true);
				}
				else if (t != Team.HUNTERS && t != Team.PREY)
				{
					e.setCancelled(true);
				}
			}
		}
		else
		{
			e.setCancelled(true);
		}
	}

	/**
	 * Handles Creature Spawning events. Stops hostile and passive mobs from
	 * appearing if the lobby does not allow it.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		
		GameLobby lobby;
		
		lobby = Manhunt.getLobby(e.getLocation().getWorld());
		
		if (lobby == null)
			return;
		
		if (e.isCancelled())
			return;
		
		if (!lobby.getSettings().HOSTILE_MOBS.getValue() && ManhuntUtil.isHostile(e.getEntity()))
		{
			e.setCancelled(true);
		}
		
		if (!lobby.getSettings().PASSIVE_MOBS.getValue() && ManhuntUtil.isPassive(e.getEntity()))
		{
			e.setCancelled(true);
		}
	}
	
	
}