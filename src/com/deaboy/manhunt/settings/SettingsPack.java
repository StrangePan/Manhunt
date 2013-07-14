package com.deaboy.manhunt.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsPack
{
	//////////////// PROPERTIES ////////////////
	private List<Setting> settings;
	private List<Setting> settings_visible;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public SettingsPack()
	{
		this.settings = new ArrayList<Setting>();
		this.settings_visible = new ArrayList<Setting>();
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- SETTINGS ----------------//
	protected SettingsPack addSetting(Setting setting, boolean visible)
	{
		if (setting != null && !this.settings.contains(setting))
		{
			this.settings.add(setting);
			if (visible && !this.settings_visible.contains(setting))
			{
				this.settings_visible.add(setting);
			}
		}
		return this;
	}
	public List<Setting> getVisibleSettings()
	{
		return new ArrayList<Setting>(this.settings);
	}
	public List<Setting> getAllSettings()
	{
		return new ArrayList<Setting>(this.settings_visible);
	}
	public boolean containsSetting(String label)
	{
		for (Setting setting : settings)
		{
			if (setting.getLabel().equalsIgnoreCase(label))
				return true;
		}
		return false;
	}
	public Setting getSetting(String label)
	{
		for (Setting setting : settings)
		{
			if (setting.getLabel().equalsIgnoreCase(label))
				return setting;
		}
		return null;
	}
	
	
}
