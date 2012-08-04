package com.bendude56.hunted.listeners;

import java.util.logging.Level;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;


import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.games.ManhuntGame;
import com.bendude56.hunted.loadouts.LoadoutManager;
import com.bendude56.hunted.settings.SettingsManager;

public class EntityEventHandler implements Listener
{
	private HuntedPlugin plugin;

	public EntityEventHandler(HuntedPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Player p;

		if (e.getEntity().getWorld() != plugin.getWorld()) {
			return;
		}

		if (e.getEntity() instanceof Player) {
			if (plugin.getGame().isSpectating((Player) e.getEntity())) { // SPECTATOR
															// EXCEPTIONS
				e.setCancelled(true);
				return;
			}
			if (!plugin.getGame().gameHasBegun()) { // GAME HASN'T STARTED EXCEPTIONS
				e.setCancelled(true);
				return;
			}
		}
		if (e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
			if (event.getDamager() instanceof Player) {
				if (plugin.getGame().isSpectating((Player) event.getDamager())
						&& plugin.getGame().gameHasBegun()) {
					e.setCancelled(true);
					return;
				}
			}
		}

		// END SPECTATOR EXCEPTIONS

		if (e.getEntity() instanceof Player) {
			p = (Player) e.getEntity();
		} else {
			return;
		}

		if (plugin.getGame().isSpectating(p)) {
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
					if (!settings.ENVIRONMENT_DEATH.value) {
						if (e.getDamage() >= p.getHealth()) {
							e.setDamage(p.getHealth() - 1);
						}
					}
					return;
				}
			} else if (event.getDamager() instanceof Player) {
				p2 = (Player) event.getDamager();
			} else {
				if (!settings.ENVIRONMENT_DEATH.value) {
					if (e.getDamage() >= p.getHealth()) {
						e.setDamage(p.getHealth() - 1);
					}
				}
				return;
			}

			if (plugin.getGame().isSpectating(p2)) {
				e.setCancelled(true);
				return;
			}

			if (!plugin.getGame().huntHasBegun()) {
				e.setCancelled(true);
				return;
			}

			if (!settings.FRIENDLY_FIRE.value
					&& ((plugin.getGame().isHunted(p) && plugin.getGame().isHunted(p2)) || (plugin.getGame().isHunter(p) && plugin.getGame()
							.isHunter(p2)))) {
				e.setCancelled(true);
				return;
			}

