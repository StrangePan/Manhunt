package com.deaboy.manhunt.finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;

public class FinderManager
{
	//////////////// CONSTANTS ////////////////
	private static final String FINDER_CANCEL = ChatManager.leftborder + "The PreyFinder was cancelled.";
	
	
	//////////////// PROPERTIES ////////////////
	HashMap<String, Finder> finders;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public FinderManager()
	{
		this.finders = new HashMap<String, Finder>();
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- Finders ----------------//
	public boolean startFinderFor(Player p, long charge_ticks, long cooldown_ticks, Material finder_material, int food_cost, double health_cost, boolean use_xp)
	{
		if (p == null || !p.isOnline())
		{
			return false;
		}
		
		Lobby lobby = Manhunt.getPlayerLobby(p);
		if (lobby == null || lobby.getType() != LobbyType.GAME || !((GameLobby) lobby).gameIsRunning() || ((GameLobby) lobby).getCurrentMap() == null)
		{
			return false;
		}
		
		if (finders.containsKey(p.getName()))
		{
			cancelFinderFor(p);
		}
		
		this.finders.put(p.getName(), new Finder(p, (GameLobby) lobby, charge_ticks, cooldown_ticks, finder_material, food_cost, health_cost, use_xp));
		return true;
	}
	public boolean finderExistsFor(Player p)
	{
		if (p == null)
		{
			return false;
		}
		else
		{
			return finderExistsFor(p.getName());
		}
	}
	public boolean finderExistsFor(String name)
	{
		return finders.containsKey(name);
	}
	public boolean cancelFinderFor(Player p)
	{
		if (p == null)
		{
			return false;
		}
		else
		{
			return cancelFinderFor(p.getName());
		}
	}
	public boolean cancelFinderFor(String name)
	{
		if (!this.finders.containsKey(name))
		{
			return false;
		}
		
		this.finders.get(name).cancel();
		return true;
	}
	public void cancelAllFinders(GameLobby lobby)
	{
		if (lobby == null)
		{
			return;
		}
		else
		{
			cancelAllFinders(lobby.getId());
		}
	}
	public void cancelAllFinders(long lobby_id)
	{
		List<String> players = new ArrayList<String>(this.finders.keySet());
		for (String player : players)
		{
			if (this.finders.get(player).getLobbyId() == lobby_id)
			{
				this.finders.get(player).cancel();
				this.finders.remove(player);
			}
		}
	}
	public void cancelAllFinders()
	{
		for (Finder finder : this.finders.values())
		{
			finder.cancel();
		}
		this.finders.clear();
	}
	public long getFinderChargeTicksRemainingFor(Player p)
	{
		if (p != null)
		{
			return getFinderChargeTicksRemainingFor(p.getName());
		}
		else
		{
			return -1;
		}
	}
	public long getFinderChargeTicksRemainingFor(String name)
	{
		if (finderExistsFor(name))
		{
			return this.finders.get(name).getChargeTicksRemaining();
		}
		else
		{
			return -1;
		}
	}
	public long getFinderCooldownTicksRemainingFor(Player p)
	{
		if (p != null)
		{
			return getFinderCooldownTicksRemainingFor(p.getName());
		}
		else
		{
			return -1;
		}
	}
	public long getFinderCooldownTicksRemainingFor(String name)
	{
		if (finderExistsFor(name))
		{
			return this.finders.get(name).getCooldownTicksRemaining();
		}
		else
		{
			return -1;
		}
	}
	public boolean finderIsUsed(Player p)
	{
		if (p != null)
		{
			return finderIsUsed(p.getName());
		}
		else
		{
			return false;
		}
	}
	public boolean finderIsUsed(String name)
	{
		if (finderExistsFor(name))
		{
			return this.finders.get(name).isUsed();
		}
		else
		{
			return false;
		}
	}
	
	
	public class Finder
	{
		private Player player;
		private String player_name;
		private World world;
		private Location origin;
		private final Material finder_material;
		private final int food_cost;
		private final double health_cost;
		private final long lobby_id;
		private final long charge_ticks;
		private final long charge_time;
		private final long cooldown_ticks;
		private final long cooldown_time;
		private final boolean use_xp;
		private final int schedule;
		private boolean used;
		
		public Finder(Player player, GameLobby lobby, long charge_ticks, long cooldown_ticks, Material finder_material, int food_cost, double health_cost, boolean use_xp)
		{
			this.player = player;
			this.player_name = player.getName();
			this.world = lobby.getCurrentMap().getWorld().getWorld();
			this.origin = player.getLocation().clone();
			this.finder_material = finder_material;
			this.food_cost = food_cost;
			this.health_cost = health_cost;
			this.lobby_id = lobby.getId();
			this.charge_ticks = charge_ticks;
			this.charge_time = world.getFullTime() + charge_ticks;
			this.cooldown_ticks = cooldown_ticks;
			this.cooldown_time = charge_time + cooldown_ticks;
			this.use_xp = use_xp;
			this.used = false;
			
			this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
			{
				public void run()
				{
					step();
				}
			}, 0, 0);
		}
		
		public boolean isUsed()
		{
			return used;
		}
		public long getChargeTicksRemaining()
		{
			return charge_time - world.getFullTime();
		}
		public long getCooldownTicksRemaining()
		{
			return cooldown_time - world.getFullTime();
		}
		
		private void step()
		{
			if (!player.isOnline())
			{
				cancel();
				return;
			}
			if (!used && (!ManhuntUtil.areEqualLocations(player.getLocation(), origin, 0.4, false) || player.getItemInHand() == null || player.getItemInHand().getType() != finder_material))
			{
				player.sendMessage(FINDER_CANCEL);
				cancel();
				return;
			}
			
			if (!used)
			{
				if (world.getFullTime() < charge_time)
				{
					if (use_xp && charge_ticks > 0)
					{
						player.setExp((float) (charge_time - world.getFullTime()) / charge_ticks);
					}
				}
				else
				{
					if (use_xp)
					{
						player.setExp(0f);
					}
					activate();
				}
			}
			else
			{
				if (world.getFullTime() < cooldown_time)
				{
					if (use_xp && cooldown_ticks > 0)
					{
						player.setExp(1f - (float) (cooldown_time - world.getFullTime()) / cooldown_ticks);
					}
				}
				else
				{
					if (use_xp)
					{
						player.setExp(1f);
					}
					player.sendMessage(ChatManager.bracket1_ + "Your Prey Finder is ready to be used." + ChatManager.bracket2_);
					cancel();
					return;
				}
			}
		}
		public void activate()
		{
			GameLobby lobby;
			Team team;
			Player enemy = null;
			double d, distance = -1;
			
			if (used) return;
			this.used = true;
			
			if (!player.isOnline() || Manhunt.getLobby(lobby_id) == null || Manhunt.getLobby(lobby_id).getType() != LobbyType.GAME)
			{
				cancel();
				return;
			}
			else
			{
				lobby = (GameLobby) Manhunt.getLobby(lobby_id);
			}
			
			if (player.getFoodLevel() < food_cost)
			{
				player.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Not enough food to charge the Prey Finder!" + ChatManager.bracket2_);
				cancel();
				return;
			}
			else if (player.getHealth() <= health_cost)
			{
				player.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Not enough health to charge the Prey Finder!" + ChatManager.bracket2_);
				cancel();
				return;
			}
			
			team = lobby.getPlayerTeam(player);
			switch (team)
			{
			case HUNTERS:
				team = Team.PREY;
				break;
			case PREY:
				team = Team.HUNTERS;
				break;
			default:
				team = null;
				break;
			}
			
			if (team == null)
			{
				cancel();
				return;
			}
			
			for (Player p : lobby.getOnlinePlayers(team))
			{
				d = ManhuntUtil.getDistance(player.getLocation(), p.getLocation(), false);
				if (enemy == null || d < distance)
				{
					enemy = p;
					distance = d;
				}
			}
			
			if (enemy == null)
			{
				player.sendMessage(ChatManager.bracket1_ + "There are no online " + team.getColor() + team.getName(true) + ChatManager.color + "!" + ChatManager.bracket2_);
				cancel();
				return;
			}
			else
			{
				player.setCompassTarget(enemy.getLocation().clone());
				if (distance < 25.0)
				{
					player.sendMessage(ChatManager.bracket1_ + "The nearest " + team.getColor() + team.getName(false) + ChatManager.color + " is very close by!" + ChatManager.bracket2_);
				}
				else
				{
					player.sendMessage(ChatManager.bracket1_ + "Location found of the nearest " + team.getColor() + team.getName(false) + ChatManager.color + "." + ChatManager.bracket2_);
				}
				enemy.sendMessage(ChatManager.bracket1_ + "A " + ChatColor.DARK_RED + "PreyFinder" + ChatManager.color + " has gotten your location!" + ChatManager.bracket2_);
			}
			
			player.setFoodLevel(Math.max(player.getFoodLevel() - food_cost, 0));
			player.setHealth(Math.max(player.getHealth() - health_cost, 0));
		}
		public void cancel()
		{
			finders.remove(player_name);
			stop();
		}
		public long getLobbyId()
		{
			return this.lobby_id;
		}
		private void stop()
		{
			player.setExp(1f);
			player = null;
			world = null;
			Bukkit.getScheduler().cancelTask(schedule);
		}
		
		
	}
	
}
