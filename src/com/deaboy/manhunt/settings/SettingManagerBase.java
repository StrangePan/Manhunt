package com.deaboy.manhunt.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.deaboy.manhunt.ManhuntPlugin;

public abstract class SettingManagerBase extends Properties implements SettingManager
{
	
	private static final long serialVersionUID = 8544720090213471558L;
	
	private File file;
	private List<Setting> settings;
	private List<Setting> settings_visible;
	
	private static final String header = "Manhnt settings file";
	
	
	public SettingManagerBase(String filepath)
	{
		this.file = new File(filepath);
		this.settings = new ArrayList<Setting>();
		this.settings_visible = new ArrayList<Setting>();
		
		addSetting(new SettingString("version", ManhuntPlugin.getVersion(), ""), false);
	}
	
	public void addSetting(Setting setting, boolean visible)
	{
		this.settings.add(setting);
		if (visible)
		{
			this.settings_visible.add(setting);
		}
	}
	
	@Override
	public void save()
	{
		for (Setting setting : settings)
		{
			this.clear();
			this.put(setting.getLabel(), this.toString());
		}
		saveFile();
	}
	
	private void saveFile()
	{
		FileOutputStream stream_out;
		
		file.mkdirs();
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			stream_out = new FileOutputStream(file);
			store(stream_out, header);
			stream_out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void load()
	{
		loadFile();
		
		for (Setting setting : settings)
		{
			if (!this.containsKey(setting.getLabel()))
			{
				continue;
			}
			
			setting.setValue((String) this.get(setting.getLabel()));
		}
	}
	
	private void loadFile()
	{
		FileInputStream stream_in;
		
		if (!file.exists())
		{
			return;
		}
		
		try
		{
			stream_in = new FileInputStream(file);
			load(stream_in);
			stream_in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<Setting> getVisibleSettings()
	{
		return settings_visible;
	}

	@Override
	public List<Setting> getAllSettings()
	{
		return settings;
	}

}
