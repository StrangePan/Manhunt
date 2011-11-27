package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class HuntedEntityListener extends EntityListener {
	
	public HuntedEntityListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.ENTITY_TARGET, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, this, Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onEntityDamage(EntityDamageEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		SettingsFile settings = HuntedPlugin.getInstance().settings;
		Player p;
		
		
		if (e.getEntity() instanceof Player) {
			if (g.isSpectating((Player) e.getEntity())) {	//SPECTATOR EXCEPTIONS
				e.setCancelled(true);
				return;
			}
			if (!g.gameHasBegun()) {		//GAME HASN'T STARTED EXCEPTIONS
				e.setCancelled(true);
				return;
			}
		} else {
			return;
		}
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
			if (event.getDamager() instanceof Player) {
				if (g.isSpectating((Player) event.getEntity())) {
					e.setCancelled(true);
					return;
				}
			}
		}
		//END SPECTATOR EXCEPTIONS
		
		if (!(e.getEntity() instanceof Player)) {
			return;
		} else {
			p = (Player) e.getEntity();
		}
		
		if (g.isSpectating(p)) {
			e.setCancelled(true);
			return;
		}
		
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
			Player p2;
			
			if (event.getDamager() instanceof Arrow) {
				if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
					p2 = (Player) ((Arrow) event.getDamager()).getShooter();
				} else {
					if (!settings.envDeath) {
						if (e.getDamage() >= p.getHealth()) {
							e.setDamage(p.getHealth()-1);
						}
					}
					return;
				}
			} else if (event.getDamager() instanceof Player) {
				p2 = (Player) event.getDamager();
			} else {
				if (!settings.envDeath) {
					if (e.getDamage() >= p.getHealth()) {
						e.setDamage(p.getHealth()-1);
					}
				}
				return;
			}
			
			if (g.isSpectating(p2)) {
				e.setCancelled(true);
				return;
			}
				
			if (!settings.friendlyFire
					&& ((g.isHunted(p) && g.isHunted(p2))
							|| (g.isHunter(p) && g.isHunter(p2)))) {
				e.setCancelled(true);
				return;
			}
			
			if (settings.pvpInstantDeath &&
					((g.isHunter(p) && g.isHunted(p2))
							|| (g.isHunted(p) && g.isHunter(p2)))) {
				p.setHealth(0);
				return;
			}
		} else {
			if (!settings.envDeath || !g.huntHasBegun()) {
				if (e.getDamage() >= p.getHealth()) {
					e.setDamage(p.getHealth()-1);
				}
			}
		}
	}
	
	public void onEntityDeath(EntityDeathEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		SettingsFile settings = HuntedPlugin.getInstance().settings;

		if (!e.getEntity().getWorld().equals(HuntedPlugin.getInstance().getWorld())
				|| !(e.getEntity() instanceof Player)) {
			return;
		}
		if (!g.gameHasBegun()) {
			return;
		}
		if (e instanceof PlayerDeathEvent) {
			PlayerDeathEvent event = (PlayerDeathEvent) e;
			Player p = (Player) event.getEntity();
			Player p2;
			if (!g.isHunted(p) && !g.isHunter(p)) {
				return;
			}
			if (g.getLocatorByPlayer(p) != -1) { //PLAYER IS IN LOCATOR LIST
				g.stopLocator(p);
			}
			if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				if (((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof Arrow
						&& ((Arrow) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager()).getShooter() instanceof Player) {
					p2 = (Player) ((Arrow) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager()).getShooter();
				} else if (((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof Player) {
					p2 = (Player) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();
				} else if ((settings.envHunterRespawn && g.isHunter(p))
						|| (settings.envPreyRespawn && g.isHunted(p))) {
					p.setHealth(20);
					p.setFoodLevel(20);
					
					if (g.isHunter(p)) {
					} else {
						p.teleport(settings.preySpawn);
					}
					g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE + " died from natural causes, and has respawned!");
					return;
				} else {
					g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE + " died from natural causes and is now " + ChatColor.YELLOW + "Spectating!");
					g.onDie(p);
					return;
				}
				
				if ((g.isHunter(p) && g.isHunter(p2))
					|| (g.isHunted(p) && g.isHunted(p2))) {
					g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE + " has been slain by "
							+ g.getColor(p2) + "teammate " + p2.getName() + "!");
					g.onDie(p);
				} else {
					g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE + " has been slain by "
						+ g.getColor(p2) + p2.getName() + "!");
					g.onDie(p);
				}
			} 
		}
	}
	
	public void onEntityTarget(EntityTargetEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		if (e.getTarget() instanceof Player) {
			if (g.isSpectating((Player) e.getTarget()) && g.gameHasBegun()) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (!e.getLocation().getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
			return;
		}
		Entity ent = e.getEntity();
		SettingsFile settings = HuntedPlugin.getInstance().settings;
		if (ent instanceof Zombie
				|| ent instanceof Skeleton
				|| ent instanceof Creeper
				|| ent instanceof Slime
				|| ent instanceof Spider
				|| ent instanceof CaveSpider
				|| ent instanceof Enderman
				|| ent instanceof Silverfish
				|| ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !settings.spawnHostile);
		} else if (ent instanceof Chicken
				|| ent instanceof Pig
				|| ent instanceof Cow
				|| ent instanceof Sheep
				|| ent instanceof Squid
				|| ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !settings.spawnPassive);
		}
	}
}