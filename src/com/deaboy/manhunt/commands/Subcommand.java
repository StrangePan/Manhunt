package com.deaboy.manhunt.commands;

import java.util.HashMap;

public class Subcommand
{
	//////////////// Properties ////////////////
	private final String name;
	private final String label;
	private final CommandTemplate template;
	private HashMap<String, Argument> arguments;
	
	
	//////////////// Constructors ////////////////
	public Subcommand(String cmd_name, String cmd_label, CommandTemplate template)
	{
		if (cmd_name == null || cmd_label == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		
		this.name = cmd_name;
		this.label = cmd_label;
		this.template = template;
		this.arguments = new HashMap<String, Argument>();
	}

	
	//////////////// Setters ////////////////
	public void addArgument(Argument argument)
	{
		if (argument != null)
		{
			arguments.put(argument.getName(), argument);
		}
		
	}

	
	//////////////// Getters ////////////////
	public String getName()
	{
		return this.name;
	}
	public String getLabel()
	{
		return this.label;
	}
	public boolean containsArgument(String arg)
	{
		return this.arguments.containsKey(arg);
	}
	public boolean containsArgument(ArgumentTemplate template)
	{
		if (template != null)
		{
			return this.arguments.containsKey(template.getName());
		}
		else
		{
			return false;
		}
	}
	public Argument getArgument(String arg)
	{
		if (this.arguments.containsKey(arg))
		{
			return this.arguments.get(arg);
		}
		else
		{
			return null;
		}
	}
	public Argument getArgument(ArgumentTemplate template)
	{
		if (template != null)
		{
			return this.getArgument(template.getName());
		}
		else
		{
			return null;
		}
	}
	public int getArgumentCount()
	{
		return this.arguments.size();
	}
	public CommandTemplate getTemplate()
	{
		return this.template;
	}
	
	
}



