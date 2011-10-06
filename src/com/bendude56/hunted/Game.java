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
	private int dayLimit;
	private long endTime;
	
	public Game(HuntedPlugin instance) {
		plugin = instance;
		hunter = new ArrayList<String>();
		hunted = new ArrayList<String>();
		spectator = new ArrayList<String>();
		timeout = new HashMap<String, Long>();
		gameRunning = false;
		huntStarted = false;
		hunterReleaseTime = 0;
		dayLimit = 3;
		endTime = 0;
		
		//Schedule the onTick method!
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,
				new Runnable() {
				public void run() {
						plugin.manhuntGame.onTick();
				}
		}, 0, 1);
		
	}
	
	public void join(Player p) {
		if (!gameRunning && !playerExists(p.getName())) {
			this.makeHunter(p.getName());
		}
	}
	
	public void start() {
		if (!gameRunning) {
			plugin.getWorld().setTime(0);
			
			this.broadcastPlayers(ChatColor.GREEN +
					"The Manhunt game has been started! Start preparing for the hunt!");
			this.broadcastHunted(ChatColor.GREEN +
					"You have until sundown to prepare! Good luck!");
			this.broadcastHunters(ChatColor.YELLOW +
					"You are restricted to a 32 block radius around spawn. No cheating, now!");
			this.broadcastSpectator(ChatColor.GREEN +
					"The Manhunt game has started! Hunters and hunted are preparing for sundown!");
			
			hunterReleaseTime = plugin.getWorld().getFullTime() + 12000;
			endTime = plugin.getWorld().getFullTime() + dayLimit*24000 + 14000;
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
		
		for (String p : timeout.keySet()) {
			if (new Date().getTime() >= timeout.get(p)) {
				if (isHunter(p)) {
					this.broadcastAll(ChatColor.RED + p +
							ChatColor.YELLOW + " is no longer a " +
							ChatColor.RED + "hunter" + ChatColor.YELLOW + "!");
				} else if (isHunted(p)) {
					this.broadcastAll(ChatColor.BLUE + p +
							ChatColor.YELLOW + " is no longer one of the " +
							ChatColor.BLUE + "hunted" + ChatColor.YELLOW + "!");
				}
				this.removeUser(p);
			}
		}
		
		if (gameRunning) {
			Long time = plugin.getWorld().getFullTime();
			
			if (time <= hunterReleaseTime) { //Near-start alerts
				huntStarted = false;
				if (time == hunterReleaseTime - 6000) {
					this.broadcastAll(ChatColor.GREEN +
							"It is high noon! Only 5 minutes left to prepare!");
				} else if (time == hunterReleaseTime + 2400) {
					this.broadcastAll(ChatColor.YELLOW +
							"Sundown is approaching! The hunt begins in 2 minutes!");
				} else if (time == hunterReleaseTime - 1200) {
					this.broadcastAll(ChatColor.YELLOW +
							"The time is nearing! Only 1 minute left till the hunt!");
				} else if (time == hunterReleaseTime - 600) {
					this.broadcastAll(ChatColor.YELLOW +
							"30 SECONDS!");
				} else if (time == hunterReleaseTime - 100) {
					this.broadcastAll(ChatColor.YELLOW +
							"5");
				} else if (time == hunterReleaseTime - 100) {
					this.broadcastAll(ChatColor.YELLOW +
					"5");
				} else if (time == hunterReleaseTime - 80) {
					this.broadcastAll(ChatColor.YELLOW +
					"4");
				} else if (time == hunterReleaseTime - 60) {
					this.broadcastAll(ChatColor.YELLOW +
					"3");
				} else if (time == hunterReleaseTime - 40) {
					this.broadcastAll(ChatColor.YELLOW +
					"2");
				} else if (time == hunterReleaseTime - 20) {
					this.broadcastAll(ChatColor.YELLOW +
					"1");
				}
			
			} else if (time >= hunterReleaseTime && huntStarted == false) { //Begin the hunt
				huntStarted = true;
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! Hunters, kill the hunted!");
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! The hunters are on their way!");
				this.broadcastHunters(ChatColor.GREEN +
						"The hunt has begun! The hunters now on the move!");
				
			} else if (huntStarted == true && time < endTime) { //Near-end alerts
				if (time == endTime - 24000) {
					this.broadcastAll(ChatColor.YELLOW +
							"One day left in the manhunt!");
				} else if (time == endTime - 12000) {
					this.broadcastAll(ChatColor.YELLOW +
					"The manhunt will end at sundown!");
				} else if (time == endTime - 6000) {
					this.broadcastAll(ChatColor.GREEN +
					"5 minutes left in the manhunt!");
				} else if (time == endTime + 2400) {
					this.broadcastAll(ChatColor.YELLOW +
						"Sundown is approaching! The hunt ends in 2 minutes!");
				} else if (time == endTime - 1200) {
					this.broadcastAll(ChatColor.YELLOW +
						"The time is nearing! Only 1 minute left in the hunt!");
				} else if (time == endTime - 600) {
					this.broadcastAll(ChatColor.YELLOW +
						"30 SECONDS!");
				} else if (time == endTime - 100) {
					this.broadcastAll(ChatColor.YELLOW +
						"5");
				} else if (time == endTime - 100) {
					this.broadcastAll(ChatColor.YELLOW +
						"5");
				} else if (time == endTime - 80) {
					this.broadcastAll(ChatColor.YELLOW +
						"4");
				} else if (time == endTime - 60) {
					this.broadcastAll(ChatColor.YELLOW +
						"3");	
				} else if (time == endTime - 40) {
					this.broadcastAll(ChatColor.YELLOW +
						"2");
				} else if (time == endTime - 20) {
					this.broadcastAll(ChatColor.YELLOW +
						"1");
		}
				
			
			} else if (huntStarted == true && time >= endTime) { //End the hunt
				this.broadcastAll(ChatColor.GREEN +
						"Time's up! The manhunt is OVER!");
				endGameHunted();
			}
		}
	}
	
	public boolean onLogin(Player p) {
		if (timeout.containsKey(p.getName())) {
			timeout.remove(p.getName());
			if (isHunter(p.getName())) {
				broadcastAll(ChatColor.RED + p.getName() +
					ChatColor.GREEN + " has reconnected and is back in the game!");
			}
			if (isHunted(p.getName())) {
				broadcastAll(ChatColor.BLUE + p.getName() +
					ChatColor.GREEN + " has reconnected and is back in the game!");
			}
			return true;
		} else {
			if (isSpectator(p.getName())) {
				broadcastAll(ChatColor.YELLOW + p.getName() +
						ChatColor.WHITE + " is watching the game.");
			}
			return false;
		}
	}
	
	public void onLogout(Player p) {
		if ((hunter.contains(p.getName()) || hunted.contains(p.getName())) && plugin.offlineTimeout >= 0) {
			timeout.put(p.getName(), new Date().getTime() + plugin.offlineTimeout*60000L);
			if (plugin.offlineTimeout > 0) {
				broadcastAll(ChatColor.RED + p.getName() +
						" has diconnected. If they don't reconnect in " + plugin.offlineTimeout +
						" minute, they will be booted from the game.");
			}
		} else if (spectator.contains(p.getName())) {
			p.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
			spectator.remove(p.getName());
			broadcastAll(ChatColor.YELLOW + p.getName() +
					ChatColor.WHITE + " is no longer spectating.");
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
		if (Bukkit.getServer().getPlayerExact(name) == null) {
			return false;
		}
		Player player = Bukkit.getServer().getPlayerExact(name);
		name = player.getName();
		if (this.hunter.contains(name) && player.getWorld() == plugin.getWorld()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isHunted(String name) {
		if (Bukkit.getServer().getPlayerExact(name) == null) {
			return false;
		}
		Player player = Bukkit.getServer().getPlayerExact(name);
		name = player.getName();
		if (this.hunted.contains(name) && player.getWorld() == plugin.getWorld()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isSpectator(String name) {
		if (Bukkit.getServer().getPlayerExact(name) == null) {
			return false;
		}
		Player player = Bukkit.getServer().getPlayerExact(name);
		name = player.getName();
		if (this.spectator.contains(name) && player.getWorld() == plugin.getWorld()) {
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
			this.broadcastAll(ChatColor.RED + name +
					ChatColor.GREEN + " has joined the ranks of the " +
					ChatColor.RED + "hunters" + ChatColor.GREEN + "!");
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
			this.broadcastAll(ChatColor.BLUE + name +
					ChatColor.GREEN + " has become one of the " +
					ChatColor.BLUE + "hunted" + ChatColor.GREEN + "!");
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
			this.broadcastAll(ChatColor.YELLOW + name +
					ChatColor.GREEN + " is now a " +
					ChatColor.YELLOW + "spectator" + ChatColor.GREEN + "!");
		}
	}
	
	public void removeUser(String name) {
		hunter.remove(name);
		hunted.remove(name);
		spectator.remove(name);
	}
	public boolean playerExists(String name) {
		if (hunter.contains(name)
				|| hunted.contains(name)
				|| spectator.contains(name)) {
			return true;
		} else {
			return false;
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
	
	public void endGame() {
		gameRunning = false;
		huntStarted = false;
		hunterReleaseTime = 0;
		endTime = 0;
		this.broadcastAll(ChatColor.GREEN +
				"The game has been ended.");
	}
	public void endGameHunted() {
		gameRunning = false;
		huntStarted = false;
		hunterReleaseTime = 0;
		endTime = 0;
		this.broadcastHunters(ChatColor.RED+
				"You have lost the manhunt. The " +
				ChatColor.BLUE + "hunted" +
				ChatColor.RED + "have escaped your fury. =(");
		this.broadcastHunted(ChatColor.GREEN +
				"You have won the manhunt! Congratulations!");
		this.broadcastSpectator(ChatColor.GREEN +
				"The manhunt is over! The " +
				ChatColor.BLUE + "hunted" +
				ChatColor.GREEN + " have defeated the " +
				ChatColor.RED + "hunters!");
	}
	public void endGameHunters() {
		gameRunning = false;
		huntStarted = false;
		hunterReleaseTime = 0;
		endTime = 0;
		this.broadcastHunted(ChatColor.RED+
				"You have lost the manhunt. The " +
				ChatColor.RED + "hunters" +
				ChatColor.RED + "have killed you all. =(");
		this.broadcastHunted(ChatColor.GREEN +
				"You have won the manhunt! Congratulations!");
		this.broadcastSpectator(ChatColor.GREEN +
				"The manhunt is over! The " +
				ChatColor.RED + "hunters" +
				ChatColor.GREEN + " have defeated the " +
				ChatColor.BLUE + "hunted!");
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
