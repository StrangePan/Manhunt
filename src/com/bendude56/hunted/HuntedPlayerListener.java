package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntedPlayerListener extends PlayerListener {
	HuntedPlugin plugin;
	Game game;
	
	public HuntedPlayerListener(HuntedPlugin instance) {
		plugin = instance;
		game = plugin.manhuntGame;
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this, Event.Priority.Normal, plugin);
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().getWorld() == plugin.getWorld()) {
			if (Bukkit.getServer().getWorlds().get(0) != plugin.getWorld() && game.gameStarted() && !plugin.allowSpectators) {
				event.getPlayer().teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
				return;
			} else if (game.gameStarted() && !plugin.allowSpectators) {
				event.getPlayer().kickPlayer("No spectating is allowed during this game!");
				return;
			}
			game.onLogin(event.getPlayer());
			if (plugin.spoutEnabled) {
				plugin.spoutConnect.showTime(1, 1, event.getPlayer());
			}
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (event.getPlayer().getWorld() == plugin.getWorld()) {
			game.onLogout(event.getPlayer());
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event.getPlayer().getWorld() == plugin.getWorld()) {
			game.onLogout(event.getPlayer());
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		if (game.gameStarted()) {
			Player player = event.getPlayer();
			if (game.isHunter(player.getName())
					&& !game.huntStarted()
					&& player.getWorld()==plugin.getWorld()) {
				if (game.getDistance(player.getLocation(), plugin.getWorld().getSpawnLocation()) > 32) {
					event.getPlayer().teleport(plugin.getWorld().getSpawnLocation());
					player.sendMessage(ChatColor.RED + "You have wandered too far! The hunt hasn't started yet!");
				}
			}
		}
	}
	
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		if (event.getPlayer().getWorld() == plugin.getWorld()) {
			if (!plugin.allowSpectators) {
				event.getPlayer().teleport(event.getFrom().getSpawnLocation());
				event.getPlayer().sendMessage(ChatColor.RED + "Spectating is not allowed in this manhunt!");
			}
			game.onLogin(event.getPlayer());
			if (plugin.spoutEnabled) {
				plugin.spoutConnect.showTime(1, 1, event.getPlayer());
			}
		}
	}
}
