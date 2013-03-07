package com.deaboy.manhunt.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.lobby.Lobby;

public class PlayerEventHandler implements Listener
{
	
	/**
	 * Handles Manhunt chat events. Handles team-exclusive chat
	 * and name colors.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onAsynchPlayerChat(AsyncPlayerChatEvent e)
	{
		if (!Manhunt.getSettings().HANDLE_CHAT.getValue())
			return;
		
		String message;
		Lobby lobby = Manhunt.getPlayerLobby(e.getPlayer());
		List<Player> recipients = new ArrayList<Player>();
		
		if (lobby == null)
			recipients.addAll(e.getPlayer().getWorld().getPlayers());
		else if (!lobby.gameIsRunning() || lobby.getSettings().ALL_TALK.getValue())
			recipients.addAll(lobby.getOnlinePlayers());
		else
			recipients.addAll(lobby.getOnlinePlayers(lobby.getPlayerTeam(e.getPlayer())));
		if (!recipients.contains(e.getPlayer()))
			recipients.add(e.getPlayer());
		
		message = lobby.getPlayerTeam(e.getPlayer()).getColor() + 
				e.getPlayer().getName() + ChatColor.WHITE + ": " +
				e.getMessage();
		
		for (Player p : recipients)
			p.sendMessage(message);
		
		Bukkit.getServer().getConsoleSender().sendMessage(message);
		
		e.setCancelled(true);
	}

	/**
	 * Handles player joining events. Sends them to the main lobby
	 * or broadcasts a return message to the players in the game.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);
		
		Manhunt.playerJoinServer(e.getPlayer());
	}
	
	/**
	 * Handles player kick events. Sends them to the main lobby
	 * and removes them from their current lobby (because they
	 * were kicked!)
	 * Updated 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e)
	{
		e.setLeaveMessage(null);
		
		Manhunt.playerLeaveServer(e.getPlayer());
	}

	/**
	 * Handles player quit events. Will initiate a timeout
	 * countdown for that player.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		e.setQuitMessage(null);
		
		Manhunt.playerLeaveServer(e.getPlayer());
	}
	
}