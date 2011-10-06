package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Game {
	
	HuntedPlugin plugin;
	
	private HashMap<String, Long> timeout;
	
	private List<String> hunter;
	private List<String> hunted;
	private List<String> spectator;
	
	private boolean gameRunning;
	private long hunterReleaseTime; //When the hunters are to be released
	private boolean huntStarted; //TRUE = Preparation is over
	private long startTime;
	
	public Game(HuntedPlugin instance) {
		plugin = instance;
		hunter = new ArrayList<String>();
		hunted = new ArrayList<String>();
		spectator = new ArrayList<String>();
		timeout = new HashMap<String, Long>();
		gameRunning = false;
		huntStarted = false;
		hunterReleaseTime = 0;
		startTime = 0;
	}
	
	public void join(Player p) {
		if (!gameRunning) {
			hunter.add(p.getName());
		}
	}
	
	public void onDie(String dead, boolean natural) {
		
	}
	
	public void start() {
		if (!gameRunning) {
			plugin.getWorld().setTime(23000);
			this.broadcastPlayers(ChatColor.GREEN +
					"The Manhunt game has started! Prepare for the hunt!");
			this.broadcastHunters(ChatColor.YELLOW +
					"You are restricted toa 32 block radius around spawn. No cheating, now!");
			this.broadcastSpectator(ChatColor.GREEN +
					"The Manhunt game has started! Hunters and hunted are preparing for sundown!");
			hunterReleaseTime = plugin.getWorld().getFullTime() + 12000;
			startTime = plugin.getWorld().getFullTime();
			gameRunning = true;
			
			for (String n : hunter) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				if (p != null) {
					p.teleport(plugin.getWorld().getSpawnLocation());
				}
			}
			
		}
	}
	
	public void onTick() {
		if (gameRunning) {
			Long time = plugin.getWorld().getFullTime();
			if (time <= hunterReleaseTime) {
				huntStarted = false;
				if (time == startTime + 6000) {
					this.broadcastAll(ChatColor.GREEN +
							"It is high noon! Only 5 minutes left to prepare!");
				}
				if (time == startTime + 9600) {
					this.broadcastAll(ChatColor.YELLOW +
							"Sundown is approaching! The hunt begins in 2 minutes!");
				}
				if (time == startTime + 10800) {
					this.broadcastAll(ChatColor.YELLOW +
							"The time is nearing! Only 1 minute left till the hunt!");
				}
				if (time == startTime + 11400) {
					this.broadcastAll(ChatColor.YELLOW +
							"30 SECONDS!");
				}
			} else if (time >= hunterReleaseTime && huntStarted == false) {
				huntStarted = true;
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! Hunters, kill the hunted!");
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! The hunters are on their way!");
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! The hunters now on the move!");
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
			timeout.put(p.getName(), new Date().getTime() + 1200L);
			broadcastAll(ChatColor.RED + p.getName() + " has diconnected... If they don't reconnect in 1 minute, they will be booted from the game...");
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
	
	public void broadcastHunters(String msg) {
		for (String n : hunter) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
	
	public void broadcastHunted(String msg) {
		for (String n : hunter) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
	
	public void broadcastPlayers(String msg) {
		for (String n : hunter) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
		for (String n : hunter) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
	
	public void broadcastSpectator(String msg) {
		for (String n : spectator) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
		
	public boolean gameStarted() {
		return gameRunning;
	}
	
	public boolean huntStarted() {
		return huntStarted;
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
	
	public double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double distance = 0;
		distance = Math.sqrt( Math.pow(x2-x1,2) + Math.pow(y2-y1,2) + Math.pow(z2-z1,2) );
		return distance;
	}
	
	public double getDistance(Location loc1, Location loc2) {
		double distance = 0;
		distance = getDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
		return distance;
	}
	
	public double getDistance(Player p1, Player p2) {
		double distance = 0;
		distance= getDistance(p1.getLocation(), p2.getLocation());
		return distance;
	}
	
}
