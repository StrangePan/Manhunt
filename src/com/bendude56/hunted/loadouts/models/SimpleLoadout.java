package com.bendude56.hunted.loadouts.models;

import java.util.ArrayList;

import com.bendude56.hunted.ManhuntPlugin;

public class SimpleLoadout
{
	public String version = ManhuntPlugin.getInstance().getDescription().getVersion();
	public ArrayList<SimpleItem> inventory = new ArrayList<SimpleItem>();
	public ArrayList<SimpleItem> armor = new ArrayList<SimpleItem>();
}
