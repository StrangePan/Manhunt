package com.deaboy.manhunt.settings;

import org.jnbt.DoubleTag;

public class SettingDouble extends SettingBase<Double> implements Setting
{
	
	public SettingDouble(String label, Double defaultValue, String description)
	{
		super( label, defaultValue, SettingType.DOUBLE, description);
	}
	
	@Override
	public void setValue(String value)
	{
		try
		{
			super.setValue(Double.parseDouble(value));
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Argument must be parseable to type Integer");
		}
	}
	
	@Override
	public Double getValue()
	{
		return (Double) super.getValue();
	}
	
	@Override
	public Double getValueDefault()
	{
		return (Double) super.getValueDefault();
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
	public DoubleTag getNBT()
	{
		return new DoubleTag(getLabel(), getValue());
	}
	
	
}
