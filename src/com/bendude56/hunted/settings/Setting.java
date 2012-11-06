package com.bendude56.hunted.settings;

public interface Setting
{
	public String getLabel();
	
	public void setValue(Object value) throws IllegalArgumentException;
	public void setValue(String value) throws IllegalArgumentException;
	public Object getValue();
	public Object getValueDefault();
	
	public String toString();
	public String getDescription();
}
