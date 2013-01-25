package com.deaboy.manhunt.settings;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jnbt.CompoundTag;
import org.jnbt.DoubleTag;
import org.jnbt.FloatTag;
import org.jnbt.Tag;

public class SettingLocation extends SettingBase<Location> implements Setting
{
	
	public SettingLocation(String label, Location defaultValue)
	{
		super(label, defaultValue, SettingType.LOCATION);
	}

	@Override
	public void setValue(String value) throws IllegalArgumentException
	{
		Location loc;
		World world;
		Double x, y, z;
		Float yaw, pitch;
		String[] values = value.split(",");
		
		if (values.length != 6)
			return;
		
		try
		{
			world = Bukkit.getWorld(values[0]);
			x = Double.parseDouble(values[1]);
			y = Double.parseDouble(values[2]);
			z = Double.parseDouble(values[3]);
			yaw = Float.parseFloat(values[4]);
			pitch = Float.parseFloat(values[5]);
			
			loc = new Location(world, x, y, z, yaw, pitch);
			
			setValue(loc);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException( "Argument could not be parsed to a Location!" );
		}
		
	}

	@Override
	public Location getValue()
	{
		return ((Location) super.getValue()).clone();
	}

	@Override
	public Location getValueDefault()
	{
		return ((Location) super.getValueDefault()).clone();
	}

	@Override
	public String toString()
	{
		String string = new String();
		Location loc = getValue();
		
		string += loc.getWorld().getName() + ",";
		string += loc.getX() + ",";
		string += loc.getY() + ",";
		string += loc.getZ() + ",";
		string += loc.getYaw() + ",";
		string += loc.getPitch();
		
		return string;
	}

	@Override
	public String getDescription()
	{
		return null;
	}
	
	@Override
	public CompoundTag getNBT()
	{
		Map<String, Tag> tags = new HashMap<String, Tag>();
		tags.put("x", new DoubleTag("x", getValue().getX()));
		tags.put("y", new DoubleTag("y", getValue().getY()));
		tags.put("z", new DoubleTag("z", getValue().getZ()));
		tags.put("yaw", new FloatTag("yaw", getValue().getYaw()));
		tags.put("pitch", new FloatTag("yaw", getValue().getPitch()));
		
		return new CompoundTag(getLabel(), tags);
	}

}
