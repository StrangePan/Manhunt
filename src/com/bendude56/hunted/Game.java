package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.Packet29DestroyEntity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class Game {
	private SettingsFile settings;
	
	private HashMap<String, Long> timeout;
	
	private List<String> hunter;
	private List<String> hunted;
	private List<String> spectator;
	
	private boolean gameRunning;
	private long hunterReleaseTick;
	private long endTick;
	
	private int tickSched;
	
	public Game() {
		hunter = new ArrayList<String>();
		hunted = new ArrayList<String>();
		spectator = new ArrayList<String>();
		timeout = new HashMap<String, Long>();
		gameRunning = false;
		hunterReleaseTick = 0;
		endTick = 0;
		settings = HuntedPlugin.getInstance().settings;
	}
	
	public void start() {
		if (!gameRunning) {
			HuntedPlugin.getInstance().getWorld().setTime(0);
			
			this.broadcastSpectators(ChatColor.YELLOW +
					"The game has started! The hunt begins at sundown!");
			this.broadcastHunters(ChatColor.YELLOW +
					"The game has started! The " + ChatColor.BLUE + "Hunted" + ChatColor.YELLOW + " are preparing for nightfall.");
			this.broadcastHunted(ChatColor.YELLOW +
					"The game has started! The " + ChatColor.RED + "Hunters" + ChatColor.YELLOW + " will be released at sundown!");
			
			hunterReleaseTick = HuntedPlugin.getInstance().manhuntWorld.getFullTime() + 12000;
			endTick = hunterReleaseTick + settings.dayLimit * 24000;
			gameRunning = true;
			
			for (String n : hunted) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				p.setHealth(20);
				p.setFoodLevel(20);
				if (p != null) {
					p.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				}
			}
			for (String n : hunter) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				p.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
				p.setHealth(20);
				p.setFoodLevel(20);
			}
			
			for (String n : spectator) {
				Player p = Bukkit.getServer().getPlayerExact(n);
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
		gameRunning = false;
		for (String s : spectator) {
			hunter.add(s);
		}
		spectator.clear();
	}
	
	public void onDie(Player p) {
		onDie(p.getName());
	}
	
	public void onDie(String s) {
		if (hunter.contains(s.toLowerCase())) {
			hunter.remove(s.toLowerCase());
			Bukkit.getPlayerExact(s).teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
			if (hunter.size() == 0) {
				broadcastAll(ChatColor.GREEN + "All " + ChatColor.RED + "hunters" + ChatColor.GREEN + " are now dead! The " + ChatColor.BLUE + "Hunted" + ChatColor.GREEN + " win!");
				stop();
			} else {
				spectator.add(s.toLowerCase());
				Bukkit.getPlayerExact(s).sendMessage(ChatColor.GRAY + "You are now a " + ChatColor.YELLOW + "spectator.");
			}
		} else if (hunted.contains(s.toLowerCase())) {
			hunted.remove(s.toLowerCase());
			if (hunted.size() == 0) {
				broadcastAll(ChatColor.GREEN + "All of the " + ChatColor.BLUE + "Hunted" + ChatColor.GREEN + " are now dead! The " + ChatColor.RED + "Hunters" + ChatColor.GREEN +" win!");
				stop();
			} else {
				spectator.add(s.toLowerCase());
				Bukkit.getPlayerExact(s).sendMessage(ChatColor.GRAY + "You are now a " + ChatColor.YELLOW + "spectator.");
			}
		}
	}
	
	private void deleteUser(String s) {
		if (hunter.contains(s))
			hunter.remove(s);
		else if (hunted.contains(s))
			hunted.remove(s);
		else if (spectator.contains(s))
			spectator.remove(s);
		if (timeout.containsKey(s)) {
			timeout.remove(s);
		}
	}
	
	private void timeout(String p) {
		if (hunter.contains(p)) {
			hunter.remove(p);
			broadcastAll(getColor(p) + p + ChatColor.WHITE + " was disqualified for not being online/in the manhunt world!");
			if (Bukkit.getPlayerExact(p) != null) {
				Bukkit.getPlayerExact(p).sendMessage(ChatColor.RED + "You were disqualified from the manhunt game!");
			}
		} else if (hunted.contains(p)) {
			hunted.remove(p);
			broadcastAll(getColor(p) + p + ChatColor.WHITE + " was disqualified for not being online/in the manhunt world!");
			Bukkit.getPlayerExact(p).sendMessage(ChatColor.RED + "You were disqualified from the manhunt game!");
		}
		if (timeout.containsKey(p)) {
			timeout.remove(p);
		}
		onDie(p);
	}
	
	private void onTick() {
		for (String p : timeout.keySet()) {
			if (new Date().getTime() >= timeout.get(p)) {
				timeout(p);
			}
		}
		
		if (gameRunning) {
			long tick = HuntedPlugin.getInstance().getWorld().getFullTime();
			
			for (String n : spectator) {
				Player p = Bukkit.getServer().getPlayerExact(n);
				for (Player p2 : Bukkit.getServer().getOnlinePlayers()) {
					if (isHunter(p2) || isHunted(p2)) { //Makes spectators invisible!
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
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until sundown", min, sec, p);
						}
					}
					for (String s : hunted) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until sundown", min, sec, p);
						}
					}
					for (String s : spectator) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until sundown", min, sec, p);
						}
					}
				}
				if (tick % 20 == 0) {
					if (min == 5 && sec == 0) {
						broadcastAll(ChatColor.GOLD + "5 minutes until sundown!");
					} else if (min == 1 && sec == 0) {
						broadcastAll(ChatColor.GOLD + "1 minute until sundown!");
					} else if (min == 0 && sec == 30) {
						broadcastAll(ChatColor.GOLD + "30 seconds until sundown!");
					} else if (min == 0 && sec == 10) {
						broadcastAll(ChatColor.GOLD + "10 seconds until sundown!");
					} else if (min == 0 && sec <= 5 && sec != 0) {
						broadcastAll(ChatColor.GOLD + String.valueOf(sec) + "...");
					}
				}
			} else if (tick >= hunterReleaseTick && hunterReleaseTick != 0) {
				hunterReleaseTick = 0;
				for (String s : hunter) {
					Player p = Bukkit.getPlayerExact(s);
					if (p != null) {
						p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
					}
				}
				broadcastSpectators(ChatColor.RED + "The hunters have been released! They have " + settings.dayLimit + " days to slay the hunted.");
				broadcastHunters(ChatColor.RED + "You have been released! Go get 'em!");
				broadcastHunted(ChatColor.RED + "The hunters are on the move! Good luck!");
				endTick = tick + settings.dayLimit* 24000;
			} else if (tick >= endTick) {
				broadcastAll(ChatColor.GREEN + "Time has run out! The hunted have won the game!");
				stop();
			} else {
				int hour;
				int min;
				int sec;
				long totalsec = (endTick - tick) / 20;
				hour = (int) (totalsec / 3600);
				min = (int) (totalsec / 60);
				if (min > 60 && hour > 0) {
					min = min-((int) Math.floor(min/60))*60;
				}
				sec = (int) (totalsec % 60);
				if (HuntedPlugin.getInstance().spoutEnabled) {
					for (String s : hunter) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", hour, min, sec, p);
						}
					}
					for (String s : hunted) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", hour, min, sec, p);
						}
					}
					for (String s : spectator) {
						Player p = Bukkit.getPlayerExact(s);
						if (p != null) {
							HuntedPlugin.getInstance().spoutConnect.showTime("Time until the game is over", hour, min, sec, p);
						}
					}
				}
				if (tick % 20 == 0) {
					if (min == 20 && sec == 0) {
						broadcastAll(ChatColor.GOLD + "1 day until the game is over!");
					} else if (min == 5 && sec == 0) {
						broadcastAll(ChatColor.GOLD + "5 minutes until the game is over!");
					} else if (min == 1 && sec == 0) {
						broadcastAll(ChatColor.GOLD + "1 minute until the game is over!");
					} else if (sec == 0 && min < 5 && min != 0) {
						broadcastAll(ChatColor.GOLD + String.valueOf(min) + " minutes until the game is over!");
					} else if (min == 0 && sec == 30) {
						broadcastAll(ChatColor.GOLD + "30 seconds until the game is over!");
					} else if (min == 0 && sec == 10) {
						broadcastAll(ChatColor.GOLD + "10 seconds until the game is over!");
					} else if (min == 0 && sec == 5) {
						broadcastAll(ChatColor.GOLD + "The game is over in 5...");
					} else if (min == 0 && sec < 5 && sec != 0) {
						broadcastAll(ChatColor.GOLD + String.valueOf(sec) + "...");
					}
				}
			}
		}
	}
	
	public void onLogin(Player p) {
		if (gameHasBegun() && (isHunter(p) || isHunted(p))) {
			broadcastAll(getColor(p) + p.getName() +
					ChatColor.WHITE + " has reconnected and is back in the game!");
			timeout.remove(p);
		} else if (gameHasBegun()) {
			addSpectator(p);
		} else if (!gameHasBegun()) {
			addHunter(p);
		}
	}
	
	public void onLogout(Player p) {
		if (isHunter(p) || isHunted(p)) {
			if (gameHasBegun() && settings.offlineTimeout >= 0) {
				broadcastAll(getColor(p) + p.getName() + ChatColor.WHITE +
						" has disconnected.");
				if (settings.offlineTimeout > 0) {
					broadcastAll(ChatColor.WHITE + "If they don't reconnect in " + ChatColor.RED +
							(settings.offlineTimeout) + " minutes" + ChatColor.WHITE + ", they will be disqualified!");
					timeout.put(p.getName(), new Date().getTime() + settings.offlineTimeout * 1200);
				} else if (settings.offlineTimeout == 0) {
					broadcastAll(ChatColor.WHITE + "They are now disqualified from the game.");
					p.sendMessage(ChatColor.RED + "You were disqualified from the manhunt game!");
					onDie(p.getName());
				}
			}
		} else if (isSpectating(p)) {
			deleteUser(p.getName().toLowerCase());
			broadcastAll(ChatColor.YELLOW + p.getName() + ChatColor.WHITE +
					" is no longer spectating.");
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
		
	public boolean gameHasBegun() {
		return gameRunning;
	}
	
	public boolean huntHasBegun() {
		if (gameRunning && hunterReleaseTick == 0) { 
			return true;
		} else return false;
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
		addHunter(p.getName());
	}
	
	public void addHunted(Player p) {
		addHunted(p.getName());
	}
	
	public void addSpectator(Player p) {
		addSpectator(p.getName());
	}
	
	public void addHunter(String p) {
		hunter.add(p.toLowerCase());
		if (hunted.contains(p.toLowerCase())) {
			hunted.remove(p.toLowerCase());
		}
		if (spectator.contains(p.toLowerCase())) {
			spectator.remove(p.toLowerCase());
		}
	}
	
	public void addHunted(String p) {
		hunted.add(p.toLowerCase());
		if (hunter.contains(p.toLowerCase())) {
			hunter.remove(p.toLowerCase());
		}
		if (spectator.contains(p.toLowerCase())) {
			spectator.remove(p.toLowerCase());
		}
	}
	
	public void addSpectator(String p) {
		spectator.add(p.toLowerCase());
		if (hunter.contains(p.toLowerCase())) {
			hunter.remove(p.toLowerCase());
		}
		if (hunted.contains(p.toLowerCase())) {
			hunted.remove(p.toLowerCase());
		}
	}
	
	public void quit(Player p) {
		if (isHunter(p) || isHunted(p)) {
			onDie(p);
			broadcastAll(getColor(p) + p.getName() + ChatColor.WHITE +
					" has quit the game and is now spectating.");
		}
	}
	
	public ChatColor getColor(Player p) {
		if (p == null) {
			return ChatColor.YELLOW;
		} else if (isHunter(p)) {
			return ChatColor.RED;
		} else if (isHunted(p)) {
			return ChatColor.BLUE;
		} else return ChatColor.YELLOW;
	}
	
	public ChatColor getColor(String p) {
		return getColor(Bukkit.getPlayerExact(p));
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
	}*/
	
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
		return Math.sqrt( Math.pow(x2-x1,2) + Math.pow(y2-y1,2) + Math.pow(z2-z1,2) );
	}
	
	public double getDistance(Location loc1, Location loc2) {
		return getDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
	}
	
	public double getDistance(Player p1, Player p2) {
		return getDistance(p1.getLocation(), p2.getLocation());
	}
	
}
