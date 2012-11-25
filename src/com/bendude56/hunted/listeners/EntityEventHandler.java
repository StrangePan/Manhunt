package com.bendude56.hunted.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;


import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.game.ManhuntGame.GameStage;
import com.bendude56.hunted.teams.TeamManager.Team;

public class EntityEventHandler implements Listener
{
	private ManhuntPlugin plugin;

	public EntityEventHandler(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e)
	{
		if (e.getEntity().getWorld() != plugin.getWorld())
		{
			return;
		}
		
		if (plugin.gameIsRunning() && plugin.getGame().getStage() != GameStage.PREGAME)
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
				t = plugin.getTeams().getTeamOf(p);
			if (p2 != null)
				t2 = plugin.getTeams().getTeamOf(p2);
			
			if (t == Team.SPECTATORS || t2 == Team.SPECTATORS)
			{
				e.setCancelled(true);
			}
			if (t == t2 && p != null && p2 != null && !plugin.getSettings().FRIENDLY_FIRE.value)
			{
				e.setCancelled(true);
			}
			if (p != null && p2 != null) //Player damaged by other player
			{
				GameStage stage = plugin.getGame().getStage();
				
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
					if (t != t2 && plugin.getSettings().INSTANT_DEATH.value)
					{
						e.setDamage(200);
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

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e)
	{
		if (e.getEntity().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (plugin.gameIsRunning() && plugin.getGame().getStage() != GameStage.PREGAME)
		{
			if (e.getTarget() instanceof Player)
			{
				Player p = (Player) e.getTarget();
				Team t = plugin.getTeams().getTeamOf(p);
				
				if (plugin.getGame().getStage() == GameStage.PREGAME)
				{
					e.setCancelled(true);
				}
				if (t != Team.HUNTERS && t != Team.PREY)
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

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if (e.getLocation().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (e.isCancelled())
		{
			return;
		}
		
		if (!plugin.getSettings().HOSTILE_MOBS.value && ManhuntUtil.isHostile(e.getEntity()))
		{
			e.setCancelled(true);
		}
		
		if (!plugin.getSettings().PASSIVE_MOBS.value && ManhuntUtil.isPassive(e.getEntity()))
		{
			e.setCancelled(true);
		}
	}
}