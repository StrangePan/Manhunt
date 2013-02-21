package com.deaboy.manhunt;

public enum MGameMode
{
	PLAY, EDIT;
	
	public int getId()
	{
		return this.ordinal();
	}
	
	public static MGameMode fromId(int id)
	{
		if (id >= MGameMode.values().length || id < 0)
			return null;
		else
			return MGameMode.values()[id];
	}
}
