package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class HuntedBlockListener extends BlockListener {
	
	public HuntedBlockListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, this, Event.Priority.Normal, HuntedPlugin.getInstance());
		Bukkit.getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, this, Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onBlockPlace(BlockPlaceEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlock().getLocation(),
					HuntedPlugin.getInstance().settings.hunterSpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
			else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlock().getLocation(),
					HuntedPlugin.getInstance().settings.preySpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
		}
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		Game g = HuntedPlugin.getInstance().game;
		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			}
			else if (g.isHunted(e.getPlayer())
					&& g.getDistance(e.getBlock().getLocation(),
							HuntedPlugin.getInstance().settings.hunterSpawn)
							<= HuntedPlugin.getInstance().settings.noBuildRange
					|| g.getDistance(e.getBlock().getLocation(),
							HuntedPlugin.getInstance().getWorld().getSpawnLocation())
							<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
			else if (g.isHunter(e.getPlayer())
					&& g.getDistance(e.getBlock().getLocation(),
					HuntedPlugin.getInstance().settings.preySpawn)
					<= HuntedPlugin.getInstance().settings.noBuildRange) {
				e.setCancelled(true);
			}
		}
	}
	
}