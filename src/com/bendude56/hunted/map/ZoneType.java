package com.bendude56.hunted.map;

public enum ZoneType
{
	//---------------- Enumerations ----------------//
	NO_BUILD		(0),
	CONSTRAIN		(1),
	BUILD			(2),
	NO_MOBS			(3),
	SETUP_CONSTRAIN	(4),
	;
	
	
	
	//---------------- Properties ----------------//
	private int id;
	
	
	
	//---------------- Constructors ----------------//
	ZoneType(int id)
	{
		this.id = id;
	}
	
	
	
	//---------------- Getters ----------------//
	public int getId()
	{
		return id;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static ZoneType fromId(int id)
	{
		for (ZoneType type : ZoneType.values())
			if (type.getId() == id)
				return type;
		return null;
	}
}
