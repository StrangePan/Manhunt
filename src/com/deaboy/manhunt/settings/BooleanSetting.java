package com.deaboy.manhunt.settings;

import org.jnbt.ByteTag;

public class BooleanSetting extends BaseSetting<Boolean> implements Setting
{
	
	public BooleanSetting(String label, Boolean defaultValue, String onMessage, String offMessage)
	{
		super(label, defaultValue, SettingType.BOOLEAN, onMessage, offMessage);
	}

	@Override
	public boolean setValue(String value)
	{
		try
		{
			return super.setValue(Boolean.parseBoolean(value));
		}
		catch (NumberFormatException e)
		{
			return false;
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
