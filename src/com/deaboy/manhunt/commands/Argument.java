package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class Argument
{
	// Properties
	private final String name;
	private final String label;
	private final ArgumentType type;
	private List<String> paramters;
	
	
	
	// Constructors
	public Argument(String name, String label, ArgumentType type, String...params)
	{
		this.name = name;
		this.label = label;
		this.type = type;
		this.paramters = new ArrayList<String>();
		for (String arg: params)
			this.paramters.add(arg);
	}
	
	
	
	// Setters
	public boolean addParameter(String parameter)
	{
		if (parameter == null || parameter.isEmpty())
		{
			return false;
		}
		else
		{
			this.paramters.add(parameter);
			return true;
		}
	}
	public boolean removeParameter(String parameter)
	{
		if (parameter == null || parameter.isEmpty() || !this.paramters.contains(parameter))
		{
			return false;
		}
		else
		{
			this.paramters.remove(parameter);
			return true;
		}
	}
	public void clearParameters()
	{
		this.paramters.clear();
	}
	
	
	// Getters
	public String getName()
	{
		return this.name;
	}
	public String getLabel()
	{
		return this.label;
	}
	public ArgumentType getType()
	{
		return this.type;
	}
	public List<String> getParameters()
	{
		return this.paramters;
	}
	public boolean containsParameter(String parameter)
	{
		for (String arg : this.paramters)
		{
			if (arg.equalsIgnoreCase(parameter))
				return true;
		}
		return false;
	}
	public String getParameter()
	{
		if (paramters.isEmpty())
		{
			return null;
		}
		else
		{
			return this.paramters.get(0);
		}
	}
	
	
	// Static Constructors
	public static Argument fromTemplate(ArgumentTemplate template, String label, List<String> params)
	{
		Argument argument = new Argument(template.getName(), label, template.getType());
		
		switch (template.getType())
		{
		case TEXT:
			for (int i = 0; i < params.size(); i++)
				argument.addParameter(params.get(i));
			break;
			
		case RADIO:
			if (params.size() == 0)
				return null;
			for (String tpar : template.getParameters())
			{
				if (params.get(0).equalsIgnoreCase(tpar))
				{
					argument.addParameter(tpar);
					break;
				}
			}
			return null;
			
		case CHECK:
			if (params.size() == 0)
				return null;
			for (String par : params)
			{
				for (String tpar : template.getParameters())
				{
					if (par.equalsIgnoreCase(tpar))
					{
						argument.addParameter(tpar);
						break;
					}
				}
			}
			break;
			
		case FLAG:
			break;
			
		default:
			break;
		
		}
		
		return argument;
	}
	
	
}
