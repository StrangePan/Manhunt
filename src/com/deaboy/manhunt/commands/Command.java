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
		super(name, label, template, null);
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
		SubcommandTemplate subcommandtemp = null;
		Argument argument;
		List<String> arguments = new ArrayList<String>();
		String temp = new String();
		
		if (args.length > 0)
		{
			for (SubcommandTemplate subtemp : template.getSubcommands())
			{
				if (subtemp.getRootArgument().matches(args[0].substring(1)))
				{
					subcommandtemp = subtemp;
					break;
				}
			}
			if (subcommandtemp == null && template.getDefaultSubcommand() != null)
			{
				subcommandtemp = template.getDefaultSubcommand();
				subcommand = new Subcommand(subcommandtemp.getRootArgument().getName(), subcommandtemp.getRootArgument().getName(), template, subcommandtemp);
				arg = subcommandtemp.getRootArgument().getName();
			}
			else
			{
				subcommandtemp = null;
			}
		}
		
		for (String a : args)
		{
			if (!temp.isEmpty())
			{
				temp += ' ' + a;
				if (temp.endsWith("\""))
				{
					if (temp.equals("\"\""))
					{
						arguments.add("");
					}
					else
					{
						arguments.add(temp.substring(1, temp.length()-1));
					}
					temp = "";
				}
			}
			else if (a.startsWith("\""))
			{
				temp = a;
				if (temp.endsWith("\"") && temp.length() > 1)
				{
					/*if (temp.equals("\"\""))
					{
						arguments.add("");
					}
					else
					{*/
						arguments.add(temp.substring(1, temp.length()-1));
					//}
					temp = "";
				}
			}
			else if (a.startsWith("-"))
			{
				if (!arg.isEmpty())
				{
					boolean cont = false;
					if (subcommandtemp != null)
					{
						for (ArgumentTemplate argtemp : subcommandtemp.getArguments())
						{
							if (argtemp.matches(arg))
							{
								argument = Argument.fromTemplate(argtemp, arg, arguments);
								if (subcommand != null)
								{
									subcommand.addArgument(argument);
								}
								else
								{
									command.addArgument(argument);
								}
								cont = true;
								break;
							}
						}
					}
					
					if (subcommandtemp == null || !cont)
					{
						for (SubcommandTemplate subtemp : template.getSubcommands())
						{
							if (subtemp.getRootArgument().matches(arg))
							{
								subcommandtemp = subtemp;
								argument = Argument.fromTemplate(subtemp.getRootArgument(), arg, arguments);
								if (subcommand != null)
								{
									command.addSubcommand(subcommand);
								}
								subcommand = new Subcommand(command.getName(), command.getLabel(), template, subtemp);
								subcommand.addArgument(argument);
							}
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
			boolean cont = false;
			if (subcommandtemp != null)
			{
				for (ArgumentTemplate argtemp : subcommandtemp.getArguments())
				{
					if (argtemp.matches(arg))
					{
						argument = Argument.fromTemplate(argtemp, arg, arguments);
						if (subcommand != null)
						{
							subcommand.addArgument(argument);
						}
						else
						{
							command.addArgument(argument);
						}
						cont = true;
						break;
					}
				}
			}
			
			if (subcommandtemp == null || !cont)
			{
				for (SubcommandTemplate subtemp : template.getSubcommands())
				{
					if (subtemp.getRootArgument().matches(arg))
					{
						subcommandtemp = subtemp;
						argument = Argument.fromTemplate(subtemp.getRootArgument(), arg, arguments);
						if (subcommand != null)
						{
							command.addSubcommand(subcommand);
						}
						subcommand = new Subcommand(command.getName(), command.getLabel(), template, subtemp);
						subcommand.addArgument(argument);
					}
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



