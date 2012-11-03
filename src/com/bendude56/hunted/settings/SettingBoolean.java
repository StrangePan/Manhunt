package com.bendude56.hunted.settings;

public class SettingBoolean extends Setting<Boolean> implements ISetting
{
	
	public SettingBoolean(String label, Boolean defaultValue, String onMessage, String offMessage)
	{
		super(label, defaultValue, onMessage, offMessage);
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

}
