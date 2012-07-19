package com.bendude56.hunted.config;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.config.SettingsFile;

public class Setting<Type> {

	public final String label;
	public Type value;
	public final Type defaultValue;
	
	public final SettingsFile file;
	private final String onMessage;
	private final String offMessage;
	
	public Setting(String label, Type defaultValue, SettingsFile file, String onMessage, String offMessage){
		this.label = label;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.file = file;
		this.onMessage = onMessage;
		this.offMessage = offMessage;
		
		load();
		save();
	}

	public void load()
	{
		String value = file.getProperty(label);
		if (value == null)
		{
			this.value = defaultValue;
			return;
		}
		
		if (!setValue(value))
			this.reset();
	}
	
	public void save()
	{
		file.put(this.label, this.value.toString());
	}

	@SuppressWarnings("unchecked")
	public boolean setValue(String string)
	{
		try {
			if (this.value instanceof Boolean)
			{
				this.value = (Type) Boolean.class.cast(Boolean.parseBoolean(string));
			}
			else if (this.value instanceof Integer)
			{
				this.value = (Type) Integer.class.cast(Integer.parseInt(string));
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
			save();
			return true;
		} catch (Exception e) {
			HuntedPlugin.getInstance().log(Level.SEVERE, "Value Format Exception for setting \"" + label + "\"");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return false;
		}
	}

	public void reset()
	{
		this.value = this.defaultValue;
		save();
	}

	public String valueToString()
	{
		if (value instanceof Boolean)
			return (((Boolean)value ? ChatColor.GREEN : ChatColor.RED) + "[" + ((Boolean)value ? "ON" : "OFF") + "]");
		else if (value instanceof Integer)
			return (((Integer)value > 0 ? ChatColor.GREEN : ChatColor.RED) + "[" + ((Integer)value > 0 ? value.toString() : "OFF") + "]");
		else
			return (ChatColor.GREEN + "[" + value + "]");
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
