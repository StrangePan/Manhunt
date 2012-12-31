package com.bendude56.hunted.loadouts.models;

import java.util.ArrayList;

import com.bendude56.hunted.NewManhuntPlugin;

public class SimpleLoadout
{
	public String version = NewManhuntPlugin.getInstance().getDescription().getVersion();
	public ArrayList<SimpleEffect> effects = new ArrayList<SimpleEffect>();
	public ArrayList<SimpleItem> inventory = new ArrayList<SimpleItem>();
	public ArrayList<SimpleItem> armor = new ArrayList<SimpleItem>();
}
