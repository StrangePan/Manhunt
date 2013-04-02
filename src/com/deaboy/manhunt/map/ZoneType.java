package com.deaboy.manhunt.map;

public enum ZoneType
{
	//---------------- Enumerations ----------------//
	NO_BUILD,
	CONSTRAIN,
	BUILD,
	NO_MOBS,
	SETUP_CONSTRAIN;
	
	
	
	//---------------- Getters ----------------//
	public int getId()
	{
		return ordinal();
	}
	
	public String getName()
	{
		switch (this)
		{
		case NO_BUILD:
			return "NO_BUILD";
		case CONSTRAIN:
			return "CONSTRAIN";
		case BUILD:
			return "BUILD";
		case NO_MOBS:
			return "NO_MOBS";
		case SETUP_CONSTRAIN:
			return "SETUP_CONSTRAIN";
		default:
			return "";
		}
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static ZoneType fromId(int id)
	{
		for (ZoneType type : ZoneType.values())
			if (type.getId() == id)
				return type;
		return null;
	}
	
	public static ZoneType fromName(String name)
	{
		switch (name.toLowerCase())
		{
		case "no_build":
		case "no build":
		case "nobuild":
		case "0":
			return NO_BUILD;
		case "constrain":
		case "1":
			return CONSTRAIN;
		case "build":
		case "2":
			return BUILD;
		case "no_mobs":
		case "no mobs":
		case "nomobs":
		case "3":
			return NO_MOBS;
		case "setup_constrain":
		case "setup constrain":
		case "setupconstrain":
		case "constrainhunters":
		case "constrain_hunters":
		case "constrainsetup":
		case "constrain_setup":
		case "4":
			return SETUP_CONSTRAIN;
		default:
			return null;
		}
	}
	
	
	
}
