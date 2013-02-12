package com.deaboy.manhunt.settings;

import org.jnbt.IntTag;

public class SettingInteger extends SettingBase<Integer> implements Setting
{
	
	public SettingInteger(String label, Integer defaultValue, String onDescription, String offDescription)
	{
		super( label, defaultValue, SettingType.INTEGER, onDescription, offDescription );
	}

	@Override
	public boolean setValue(String value)
	{
		try
		{
			return super.setValue(Integer.parseInt(value));
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Override
	public Integer getValue()
	{
		return (Integer) super.getValue();
	}

	@Override
	public Integer getValueDefault()
	{
		return (Integer) super.getValueDefault();
	}
	
	@Override
	public String getDescription()
	{
		if (getValue() == null)
		{
			return super.getDescriptions().get(1);
		}
		else
		{
			return super.getDescriptions().get(0);
		}
	}
	
	@Override
	public IntTag getNBT()
	{
		return new IntTag(getLabel(), getValue());
	}

}
