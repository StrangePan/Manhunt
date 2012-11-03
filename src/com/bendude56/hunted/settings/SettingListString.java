package com.bendude56.hunted.settings;

import java.util.ArrayList;

public class SettingListString extends Setting<ArrayList<String>> implements ISetting
{

	public SettingListString(String label, ArrayList<String> defaultValue, String description)
	{
		super(label, defaultValue, description);
	}
	
	@Override
	public void setValue(String value) throws IllegalArgumentException
	{
		String[] values1 = value.split(",");
		ArrayList<String> values2 = new ArrayList<String>(values1.length);
		
		for (int i = 0; i < values1.length; i++)
		{
			values2.set(i, values1[i]);
		}
		
		super.setValue(values1);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getValue()
	{
		return (ArrayList<String>) super.getValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> getValueDefault()
	{
		return (ArrayList<String>) super.getValueDefault();
	}
	
	@Override
	public String getDescription()
	{
		return super.getDescriptions().get(0);
	}
	
}
