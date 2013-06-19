package com.deaboy.manhunt.lobby;

public enum LobbyType
{
	HUB,
	GAME;
	
	
	
	//---------------- Getters ----------------//
	public int getId()
	{
		return ordinal();
	}
	
	public String getName()
	{
		switch (this)
		{
		case HUB:
			return "HUB";
		case GAME:
			return "GAME";
		default:
			return "";
		}
	}

	
	
	//---------------- Public Static Methods ----------------//
	public static LobbyType fromId(int id)
	{
		for (LobbyType type : LobbyType.values())
			if (type.getId() == id)
				return type;
		return null;
	}
	
	public static LobbyType fromName(String name)
	{
		switch (name.toLowerCase())
		{
		case "hub":
		case "0":
			return HUB;
		case "game":
		case "1":
			return GAME;
		default:
			return null;
		}
	}
	
}
