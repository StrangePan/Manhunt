package com.deaboy.manhunt.game.events;

import java.util.logging.Level;

import org.bukkit.Bukkit;

public class LogAction implements Action
{

	private final Level level;
	private final String message;
	
	public LogAction(Level level, String message)
	{
		this.level = level;
		this.message = message;
		
	}
	
	@Override
	public void execute()
	{
		Bukkit.getLogger().log(level, message);
	}
}
