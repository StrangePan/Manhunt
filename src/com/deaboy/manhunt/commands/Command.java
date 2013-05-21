package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class Command
{
	// Properties
	private final String name;
	private final String label;
	private List<Argument> arguments;
	private List<String> argument_names;
	
	
	
	// Constructors
	public Command(String name, String label, Argument...args)
	{
		this.name = name;
		this.label = label;
		this.arguments = new ArrayList<Argument>();
		this.argument_names = new ArrayList<String>();
		
		for (Argument arg : args)
		{
			if (!this.arguments.contains(arg))
				this.arguments.add(arg);
			if (!this.argument_names.contains(arg.getName()))
				this.argument_names.add(arg.getName());
		}
	}
	
	
	
	// Setters
	public boolean addArgument(Argument arg)
	{
		if (arg == null || this.arguments.contains(arg))
		{
			return false;
		}
		
		if (!this.arguments.contains(arg))
			this.arguments.add(arg);
		if (!this.argument_names.contains(arg.getName()))
			this.argument_names.add(arg.getName());
		return true;
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
	public boolean containsArgument(String arg)
	{
		return argument_names.contains(arg);
	}
	public Argument getArgument(String arg)
	{
		if (!argument_names.contains(arg))
		{
			return null;
		}
		else
		{
			for (Argument argument : arguments)
			{
				if (argument.getName().equalsIgnoreCase(arg))
					return argument;
			}
		}
		return null;
	}
	
	
	// Static Constructors
	public static Command fromTemplate(CommandTemplate template, String label, String...args)
	{
		Command command = new Command(template.getName(), label);
		
		String argument = new String();
		List<String> arguments = new ArrayList<String>();
		String temp = new String();
		
		for (String arg : args)
		{
			if (!temp.isEmpty())
			{
				temp += arg;
				if (temp.endsWith("\""))
				{
					arguments.add(temp.substring(1, temp.length()-1));
					temp = "";
				}
				else
				{
					temp += ' ';
				}
			}
			else if (arg.startsWith("\""))
			{
				temp = arg;
			}
			else if (arg.startsWith("-"))
			{
				if (!argument.isEmpty())
				{
					for (ArgumentTemplate argtemp : template.getArguments())
					{
						if (argtemp.matches(argument))
						{
							command.addArgument(Argument.fromTemplate(argtemp, argument, (String[]) arguments.toArray()));
							break;
						}
					}
				}
				argument = arg.substring(1);
				arguments.clear();
				temp = "";
			}
			else
			{
				arguments.add(arg);
			}
		}
		
		return command;
	}
	
}
