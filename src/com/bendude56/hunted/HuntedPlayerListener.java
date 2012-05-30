package com.bendude56.hunted;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.EventException;
//import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
//import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
//import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
//import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
//import org.bukkit.plugin.EventExecutor;
//import org.bukkit.plugin.RegisteredListener;

public class HuntedPlayerListener implements Listener {
	
	/*public HuntedPlayerListener() {
		PlayerChatEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerJoinEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerQuitEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerKickEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerMoveEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerItemHeldEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerInteractEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		// PlayerGameModeChangeEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerChangedWorldEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerBucketFillEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerBucketEmptyEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerPickupItemEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		PlayerDropItemEvent.getHandlerList().register(new RegisteredListener(this, this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
	}*/

	Game g = HuntedPlugin.getInstance().getGame();
	SettingsFile settings = HuntedPlugin.getInstance().getSettings();
	WorldDataFile worlddata = HuntedPlugin.getInstance().getWorldData();

	/*@Override
	public void execute(Listener listener, Event event) throws EventException {
		if (event instanceof PlayerChatEvent) {
			PlayerChatEvent e = (PlayerChatEvent) event;
			onPlayerChat(e);
		} else if (event instanceof PlayerJoinEvent) {
			PlayerJoinEvent e = (PlayerJoinEvent) event;
			onPlayerJoin(e);
		} else if (event instanceof PlayerQuitEvent) {
			PlayerQuitEvent e = (PlayerQuitEvent) event;
			onPlayerQuit(e);
		} else if (event instanceof PlayerMoveEvent) {
			PlayerMoveEvent e = (PlayerMoveEvent) event;
			onPlayerMove(e);
		} else if (event instanceof PlayerItemHeldEvent) {
			PlayerItemHeldEvent e = (PlayerItemHeldEvent) event;
			onPlayerItemHeld(e);
		} else if (event instanceof PlayerInteractEvent) {
			PlayerInteractEvent e = (PlayerInteractEvent) event;
			onPlayerInteract(e);
		} else if (event instanceof PlayerBucketFillEvent) {
			PlayerBucketFillEvent e = (PlayerBucketFillEvent) event;
			onBucketFill(e);
		} else if (event instanceof PlayerBucketEmptyEvent) {
			PlayerBucketEmptyEvent e = (PlayerBucketEmptyEvent) event;
			onBucketEmpty(e);
		} else if (event instanceof PlayerPickupItemEvent) {
			PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
			onPlayerPickupItem(e);
		} else if (event instanceof PlayerDropItemEvent) {
			PlayerDropItemEvent e = (PlayerDropItemEvent) event;
			onPlayerDropItem(e);
		}
	}*/
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		HuntedPlugin.getInstance().log(Level.INFO,
				"<" + p.getName() + "> " + e.getMessage());
		if (!g.gameHasBegun()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(ChatColor.WHITE + "<" + g.getColor(p)
						+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			}
			e.setCancelled(true);
			return;
		}
		if (settings.allTalk()) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(ChatColor.WHITE + "<" + g.getColor(p)
						+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			}
			e.setCancelled(true);
			return;
		}
		if (g.isHunter(p)
				&& p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.broadcastHunters(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else if (g.isHunted(p)
				&& p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.broadcastHunted(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else if (g.isSpectating(p)
				&& p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.broadcastSpectators(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if ((!g.isHunted(player) && !g.isHunter(player) && !g
						.isSpectating(player))
						|| player.getWorld() == HuntedPlugin.getInstance()
								.getWorld()) {
					player.sendMessage(ChatColor.WHITE + "<" + p.getName()
							+ "> " + e.getMessage());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		Player p = e.getPlayer();
		if (p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			if (g.gameHasBegun() && g.isPlaying(p)) {
				e.setJoinMessage(null);
			}
			g.onLogin(p);
		} else if (g.isPlaying(p)) {
			p.sendMessage(ChatColor.RED
					+ "To rejoin the manhunt game, type /manhunt join");
		}
	}

	/*
	 * public void onPlayerKick(PlayerKickEvent e) { if
	 * (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld() &&
	 * g.gameHasBegun() && g.isPlaying(e.getPlayer())) {
	 * e.setLeaveMessage(null); } g.onLogout(e.getPlayer()); }
	 */

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld()
				&& g.gameHasBegun() && g.isPlaying(e.getPlayer())) {
			e.setQuitMessage(null);
		}
		g.onLogout(e.getPlayer());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!g.gameHasBegun()) {
			if (e.getPlayer().getWorld() == HuntedPlugin.getInstance()
					.getWorld()) {
				e.getPlayer().setFoodLevel(20);
			}
			return;
		}
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		if (!g.isHunted(e.getPlayer()) && !g.isHunter(e.getPlayer())) {
			return;
		}
		Player p = e.getPlayer();
		if (!g.huntHasBegun() && g.isHunter(p)
				&& worlddata.pregameBoundary() > -1) {
			if (worlddata.boxBoundary()) {
				if (g.outsideBoxedArea(p.getLocation(), true)) {
					p.teleport(g.safeTeleport(g.teleportPregameBoxedLocation(p
							.getLocation())));
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED
								+ "You've ventured too far!");
					return;
				}
			} else {
				if (g.getDistance(worlddata.pregameSpawn(), p.getLocation()) > worlddata
						.pregameBoundary()) {
					g.stepPlayer(p, 1.0, worlddata.pregameSpawn());
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED
								+ "You've ventured too far!");
					return;
				}
			}
		} else {
			if (worlddata.boxBoundary()) {
				if (g.outsideBoxedArea(p.getLocation(), false)) {
					p.teleport(g.safeTeleport(g.teleportBoxedLocation(p
							.getLocation())));
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED
								+ "You've ventured too far!");
					return;
				}
			} else {
				if (g.getDistance(
						g.getNearestLocation(p.getLocation(),
								worlddata.preySpawn(), worlddata.hunterSpawn()),
						p.getLocation()) > worlddata.mapBoundary()) {
					g.stepPlayer(
							p,
							1.0,
							g.getNearestLocation(p.getLocation(),
									worlddata.preySpawn(),
									worlddata.hunterSpawn()));
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED
								+ "You've ventured too far!");
					return;
				}
			}
		}
		if (g.getLocatorByPlayer(p) != -1
				&& g.getLocatorStage(g.getLocatorByPlayer(p)) != 2) { // PLAYER
																		// IS IN
																		// LOCATOR
																		// LIST
			if (g.getDistance(p.getLocation(),
					g.getLocatorLocation(g.getLocatorByPlayer(p))) > 1.5
					|| p.getPlayer().getWorld() != HuntedPlugin.getInstance()
							.getWorld()) {
				p.sendMessage(ChatColor.RED
						+ "You moved before nearest Prey could be found!");
				g.stopLocator(p);
			}
		}
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.COMPASS
				&& g.getLocatorByPlayer(p) != -1
				&& g.getLocatorStage(g.getLocatorByPlayer(p)) != 2) {
			p.sendMessage(ChatColor.RED + "Prey Finder 9001 cancelled.");
			g.stopLocator(p);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (HuntedPlugin.getInstance().getWorld() != p.getWorld()) {
			return;
		}

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				|| e.getAction() == Action.RIGHT_CLICK_AIR
				|| e.getAction() == Action.LEFT_CLICK_BLOCK
				|| e.getAction() == Action.LEFT_CLICK_AIR) {

			if (g.gameHasBegun() && g.isSpectating(p)) {
				e.setCancelled(true);
				return;
			}

			if (g.isHunter(p)
					&& p.getItemInHand().getType() == Material.COMPASS
					&& settings.preyFinder() && g.huntHasBegun()) {
				if (g.getLocatorByPlayer(p) == -1) {
					if (g.HuntedAmount(true) == 0) {
						p.sendMessage(ChatColor.RED
								+ "There are no Prey online!");
						return;
					}
					g.startLocator(p);
					p.sendMessage(ChatColor.GOLD
							+ "Prey Finder 9001 activated! Stand still for 8 seconds.");

				} else {
					String time = ((int) Math.floor((g.getLocatorTick(g
							.getLocatorByPlayer(p)) - g.getTick()) / 1200))
							+ ":";
					if ((int) (Math.floor((g.getLocatorTick(g
							.getLocatorByPlayer(p)) - g.getTick()) / 20) - (int) Math
							.floor((g.getLocatorTick(g.getLocatorByPlayer(p)) - g
									.getTick()) / 1200) * 60) < 10) {
						time += "0";
					}
					time += ""
							+ (int) (Math
									.floor((g.getLocatorTick(g
											.getLocatorByPlayer(p)) - g
											.getTick()) / 20) - (int) Math
									.floor((g.getLocatorTick(g
											.getLocatorByPlayer(p)) - g
											.getTick()) / 1200) * 60);
					if (g.getLocatorStage(g.getLocatorByPlayer(e.getPlayer())) == 2) {
						p.sendMessage(ChatColor.RED
								+ "Prey Finder 9001 is still charging. Time left: "
								+ time);
					}
					return;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {

		Player p = e.getPlayer();
		if (e.getTo().getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.onLogin(p);
		} else {
			g.onLogout(p);
		}
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e) {

		Player p = e.getPlayer();
		if (e.getTo().getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.onLogin(p);
		} else {
			g.onLogout(p);
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {

		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							worlddata.hunterSpawn()) <= worlddata
							.noBuildRange()) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							worlddata.preySpawn()) <= worlddata.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}

	/*
	 * public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
	 * 
	 * Player p = e.getPlayer(); if (p.getWorld() !=
	 * HuntedPlugin.getInstance().getWorld()) { return; } if (!g.gameHasBegun())
	 * { return; }
	 * 
	 * if (g.isHunter(p) || g.isHunted(p)) { if (e.getNewGameMode() !=
	 * GameMode.SURVIVAL) { e.setCancelled(true); return; } } if
	 * (g.isSpectating(p)) { if (settings.flyingSpectators()) { if
	 * (e.getNewGameMode() != GameMode.CREATIVE) { e.setCancelled(true); } }
	 * else { if (e.getNewGameMode() != GameMode.SURVIVAL) {
	 * e.setCancelled(true); } } } }
	 */

	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent e) {

		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							worlddata.hunterSpawn()) <= worlddata
							.noBuildRange()) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							worlddata.preySpawn()) <= worlddata.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}

}