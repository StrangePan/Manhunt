package com.bendude56.hunted.loadouts;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class LoadoutUtil {

	public static void setPlayerInventory(Player p, Loadout l)
	{
		p.getInventory().setContents(l.getContents());
		p.getInventory().setArmorContents(l.getArmorContents());
	}

	public static void clearInventory(Inventory inv)
	{
		inv.clear();
		inv.clear(36);
		inv.clear(37);
		inv.clear(38);
		inv.clear(39);
	}

}
