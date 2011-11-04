package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HuntedPlayerListener extends PlayerListener {
	
	public HuntedPlayerListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this, Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onPlayerChat(PlayerChatEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		SettingsFile settings = HuntedPlugin.getInstance().settings;
		Player p = e.getPlayer();
		
		if (settings.allTalk) {
			return;
		}
		if (!g.gameHasBegun()) {
			return;
		}
		
		if (g.isHunter(p)) {
			g.broadcastHunters(ChatColor.RED + "[" + p.getName() + "] " + ChatColor.WHITE + e.getMessage());
			e.setCancelled(true);
		} else if (g.isHunted(p)) {
			g.broadcastHunted(ChatColor.BLUE + "[" + p.getName() + "] " + ChatColor.WHITE + e.getMessage());
			e.setCancelled(true);
		} else if (g.isSpectating(p)) {
			g.broadcastSpectators(ChatColor.YELLOW + "[" + p.getName() + "] " + ChatColor.WHITE + e.getMessage());
			e.setCancelled(true);
		} else {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (g.isHunted(player) && g.isHunter(player) && g.isSpectating(player)) {
					player.sendMessage(ChatColor.WHITE + "<" + ChatColor.YELLOW + p.getName() + ChatColor.WHITE + "] " + e.getMessage());
				}
			}
			e.setCancelled(true);
		}
	}
	
	public void onPlayerJoin(PlayerJoinEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		Player p = e.getPlayer();
		if (p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.onLogin(p);
		} else if (g.isHunted(p) || g.isHunter(p)) {
			p.sendMessage(ChatColor.RED + "To rejoin the manhunt game, type /manhunt join");
		}
	}
	
	public void onPlayerKick(PlayerKickEvent e) {
		HuntedPlugin.getInstance().game.onLogout(e.getPlayer());
	}
	
	public void onPlayerQuit(PlayerQuitEvent e) {
		HuntedPlugin.getInstance().game.onLogout(e.getPlayer());
	}
	
	public void onPlayerMove(PlayerMoveEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		Player p = e.getPlayer();
		//If Hunters venture too far during pre-game...
		if (g.gameHasBegun() && g.isHunter(p) && !g.huntHasBegun() && p.getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
			if (g.getDistance(p.getLocation(), HuntedPlugin.getInstance().getWorld().getSpawnLocation()) > HuntedPlugin.getInstance().settings.hunterBoundry) {
				g.stepPlayer(p, 1.0, HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			}
		}
		//If any player ventures too far...
		if (g.gameHasBegun() && (g.isHunter(p) || g.isHunted(p)) && p.getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
			if (g.getDistance(p.getLocation(), HuntedPlugin.getInstance().getWorld().getSpawnLocation()) > HuntedPlugin.getInstance().settings.globalBoundry) {
				g.stepPlayer(p, 1.0, HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			}
		}
	}
	
	public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		Player p = e.getPlayer();
		if (p.getWorld() == HuntedPlugin.getInstance().getWorld()) {
			g.onLogin(p);
		} else {
			g.onLogout(p);
		}
	}
}
