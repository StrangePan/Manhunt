package com.bendude56.hunted.settings;

import org.jnbt.ByteTag;

public class SettingBoolean extends SettingBase<Boolean> implements Setting
{
	
	public SettingBoolean(String label, Boolean defaultValue, String onMessage, String offMessage)
	{
		super(label, defaultValue, SettingType.BOOLEAN, onMessage, offMessage);
	}

	@Override
	public void setValue(String value) throws IllegalArgumentException
	{
		try
		{
			super.setValue(Boolean.parseBoolean(value));
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Argument could not be parsed to type Boolean");
		}
	}

	@Override
	public Boolean getValue()
	{
		return (Boolean) super.getValue();
	}

	@Override
	public Boolean getValueDefault()
	{
		return (Boolean) super.getValueDefault();
	}

	@Override
	public String getDescription()
	{
		if (getValue())
		{
			return super.getDescriptions().get(0);
		}
		else
		{
			return super.getDescriptions().get(1);
		}
	}
	
	@Override
	public ByteTag getNBT()
	{
		return new ByteTag(getLabel(), getValue() ? (byte) 1 : (byte) 0);
	}

}
