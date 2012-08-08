package com.bendude56.hunted.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

public class BlockEventHandler implements Listener {

	private ManhuntPlugin plugin;
	
	public BlockEventHandler(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e)
	{
		e.setCancelled(!canBuildHere(e.getPlayer(), e.getBlock()));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e)
	{
		e.setCancelled(!canBuildHere(e.getPlayer(), e.getBlock()));
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e)
	{
		e.setCancelled(!canBuildHere(e.getPlayer(), e.getBlockClicked()));
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent e)
	{
		e.setCancelled(!canBuildHere(e.getPlayer(), e.getBlockClicked()));
	}
	
	private boolean canBuildHere(Player p, Block b)
	{
		if (p.getWorld() != plugin.getWorld())
		{
			return true;
		}

		if (plugin.gameIsRunning())
		{
			if (plugin.getTeams().getTeamOf(p) == Team.SPECTATORS)
			{
				return false;
			}
			if (ManhuntUtil.getDistance(b.getLocation(), plugin.getSettings().SPAWN_HUNTER.value, true) <= plugin.getSettings().SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(b.getLocation(), plugin.getSettings().SPAWN_PREY.value, true) <= plugin.getSettings().SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(b.getLocation(), plugin.getSettings().SPAWN_SETUP.value, true) <= plugin.getSettings().SPAWN_PROTECTION.value)
			{
				return false;
			}
		}
		else if (plugin.getSettings().NO_BUILD.value && !p.isOp())
		{
			return false;
		}
		else if (plugin.locked)
		{
			return false;
		}
		return true;
	}
	
}