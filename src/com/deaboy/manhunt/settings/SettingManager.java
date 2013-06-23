package com.deaboy.manhunt.settings;

import java.util.List;

public interface SettingManager
{
	public String getFileLocation();
	
	public void save();
	public void load();
	
	public void addSetting(Setting setting, boolean visible);
	public List<Setting> getVisibleSettings();
	public List<Setting> getAllSettings();
}
