package com.bendude56.hunted;

import net.minecraft.server.DamageSource;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class HuntedEntityListener extends EntityListener {
	
	public HuntedEntityListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, this, Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onEntityDamage(EntityDamageEvent e) {
		if (!e.getEntity().getWorld().equals(HuntedPlugin.getInstance().manhuntWorld)
				|| !(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		Game g = Game.getActiveGame();
		if (!Game.isGameStarted() || !g.hasGameBegun() || g.isSpectating(p)) {
			e.setCancelled(true);
			return;
		}
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
			if (event.getDamager() instanceof Player) {
				Player p2 = (Player) event.getDamager();
				if (g.isSpectating(p2)) {
					e.setCancelled(true);
					return;
				} else if (g.getDataFile().noFriendlyFire && 
						((g.isHunted(p) && g.isHunted(p2)) ||
						(g.isHunter(p) && g.isHunter(p2)))) {
					e.setCancelled(true);
					return;
				} else if (g.getDataFile().pvpDeathInstant) {
					((CraftPlayer) p).getHandle().die(DamageSource.playerAttack(((CraftPlayer) p2).getHandle()));
					e.setCancelled(true);
					return;
				} else if (g.getDataFile().pvpDamageHalf) {
					e.setDamage(e.getDamage() / 2);
				}
			} else {
				if (g.getDataFile().hostileNoDamage) {
					e.setCancelled(true);
					return;
				} else if (g.getDataFile().natDamageHalf) {
					e.setDamage(e.getDamage() / 2);
				}
			}
		} else {
			switch (e.getCause()) {
			case FALL:
				if (g.getDataFile().fallNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			case FIRE:
				if (g.getDataFile().fireNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			case DROWNING:
				if (g.getDataFile().drownNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			case LAVA:
				if (g.getDataFile().lavaNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			case STARVATION:
				if (g.getDataFile().starveNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			default:
				if (g.getDataFile().otherNoDamage) {
					e.setCancelled(true);
					return;
				}
				break;
			}
			if (g.getDataFile().natDamageHalf) {
				e.setDamage(e.getDamage() / 2);
			}
		}
	}
	
	public void onEntityDeath(EntityDeathEvent e) {
		Game g = Game.getActiveGame();
		if (!Game.isGameStarted() || !g.hasGameBegun()) {
			return;
		}
		if (e instanceof PlayerDeathEvent) {
			PlayerDeathEvent event = (PlayerDeathEvent) e;
			Player p = (Player) event.getEntity();
			if (!g.isHunted(p) && !g.isHunter(p)) {
				return;
			}
			if (p.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
				if (((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof Player) {
					if (g.isHunter(p) && g.getDataFile().huntersRespawn) {
						p.setHealth(20);
						p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
						g.broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " has been slain, and has been respawned!");
					} else {
						g.onDie(p);
					}
				} else {
					if (g.getDataFile().mobDeathRespawn){
						p.setHealth(20);
						p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
						g.broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " has been slain, and has been respawned!");
					} else {
						g.onDie(p);
					}
				}
			} else {
				if (g.getDataFile().natDeathRespawn){
					p.setHealth(20);
					p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
					g.broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " has died of natural causes, and has been respawned!");
				} else {
					g.onDie(p);
				}
			}
		}
		/*if (!game.gameStarted()) {
			return;
		} else if(!game.huntStarted()) {
			return;
		}
		
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if (player.getLastDamageCause().getEntity() instanceof Player) {
				Player killer = (Player) player.getLastDamageCause().getEntity();
				if (game.isHunter(killer.getName())
						&& game.isHunted(player.getName())) {
					//Hunter killed a Hunted
					game.makeSpectator(player.getName());
					game.broadcastAll(
							ChatColor.RED + "Hunter " + killer.getName() + " "
							+ ChatColor.GREEN + "has slain"
							+ ChatColor.BLUE + " " + player.getName() + " "
							+ ChatColor.GREEN + "!");
					if (game.HuntedAmount() == 0) {
						game.broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.BLUE + "hunted " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.RED + "Hunters win!");
					} else {
						game.broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ game.HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ game.HuntedAmount());
					}
				}
				if (game.isHunted(killer.getName())
						&& game.isHunter(player.getName())) {
					//Hunted killed a Hunter
					game.makeSpectator(player.getName());
					game.broadcastAll(
							ChatColor.RED + "Hunter " + player.getName() + " "
							+ ChatColor.GREEN + "has slain by"
							+ ChatColor.BLUE + " " + killer.getName() + " "
							+ ChatColor.GREEN + "!");
					if (game.HuntersAmount() == 0) {
						game.broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.RED + "hunters " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.BLUE + "Hunters lose!");
					} else {
						game.broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ game.HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ game.HuntedAmount());
					}
				}
			} else if(!plugin.pvpOnly) {
				if (game.isHunted(player.getName())) {
					//Hunted is killed
					game.makeSpectator(player.getName());
					game.broadcastAll(
							ChatColor.BLUE + "Hunted " + player.getName() + " "
							+ ChatColor.GREEN + "died from natural causes!");
					if (game.HuntedAmount() == 0) {
						game.broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.BLUE + "hunted " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.RED + "Hunters win!");
					} else {
						game.broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ game.HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ game.HuntedAmount());
					}
				}
				if (game.isHunter(player.getName())) {
					//Hunter is killed 
					game.makeSpectator(player.getName());
					game.broadcastAll(
							ChatColor.RED + "Hunter " + player.getName() + " "
							+ ChatColor.GREEN + "died from natural causes!");
					if (game.HuntersAmount() == 0) {
						game.broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.RED + "hunters " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.BLUE + "Hunters lose!");
					} else {
						game.broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ game.HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ game.HuntedAmount());
					}
				}
			}
		}*/
	}
	
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.getLocation().getWorld().equals(HuntedPlugin.getInstance().manhuntWorld)) {
			return;
		}
		Entity ent = e.getEntity();
		Game g = Game.getActiveGame();
		if (!Game.isGameStarted() || !g.hasGameBegun()) {
			e.setCancelled(true);
			return;
		}
		if (ent instanceof Zombie
				|| ent instanceof Skeleton
				|| ent instanceof Slime
				|| ent instanceof Spider
				|| ent instanceof CaveSpider
				|| ent instanceof Enderman
				|| ent instanceof Silverfish
				|| ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !g.getDataFile().spawnHostile);
		} else if (ent instanceof Creeper) {
			e.setCancelled(e.isCancelled() || !g.getDataFile().spawnCreeper);
		} else if (ent instanceof Chicken
				|| ent instanceof Pig
				|| ent instanceof Cow
				|| ent instanceof Sheep
				|| ent instanceof Squid) {
			e.setCancelled(e.isCancelled() || !g.getDataFile().spawnAnimals);
		}
	}
	
}