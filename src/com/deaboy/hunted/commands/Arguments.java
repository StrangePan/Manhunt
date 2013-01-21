package com.deaboy.hunted.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Arguments
{
	//---------------- Properties ----------------// 
	private HashMap<String, String> arguments = new HashMap<String, String>();
	private String[] args;
	
	
	
	//---------------- Constructors ----------------//
	private Arguments(String string)
	{
		this(string.split(" "));
	}

	private Arguments(String[] string)
	{
		this.args = string;
		this.arguments = parseArgs(string);
	}
	
	
	
	//---------------- Getters ----------------//
	/**
	 * Gets the String value of an argument if it exists.
	 * @param key The argument to get the value for.
	 * @return The string value of the argument, or null if it never existed.
	 */
	public String getString(String key)
	{
		if (arguments.containsKey(key))
		{
			return arguments.get(key);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Gets the String value of an argument if it exists.
	 * @param arg The argument to get the value for.
	 * @return The String value of the argument, or null if it never existed.
	 */public String getString(Argument arg)
	{
		if (contains(arg.getLabel()))
			return getString(arg.getLabel());
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return getString(alias);
		
		return null;
	}
	
	/**
	 * Gets the Boolean value of an argument if it exists.
	 * @param key The argument to get the value for
	 * @return The Boolean value of the argument, or null if it never existed  or if there was a NumberFormatException..
	 */
	public Boolean getBoolean(String key)
	{
		if (!arguments.containsKey(key))
		{
			return null;
		}
		else
		{
			try
			{
				return Boolean.parseBoolean(arguments.get(key));
			}
			catch (Exception e)
			{
				return true;
			}
		}
	}
	
	/**
	 * Gets the Boolean value of an argument if it exists.
	 * @param arg The argument to get the value for.
	 * @return The Boolean value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Boolean getBoolean(Argument arg)
	{
		if (contains(arg.getLabel()))
			return getBoolean(arg.getLabel());
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return getBoolean(alias);
		
		return null;
	}
	
	/**
	 * Gets the Integer value of an argument if it exists.
	 * @param key The argument to get the value for.
	 * @return The Integer value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Integer getInteger(String key)
	{
		if (!arguments.containsKey(key))
		{
			return null;
		}
		else
		{
			try
			{
				return Integer.parseInt(arguments.get(key));
			}
			catch (NumberFormatException e)
			{
				return null;
			}
		}
	}
	
	/**
	 * Gets the Integer value of an argument if it exists.
	 * @param arg The argument to get the value for.
	 * @return The Integer value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Integer getInteger(Argument arg)
	{
		if (contains(arg.getLabel()))
			return getInteger(arg.getLabel());
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return getInteger(alias);
		
		return null;
	}
	
	/**
	 * Gets the Long value of an argument if it exists.
	 * @param key The argument to get the value for.
	 * @return The Long value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Long getLong(String key)
	{
		if (!arguments.containsKey(key))
		{
			return null;
		}
		else
		{
			try
			{
				return Long.parseLong(arguments.get(key));
			}
			catch (NumberFormatException e)
			{
				return null;
			}
		}
	}
	
	/**
	 * Gets the Long value of an argument if it exists.
	 * @param arg The argument to get the value for.
	 * @return The Long value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Long getLong(Argument arg)
	{
		if (contains(arg.getLabel()))
			return getLong(arg.getLabel());
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return getLong(alias);
		
		return null;
	}
	
	/**
	 * Gets the Double value of an argument if it exists.
	 * @param key The argument to get the value for.
	 * @return The Double value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Double getDouble(String key)
	{
		if (!arguments.containsKey(key))
		{
			return null;
		}
		else
		{
			try
			{
				return Double.parseDouble(arguments.get(key));
			}
			catch (NumberFormatException e)
			{
				return null;
			}
		}
	}
	
	/**
	 * Gets the Double value of an argument if it exists.
	 * @param arg The argument to get the value for.
	 * @return The Double value of the argument, or null if it never existed or if there was a NumberFormatException.
	 */
	public Double getDouble(Argument arg)
	{
		if (contains(arg.getLabel()))
			return getDouble(arg.getLabel());
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return getDouble(alias);
		
		return null;
	}
	
	/**
	 * Checks to see if a given argument ever existed.
	 * @param key The argument to check.
	 * @return True if the argument exists, false if it doesn't.
	 */
	public boolean contains(String key)
	{
		return arguments.containsKey(key);
	}
	
	/**
	 * Checks to see if a given argument ever existed.
	 * @param arg The argument to check.
	 * @return True if the argument exists, false if it doesn't.
	 */
	public boolean contains(Argument arg)
	{
		if (contains(arg.getLabel()))
			return true;
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				return true;
		
		return false;
	}
	
	/**
	 * Checks to see if a given argument has multiple occurrences
	 * in the argument list, including it's aliases.
	 * @param arg The argument to check.
	 * @return True if multiple aliases of the argument occur in the argument list.
	 */
	public boolean containsMultiple(Argument arg)
	{
		int count = 0;
		
		if (contains(arg.getLabel()))
			count++;
		
		for (String alias : arg.getAliases())
			if (contains(alias))
				count++;
		
		return count == 0 ? false : true;
	}
	
	public List<String> getArguments()
	{
		return new ArrayList<String>(this.arguments.keySet());
	}
	
	public String[] getRawArgs()
	{
		return this.args;
	}
	
	
	
	//---------------- Private Methods ----------------//
	private static HashMap<String, String> parseArgs(String[] a)
	{
		HashMap<String, String> args = new HashMap<String, String>();
		
		int i;
		String key = null;
		
		for (i = 0; i < a.length; i++)
		{
			String[] temp = a[i].split("[:;=]");
			if (temp[0].startsWith("-"))
			{
				if (key != null)
				{
					args.put(key, "true");
					key = null;
				}
				temp[0] = temp[0].substring(1);
			}
			if (temp.length > 1)
			{
				if (key != null)
				{
					args.put(key, "true");
					key = null;
				}
				args.put(temp[1], temp[2]);
			}
			else
			{
				if (key != null)
				{
					args.put(key, temp[0]);
					key = null;
				}
				else
				{
					key = temp[0];
				}
			}
		}
		return args;
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static Arguments fromStringArray(String[] arguments)
	{
		return new Arguments(arguments);
	}
	
	public static Arguments fromString(String arguments)
	{
		return new Arguments(arguments);
	}
	
	
}
