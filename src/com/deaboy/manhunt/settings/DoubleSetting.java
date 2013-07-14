package com.deaboy.manhunt.settings;

import org.jnbt.DoubleTag;

public class DoubleSetting extends BaseSetting<Double> implements Setting
{
	
	public DoubleSetting(String label, Double defaultValue, String description)
	{
		super( label, defaultValue, SettingType.DOUBLE, description);
	}
	
	@Override
	public boolean setValue(String value)
	{
		try
		{
			return super.setValue(Double.parseDouble(value));
		}
		catch (NumberFormatException e)
		{
			return false;
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
