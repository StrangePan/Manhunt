package com.deaboy.manhunt.settings;

public enum SettingType
{
	BOOLEAN(0),
	INTEGER(1),
	DOUBLE(2),
	LIST_STRING(3),
	LOCATION(4),
	STRING(5),
	FLOAT(6);
	
	
	private final int id;
	
	
	private SettingType(int id)
	{
		this.id = id;
	}
	
	
	
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
		case FLOAT:
			return "float";
		default:
			return "";
		}
	}
	
	public int getId()
	{
		return id;
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
		case "Float":
		case "float":
			return SettingType.FLOAT;
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
