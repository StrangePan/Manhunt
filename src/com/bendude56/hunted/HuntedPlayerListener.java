package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntedPlayerListener extends PlayerListener {
	HuntedPlugin plugin;
	
	public HuntedPlayerListener(HuntedPlugin instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal, plugin);
		plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this, Event.Priority.Normal, plugin);
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (Game.isGameInProgress()) {
			if (!Game.getInstance().onLogin(event.getPlayer()) && event.getPlayer().getWorld().getName().equals("manhunt")) {
				event.getPlayer().teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
			}
		}
		if (plugin.spoutEnabled) {
			plugin.spoutConnect.showTime(1, 1, event.getPlayer());
		}
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (Game.isGameInProgress()) {
			Game.getInstance().onLogout(event.getPlayer());
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Game.isGameInProgress()) {
			Game.getInstance().onLogout(event.getPlayer());
		}
	}
}
