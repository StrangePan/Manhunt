package com.bendude56.hunted;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (game.gameStarted()) {
			if (!game.onLogin(event.getPlayer()) && event.getPlayer().getWorld() == plugin.getWorld()) {
				event.getPlayer().teleport(plugin.getWorld().getSpawnLocation());
			}
		}
		if (plugin.spoutEnabled) {
			plugin.spoutConnect.showTime(1, 1, event.getPlayer());
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (game.gameStarted()) {
			game.onLogout(event.getPlayer());
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (game.gameStarted()) {
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
}
