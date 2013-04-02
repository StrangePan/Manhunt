package com.deaboy.manhunt.map;

import java.io.Closeable;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Selection implements Closeable
{
	private Player player;
	private Location primary;
	private Location secondary;
	
	public Selection(Player p)
	{
		this.player = p;
		this.primary = null;
		this.secondary = null;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Location getPrimaryCorner()
	{
		return primary.clone();
	}
	
	public Location getSecondaryCorner()
	{
		return secondary.clone();
	}
	
	public void setPrimaryCorner(Location corner)
	{
		if (corner == null)
			this.primary = null;
		else
			this.primary = corner.clone();
	}
	
	public void setSecondaryCorner(Location corner)
	{
		if (corner == null)
			this.secondary = null;
		else
			this.secondary = corner.clone();
	}
	
	public void clear()
	{
		this.primary = null;
		this.secondary = null;
	}
	
	public void close()
	{
		player = null;
		primary = null;
		secondary = null;
	}
}
