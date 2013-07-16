package com.deaboy.manhunt.timeline;

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
