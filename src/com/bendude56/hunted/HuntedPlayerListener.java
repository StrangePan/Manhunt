package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntedPlayerListener extends PlayerListener {
	
	public HuntedPlayerListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this, Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		Game g = Game.getActiveGame();
		Player p = e.getPlayer();
		if (!Game.isGameStarted() || !g.hasGameBegun()) {
			if (e.getPlayer().getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
				p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			}
		} else if (g.isHunter(p) || g.isHunted(p)) {
			g.onLogin(p);
		} else if (e.getPlayer().getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
			p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
		}
	}
	
	public void onPlayerKick(PlayerKickEvent e) {
		Game g = Game.getActiveGame();
		Player p = e.getPlayer();
		if (Game.isGameStarted() && (g.isSpectating(p) ||
				g.isHunted(p) || g.isHunter(p))) {
			g.onLogout(p);
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent e) {
		Game g = Game.getActiveGame();
		Player p = e.getPlayer();
		if (Game.isGameStarted() && (g.isSpectating(p) ||
				g.isHunted(p) || g.isHunter(p))) {
			g.onLogout(p);
		}
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		// RESERVED FOR FUTURE USE
	}
	
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		// RESERVED FOR FUTURE USE
	}
}
