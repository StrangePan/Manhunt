package com.bendude56.hunted;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class HuntedBlockListener implements Listener {

	private Game g = HuntedPlugin.getInstance().getGame();
	WorldDataFile worlddata = HuntedPlugin.getInstance().getWorldData();

	@EventHandler
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
			} else if (g.getDistance(e.getBlock().getLocation(),
					worlddata.hunterSpawn()) <= worlddata.noBuildRange()
					|| g.getDistance(e.getBlock().getLocation(),
							worlddata.preySpawn()) <= worlddata.noBuildRange()
					|| g.getDistance(e.getBlock().getLocation(),
							worlddata.pregameSpawn()) <= worlddata
							.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
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
			} else if (g.getDistance(e.getBlock().getLocation(),
					worlddata.hunterSpawn()) <= worlddata.noBuildRange()
					|| g.getDistance(e.getBlock().getLocation(),
							worlddata.preySpawn()) <= worlddata.noBuildRange()
					|| g.getDistance(e.getBlock().getLocation(),
							worlddata.pregameSpawn()) <= worlddata
							.noBuildRange()) {
				e.setCancelled(true);
			}
		}
	}
}