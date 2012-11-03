package com.bendude56.hunted.settings;

import java.util.List;

public interface ISettingManager
{
	public void save();
	public void load();
	
	public List<ISetting> getVisibleSettings();
	public List<ISetting> getAllSettings();
}
