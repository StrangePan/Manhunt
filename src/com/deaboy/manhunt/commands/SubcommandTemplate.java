package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class SubcommandTemplate
{
	private ArgumentTemplate argument;
	private List<ArgumentTemplate> arguments;
	private boolean locked;
	
	public SubcommandTemplate(ArgumentTemplate template)
	{
		if (template == null)
		{
			throw new IllegalArgumentException("Argument cannot be null");
		}
		
		this.argument = template;
		this.arguments = new ArrayList<ArgumentTemplate>();
	}
	
	public ArgumentTemplate getRootArgument()
	{
		return argument;
	}
	
	public SubcommandTemplate addArgument(ArgumentTemplate argument)
	{
		if (locked)
			return this;
		
		if (argument != null && !this.arguments.contains(argument) && argument != this.argument)
		{
			this.arguments.add(argument);
		}
		return this;
	}
	public SubcommandTemplate removeArgument(ArgumentTemplate argument)
	{
		if (locked)
			return this;
		
		if (argument != null && this.arguments.contains(argument))
		{
			arguments.remove(argument);
		}
		return this;
	}
	public SubcommandTemplate clearArguments()
	{
		if (locked)
			return this;
		
		arguments.clear();
		return this;
	}
	public List<ArgumentTemplate> getArguments()
	{
		return arguments;
	}
	
	public SubcommandTemplate finalize_()
	{
		this.locked = true;
		return this;
	}
}
