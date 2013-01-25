package com.deaboy.hunted.game.events;

public class RunnableAction implements Action
{
	private final Runnable action;
	
	public RunnableAction(Runnable action)
	{
		this.action = action;
	}

	@Override
	public void execute()
	{
		action.run();
	}

}
