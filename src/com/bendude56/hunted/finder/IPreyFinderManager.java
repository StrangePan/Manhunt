package com.bendude56.hunted.finder;

import java.util.List;

import org.bukkit.entity.Player;

public interface IPreyFinderManager {

	public void registerNewFinder(Player p);

	public List<PreyFinder> getExpiredFinders();

	public boolean removeFinder(PreyFinder f);

	public boolean removeFinder(String s);

}
