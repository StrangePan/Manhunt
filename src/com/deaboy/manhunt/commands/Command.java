package com.deaboy.manhunt.commands;

import java.util.ArrayList;
import java.util.List;

public class Command extends Subcommand
{
	//////////////// Properties ////////////////
	private List<Subcommand> subcommands;
	
	
	//////////////// Constructors ////////////////
	public Command(String name, String label, CommandTemplate template)
	{
		super(name, label, template);
		this.subcommands = new ArrayList<Subcommand>();
	}
	
	
	//////////////// Setters ////////////////
	private void addSubcommand(Subcommand subcommand)
	{
		if (!this.subcommands.contains(subcommand) && subcommand != this)
		{
			this.subcommands.add(subcommand);
		}
	}
	
	
	//////////////// Getters ////////////////
	@Override
	public int getArgumentCount()
	{
		int argcount = super.getArgumentCount();
		for (Subcommand subcommand : subcommands)
		{
			argcount += subcommand.getArgumentCount();
		}
		return argcount;
	}
	public List<Subcommand> getSubcommands()
	{
		return new ArrayList<Subcommand>(this.subcommands);
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
								subcommand = new Subcommand(command.getName(), command.getLabel(), template);
								subcommand.addArgument(argument);
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
						subcommand = new Subcommand(command.getName(), command.getLabel(), template);
						subcommand.addArgument(argument);
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



