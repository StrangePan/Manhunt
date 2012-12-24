package com.bendude56.hunted.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.Team;

public class BlockEventHandler implements Listener
{
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlock()))
		{
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlock()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlockClicked()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent e)
	{
		if (!canBuildHere(e.getPlayer(), e.getBlockClicked()))
		{
			e.setCancelled(true);
		}
	}
	
	private boolean canBuildHere(Player p, Block b)
	{
		GameLobby lobby;
		
		lobby = Manhunt.getLobby(p.getWorld());
		
		if (lobby == null)
			return true;
		
		if (lobby.getGame().isRunning())
		{
			if (lobby.getPlayerTeam(p) == Team.SPECTATORS)
			{
				return false;
			}
			
			// TODO Makes sure players don't build within the various spawn protections.
			
		}
		else
		{
			return false;
		}
		return true;
	}
	
}