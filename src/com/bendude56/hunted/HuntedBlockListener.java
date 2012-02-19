package com.bendude56.hunted;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

public class HuntedBlockListener {
	
	public HuntedBlockListener() {
		BlockBreakEvent.getHandlerList().register(new RegisteredListener((Listener) this, (EventExecutor) this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
		BlockPlaceEvent.getHandlerList().register(new RegisteredListener((Listener) this, (EventExecutor) this, EventPriority.NORMAL, HuntedPlugin.getInstance(), false));
	}

	private Game g = HuntedPlugin.getInstance().getGame();
	WorldDataFile worlddata = HuntedPlugin.getInstance().getWorldData();
	
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		
		if (worlddata.noBuild() && !e.getPlayer().isOp() && !g.gameHasBegun()) {
			e.setCancelled(true);
		}
		
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.getDistance(e.getBlock().getLocation(),
					worlddata.hunterSpawn()) <= worlddata.noBuildRange() || 
					g.getDistance(e.getBlock().getLocation(),
					worlddata.preySpawn()) <= worlddata.noBuildRange() ||
					g.getDistance(e.getBlock().getLocation(),
					worlddata.pregameSpawn()) <= worlddata.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}
		
		if (worlddata.noBuild() && !e.getPlayer().isOp() && !g.gameHasBegun()) {
			e.setCancelled(true);
		}
		
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.getDistance(e.getBlock().getLocation(),
					worlddata.hunterSpawn()) <= worlddata.noBuildRange() || 
					g.getDistance(e.getBlock().getLocation(),
					worlddata.preySpawn()) <= worlddata.noBuildRange() ||
					g.getDistance(e.getBlock().getLocation(),
					worlddata.pregameSpawn()) <= worlddata.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}
}