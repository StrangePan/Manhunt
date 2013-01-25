package com.deaboy.hunted.settings;

import java.util.List;

public interface SettingManager
{
	public void save();
	public void load();
	
	public List<Setting> getVisibleSettings();
	public List<Setting> getAllSettings();
}
