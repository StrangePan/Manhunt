package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command
{
	//////////////// Properties ////////////////
	private final String name;
	private final String label;
	private HashMap<String, Argument> arguments;
	private List<Subcommand> subcommands;
	private CommandTemplate template;
	
	
	//////////////// Constructors ////////////////
	public Command(String name, String label, CommandTemplate template)
	{
		this.name = name;
		this.label = label;
		this.arguments = new HashMap<String, Argument>();
		this.subcommands = new ArrayList<Subcommand>();
		this.template = template;
	}
	
	
	//////////////// Setters ////////////////
	private void addArgument(Argument arg)
	{
		if (arg != null)
		{
			this.arguments.put(arg.getName(), arg);
		}
	}
	private void addSubcommand(Subcommand subcommand)
	{
		if (!this.subcommands.contains(subcommand))
		{
			this.subcommands.add(subcommand);
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
		if (arg != null)
		{
			return this.arguments.containsKey(arg);
		}
		else
		{
			return false;
		}
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
		return getArgument(template.getName());
	}
	public int getArgumentCount()
	{
		return arguments.size();
	}
	public List<Subcommand> getSubcommands()
	{
		return new ArrayList<Subcommand>(this.subcommands);
	}
	public CommandTemplate getTemplate()
	{
		return this.template;
	}
	
	
	//////////////// Static Constructors ////////////////
	public static Command fromTemplate(CommandTemplate template, String label, String...args)
	{
		Command command = new Command(template.getName(), label, template);
		
		String arg = new String();
		Subcommand subcommand = null;
		Argument argument;
		List<String> arguments = new ArrayList<String>();
		String temp = new String();
		
		for (String a : args)
		{
			if (!temp.isEmpty())
			{
				temp += ' ' + a;
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
			else if (a.startsWith("\""))
			{
				temp = a;
				if (temp.endsWith("\""))
				{
					arguments.add(temp.substring(1, temp.length()-1));
					temp = "";
				}
			}
			else if (a.startsWith("-"))
			{
				if (!arg.isEmpty())
				{
					for (ArgumentTemplate argtemp : template.getArguments())
					{
						if (argtemp.matches(arg))
						{
							argument = Argument.fromTemplate(argtemp, arg, arguments);
							if (argument.isSubcommand())
							{
								if (subcommand != null)
								{
									command.addSubcommand(subcommand);
								}
								subcommand = new Subcommand(command.getName(), command.getLabel(), argument);
							}
							else
							{
								if (subcommand != null)
								{
									subcommand.addArgument(argument);
								}
								else
								{
									command.addArgument(argument);
								}
							}
							break;
						}
					}
				}
				arg = a.substring(1);
				arguments.clear();
				temp = "";
			}
			else
			{
				arguments.add(a);
			}
		}
		if (!arg.isEmpty())
		{
			for (ArgumentTemplate argtemp : template.getArguments())
			{
				if (argtemp.matches(arg))
				{
					argument = Argument.fromTemplate(argtemp, arg, arguments);
					if (argument.isSubcommand())
					{
						if (subcommand != null)
						{
							command.addSubcommand(subcommand);
						}
						subcommand = new Subcommand(command.getName(), command.getLabel(), argument);
					}
					else
					{
						if (subcommand != null)
						{
							subcommand.addArgument(argument);
						}
						else
						{
							command.addArgument(argument);
						}
					}
					break;
				}
			}
		}
		if (subcommand != null)
		{
			command.addSubcommand(subcommand);
		}
		
		return command;
	}
	
	
}



