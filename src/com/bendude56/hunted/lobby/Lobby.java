package com.bendude56.hunted.lobby;

import java.io.Closeable;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Lobby extends Closeable
{
	public void addPlayer(Player p);
	public void removePlayer(Player p);
	public List<Player> getPlayers();
	
	public World getWorld();
	public Location getLocation();
	public void setLocation(Location location);
	
	public void messagePlayers(String message);
	
	public void clearPlayers();
	public void close();
}
