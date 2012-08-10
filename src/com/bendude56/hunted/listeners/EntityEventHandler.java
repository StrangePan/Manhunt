package com.bendude56.hunted.listeners;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;


import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.games.Game.GameStage;
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
				
				switch (stage)
				{
					case PREGAME: e.setCancelled(true);
					case SETUP:	if (t != t2) e.setCancelled(true);
					default:	break;
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
		if (e.getTarget() instanceof Player)
		{
			Player p = (Player) e.getTarget();
			Team t = plugin.getTeams().getTeamOf(p);
			
			if (t != Team.HUNTERS && t != Team.PREY && plugin.gameIsRunning())
			{
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.getLocation().getWorld() != plugin.getWorld()) {
			return;
		}
		Entity ent = e.getEntity();
		if (ent instanceof Zombie || ent instanceof Skeleton
				|| ent instanceof Creeper || ent instanceof Slime
				|| ent instanceof Spider || ent instanceof CaveSpider
				|| ent instanceof Enderman || ent instanceof Silverfish
				|| ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !plugin.getSettings().HOSTILE_MOBS.value);
		} else if (ent instanceof Chicken || ent instanceof Pig
				|| ent instanceof Cow || ent instanceof Sheep
				|| ent instanceof Squid || ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !plugin.getSettings().PASSIVE_MOBS.value);
		}
	}
}