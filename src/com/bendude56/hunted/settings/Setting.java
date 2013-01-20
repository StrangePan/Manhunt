package com.bendude56.hunted.settings;

import org.jnbt.Tag;

public interface Setting
{
	//---------------- Getters ----------------//
	/**
	 * Gets the label for this setting.
	 * @return The label of this setting.
	 */
	public String getLabel();
	
	//---------------- Setters ----------------//
	/**
	 * Sets the value of the setting. Data type depends on
	 * the setting. Will throw an error if the type
	 * is not an instance of the setting's supported data type.
	 * @param value The new value.
	 */
	public void setValue(Object value);
	
	/**
	 * Sets the value of the setting via string, parsing the value
	 * to the appropriate data type.
	 * @param value The String representation of the new value.
	 */
	public void setValue(String value);
	
	/**
	 * Gets the value of the setting.
	 * @return The setting's value.
	 */
	public Object getValue();
	
	/**
	 * Gets the setting's default value as predefined
	 * during construction.
	 * @return The setting's default value.
	 */
	public Object getValueDefault();
	
	public SettingType getType();
	
	/**
	 * Gets the String representation of the setting's value.
	 * @return The toString() representation of the setting's value.
	 */
	public String toString();
	
	/**
	 * Gets the appropriate description for the setting. If multiple
	 * descriptions are available for the setting, will select the
	 * most appropriate description based on the setting's value.
	 * @return String description of the setting.
	 */
	public String getDescription();
	
	/**
	 * Gets the NBT Tag version of the setting.
	 * @return Tag representation of the setting.
	 */
	public Tag getNBT();
}
