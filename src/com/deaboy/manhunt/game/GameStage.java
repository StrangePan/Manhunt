package com.deaboy.manhunt.game;

public enum GameStage
{
	//---------------- Enums ----------------//
	INTERMISSION(0),PREGAME(1),SETUP(2),HUNT(3);
	
	
	//---------------- Properties ----------------//
	private final int id;
	
	
	//---------------- Constructors ----------------//
	private GameStage(int id)
	{
		this.id = id;
	}
	
	
	//---------------- Getters ----------------//
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		switch (this)
		{
		case INTERMISSION:
			return "Intermission";
		case PREGAME:
			return "Pregame";
		case SETUP:
			return "Setup";
		case HUNT:
			return "Hunt";
		default:
			return new String();
		}
	}
	
	
	//---------------- Public Static Methods ----------------//
	public static GameStage fromId(int id)
	{
		switch (id)
		{
		case 0:
			return INTERMISSION;
		case 1:
			return PREGAME;
		case 2:
			return SETUP;
		case 3:
			return HUNT;
		default:
			return null;
		}
	}
	
}
