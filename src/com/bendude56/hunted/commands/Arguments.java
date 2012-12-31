package com.bendude56.hunted.commands;

import java.util.HashMap;

public class Arguments
{
	//---------------- Properties ----------------// 
	private HashMap<String, String> arguments = new HashMap<String, String>();
	
	
	
	//---------------- Constructors ----------------//
	private Arguments(String string)
	{
		this(string.split(" "));
	}

	private Arguments(String[] string)
	{
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
	 * Checks to see if a given argument ever existed.
	 * @param key The argument to check.
	 * @return True if the argument exists, false if it doesn't or if there was a NumberFormatException.
	 */
	public boolean contains(String key)
	{
		return arguments.containsKey(key);
	}
	
	
	
	//---------------- Public Methods ----------------//
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
