package com.deaboy.manhunt;

public enum ManhuntMode
{
	PLAY, EDIT;
	
	public int getId()
	{
		return this.ordinal();
	}
	
	public static ManhuntMode fromId(int id)
	{
		if (id >= values().length || id < 0)
			return null;
		else
			return ManhuntMode.values()[id];
	}
	
	public static ManhuntMode fromName(String name)
	{
		switch (name.toLowerCase())
		{
		case "play":
		case "0":
			return PLAY;
		case "edit":
		case "1":
			return EDIT;
		default:
			return null;
		}
	}
	
	public String getName()
	{
		switch (this)
		{
		case PLAY:
			return "PLAY";
		case EDIT:
			return "EDIT";
		default:
			return "";
		}
	}
	
}
