package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.getspout.spoutapi.event.inventory.InventoryListener;
import org.getspout.spoutapi.event.inventory.InventoryClickEvent;
import org.getspout.spoutapi.event.inventory.InventorySlotType;



public class HuntedInventoryListener extends InventoryListener {

	public HuntedInventoryListener() {
		Bukkit.getPluginManager().registerEvent(Event.Type.CUSTOM_EVENT, this,
				Event.Priority.Normal, HuntedPlugin.getInstance());
	}
	
	public void onInventoryClick(InventoryClickEvent e) {
		e.getPlayer().sendMessage("" + e.getSlot());
		if (HuntedPlugin.getInstance().getSettings().teamHats()
				&& HuntedPlugin.getInstance().getGame().gameHasBegun()
				&& (HuntedPlugin.getInstance().getGame().isHunter(e.getPlayer())
				|| HuntedPlugin.getInstance().getGame().isHunted(e.getPlayer()))
				&& e.getSlotType() == InventorySlotType.HELMET) {
			if (HuntedPlugin.getInstance().getGame().isHunter(e.getPlayer())) {
				e.getInventory().setItem(39, new Wool(DyeColor.RED).toItemStack());
			} else if (HuntedPlugin.getInstance().getGame().isHunted(e.getPlayer())) {
				e.getInventory().setItem(39, new ItemStack(Material.LEAVES));
			}
			return;
		}
		if (HuntedPlugin.getInstance().getGame().isSpectating(e.getPlayer())
				&& e.getPlayer().getGameMode() == GameMode.CREATIVE
				&& !HuntedPlugin.getInstance().getGame().isCreative(e.getPlayer())
				&& !e.getPlayer().isOp()) {
			return;
		}
	}
	
}