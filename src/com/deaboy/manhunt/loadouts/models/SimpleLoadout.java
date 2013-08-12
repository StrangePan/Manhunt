package com.deaboy.manhunt.loadouts.models;

import java.util.ArrayList;

import com.deaboy.manhunt.ManhuntPlugin;

public class SimpleLoadout
{
	public String version = ManhuntPlugin.getInstance().getDescription().getVersion();
	public String name = "untitled";
	public ArrayList<SimpleEffect> effects = new ArrayList<SimpleEffect>();
	public ArrayList<SimpleItem> inventory = new ArrayList<SimpleItem>();
	public ArrayList<SimpleItem> armor = new ArrayList<SimpleItem>();
}
