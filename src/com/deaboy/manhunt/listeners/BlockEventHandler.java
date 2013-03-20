package com.deaboy.manhunt.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;

public class BlockEventHandler implements Listener
{
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlock()))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlock()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlockClicked()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerBucketFill(PlayerBucketFillEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlockClicked()))
		{
			e.setCancelled(true);
		}
	}
	
	private boolean canBuildHere(Player p, Block b)
	{
		Lobby lobby;
		
		lobby = Manhunt.getPlayerLobby(p);
		
		if (lobby == null)
			return true;
		
		if (lobby.gameIsRunning())
		{
			if (lobby.getPlayerTeam(p) != Team.HUNTERS || lobby.getPlayerTeam(p) != Team.PREY)
			{
				return false;
			}
		}
		else
		{
			return p.isOp();
		}
		return true;
	}
	
}