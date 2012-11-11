package com.bendude56.hunted.lobby;

import java.io.Closeable;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.settings.WorldSettings;

public interface GameLobby extends Closeable
{
	
	public World getWorld();
	public WorldSettings getSettings();
	
	public void addPlayer(Player p, Team t);
	public void removePlayer(Player p);
	public void clearPlayers();
	
	public void setPlayerTeam(Player p, Team t) throws Exception;
	public Team getPlayerTeam(Player p);
	
	public List<Player> 		getPlayers(Team ... teams);
	public List<OfflinePlayer> getOfflinePlayers(Team ... teams);
	
	public void messagePlayers(String message, Team ... teams);
	public void messagePlayers(String message);
	
	public void close();
}
