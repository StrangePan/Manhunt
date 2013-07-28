package com.deaboy.manhunt.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;

public class EntityEventHandler implements Listener
{
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent e)
	{
		if (e.isCancelled())
			return;
		if (e.getEntityType() != EntityType.PLAYER)
			return;
		
		Lobby lobby = Manhunt.getPlayerLobby((Player) e.getEntity());
		
		if (lobby != null && (lobby.getType() != LobbyType.GAME || !((GameLobby) lobby).gameIsRunning()))
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent e)
	{
		if (e.isCancelled())
			return;
		if (e.getTarget().getType() != EntityType.PLAYER)
			return;
		
		Lobby lobby;
		Team team;
		
		lobby = Manhunt.getLobby(e.getEntity().getWorld());
		
		if (lobby == null || lobby.getType() != LobbyType.GAME)
			return;
		
		if (!((GameLobby) lobby).gameIsRunning())
		{
			e.setCancelled(true);
			return;
		}
		else
		{
			team = ((GameLobby) lobby).getPlayerTeam((Player) e);
			if (team != Team.HUNTERS && team != Team.PREY)
				e.setCancelled(true);
		}
		
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if (e.isCancelled())
			return;
		
		Lobby lobby;
		
		lobby = Manhunt.getLobby(e.getLocation().getWorld());
		
		if (lobby == null || lobby.getType() != LobbyType.GAME)
		{
			e.setCancelled(true);
			return;
		}
		
		if (!((GameLobby) lobby).gameIsRunning())
		{
			e.setCancelled(true);
			return;
		}
		else
		{
			if (!((GameLobby) lobby).getSettings().HOSTILE_MOBS.getValue() && ManhuntUtil.isHostile(e.getEntity()))
				e.setCancelled(true);
			else if (!((GameLobby) lobby).getSettings().PASSIVE_MOBS.getValue() && ManhuntUtil.isPassive(e.getEntity()))
				e.setCancelled(true);
		}
	}
	
	
}