package com.bendude56.hunted.listeners;

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

public class PlayerEventHandler implements Listener {
	
	private HuntedPlugin plugin;
	
	public PlayerEventHandler(HuntedPlugin plugin)
	{
		this.plugin = plugin;
	}
	
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
	 * (e.getPlayer().getWorld() == plugin.getWorld() &&
	 * plugin.gameIsRunning() && plugin.getGame().isPlaying(e.getPlayer())) {
	 * e.setLeaveMessage(null); } plugin.getGame().onLogout(e.getPlayer()); }
	 */

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		plugin.getGame().onPlayerLeave(e.getPlayer());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (!plugin.gameIsRunning())
		{
			return;
		}
		
		Player p = e.getPlayer();
		Team team = plugin.getTeams().getTeamOf(p);
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			return;
		}
		
		ManhuntUtil.checkPlayerInBounds(p);
		
		plugin.getGame().finders.verifyFinder(p);
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e)
	{
		plugin.getGame().finders.verifyFinder(e.getPlayer());
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (plugin.getWorld() != p.getWorld()) {
			return;
		}

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				|| e.getAction() == Action.RIGHT_CLICK_AIR
				|| e.getAction() == Action.LEFT_CLICK_BLOCK
				|| e.getAction() == Action.LEFT_CLICK_AIR) {

			if (plugin.gameIsRunning() && plugin.getGame().isSpectating(p)) {
				e.setCancelled(true);
				return;
			}

			if (plugin.getGame().isHunter(p)
					&& p.getItemInHand().getType() == Material.COMPASS
					&& plugin.getSettings().PREY_FINDER.value && plugin.gameIsRunning()) {
				if (plugin.getGame().getLocatorByPlayer(p) == -1) {
					if (plugin.getGame().HuntedAmount(true) == 0) {
						p.sendMessage(ChatColor.RED
								+ "There are no Prey online!");
						return;
					}
					plugin.getGame().startLocator(p);
					p.sendMessage(ChatColor.GOLD
							+ "Prey Finder 9001 activated! Stand still for 8 seconds.");

				} else {
					String time = ((int) Math.floor((plugin.getGame().getLocatorTick(g
							.getLocatorByPlayer(p)) - plugin.getGame().getTick()) / 1200))
							+ ":";
					if ((int) (Math.floor((plugin.getGame().getLocatorTick(g
							.getLocatorByPlayer(p)) - plugin.getGame().getTick()) / 20) - (int) Math
							.floor((plugin.getGame().getLocatorTick(plugin.getGame().getLocatorByPlayer(p)) - g
									.getTick()) / 1200) * 60) < 10) {
						time += "0";
					}
					time += ""
							+ (int) (Math
									.floor((plugin.getGame().getLocatorTick(g
											.getLocatorByPlayer(p)) - g
											.getTick()) / 20) - (int) Math
									.floor((plugin.getGame().getLocatorTick(g
											.getLocatorByPlayer(p)) - g
											.getTick()) / 1200) * 60);
					if (plugin.getGame().getLocatorStage(plugin.getGame().getLocatorByPlayer(e.getPlayer())) == 2) {
						p.sendMessage(ChatColor.RED
								+ "Prey Finder 9001 is still charginplugin.getGame(). Time left: "
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

	/*
	 * public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
	 * 
	 * Player p = e.getPlayer(); if (p.getWorld() !=
	 * plugin.getWorld()) { return; } if (!plugin.gameIsRunning())
	 * { return; }
	 * 
	 * if (plugin.getGame().isHunter(p) || plugin.getGame().isHunted(p)) { if (e.getNewGameMode() !=
	 * GameMode.SURVIVAL) { e.setCancelled(true); return; } } if
	 * (plugin.getGame().isSpectating(p)) { if (plugin.getSettings().flyingSpectators()) { if
	 * (e.getNewGameMode() != GameMode.CREATIVE) { e.setCancelled(true); } }
	 * else { if (e.getNewGameMode() != GameMode.SURVIVAL) {
	 * e.setCancelled(true); } } } }
	 */

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