package com.bendude56.hunted.finder;

import java.util.List;

import org.bukkit.entity.Player;

public interface IFinderManager {

	public void registerNewFinder(Player p);

	public List<Finder> getExpiredFinders();

	public boolean removeFinder(Finder f);

	public boolean removeFinder(String s);

}
