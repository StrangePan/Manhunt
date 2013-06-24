package com.deaboy.manhunt.map;

public enum SpawnType
{
	OTHER,
	HUNTER,
	PREY,
	SETUP;
	
	
	
	//---------------- Getters ----------------//
	public int getId()
	{
		return ordinal();
	}
	
	public String getName()
	{
		switch(this)
		{
		case OTHER:
			return "OTHER";
		case HUNTER:
			return "HUNTER";
		case PREY:
			return "PREY";
		case SETUP:
			return "SETUP";
		default:
			return "";
		}
	}
	
	
	//---------------- Public Static Methods ----------------//
	public static SpawnType fromId(int id)
	{
		if (id >= 0 && id < SpawnType.values().length)
			return SpawnType.values()[id];
		else
			return null;
	}
	
	public static SpawnType fromName(String name)
	{
		switch (name.toLowerCase())
		{
		case "other":
		case "0":
			return OTHER;
		case "hunter":
		case "1":
			return HUNTER;
		case "prey":
		case "2":
			return PREY;
		case "setup":
		case "3":
			return SETUP;
		default:
			return null;
		}
	}
	
}
