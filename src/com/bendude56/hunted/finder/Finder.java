package com.bendude56.hunted.finder;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.chat.ChatManager;

public class Finder
{
	private FinderManager manager;

	public final String player_name;
	public final Location location;
	public final Long activation_time; //REAL-LIFE TIME to SEND FINDER INFORMATION
	public final Long expire_time; //REAL-LIFE TIME to SELF-DESTRUCT

	private boolean used = false; //Whether or not the finder has sent the nearest enemy.

	private int schedule;

	public Finder(Player player, FinderManager manager)
	{
		this.manager = manager;

		this.player_name = player.getName();
		this.location = player.getLocation();

		Date time = new Date();
		this.activation_time = time.getTime() + 8000;
		this.expire_time = activation_time + (1000*manager.getGame().getPlugin().getSettings().FINDER_COOLDOWN.value);
		
		player.sendMessage(ChatManager.bracket1_ + "Finding nearest enemy. Stand still for " + ChatColor.DARK_RED + "8 seconds." + ChatManager.bracket2_);
		
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 5, 5);
	}

	/**
	 * Checks when to send a player the finder results.
	 * Checks when to shut itself down.
	 */
	public void onTick()
	{
		long time = new Date().getTime();
		
		if (!used && time >= activation_time) //Should I send the player the information?
		{
			Player p = Bukkit.getPlayer(player_name);
			if (p != null)
			{
				if (checkValidity())
				{
					FinderUtil.sendMessageFinderResults(p);
				}
			}
			else
			{
				manager.stopFinder(this);
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
			manager.stopFinder(this);
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
				manager.stopFinder(this);
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
			manager.stopFinder(this);
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
			p.sendMessage(ChatManager.leftborder + "Your Prey Finder is still charging. Please wait for " + ChatColor.DARK_RED + (int) Math.ceil(((double) expire_time - (double) time.getTime())/(double) 1000) + " seconds.");
		}
	}

	protected void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		manager = null;
	}


}
