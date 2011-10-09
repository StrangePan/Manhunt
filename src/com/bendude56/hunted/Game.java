package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class Game {
	
	private static Game ag = null;
	
	public static Game newActiveGame(DataFile f) {
		return ag = new Game(f);
	}
	
	public static boolean isGameStarted() {
		return ag != null;
	}
	
	public static void deleteActiveGame() {
		ag = null;
	}
	
	public static Game getActiveGame() {
		return ag;
	}
	
	private HashMap<String, Long> timeout;
	
	private List<String> hunter;
	private List<String> hunted;
	private List<String> spectator;
	
	private boolean gameRunning;
	private long hunterReleaseTick;
	private long endTick;
	
	private int tickSched;
	
	private DataFile dat;
	
	private Game(DataFile f) {
		hunter = new ArrayList<String>();
		hunted = new ArrayList<String>();
		spectator = new ArrayList<String>();
		timeout = new HashMap<String, Long>();
		gameRunning = false;
		hunterReleaseTick = 0;
		endTick = 0;
		dat = f;
	}
	
	public DataFile getDataFile() {
		return dat;
	}
	
	public void start() {
		if (!gameRunning) {
			HuntedPlugin.getInstance().getWorld().setTime(0);
			
			this.broadcastAll(ChatColor.GREEN +
					"The game has begun! The hunters will be released at sundown!");
			this.broadcastHunted(ChatColor.GREEN +
					"You may now begin your preparations... Good luck!");
			
			hunterReleaseTick = HuntedPlugin.getInstance().manhuntWorld.getFullTime();
			// endTick = HuntedPlugin.getInstance().getWorld().getFullTime() + dat.maxDays * 24000 + 14000;
			gameRunning = true;
			
			for (String n : hunted) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				if (p != null) {
					p.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				}
			}
			for (String n : spectator) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				p.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
					if (isHunter(p2) || isHunted(p2)) {
						((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(new Packet29DestroyEntity(p.getEntityId()));
					}
				}
			}
			tickSched = Bukkit.getScheduler().scheduleSyncRepeatingTask(HuntedPlugin.getInstance(),
					new Runnable() {
						public void run() {
								onTick();
						}
				}, 0, 1);
		}
	}
	
	public void stop() {
		if (gameRunning) {
			Bukkit.getScheduler().cancelTask(tickSched);
		}
	}
	
	public void onDie(Player p) {
		p.sendMessage(ChatColor.RED + "You have died! You are now spectating the match!");
		onDie(p.getName());
	}
	
	private void onDie(String s) {
		broadcastAll(ChatColor.BLUE + s + ChatColor.WHITE + " has died!");
		if (hunter.contains(s.toLowerCase())) {
			hunter.remove(s.toLowerCase());
			if (hunter.size() == 0) {
				broadcastAll(ChatColor.GREEN + "All hunters are now dead! Hunted win!");
				stop();
			} else {
				spectator.add(s.toLowerCase());
			}
		} else if (hunted.contains(s.toLowerCase())) {
			hunted.remove(s.toLowerCase());
			if (hunted.size() == 0) {
				broadcastAll(ChatColor.GREEN + "All hunted are now dead! Hunters win!");
				stop();
			} else {
				spectator.add(s.toLowerCase());
			}
		}
	}
	
	private void remGroups(String s) {
		if (hunter.contains(s))
			hunter.remove(s);
		else if (hunted.contains(s))
			hunted.remove(s);
		else if (spectator.contains(s))
			spectator.remove(s);
	}
	
	private void timeout(String p) {
		if (hunter.contains(p)) {
			hunter.remove(p);
		} else if (hunted.contains(p)) {
			hunted.remove(p);
		}
		if (timeout.containsKey(p)) {
			timeout.remove(p);
		}
		onDie(p);
	}
	
	private void onTick() {
		List<String> toRem = new ArrayList<String>();
		for (String p : timeout.keySet()) {
			if (new Date().getTime() >= timeout.get(p)) {
				toRem.add(p);
			}
		}
		
		for (String p : toRem) {
			timeout(p);
		}
		
		if (gameRunning) {
			long tick = HuntedPlugin.getInstance().getWorld().getFullTime();
			
			for (String n : spectator) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
					if (isHunter(p2) || isHunted(p2)) {
						((CraftPlayer) p2).getHandle().netServerHandler.sendPacket(new Packet29DestroyEntity(p.getEntityId()));
					}
				}
			}
			
			if (hunterReleaseTick != -1 && tick < hunterReleaseTick) {
				int min;
				int sec;
				long totalsec = (hunterReleaseTick - tick) / 20;
				min = (int) (totalsec / 60);
				sec = (int) (totalsec % 60);
				if (HuntedPlugin.getInstance().spoutEnabled) {
					for (String s : hunter) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until hunters are released", min, sec, p);
						}
					}
					for (String s : hunted) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until you are released", min, sec, p);
						}
					}
					for (String s : spectator) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until hunters are released", min, sec, p);
						}
					}
				}
				if (tick % 20 == 0) {
					if (min == 1 && sec == 0) {
						broadcastAll(ChatColor.RED + "1 minute until the hunters are released!");
					} else if (sec == 0) {
						broadcastAll(ChatColor.RED + String.valueOf(min) + " minutes until the hunters are released!");
					} else if (min == 0 && sec == 30) {
						broadcastAll(ChatColor.RED + "30 seconds until the hunters are released!");
					} else if (min == 0 && sec == 10) {
						broadcastAll(ChatColor.RED + "10 seconds until the hunters are released!");
					} else if (min == 0 && sec == 5) {
						broadcastAll(ChatColor.RED + "The hunters will be released in 5...");
					} else if (min == 0 && sec < 5) {
						broadcastAll(ChatColor.RED + String.valueOf(sec) + "...");
					}
				}
			} else if (tick >= hunterReleaseTick) {
				hunterReleaseTick = 0;
				for (String s : hunter) {
					Player p = Bukkit.getPlayerExact(s);
					if (p != null) {
						p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
					}
				}
				broadcastAll(ChatColor.RED + "The hunters have been released! The game will last " + dat.maxDays + " days.");
				broadcastHunted(ChatColor.RED + "You are now being actively hunted... Watch your step!");
				endTick = tick + dat.maxDays * 24000;
			} else if (tick >= endTick) {
				broadcastAll(ChatColor.GREEN + "Time has run out and the hunted have won!");
				stop();
			} else {
				int min;
				int sec;
				long totalsec = (endTick - tick) / 20;
				min = (int) (totalsec / 60);
				sec = (int) (totalsec % 60);
				if (HuntedPlugin.getInstance().spoutEnabled) {
					for (String s : hunter) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", min, sec, p);
						}
					}
					for (String s : hunted) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", min, sec, p);
						}
					}
					for (String s : spectator) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", min, sec, p);
						}
					}
				}
				if (tick % 20 == 0) {
					if (min == 1 && sec == 0) {
						broadcastAll(ChatColor.RED + "1 minute until the game is over!");
					} else if (sec == 0 && (min % 5 == 0 || min < 5)) {
						broadcastAll(ChatColor.RED + String.valueOf(min) + " minutes until the game is over!");
					} else if (min == 0 && sec == 30) {
						broadcastAll(ChatColor.RED + "30 seconds until the game is over!");
					} else if (min == 0 && sec == 10) {
						broadcastAll(ChatColor.RED + "10 seconds until the game is over!");
					} else if (min == 0 && sec == 5) {
						broadcastAll(ChatColor.RED + "The game is over in 5...");
					} else if (min == 0 && sec < 5) {
						broadcastAll(ChatColor.RED + String.valueOf(sec) + "...");
					}
				}
			}
		}
	}
	
	public void onLogin(Player p) {
		if (gameRunning && (isHunter(p) || isHunted(p))) {
			broadcastAll(ChatColor.BLUE + p.getName() +
					ChatColor.WHITE + " has reconnected and is back in the game!");
			timeout.remove(p);
		}
	}
	
	public void onLogout(Player p) {
		if (isHunter(p) || isHunted(p)) {
			if (hasGameBegun()) {
				broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
						" has disconnected...");
				broadcastAll(ChatColor.WHITE + "If they don't reconnect in " +
						(dat.disTimeout / 1000) + " seconds, they will be considered dead!");
			} else {
				remGroups(p.getName().toLowerCase());
				broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
						" has disconnected...");
				broadcastAll(ChatColor.WHITE + "They must rejoin when they connect.");
			}
		} else if (isSpectating(p)) {
			p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			remGroups(p.getName().toLowerCase());
			broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
					" is no longer spectating!");
		}
	}
	
	public void broadcastAll(String msg) {
		broadcastHunters(msg);
		broadcastHunted(msg);
		broadcastSpectators(msg);
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
	
	public void broadcastSpectators(String msg) {
		for (String n : spectator) {
			Player p = Bukkit.getServer().getPlayerExact(n);
			if (p != null) {
				p.sendMessage(msg);
			}
		}
	}
		
	public boolean hasGameBegun() {
		return gameRunning;
	}
	
	public boolean isHunter(Player p) {
		return (this.hunter.contains(p.getName().toLowerCase()));
	}
	
	public boolean isHunted(Player p) {
		return (this.hunted.contains(p.getName().toLowerCase()));
	}
	
	public boolean isSpectating(Player p) {
		return (this.spectator.contains(p.getName().toLowerCase()));
	}
	
	public void addHunter(Player p) {
		if (hasGameBegun())
			throw new UnsupportedOperationException("Cannot add hunters/hunted once game has begun!");
		hunter.add(p.getName().toLowerCase());
		broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
				" has joined as a hunter...");
	}
	
	public void addHunted(Player p) {
		if (hasGameBegun())
			throw new UnsupportedOperationException("Cannot add hunters/hunted once game has begun!");
		hunted.add(p.getName().toLowerCase());
		broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
				" has joined as a hunted player...");
	}
	
	public void addSpectator(Player p) {
		spectator.add(p.getName().toLowerCase());
		broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
				" is now spectating the game...");
		p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
	}
	
	public void quit(Player p) {
		if (isHunter(p) || isHunted(p)) {
			onDie(p);
			broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
					" has ragequit!");
		} else if (isSpectating(p)) {
			p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			remGroups(p.getName().toLowerCase());
			broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE +
					" is no longer spectating!");
		}
	}
	
	/*public void makeHunter(String name) {
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
					ChatColor.WHITE + " is now a watching the game.");
		}
	}
	
	public void makeAllPlayersSpectators() {
		for (String p : hunter) {
			makeSpectator(p);
		}
		for (String p : hunted) {
			makeSpectator(p);
		}
	}
	
	public void removeAll() {
		hunted.clear();
		hunter.clear();
		spectator.clear();
	}
	public void removeAllHunters() {
		hunter.clear();
	}
	public void removeAllHunted() {
		hunted.clear();
	}
	public void removeAllSpectators() {
		spectator.clear();
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
	
	public void kickSpectators() {
		Player player;
		if (Bukkit.getServer().getWorlds().get(0) == plugin.getWorld()) {
			for (String p : spectator) {
				player = Bukkit.getPlayerExact(p);
				player.kickPlayer("Spectating is not allowed during this game!");
			}
		} else {
			for (String p : spectator) {
				player = Bukkit.getPlayerExact(p);
				player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
			}
		}
		removeAllSpectators();
	}
	public boolean kickSpectators(String worldName) {
		Player player;
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			return false;
		} else {
			for (String p : spectator) {
				player = Bukkit.getPlayerExact(p);
				if (player != null) {
					player.teleport(world.getSpawnLocation());					
				}
			}
			removeAllSpectators();
			return true;
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
		return Math.sqrt( Math.pow(x2-x1,2) + Math.pow(y2-y1,2) + Math.pow(z2-z1,2) );
	}
	public double getDistance(Location loc1, Location loc2) {
		return getDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
	}
	public double getDistance(Player p1, Player p2) {
		return getDistance(p1.getLocation(), p2.getLocation());
	}*/
	
}
