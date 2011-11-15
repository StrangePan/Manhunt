package com.bendude56.hunted;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
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
		if (!g.gameHasBegun()) {
			if (!g.gameHasBegun()) {
				g.broadcastAll(ChatColor.WHITE + "<" + g.getColor(p) + p.getName() + ChatColor.WHITE + "> " + e.getMessage());
				e.setCancelled(true);
				HuntedPlugin.getInstance().log(Level.INFO, "<" + p.getName() + "> " + e.getMessage());
				return;
			}
		}
		if (settings.allTalk) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(ChatColor.WHITE + "<" + g.getColor(p) + p.getName() +
						ChatColor.WHITE + "> " + e.getMessage());
			}
			e.setCancelled(true);
			HuntedPlugin.getInstance().log(Level.INFO, "<" + p.getName() + "> " + e.getMessage());
			return;
		}
		if (g.isHunter(p)) {
			g.broadcastHunters(ChatColor.WHITE + "<" + ChatColor.RED + p.getName() +
					ChatColor.WHITE + "> " + e.getMessage());
			HuntedPlugin.getInstance().log(Level.INFO, "<" + p.getName() + "> " + e.getMessage());
			e.setCancelled(true);
		} else if (g.isHunted(p)) {
			g.broadcastHunted(ChatColor.WHITE + "<" + ChatColor.BLUE + p.getName() +
					ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
		} else if (g.isSpectating(p)) {
			g.broadcastSpectators(ChatColor.WHITE + "<" + ChatColor.YELLOW + p.getName() +
					ChatColor.WHITE + "> " + e.getMessage());
			e.setCancelled(true);
		} else for (Player player : Bukkit.getOnlinePlayers()) {
			if (!g.isHunted(player) && !g.isHunter(player) && !g.isSpectating(player)) {
				player.sendMessage(ChatColor.WHITE + "<" + g.getColor(p) + p.getName() +
						ChatColor.WHITE + "> " + e.getMessage());
			}
		}
		e.setCancelled(true);
		HuntedPlugin.getInstance().log(Level.INFO, "<" + p.getName() + "> " + e.getMessage());
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
		SettingsFile settings = HuntedPlugin.getInstance().settings;
		Player p = e.getPlayer();
		if (!g.gameHasBegun()) {
			return;
		}
		if (p.getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		if (!g.huntHasBegun()
				&& g.isHunter(p)
				&& g.getDistance(p.getLocation(),
						HuntedPlugin.getInstance().getWorld().getSpawnLocation())
						> HuntedPlugin.getInstance().settings.hunterBoundry) {
			g.stepPlayer(p, 1.0, HuntedPlugin.getInstance().getWorld().getSpawnLocation());
			p.sendMessage(ChatColor.RED + "You've ventured too far!");
			
		}
		if (g.getDistance(p.getLocation(), settings.preySpawn) > settings.globalBoundry
				&& g.getDistance(p.getLocation(), settings.hunterSpawn) > settings.globalBoundry) {
			if (g.isHunter(p) && !g.huntHasBegun()) {
				return;
			}
			if (g.getDistance(p.getLocation(), settings.preySpawn)
					< g.getDistance(p.getLocation(), settings.hunterSpawn)) {
				g.stepPlayer(p, 1.0, settings.preySpawn);
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			} else {

				g.stepPlayer(p, 1.0, settings.hunterSpawn);
				p.sendMessage(ChatColor.RED + "You've ventured too far!");
			}
			g.stepPlayer(p, 1.0, HuntedPlugin.getInstance().getWorld().getSpawnLocation());
			p.sendMessage(ChatColor.RED + "You've ventured too far!");
		}
		if (g.getLocatorByPlayer(p) != -1 && g.getLocatorStage(g.getLocatorByPlayer(p))!=2) { //PLAYER IS IN LOCATOR LIST
			if (g.getDistance(p.getLocation(), g.getLocatorLocation(g.getLocatorByPlayer(p))) > 1.5
					|| p.getPlayer().equals(HuntedPlugin.getInstance().getWorld())) {
				p.sendMessage(ChatColor.RED + "You moved before nearest prey could be found!");
				g.stopLocator(p);
			}
		}
	}
	
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (!p.getItemInHand().equals(Material.COMPASS)
				&& HuntedPlugin.getInstance().game.getLocatorByPlayer(p) != -1) {
			p.sendMessage(ChatColor.RED + "Prey locating cancelled.");
			HuntedPlugin.getInstance().game.stopLocator(p);
		}
	}
	
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Game g = HuntedPlugin.getInstance().game;
		if ((!g.huntHasBegun() || !g.isHunter(p) || !HuntedPlugin.getInstance().getWorld().equals(p.getWorld()))
				&& HuntedPlugin.getInstance().settings.preyFinder) {
			return;
		}
		if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				|| e.getAction().equals(Action.RIGHT_CLICK_AIR))) {
			if (g.getLocatorStage(g.getLocatorByPlayer(e.getPlayer()))==2) {
				p.sendMessage(ChatColor.RED + "Prey locator is still charging. Time left: " +
						(int) Math.floor((g.getLocatorTick(g.getLocatorByPlayer(p))-g.getTick())/1200)
						+ ":" +
						(int) (Math.floor((g.getLocatorTick(g.getLocatorByPlayer(p))-g.getTick())/20)-
								(int) Math.floor((g.getLocatorTick(g.getLocatorByPlayer(p))-g.getTick())/1200)*60)
						);
				return;
			}
			
			if (HuntedPlugin.getInstance().getWorld().equals(p.getWorld())) {
				if (p.getItemInHand().equals(Material.COMPASS)) {
					
					g.startLocator(p);
					p.sendMessage(ChatColor.GOLD + "Beginning search for nearby Prey... Stand still for 10 seconds.");
				}
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
	
	public void onBucketEmptyEvent(PlayerBucketEmptyEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
					HuntedPlugin.getInstance().settings.hunterSpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
			else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
					HuntedPlugin.getInstance().settings.preySpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onBucketFillEvent(PlayerBucketFillEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
					HuntedPlugin.getInstance().settings.hunterSpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
			else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlockClicked().getLocation(),
					HuntedPlugin.getInstance().settings.preySpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
		}
	}
}