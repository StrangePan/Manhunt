package com.bendude56.hunted.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bendude56.hunted.ManhuntPlugin;

public abstract class SettingManager extends Properties implements ISettingManager
{
	
	private static final long serialVersionUID = 8544720090213471558L;
	
	private File file;
	private List<ISetting> settings;
	private List<ISetting> settings_visible;
	
	private static final String header = "Manhnt settings file";
	
	public SettingManager(String filepath)
	{
		this.file = new File(filepath);
		this.settings = new ArrayList<ISetting>();
		this.settings_visible = new ArrayList<ISetting>();
		
		addSetting(new SettingString("version", ManhuntPlugin.getInstance().getDescription().getVersion(), ""), false);
	}
	
	public void addSetting(ISetting setting, boolean visible)
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
		for (ISetting setting : settings)
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
		
		for (ISetting setting : settings)
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
	public List<ISetting> getVisibleSettings()
	{
		return null;
	}

	@Override
	public List<ISetting> getAllSettings()
	{
		return null;
	}

}
