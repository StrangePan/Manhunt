package com.deaboy.manhunt.settings;

import com.deaboy.manhunt.game.ManhuntGame;

public class GameLobbySettings extends LobbySettings
{
	//////////////// PROPERTIES ////////////////
	public final BooleanSetting		USE_AMBER;
	public final StringSetting		GAME_CLASS;
	public final StringListSetting	MAPS;
	public final IntegerSetting 	OFFLINE_TIMEOUT;
	
	public final IntegerSetting 	TIME_LIMIT;
	public final IntegerSetting 	TIME_INTERMISSION;
	public final BooleanSetting 	FRIENDLY_FIRE;
	public final BooleanSetting 	ALL_TALK;
	public final BooleanSetting 	HOSTILE_MOBS;
	public final BooleanSetting 	PASSIVE_MOBS;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public GameLobbySettings()
	{
		addSetting(USE_AMBER =		new BooleanSetting("useamber", true, "Manhunt will record/restore the world with Amber.", "Manhunt will not restore the world."), true);
		addSetting(GAME_CLASS =		new StringSetting("gametype", ManhuntGame.class.getCanonicalName(), ""), false);
		addSetting(MAPS =			new StringListSetting("maps", ""), false);
		addSetting(OFFLINE_TIMEOUT =	new IntegerSetting("timeout", 30, "Seconds before players are disqualified.", "Players will be immediately disqualified."), true);
		
		addSetting(TIME_LIMIT =			new IntegerSetting("timelimit", 60, "Minutes that the hunt will last.", "The game will never end."), true );
		addSetting(TIME_INTERMISSION =	new IntegerSetting("intermission", 3, "Minutes between Manhunt games.", ""), true);
		addSetting(FRIENDLY_FIRE =	new BooleanSetting("friendlyfire", false, "Teammates can damage each other.", "Teammates cannot kill each other."), true);
		addSetting(ALL_TALK =		new BooleanSetting("alltalk", false, "Teams can communicate with each other.", "Teams cannot see each other's chat."), true);
		addSetting(HOSTILE_MOBS =	new BooleanSetting("hostilemobs", true, "Hostile mobs are enabled.", "Hostile mobs are disabled."), true);
		addSetting(PASSIVE_MOBS =	new BooleanSetting("passivemobs", true, "Passive mobs are enabled.", "Passive mobs are disabled."), true);
		
	}
	
}
