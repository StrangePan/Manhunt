package com.deaboy.manhunt.settings;

import org.jnbt.StringTag;

public class StringSetting extends BaseSetting<String> implements Setting
{

	public StringSetting(String label, String defaultValue, String description)
	{
		super( label, defaultValue, SettingType.STRING, description );
	}

	@Override
	public boolean setValue(String value)
	{
		return super.setValue((Object) value);
	}

	@Override
	public String getValue()
	{
		return (String) super.getValue();
	}

	@Override
	public String getValueDefault()
	{
		return (String) super.getValueDefault();
	}

	@Override
	public String getDescription()
	{
		return super.getDescriptions().get(0);
	}
	
	@Override
	public StringTag getNBT()
	{
		return new StringTag(getLabel(), getValue());
	}

}
