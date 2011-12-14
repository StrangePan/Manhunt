package com.bendude56.hunted;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
//import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
//import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
//import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntedPlayerListener extends PlayerListener {

	public HuntedPlayerListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_ITEM_HELD, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		//Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_GAME_MODE_CHANGE, this,
		//		Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_BUCKET_FILL, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_PICKUP_ITEM, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_DROP_ITEM, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	Game g = HuntedPlugin.getInstance().getGame();
	SettingsFile settings = HuntedPlugin.getInstance().getSettings();

	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		HuntedPlugin.getInstance().log(Level.INFO,
				"<" + p.getName() + "> " + e.getMessage());
		if (!g.gameHasBegun()) {
			g.broadcastAll(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
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
		if (g.isHunter(p)) {
			g.broadcastHunters(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else if (g.isHunted(p)) {
			g.broadcastHunted(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else if (g.isSpectating(p)) {
			g.broadcastSpectators(ChatColor.WHITE + "<" + g.getColor(p)
					+ p.getName() + ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
			return;
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!g.isHunted(player) && !g.isHunter(player)
						&& !g.isSpectating(player)) {
					player.sendMessage(ChatColor.WHITE + "<"
							+ p.getName() + "> "
							+ e.getMessage());
				}
			}
		}
	}

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
	public void onPlayerKick(PlayerKickEvent e) {
		if (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld()
				&& g.gameHasBegun()
				&& g.isPlaying(e.getPlayer())) {
			e.setLeaveMessage(null);
		}
		g.onLogout(e.getPlayer());
	}
	*/

	public void onPlayerQuit(PlayerQuitEvent e) {
		if (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld()
				&& g.gameHasBegun()
				&& g.isPlaying(e.getPlayer())) {
			e.setQuitMessage(null);
		}
		g.onLogout(e.getPlayer());
	}
	
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!g.gameHasBegun()) {
			if (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld()) {
				e.getPlayer().setFoodLevel(20);
			}
			return;
		}
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		if (!g.isHunted(e.getPlayer())
				&& !g.isHunter(e.getPlayer())) {
			return;
		}
		Player p = e.getPlayer();
		if (!g.huntHasBegun()
				&& g.isHunter(p)
				&& g.getDistance(p.getLocation(), HuntedPlugin.getInstance()
						.getWorld().getSpawnLocation()) > settings.hunterBoundry()) {
			g.stepPlayer(p, 1.0, HuntedPlugin.getInstance().getWorld()
					.getSpawnLocation());
			p.sendMessage(ChatColor.RED + "You've ventured too far!");
			return;
		} else if (g.getDistance(p.getLocation(), settings.preySpawn()) > settings.globalBoundry()
				&& g.getDistance(p.getLocation(), settings.hunterSpawn()) > settings.globalBoundry()) {
			if (g.getDistance(p.getLocation(), settings.preySpawn()) < g
					.getDistance(p.getLocation(), settings.hunterSpawn())) {
				g.stepPlayer(p, 1.0, settings.preySpawn());
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			} else {
				g.stepPlayer(p, 1.0, settings.hunterSpawn());
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			}
		}
		if (g.getLocatorByPlayer(p) != -1
				&& g.getLocatorStage(g.getLocatorByPlayer(p)) != 2) { // PLAYER
																		// IS IN
																		// LOCATOR
																		// LIST
			if (g.getDistance(p.getLocation(),
					g.getLocatorLocation(g.getLocatorByPlayer(p))) > 1.5
					|| p.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
				p.sendMessage(ChatColor.RED
						+ "You moved before nearest Prey could be found!");
				g.stopLocator(p);
			}
		}
	}

	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.COMPASS
				&& g.getLocatorByPlayer(p) != -1
				&& g.getLocatorStage(g.getLocatorByPlayer(p)) != 2) {
			p.sendMessage(ChatColor.RED + "Prey Finder 9001 cancelled.");
			g.stopLocator(p);
		}
	}

	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		if (HuntedPlugin.getInstance().getWorld() != p.getWorld()) {
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (g.gameHasBegun() && g.isSpectating(p)) {
				e.setCancelled(true);
				return;
			}
		}
		if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) {
			if (g.isHunter(p) && p.getItemInHand().getType() == Material.COMPASS
					&& settings.preyFinder()
					&& g.huntHasBegun()) {
				if (g.getLocatorByPlayer(p) == -1) {
					if (g.HuntedAmount(true) == 0) {
						p.sendMessage(ChatColor.RED + "There are no Prey online!");
						return;
					}
					g.startLocator(p);
					p.sendMessage(ChatColor.GOLD
							+ "Prey Finder 9001 activated! Stand still for 8 seconds.");
					
				} else {
					String time = ((int) Math.floor((g.getLocatorTick(g
										.getLocatorByPlayer(p)) - g.getTick()) / 1200)) + ":";
					if ((int) (Math.floor((g.getLocatorTick(g
										.getLocatorByPlayer(p)) - g.getTick()) / 20) - (int) Math.floor((g
										.getLocatorTick(g.getLocatorByPlayer(p)) - g
										.getTick()) / 1200) * 60) < 10) {
						time += "0";
					}
					time += "" + (int) (Math.floor((g.getLocatorTick(g
							.getLocatorByPlayer(p)) - g.getTick()) / 20) - (int) Math.floor((g
							.getLocatorTick(g.getLocatorByPlayer(p)) - g
							.getTick()) / 1200) * 60);
					if (g.getLocatorStage(g.getLocatorByPlayer(e.getPlayer())) == 2) {
						p.sendMessage(ChatColor.RED
								+ "Prey Finder 9001 is still charging. Time left: " + time);
					}
					return;
				}
			}
		}
	}
	
	public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
		
		Player p = e.getPlayer();
		if (p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.onLogin(p);
		} else {
			g.onLogout(p);
		}
	}

	public void onBucketEmpty(PlayerBucketEmptyEvent e) {
		
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							settings.hunterSpawn()) <= settings.noBuildRange()) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							settings.preySpawn()) <= settings.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}
	/*
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
		
		Player p = e.getPlayer();
		if (p.getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		if (!g.gameHasBegun()) {
			return;
		}
		
		if (g.isHunter(p) || g.isHunted(p)) {
			if (e.getNewGameMode() != GameMode.SURVIVAL) {
				e.setCancelled(true);
				return;
			}
		}
		if (g.isSpectating(p)) {
			if (settings.flyingSpectators()) {
				if (e.getNewGameMode() != GameMode.CREATIVE) {
					e.setCancelled(true);
				}
			} else {
				if (e.getNewGameMode() != GameMode.SURVIVAL) {
					e.setCancelled(true);
				}
			}
		}
	}
	*/
	public void onBucketFill(PlayerBucketFillEvent e) {
		
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							settings.hunterSpawn()) <= settings.noBuildRange()) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
							settings.preySpawn()) <= settings.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onPlayerPickupItem(PlayerPickupItemEvent e) {
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}
	
}