			if (settings.FRIENDLY_FIRE.value
					&& ((plugin.getGame().isHunter(p) && plugin.getGame().isHunted(p2)) || (plugin.getGame().isHunted(p) && plugin.getGame()
							.isHunter(p2)))) {
				p.setHealth(0);
				return;
			}
		} else {
			if (!settings.ENVIRONMENT_DEATH.value || !plugin.getGame().huntHasBegun()) {
				if (e.getDamage() >= p.getHealth()) {
					e.setDamage(p.getHealth() - 1);
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {

		if (!e.getEntity().getWorld()
				.equals(plugin.getWorld())
				|| !(e.getEntity() instanceof Player)) {
			return;
		}
		if (!plugin.getGame().gameHasBegun()) {
			return;
		}
		if (e instanceof PlayerDeathEvent) {
			PlayerDeathEvent event = (PlayerDeathEvent) e;
			Player p = (Player) event.getEntity();

			if (!plugin.getGame().isSpectating(p) && !plugin.getGame().isHunted(p) && !plugin.getGame().isHunter(p)) {
				return;
			}

			Player p2;
			if (!plugin.getGame().isHunted(p) && !plugin.getGame().isHunter(p)) {
				return;
			}
			if (plugin.getGame().getLocatorByPlayer(p) != -1) { // PLAYER IS IN LOCATOR LIST
				plugin.getGame().stopLocator(p);
			}
			if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				if (((EntityDamageByEntityEvent) p.getLastDamageCause())
						.getDamager() instanceof Arrow
						&& ((Arrow) ((EntityDamageByEntityEvent) p
								.getLastDamageCause()).getDamager())
								.getShooter() instanceof Player) {
					p2 = (Player) ((Arrow) ((EntityDamageByEntityEvent) p
							.getLastDamageCause()).getDamager()).getShooter();
				} else if (((EntityDamageByEntityEvent) p.getLastDamageCause())
						.getDamager() instanceof Player) {
					p2 = (Player) ((EntityDamageByEntityEvent) p
							.getLastDamageCause()).getDamager();
				} else if (settings.ENVIRONMENT_RESPAWN.value) {
					p.setHealth(20);
					p.setFoodLevel(20);

					if (plugin.getGame().isHunter(p)) {
						p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_HUNTER.value));
					} else {
						p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_PREY.value));
					}
					plugin.getGame().broadcastAll(ChatColor.GOLD + "---[ " + plugin.getGame().getColor(p)
							+ p.getName() + ChatColor.WHITE
							+ " died from natural causes and has respawned!"
							+ ChatColor.GOLD + " ]---");
					HuntedPlugin
							.getInstance()
							.log(Level.INFO,
									"---[ "
											+ p.getName()
											+ " died from natural causes and has respawned! ]---");
					((PlayerDeathEvent) e).setDeathMessage(null);
					if (plugin.getGame().isHunter(p))
						p.getInventory().setContents(loadouts.getHunterLoadout().getContents());
					if (plugin.getGame().isHunted(p))
						p.getInventory().setContents(loadouts.getPreyLoadout().getContents());
					return;
				} else {
					plugin.getGame().broadcastAll(ChatColor.GOLD + "---[ " + plugin.getGame().getColor(p)
							+ p.getName() + ChatColor.WHITE
							+ " died from natural causes and is now "
							+ ChatColor.YELLOW + "Spectating!" + ChatColor.GOLD
							+ " ]---");
					HuntedPlugin
							.getInstance()
							.log(Level.INFO,
									"---[ "
											+ p.getName()
											+ " died from natural causes and is now spectating! ]---");
					((PlayerDeathEvent) e).setDeathMessage(null);
					plugin.getGame().onDie(p);
					return;
				}

				if ((plugin.getGame().isHunter(p) && plugin.getGame().isHunter(p2))
						|| (plugin.getGame().isHunted(p) && plugin.getGame().isHunted(p2))) {
					plugin.getGame().broadcastAll(ChatColor.GOLD + "---[ " + plugin.getGame().getColor(p)
							+ p.getName() + ChatColor.WHITE + " was "
							+ ChatColor.RED + "ELIMINATED" + ChatColor.WHITE
							+ " by " + plugin.getGame().getColor(p2) + "teammate "
							+ p2.getName() + "!" + ChatColor.GOLD + " ]---");
					plugin.log(
							Level.INFO,
							"---[ " + p.getName()
									+ " was ELIMINATED by teammate "
									+ p2.getName() + "! ]---");
					((PlayerDeathEvent) e).setDeathMessage(null);
					plugin.getGame().onDie(p);
				} else {
					plugin.getGame().broadcastAll(ChatColor.GOLD + "---[   " + plugin.getGame().getColor(p)
							+ p.getName() + ChatColor.WHITE + " was "
							+ ChatColor.RED + "ELIMINATED" + ChatColor.WHITE
							+ " by " + plugin.getGame().getColor(p2) + p2.getName() + "!"
							+ ChatColor.GOLD + " ]---");
					plugin.log(
							Level.INFO,
							"---[ " + p.getName() + " was ELIMINATED by "
									+ p2.getName() + "! ]---");
					((PlayerDeathEvent) e).setDeathMessage(null);
					plugin.getGame().onDie(p);
				}
			} else if (settings.ENVIRONMENT_RESPAWN.value) {
				p.setHealth(20);
				p.setFoodLevel(20);

				if (plugin.getGame().isHunter(p)) {
					p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_HUNTER.value));
				} else {
					p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_PREY.value));
				}
				plugin.getGame().broadcastAll(ChatColor.GOLD + "---[ " + plugin.getGame().getColor(p)
						+ p.getName() + ChatColor.WHITE
						+ " died from natural causes and has respawned!"
						+ ChatColor.GOLD + " ]---");
				HuntedPlugin
						.getInstance()
						.log(Level.INFO,
								"---[ "
										+ p.getName()
										+ " died from natural causes and has respawned! ]---");
				((PlayerDeathEvent) e).setDeathMessage(null);
				if (plugin.getGame().isHunter(p))
					p.getInventory().setContents(loadouts.getHunterLoadout().getContents());
				if (plugin.getGame().isHunted(p))
					p.getInventory().setContents(loadouts.getPreyLoadout().getContents());
				return;
			} else {
				plugin.getGame().broadcastAll(ChatColor.GOLD + "---[ " + plugin.getGame().getColor(p)
						+ p.getName() + ChatColor.WHITE
						+ " died from natural causes and is now "
						+ ChatColor.YELLOW + "Spectating!" + ChatColor.GOLD
						+ " ]---");
				HuntedPlugin
						.getInstance()
						.log(Level.INFO,
								"---[ "
										+ p.getName()
										+ " died from natural causes and is now spectating! ]---");
				((PlayerDeathEvent) e).setDeathMessage(null);
				plugin.getGame().onDie(p);
				return;
			}
		}
	}

	@EventHandler
	public void onEntityTarget(EntityTargetEvent e) {
		if (e.getTarget() instanceof Player) {
			if (plugin.getGame().isSpectating((Player) e.getTarget()) && plugin.getGame().gameHasBegun()) {
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
			e.setCancelled(e.isCancelled() || !settings.HOSTILE_MOBS.value);
		} else if (ent instanceof Chicken || ent instanceof Pig
				|| ent instanceof Cow || ent instanceof Sheep
				|| ent instanceof Squid || ent instanceof Wolf) {
			e.setCancelled(e.isCancelled() || !settings.PASSIVE_MOBS.value);
		}
	}
}