package com.deaboy.manhunt.loadouts.models;

import java.util.ArrayList;

import com.deaboy.manhunt.NewManhuntPlugin;

public class SimpleLoadout
{
	public String version = NewManhuntPlugin.getInstance().getDescription().getVersion();
	public ArrayList<SimpleEffect> effects = new ArrayList<SimpleEffect>();
	public ArrayList<SimpleItem> inventory = new ArrayList<SimpleItem>();
	public ArrayList<SimpleItem> armor = new ArrayList<SimpleItem>();
}
