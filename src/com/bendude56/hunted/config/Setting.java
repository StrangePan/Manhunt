package com.bendude56.hunted.config;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.bendude56.hunted.HuntedPlugin;

public class Setting<Type> {

	public final String label;
	public Type value;
	public final Type defaultValue;
	
	public final ManhuntFile file;
	private final String onMessage;
	private final String offMessage;
	
	public Setting(String label, Type defaultValue, ManhuntFile file, String onMessage, String offMessage){
		this.label = label;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.file = file;
		this.onMessage = onMessage;
		this.offMessage = offMessage;
		
		load();
		save(false);
	}
	
	public void load()
	{
		String value = file.getProperty(label);
		if (value == null)
		{
			this.value = defaultValue;
			return;
		}
		
		if(!parseValue(value))
			reset(true);
	}
	
	public void save(boolean write)
	{
		file.put(this.label, this.value.toString());
		if (write)
			file.saveFile();
	}
	
	public void setValue(Type value)
	{
		this.value = value;
		save(true);
	}
	
	@SuppressWarnings("unchecked")
	public boolean parseValue(String value)
	{
		try {
			if (this.value instanceof Boolean)
			{
				value = (value.equalsIgnoreCase("on") ? "true" : value);
				this.value = (Type) Boolean.class.cast(Boolean.parseBoolean(value));
			}
			else if (this.value instanceof Integer)
			{
				this.value = (Type) Integer.class.cast(Integer.parseInt(value));
			}
			else if (this.value instanceof String)
			{
				this.value = (Type) value;
			}
			else
			{
				HuntedPlugin.getInstance().log(Level.SEVERE, "Unknown value type for setting \"" + label + "\"");
				return false;
			}
			save(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void reset(boolean write)
	{
		this.value = this.defaultValue;
		save(write);
	}

	public String valueToString()
	{
		if (value instanceof Boolean)
			return (((Boolean)value ? ChatColor.GREEN : ChatColor.RED) + "[" + ((Boolean)value ? "ON" : "OFF") + "]" + ChatColor.WHITE);
		else if (value instanceof Integer)
			return (ChatColor.GREEN + "[" + ChatColor.WHITE + ((Integer)value > 0 ? value.toString() : "OFF") + ChatColor.GREEN + "]" + ChatColor.WHITE);
		else
			return (ChatColor.GREEN + "[" + ChatColor.YELLOW + value+ ChatColor.GREEN + "]" + ChatColor.WHITE);
	}

	public String message()
	{
		if (value instanceof Boolean)
			return ((Boolean)value ? onMessage : offMessage);
		else if (value instanceof Integer)
			return ((Integer)value > 0 ? onMessage : offMessage);
		else
			return onMessage;
	}
}
