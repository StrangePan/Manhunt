package com.deaboy.manhunt.settings;

public interface ListSetting
{
	public void add(Object o) throws IllegalArgumentException;
	public void add(Object o, int i) throws IllegalArgumentException;
	public void remove(Object o) throws IllegalArgumentException;
	public void remove(int i);
	public void clear();
}
