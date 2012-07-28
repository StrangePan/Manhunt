package com.bendude56.hunted.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.ManhuntGame;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.settings.SettingsManager;

public class HuntedBlockListener implements Listener {

	private ManhuntGame g = HuntedPlugin.getInstance().getGame();
	SettingsManager settings = HuntedPlugin.getInstance().getSettings();

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}

		if (settings.NO_BUILD.value && !e.getPlayer().isOp() && !g.gameHasBegun()) {
			e.setCancelled(true);
		}

		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (ManhuntUtil.getDistance(e.getBlock().getLocation(),
					settings.SPAWN_HUNTER.value) <= settings.SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(e.getBlock().getLocation(),
							settings.SPAWN_PREY.value) <= settings.SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(e.getBlock().getLocation(),
							settings.SPAWN_SETUP.value) <= settings.SPAWN_PROTECTION.value) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.getPlayer().getWorld() != HuntedPlugin.getInstance().getWorld()) {
			return;
		}

		if (settings.NO_BUILD.value && !e.getPlayer().isOp() && !g.gameHasBegun()) {
			e.setCancelled(true);
		}

		if (g.gameHasBegun()) {
			if (g.isSpectating(e.getPlayer())) {
				e.setCancelled(true);
			} else if (ManhuntUtil.getDistance(e.getBlock().getLocation(),
					settings.SPAWN_HUNTER.value) <= settings.SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(e.getBlock().getLocation(),
							settings.SPAWN_PREY.value) <= settings.SPAWN_PROTECTION.value
					|| ManhuntUtil.getDistance(e.getBlock().getLocation(),
							settings.SPAWN_SETUP.value) <= settings.SPAWN_PROTECTION.value) {
				e.setCancelled(true);
			}
		}
	}
}