package com.bendude56.hunted.lobby;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Lobby
{
	public World getWorld();
	
	public void addPlayer(OfflinePlayer p, Team t);
	public void removePlayer(OfflinePlayer p);
	
	public void setPlayerTeam(OfflinePlayer p, Team t) throws Exception;
	public Team getPlayerTeam(OfflinePlayer p);
	
	public List<Player> 		getPlayers(Team ... teams);
	public List<OfflinePlayer> getOfflinePlayers(Team ... teams);
	
	public void messageTeams(String mesage, Team ... teams);
	
	public void clear();
}
