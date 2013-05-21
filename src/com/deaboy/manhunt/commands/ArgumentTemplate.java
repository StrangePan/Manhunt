package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class ArgumentTemplate
{
	// Properties
	private String name;
	private ArgumentType type;
	private List<String> aliases;
	private List<String> parameters;
	private boolean required;
	
	
	
	// Constructors
	public ArgumentTemplate(String name)
	{
		this();
		this.name = name;
	}
	
	public ArgumentTemplate()
	{
		this.name = new String();
		this.type = null;
		aliases = new ArrayList<String>();
		parameters = new ArrayList<String>();
		required = false;
	}
	
	
	
	// Setters
	public ArgumentTemplate setName(String name)
	{
		if (name != null && !name.isEmpty())
			this.name = name;
		return this;
	}
	public ArgumentTemplate setType(ArgumentType type)
	{
		if (type != null)
			this.type = type;
		return this;
	}
	public ArgumentTemplate addAlias(String alias)
	{
		if (alias != null && !alias.isEmpty() && !this.aliases.contains(alias))
			this.aliases.add(alias);
		return this;
	}
	public ArgumentTemplate removeAlias(String alias)
	{
		if (alias != null && !alias.isEmpty() && this.aliases.contains(alias))
			this.aliases.remove(alias);
		return this;
	}
	public ArgumentTemplate clearAliases()
	{
		this.aliases.clear();
		return this;
	}
	public ArgumentTemplate addParameter(String parameter)
	{
		if (parameter != null && !parameter.isEmpty() && !this.parameters.contains(parameter) && (this.type == ArgumentType.RADIO || this.type == ArgumentType.CHECK))
			this.parameters.add(parameter);
		return this;
	}
	public ArgumentTemplate removeParameter(String parameter)
	{
		if (parameter != null && !parameter.isEmpty() && this.parameters.contains(parameter))
			this.parameters.remove(parameter);
		return this;
	}
	public ArgumentTemplate clearParameters()
	{
		this.parameters.clear();
		return this;
	}
	public ArgumentTemplate setRequired(boolean required)
	{
		this.required = required;
		return this;
	}
	
	// Getters
	public String getName()
	{
		return this.name;
	}
	public ArgumentType getType()
	{
		return this.type;
	}
	public List<String> getAliases()
	{
		return this.aliases;
	}
	public List<String> getParameters()
	{
		return this.parameters;
	}
	public boolean isRequired()
	{
		return this.required;
	}
	
	
	
	// Argument processing
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
