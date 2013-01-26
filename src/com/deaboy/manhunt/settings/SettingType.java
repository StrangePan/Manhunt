package com.deaboy.manhunt.settings;

public enum SettingType
{
	BOOLEAN,
	INTEGER,
	DOUBLE,
	LIST_STRING,
	LOCATION,
	STRING;
	
	public String toString()
	{
		switch (this)
		{
		case BOOLEAN:
			return "boolean";
		case INTEGER:
			return "integer";
		case DOUBLE:
			return "double";
		case LIST_STRING:
			return "list_string";
		case LOCATION:
			return "location";
		case STRING:
			return "string";
		default:
			return "";
		}
	}
	
	public int getId()
	{
		switch (this)
		{
		case BOOLEAN:
			return 0;
		case INTEGER:
			return 1;
		case DOUBLE:
			return 2;
		case LIST_STRING:
			return 3;
		case LOCATION:
			return 4;
		case STRING:
			return 5;
		default:
			return -1;
		}
	}
	
	/**
	 * Matches a SettingType with the string, returning the matching type.
	 * Returns null if there is no match.
	 * @param name
	 * @return
	 */
	public static SettingType fromTypeName(String name)
	{
		switch (name)
		{
		case "Boolean":
		case "boolean":
		case "Bool":
		case "bool":
			return SettingType.BOOLEAN;
		case "Integer":
		case "integer":
		case "Int":
		case "int":
			return SettingType.INTEGER;
		case "Double":
		case "double":
			return SettingType.DOUBLE;
		case "List_String":
		case "list_string":
		case "String_List":
		case "string_list":
			return SettingType.LIST_STRING;
		case "Location":
		case "location":
			return SettingType.LOCATION;
		case "String":
		case "string":
			return SettingType.STRING;
		default:
			return null;
		}
	}
	
	public static SettingType fromId(int id)
	{
		if (id >= 0 && id < SettingType.values().length)
			return SettingType.values()[id];
		else
			return null;
	}
	
	
}
