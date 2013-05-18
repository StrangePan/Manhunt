package com.deaboy.manhunt.map;

public enum ZoneFlag
{
	//---------------- Enumerations ----------------//
	NO_BUILD,
	BOUNDARY,
	BUILD,
	NO_MOBS,
	SETUP_BOUNDARY;
	
	
	
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
		case BOUNDARY:
			return "BOUNDARY";
		case BUILD:
			return "BUILD";
		case NO_MOBS:
			return "NO_MOBS";
		case SETUP_BOUNDARY:
			return "SETUP_BOUNDARY";
		default:
			return "";
		}
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static ZoneFlag fromId(int id)
	{
		for (ZoneFlag type : ZoneFlag.values())
			if (type.getId() == id)
				return type;
		return null;
	}
	
	public static ZoneFlag fromName(String name)
	{
		switch (name.toLowerCase())
		{
		case "no_build":
		case "no build":
		case "nobuild":
		case "0":
			return NO_BUILD;
		case "constrain":
		case "boundary":
		case "border":
		case "1":
			return BOUNDARY;
		case "build":
		case "2":
			return BUILD;
		case "no_mobs":
		case "no mobs":
		case "nomobs":
		case "safety":
		case "safe":
		case "3":
			return NO_MOBS;
		case "setup_constrain":
		case "setup constrain":
		case "setupconstrain":
		case "constrainhunters":
		case "constrain_hunters":
		case "constrainsetup":
		case "constrain_setup":
		case "setup_boundary":
		case "setup boundary":
		case "setupboundary":
		case "hunterboundary":
		case "hunter_boundary":
		case "boundarysetup":
		case "boundary_setup":
		case "setup_border":
		case "setup border":
		case "setupborder":
		case "hunterborder":
		case "hunter_border":
		case "bordersetup":
		case "border_setup":
		case "4":
			return SETUP_BOUNDARY;
		default:
			return null;
		}
	}
	
	
	
}
