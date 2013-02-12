package com.deaboy.manhunt.settings;

import org.jnbt.StringTag;

public class SettingString extends SettingBase<String> implements Setting
{

	public SettingString(String label, String defaultValue, String description)
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
