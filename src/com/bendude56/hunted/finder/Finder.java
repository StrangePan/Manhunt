package com.bendude56.hunted.finder;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.utilities.ManhuntUtil;

public class Finder {

	public final String player;
	public final Location location;
	public final Long timeout;

	public Finder(Player player)
	{
		this.player = player.getName();
		this.location = player.getLocation();

		Date time = new Date();
		this.timeout = time.getTime() + (60000*HuntedPlugin.getInstance().getSettings().OFFLINE_TIMEOUT.value);
	}

	public boolean isCancelled()
	{
		Player p = Bukkit.getPlayer(player);

		if (p != null)
		{
			if (p.getItemInHand().getType() == Material.COMPASS)
			{
				if (ManhuntUtil.areEqual(p.getLocation(), location, 0.5, true))
				{
					return false;
				}
			}
		}

		return true;

	}

	public boolean isExpired()
	{
		Date time = new Date();
		return (timeout >= time.getTime());
	}

}
