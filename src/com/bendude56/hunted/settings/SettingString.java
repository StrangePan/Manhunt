package com.bendude56.hunted.settings;

public class SettingString extends SettingBase<String> implements Setting
{

	public SettingString(String label, String defaultValue, String description)
	{
		super( label, defaultValue, description );
	}

	@Override
	public void setValue(String value) throws IllegalArgumentException
	{
		super.setValue((Object) value);
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

}
