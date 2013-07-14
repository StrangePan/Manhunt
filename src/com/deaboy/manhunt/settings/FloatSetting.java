package com.deaboy.manhunt.settings;

import org.jnbt.FloatTag;

public class FloatSetting extends BaseSetting<Float> implements Setting
{
	
	public FloatSetting(String label, Float defaultValue, String description)
	{
		super( label, defaultValue, SettingType.FLOAT, description);
	}
	
	@Override
	public boolean setValue(String value)
	{
		try
		{
			return super.setValue(Float.parseFloat(value));
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	@Override
	public Float getValue()
	{
		return (Float) super.getValue();
	}
	
	@Override
	public Float getValueDefault()
	{
		return (Float) super.getValueDefault();
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
	public FloatTag getNBT()
	{
		return new FloatTag(getLabel(), getValue());
	}
	
	
}
