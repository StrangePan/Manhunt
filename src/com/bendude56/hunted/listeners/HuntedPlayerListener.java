package com.bendude56.hunted.listeners;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.games.ManhuntGame;
import com.bendude56.hunted.settings.SettingsManager;
import com.bendude56.hunted.teams.TeamManager.Team;

public class HuntedPlayerListener implements Listener {
	
	HuntedPlugin plugin = HuntedPlugin.getInstance();
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e)
	{
		if (e.isCancelled() || !plugin.getSettings().CONTROL_CHAT.value)
		{
			return;
		}
		plugin.getChat().onPlayerchat(e);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
	}

	/*
	 * public void onPlayerKick(PlayerKickEvent e) { if
	 * (e.getPlayer().getWorld() == HuntedPlugin.getInstance().getWorld() &&
	 * g.gameHasBegun() && g.isPlaying(e.getPlayer())) {
	 * e.setLeaveMessage(null); } g.onLogout(e.getPlayer()); }
	 */

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		plugin.getGame().onPlayerLeave(e.getPlayer());
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
				&& settings.BOUNDARY_SETUP.value > -1) {
			if (settings.BOUNDARY_BOXED.value) {
				if (g.outsideBoxedArea(p.getLocation(), true)) {
					p.teleport(ManhuntUtil.safeTeleport(g.teleportPregameBoxedLocation(p
							.getLocation())));
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED
								+ "You've ventured too far!");
					return;
				}
			} else {
				if (ManhuntUtil.getDistance(settings.SPAWN_SETUP.value, p.getLocation()) > settings.BOUNDARY_SETUP.value) {
					ManhuntUtil.stepPlayer(p, 1.0, settings.SPAWN_SETUP.value);
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED + "You've ventured too far!");
					return;
				}
			}
		} else {
			if (settings.BOUNDARY_BOXED.value) {
				if (g.outsideBoxedArea(p.getLocation(), false)) {
					p.teleport(ManhuntUtil.safeTeleport(g.teleportBoxedLocation(p.getLocation())));
					if (Math.random() > 0.75)
						p.sendMessage(ChatColor.RED + "You've ventured too far!");
					return;
				}
			} else {
				if (ManhuntUtil.getDistance(
						g.getNearestLocation(p.getLocation(), settings.SPAWN_PREY.value, settings.SPAWN_HUNTER.value),
						p.getLocation()) > settings.BOUNDARY_WORLD.value) {
					ManhuntUtil.stepPlayer(
							p,
							1.0,
							g.getNearestLocation(p.getLocation(),
									settings.SPAWN_PREY.value,
									settings.SPAWN_HUNTER.value));
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
			if (ManhuntUtil.getDistance(p.getLocation(),
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
	public void onPlayerItemHeld(PlayerItemHeldEvent e)
	{
		plugin.getGame().finders.verifyFinder(e.getPlayer());
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
					&& settings.PREY_FINDER.value && g.huntHasBegun()) {
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
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		if (e.getFrom().getWorld() == plugin.getWorld() && e.getTo().getWorld() != plugin.getWorld())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else if (e.getFrom().getWorld() != plugin.getWorld() && e.getTo().getWorld() == plugin.getWorld())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
		
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e)
	{

		if (e.getFrom().getWorld() == plugin.getWorld() && e.getTo().getWorld() != plugin.getWorld())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else if (e.getFrom().getWorld() != plugin.getWorld() && e.getTo().getWorld() == plugin.getWorld())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {

		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (g.isHunted(e.getPlayer())
					&& ManhuntUtil.getDistance(e.getBlockClicked().getLocation(),
							settings.SPAWN_HUNTER.value) <= settings.SPAWN_PROTECTION.value) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& ManhuntUtil.getDistance(e.getBlockClicked().getLocation(),
							settings.SPAWN_PREY.value) <= settings.SPAWN_PROTECTION.value) {
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
					&& ManhuntUtil.getDistance(e.getBlockClicked().getLocation(),
							settings.SPAWN_HUNTER.value) <= settings.SPAWN_PROTECTION.value) {
				e.setCancelled(true);
			} else if (g.isHunter(e.getPlayer())
					&& ManhuntUtil.getDistance(e.getBlockClicked().getLocation(),
							settings.SPAWN_PREY.value) <= settings.SPAWN_PROTECTION.value) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (plugin.gameIsRunning() && plugin.getTeams().getTeamOf(e.getPlayer()) == Team.SPECTATORS)
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (plugin.gameIsRunning() && plugin.getTeams().getTeamOf(e.getPlayer()) == Team.SPECTATORS)
		{
			e.setCancelled(true);
		}
	}

}