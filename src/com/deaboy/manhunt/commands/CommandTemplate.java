package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandTemplate
{
	// Properties
	private String name;
	private List<String> aliases;
	private List<ArgumentTemplate> arguments;
	
	
	
	// Constructors
	public CommandTemplate(String name)
	{
		this();
		this.name = name;
	}
	
	public CommandTemplate()
	{
		this.name = new String();
		this.aliases = new ArrayList<String>();
		this.arguments = new ArrayList<ArgumentTemplate>();
	}
	
	
	
	// Setters
	public CommandTemplate setName(String name)
	{
		if (name != null && !name.isEmpty())
			this.name = name;
		return this;
	}
	public CommandTemplate addAlias(String alias)
	{
		if (alias != null && !alias.isEmpty() && !this.aliases.contains(alias))
			this.aliases.add(alias);
		return this;
	}
	public CommandTemplate removeAlias(String alias)
	{
		if (alias != null && !alias.isEmpty() && this.aliases.contains(alias))
			this.aliases.remove(alias);
		return this;
	}
	public CommandTemplate clearAliases()
	{
		this.aliases.clear();
		return this;
	}
	public CommandTemplate addArgument(ArgumentTemplate argument)
	{
		if (argument != null && !this.arguments.contains(argument))
			this.arguments.add(argument);
		return this;
	}
	public CommandTemplate removeArgument(ArgumentTemplate argument)
	{
		if (argument != null && this.arguments.contains(argument))
			this.arguments.remove(argument);
		return this;
	}
	public CommandTemplate clearArguments()
	{
		this.arguments.clear();
		return this;
	}
	
	// Getters
	public String getName()
	{
		return this.name;
	}
	public List<String> getAliases()
	{
		return this.aliases;
	}
	public List<ArgumentTemplate> getArguments()
	{
		return this.arguments;
	}
	
	
	// Command Processing
	public boolean matches(String label)
	{
		if (label.equalsIgnoreCase(getName()))
			return true;
		for (String alias : getAliases())
			if (label.equalsIgnoreCase(alias))
				return true;
		return false;
	}
	
	
}
