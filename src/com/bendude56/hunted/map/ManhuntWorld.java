package com.bendude56.hunted.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.Tag;
import org.jnbt.TagType;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.map.models.SimpleWorld;

public class ManhuntWorld implements World
{

	//---------------- Constants ----------------//
	private final org.bukkit.World world;
	private Spawn spawn;
	private HashMap<String, Map> maps;
	
	
	//---------------- Constructors ----------------//
	public ManhuntWorld(org.bukkit.World world, String file_location)
	{
		if (world == null)
		{
			throw new IllegalArgumentException("Argument cannor be null.");
		}
		
		this.world = world;
		this.spawn = new ManhuntSpawn(world.getSpawnLocation());
		this.maps = new HashMap<String, Map>();
		
		load();
	}
	
	
	//---------------- Getters ----------------//
	@Override
	public String getFileLocation()
	{
		return world.getWorldFolder() + "/" + Manhunt.dirname_world + "/" + Manhunt.filename_worldprops + Manhunt.extension_worldprops;
	}
	
	@Override
	public String getName()
	{
		return getWorld().getName();
	}
	
	@Override
	public org.bukkit.World getWorld()
	{
		return world;
	}
	
	@Override
	public Spawn getSpawn()
	{
		return spawn;
	}

	@Override
	public List<Map> getMaps()
	{
		return (List<Map>) maps.values();
	}

	@Override
	public Map getMap(String label)
	{
		if (maps.containsKey(label))
		{
			return maps.get(label);
		}
		else
		{
			return null;
		}
	}
	

	
	//---------------- Setters----------------//
	@Override
	public void addMap(String label, Map map)
	{
		if (!maps.containsKey(label))
		{
			maps.put(label, map);
		}
	}
	
	@Override
	public void removeMap(String label)
	{
		if (maps.containsKey(label))
		{
			maps.remove(label);
		}
	}
	
	@Override
	public void clearMaps()
	{
		maps.clear();
	}
	
	
	
	//---------------- Private Methods ----------------//
	
	
	
	//---------------- Public Methods ----------------//
	@Override
	public void save()
	{
		File file = new File(getFileLocation());
		NBTOutputStream output;
		SimpleWorld model = new SimpleWorld();
		
		try
		{
			if (!file.exists())
			{
				file.mkdirs();
				file.createNewFile();
			}
			
			output = new NBTOutputStream(new FileOutputStream(file));
			
			save(model);
			
			output.writeTag(CompoundTag.fromObject(model));
			
			output.close();
			
			Manhunt.log("World " + world.getName() + " successfully saved.");
			
		}
		catch (IOException e)
		{
			Manhunt.log(Level.SEVERE, "Could not save world " + world.getName());
			return;
		}
		
	}

	@Override
	public void load()
	{
		File file = new File(getFileLocation());
		Tag tag;
		NBTInputStream input;
		
		if (!file.exists())
		{
			Manhunt.log(Level.SEVERE, "Could not load world " + world.getName());
			return;
		}
		else
		{
			try
			{
				input = new NBTInputStream(new FileInputStream(file));
				
				tag = input.readTag();
				SimpleWorld model = new SimpleWorld();
				
				if (tag.getTagType() == TagType.COMPOUND && tag.getName().equals(""))
				{
					((CompoundTag) tag).toObject(model);
					load(model);
				}
				else
				{
					Manhunt.log(Level.SEVERE, "Could not load world " + world.getName());
					input.close();
					return;
				}
				
				input.close();
				
				Manhunt.log("World " + world.getName() + " successfully loaded.");
				
			}
			catch (IOException e)
			{
				Manhunt.log(Level.SEVERE, "Could not load world " + world.getName());
				return;
			}
			
		}
	}

}
