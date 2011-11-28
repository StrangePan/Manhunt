package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryListener;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;



public class HuntedInventoryListener extends InventoryListener {
	
	public HuntedInventoryListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.INVENTORY_CHANGE, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onInventoryChange(InventoryClickEvent e) {
		if (HuntedPlugin.getInstance().settings.woolHats
				&& HuntedPlugin.getInstance().game.gameHasBegun()
				&& (HuntedPlugin.getInstance().game.isHunter(e.getPlayer())
				|| HuntedPlugin.getInstance().game.isHunted(e.getPlayer()))
				&& e.getSlot() == 40) {
			e.setCancelled(true);
		}
	}
	
}