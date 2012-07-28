package com.bendude56.hunted.loadouts;

import org.bukkit.entity.Player;


public class LoadoutUtil {

	public static void setPlayerInventory(Player p, Loadout l)
	{
		p.getInventory().setContents(l.getContents());
		p.getInventory().setArmorContents(l.getArmor());
	}

}
