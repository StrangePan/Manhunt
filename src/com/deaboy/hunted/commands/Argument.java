package com.deaboy.hunted.commands;

public class Argument
{
	//---------------- Properties ----------------//
	private final String label;
	private final String[] aliases;
	
	
	
	//---------------- Constructors ----------------//
	public Argument(String label, String...aliases)
	{
		this.label = label;
		this.aliases = aliases;
	}
	
	
	
	//---------------- Getters ----------------//
	public String getLabel()
	{
		return this.label;
	}
	
	public String[] getAliases()
	{
		return this.aliases;
	}
	
	
	
}
