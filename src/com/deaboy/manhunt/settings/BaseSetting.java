package com.deaboy.manhunt.settings;

import java.util.List;
import java.util.ArrayList;

public abstract class BaseSetting<Type> implements Setting
{

	private final String label;
	private final List<String> description;

	private final Object value_default;
	private final Class<?> value_class;
	
	private final SettingType type;
	
	private Object value;
	
	public BaseSetting(String label, Type defaultValue, SettingType type, String ... descriptions)
	{
		this.label = label;
		this.description = new ArrayList<String>(descriptions.length);
		
		this.value_default = defaultValue;
		this.value_class = defaultValue.getClass();
		
		this.type = type;
		
		this.value = defaultValue;
		
		for (int i = 0; i < descriptions.length; i++)
		{
			this.description.add(descriptions[i]);
		}
	}
	
	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public boolean setValue(Object value)
	{
		if (value == null || value.getClass() == this.value_class)
		{
			this.value = value;
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public abstract boolean setValue(String value);

	@Override
	public Object getValue()
	{
		return this.value;
	}

	@Override
	public Object getValueDefault()
	{
		return this.value_default;
	}
	
	@Override
	public SettingType getType()
	{
		return this.type;
	}

	@Override
	public String getDescription()
	{
		return null;
	}
	
	protected List<String> getDescriptions()
	{
		return this.description;
	}
	
	@Override
	public void resetToDefault()
	{
		this.value = this.value_default;
	}
	
	

}
