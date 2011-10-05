package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Game {
	private HashMap<String, Long> timeout;
	
	private List<String> hunter;
	private List<String> hunted;
	private List<String> spectator;
	
	private boolean playing;
	private long hunterRelease;
	
	private Game() {
		hunter = new ArrayList<String>();
		hunted = new ArrayList<String>();
		spectator = new ArrayList<String>();
		timeout = new HashMap<String, Long>();
		playing = false;
		hunterRelease = 0;
	}
	
	public void join(Player p) {
		if (!playing) {
			hunter.add(p.getName());
		}
	}
	
	public void onDie(String dead, boolean natural) {
		
	}
	
	public void start() {
		hunterRelease = new Date().getTime() + 120000L;
		playing = true;
	}
	
	public void onTick() {
		if (playing) {
			if (new Date().getTime() <= hunterRelease) {
				
			}
		}
	}
	
	public boolean onLogin(Player p) {
		if (timeout.containsKey(p.getName())) {
			timeout.remove(p.getName());
			broadcastAll(ChatColor.GREEN + p.getName() + " has reconnected and is back in the game!");
			return true;
		} else {
			return false;
		}
	}
	
	public void onLogout(Player p) {
		if (hunter.contains(p.getName()) || hunted.contains(p.getName())) {
			timeout.put(p.getName(), new Date().getTime() + 20000L);
			broadcastAll(ChatColor.RED + p.getName() + " has diconnected... If they don't reconnect in 20 seconds, they will be booted from the game...");
		} else if (spectator.contains(p.getName())) {
			p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
			spectator.remove(p.getName());
			broadcastAll(ChatColor.RED + p.getName() + " is no longer spectating...");
		}
	}
	
	public void broadcastAll(String msg) {
		for (String n : hunter) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
		for (String n : hunted) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
		for (String n : spectator) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
	
	public void end() {
		Game.instance = null;
	}
	
	public boolean hasBegun() {
		return playing;
	}
	
	public boolean isHunter(String name) {
		if (this.hunter.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isHunted(String name) {
		if (this.hunted.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSpectator(String name) {
		if (this.spectator.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void makeHunter(String name) {
		if (name == null) {
			return;
		}
		this.hunted.remove(name);
		this.spectator.remove(name);
		if (!this.hunter.contains(name)) {
			this.hunter.add(name);
		}
	}
	
	public void makeHunted(String name) {
		if (name == null) {
			return;
		}
		this.hunter.remove(name);
		this.spectator.remove(name);
		if (!this.hunted.contains(name)) {
			this.hunted.add(name);
		}
	}
	
	public void makeSpectator(String name) {
		if (name == null) {
			return;
		}
		this.hunted.remove(name);
		this.hunter.remove(name);
		if (!this.spectator.contains(name)) {
			this.spectator.add(name);
		}
	}
	
	public int HuntersAmount() {
		int num = hunter.size();
		return num;
	}
	
	public int HuntedAmount() {
		int num = hunted.size();
		return num;
	}
	
	public int SpectatorsAmount() {
		int num = spectator.size();
		return num;
	}
	
	// Static methods
	
	private static Game instance = null;
	
	public static boolean isGameInProgress() {
		return instance != null;
	}
	
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}
}
