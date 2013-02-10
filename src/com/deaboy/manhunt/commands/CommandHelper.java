package com.deaboy.manhunt.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.deaboy.manhunt.map.Map;

/**
 * A class dedicated to providing helpful functions for commands.
 * For example, keeps track of context commands, such as which map
 * or lobby the console has selected, or which map the player wishes
 * to edit.
 * @author Deaboy
 *
 */
public class CommandHelper
{
	//---------------- Properties ----------------//
	Long console_selected_lobby;
	HashMap<String, Long> selected_lobbies;
	HashMap<String, Map> selected_maps;
	
	
	
	//---------------- Constructors ----------------//
	public CommandHelper()
	{
		this.console_selected_lobby = null;
		this.selected_lobbies = new HashMap<String, Long>();
		this.selected_maps = new HashMap<String, Map>();
	}
	
	
	
	//---------------- Getters ----------------//
	/*
	public GameLobby getSelectedLobby(String name)
	{
		if (selected_lobbies.containsKey(name))
			return Manhunt.getLobby(selected_lobbies.get(name));
		else
			return null;
	}
	
	public GameLobby getSelectedLobby(CommandSender sender)
	{
		if (sender instanceof ConsoleCommandSender)
			return Manhunt.getLobby(console_selected_lobby);
		else
			return getSelectedLobby(sender.getName());
	}
	*/
	
	public Map getSelectedMap(String name)
	{
		if (selected_maps.containsKey(name))
		{
			return selected_maps.get(name);
		}
		return null;
	}
	
	
	
	//---------------- Setters ----------------//
	/*
	public void setSelectedLobby(String name, GameLobby lobby)
	{
		selected_lobbies.put(name, lobby.getId());
	}
	
	public void setSelectedLobby(CommandSender sender, GameLobby lobby)
	{
		if (sender instanceof ConsoleCommandSender)
			this.console_selected_lobby = lobby.getId();
		else
			setSelectedLobby(sender.getName(), lobby);
	}
	*/
	
	public void setSelectedMap(String name, Map map)
	{
		selected_maps.put(name, map);
	}
	
	public void setSelectedMap(CommandSender sender, Map map)
	{
		if (!(sender instanceof ConsoleCommandSender))
		{
			selected_maps.put(sender.getName(), map);
		}
	}
	
}
