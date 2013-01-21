package com.deaboy.hunted.finder;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.deaboy.hunted.Manhunt;
import com.deaboy.hunted.ManhuntUtil;
import com.deaboy.hunted.NewManhuntPlugin;
import com.deaboy.hunted.chat.ChatManager;

public class Finder
{
	private static final int CHARGE_TIME = 8000; // Milliseconds
	
	private final long lobby_id;
	
	private final String player_name;
	private final Location location;
	private final Long activation_time; //REAL-LIFE TIME to ACTIVATE
	private final Long expire_time; //REAL-LIFE TIME to SELF-DESTRUCT

	private boolean used = false; //Whether or not the finder has sent the nearest enemy.

	private int schedule;

	public Finder(Player player)
	{
		this.lobby_id = Manhunt.getLobby(player).getId();
		
		this.player_name = player.getName();
		this.location = player.getLocation();

		Date time = new Date();
		this.activation_time = time.getTime() + CHARGE_TIME;
		this.expire_time = activation_time + (1000*Manhunt.getLobby(lobby_id).getSettings().FINDER_COOLDOWN.getValue());
		
		player.sendMessage(ChatManager.bracket1_ + "Finding nearest enemy. Stand still for " + ChatColor.DARK_RED + CHARGE_TIME / 1000 + " seconds." + ChatManager.bracket2_);
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(NewManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 5, 5);
	}
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the name of the player that this finder
	 * is assigned to.
	 * @return
	 */
	public String getPlayerName()
	{
		return player_name;
	}

	/**
	 * Checks when to send a player the finder results.
	 * Checks when to shut itself down.
	 */
	public void onTick()
	{
		if (!checkValidity())
		{
			return;
		}
		
		long time = new Date().getTime();
		
		if (!used && time >= activation_time) //Should I send the player the information?
		{
			Player p = Bukkit.getPlayer(player_name);
			if (p != null)
			{
				if (p.getFoodLevel() < 4)
				{
					p.sendMessage(ChatManager.leftborder + ChatColor.RED + "You don't have enough food to power the finder!");
					Manhunt.getFinders().stopFinder(this, true);
				}
				if (checkValidity())
				{
					// FinderUtil.sendMessageFinderResults(p);
					FinderUtil.findNearestEnemy(p);
				}
			}
			else
			{
				Manhunt.getFinders().stopFinder(this, true);
			}
			used = true;
		}
		if (time >= expire_time)
		{
			Player p = Bukkit.getPlayer(player_name);
			if (p != null)
			{
				p.sendMessage(ChatManager.bracket1_ + "The " + ChatColor.DARK_RED + "Prey Finder" + ChatManager.color + " is fully charged." + ChatManager.bracket2_);
			}
			Manhunt.getFinders().stopFinder(this, true);
		}
	}

	/**
	 * Determines if a this finder is still valid based on the
	 * player's location and item held in hand. If it returns false,
	 * will self-destruct, sending the player the cancel message.
	 * Will always return true if the finder has already been used.
	 * @return True if the finder is still valid, false if not.
	 */
	public boolean checkValidity()
	{
		if (used)
		{
			return true;
		}

		Player p = Bukkit.getPlayer(player_name);

		if (p != null)
		{
			if (p.getItemInHand().getType() != Material.COMPASS || !ManhuntUtil.areEqualLocations(p.getLocation(), location, 0.5, true))
			{
				FinderUtil.sendMessageFinderCancel(p);
				Manhunt.getFinders().stopFinder(this, true);
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			FinderUtil.sendMessageFinderCancel(p);
			Manhunt.getFinders().stopFinder(this, true);
			return false;
		}

	}

	public void sendTimeLeft()
	{
		Player p = Bukkit.getPlayer(player_name);
		
		if (p == null)
		{
			checkValidity();
		}
		else if (used)
		{
			Date time = new Date();
			p.sendMessage(ChatManager.leftborder + "The Prey Finder is still charging. Wait for " + ChatColor.DARK_RED + (int) Math.ceil(((double) expire_time - (double) time.getTime())/(double) 1000) + " seconds.");
		}
	}

	public boolean isUsed()
	{
		return used;
	}

	protected void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
	}


}
