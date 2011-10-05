package com.bendude56.hunted;

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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;



public class HuntedEntityListener extends EntityListener {
	HuntedPlugin plugin;
	
	public HuntedEntityListener(HuntedPlugin instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, this, Event.Priority.Normal, plugin);
	}
	
	public void onEntityDamage(EntityDamageEvent e) {
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
			if (event.getDamager() instanceof Player
					&& event.getEntity() instanceof Player) {
				
				Player attackingPlayer = (Player) event.getDamager();
				Player damagedPlayer = (Player) event.getEntity();
				
				if (Game.getInstance().isSpectator(attackingPlayer.getName())
						|| Game.getInstance().isSpectator(damagedPlayer.getName())) {
					event.setCancelled(true);
				}
				
				if (!plugin.friendlyFire) {
					if (Game.getInstance().isHunted(attackingPlayer.getName())
							&& Game.getInstance().isHunted(damagedPlayer.getName())) {
						event.setCancelled(true);
					}
					if (Game.getInstance().isHunter(attackingPlayer.getName())
							&& Game.getInstance().isHunter(damagedPlayer.getName())) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if (!plugin.pvpOnly) {
				if (Game.getInstance().isHunted(player.getName())) {
					//Hunted is killed
					Game.getInstance().makeSpectator(player.getName());
					Game.getInstance().broadcastAll(
							ChatColor.BLUE + "Hunted " + player.getName() + " "
							+ ChatColor.GREEN + "died from natural causes!");
					if (Game.getInstance().HuntedAmount() == 0) {
						Game.getInstance().broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.BLUE + "hunted " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.RED + "Hunters win!");
					} else {
						Game.getInstance().broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ Game.getInstance().HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ Game.getInstance().HuntedAmount());
					}
				}
				if (Game.getInstance().isHunter(player.getName())) {
					//Hunted killed a Hunter
					Game.getInstance().makeSpectator(player.getName());
					Game.getInstance().broadcastAll(
							ChatColor.RED + "Hunter " + player.getName() + " "
							+ ChatColor.GREEN + "died from natural causes!");
					if (Game.getInstance().HuntersAmount() == 0) {
						Game.getInstance().broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.RED + "hunters " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.BLUE + "Hunters lose!");
					} else {
						Game.getInstance().broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ Game.getInstance().HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ Game.getInstance().HuntedAmount());
					}
				}
			}
			
			if (player.getLastDamageCause().getEntity() instanceof Player) {
				Player killer = (Player) player.getLastDamageCause().getEntity();
				if (Game.getInstance().isHunter(killer.getName())
						&& Game.getInstance().isHunted(player.getName())) {
					//Hunter killed a Hunted
					Game.getInstance().makeSpectator(player.getName());
					Game.getInstance().broadcastAll(
							ChatColor.RED + "Hunter " + killer.getName() + " "
							+ ChatColor.GREEN + "has slain"
							+ ChatColor.BLUE + " " + player.getName() + " "
							+ ChatColor.GREEN + "!");
					if (Game.getInstance().HuntedAmount() == 0) {
						Game.getInstance().broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.BLUE + "hunted " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.RED + "Hunters win!");
					} else {
						Game.getInstance().broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ Game.getInstance().HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ Game.getInstance().HuntedAmount());
					}
				}
				if (Game.getInstance().isHunted(killer.getName())
						&& Game.getInstance().isHunter(player.getName())) {
					//Hunted killed a Hunter
					Game.getInstance().makeSpectator(player.getName());
					Game.getInstance().broadcastAll(
							ChatColor.RED + "Hunter " + player.getName() + " "
							+ ChatColor.GREEN + "has slain by"
							+ ChatColor.BLUE + " " + killer.getName() + " "
							+ ChatColor.GREEN + "!");
					if (Game.getInstance().HuntersAmount() == 0) {
						Game.getInstance().broadcastAll(
								ChatColor.GREEN + "All the " + 
								ChatColor.RED + "hunters " + 
								ChatColor.GREEN + "have been slain! The manhunt is over! " +
								ChatColor.BLUE + "Hunters lose!");
					} else {
						Game.getInstance().broadcastAll(
								ChatColor.RED + "Remaining hunters: "
								+ Game.getInstance().HuntersAmount()
								+ ChatColor.BLUE + "  Remaining Prey: "
								+ Game.getInstance().HuntedAmount());
					}
				}
			}
		}
	}
	
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!plugin.hostileMobs && event.getLocation().getWorld() == plugin.getWorld()) {
			Entity entity = event.getEntity();
			if (entity instanceof Creeper
					|| entity instanceof Zombie
					|| entity instanceof Skeleton
					|| entity instanceof Slime
					|| entity instanceof Spider
					|| entity instanceof CaveSpider
					|| entity instanceof Enderman
					|| entity instanceof Silverfish) {
				event.setCancelled(true);
			}
		}
		if (!plugin.passiveMobs && event.getLocation().getWorld() == plugin.getWorld()) {
			Entity entity = event.getEntity();
			if (entity instanceof Chicken
					|| entity instanceof Pig
					|| entity instanceof Cow
					|| entity instanceof Wolf
					|| entity instanceof Sheep
					|| entity instanceof Squid) {
				event.setCancelled(true);
			}
		}
	}
	
}