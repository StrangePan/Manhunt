package com.bendude56.hunted.utilities;

import org.bukkit.entity.Player;

import com.bendude56.hunted.loadout.Loadout;

public class LoadoutUtil {

	public static void setPlayerInventory(Player p, Loadout l)
	{
		p.getInventory().setContents(l.getContents());
		p.getInventory().setArmorContents(l.getArmor());
	}

}
