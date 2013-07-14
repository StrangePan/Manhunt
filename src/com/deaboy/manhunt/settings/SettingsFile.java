package com.deaboy.manhunt.settings;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import com.deaboy.manhunt.Manhunt;

public class SettingsFile extends Properties implements Closeable
{
	//////////////// CONSTANTS ////////////////
	private static final String header = "Manhnt settings file";
	private static final long serialVersionUID = -1981918098302909868L;
	
	
	//////////////// PROPERTIES ////////////////
	private final File file;
	List<SettingsPack> packs;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public SettingsFile(File file)
	{
		if (file == null)
		{
			throw new IllegalArgumentException("Argument cannot be null.");
		}
		this.file = file;
		this.packs = new ArrayList<SettingsPack>();
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- Packs ----------------//
	public void addPack(SettingsPack pack)
	{
		if (pack != null && !this.packs.contains(pack))
		{
			this.packs.add(pack);
		}
	}
	public void removePack(SettingsPack pack)
	{
		if (pack != null && this.packs.contains(pack))
		{
			this.packs.remove(pack);
		}
	}
	public boolean containsPack(SettingsPack pack)
	{
		return this.packs.contains(pack);
	}
	public void clearPacks()
	{
		this.packs.clear();
	}
	
	
	//---------------- Files ----------------//
	public void save()
	{
		savePacks();
		saveFile();
	}
	private void saveFile()
	{
		FileOutputStream stream_out;
		
		new File(file.getParent()).mkdirs();
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				Manhunt.log(Level.SEVERE, "Failed to create new settings file " + file.getName());
				Manhunt.log(e);
				return;
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
	public void savePacks()
	{
		this.clear();
		for (SettingsPack pack : packs)
		{
			for (Setting setting : pack.getAllSettings())
			{
				this.put(setting.getLabel(), setting.getValue().toString());
			}
		}
	}
	public void load()
	{
		loadFile();
		loadPacks();
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
	public void loadPacks()
	{
		for (SettingsPack pack : packs)
		{
			for (Setting setting : pack.getAllSettings())
			{
				if (!this.containsKey(setting.getLabel()))
				{
					continue;
				}
				
				setting.setValue((String) this.get(setting.getLabel()));
			}
		}
	}
	
	
	//---------------- Closeable ----------------//
	@Override
	public void close()
	{
		this.packs.clear();
	}
}
