package com.bendude56.hunted.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.games.GameUtil;
import com.bendude56.hunted.games.Game.GameStage;
import com.bendude56.hunted.teams.TeamUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

public class PlayerEventHandler implements Listener {
	
	private ManhuntPlugin plugin;
	
	public PlayerEventHandler(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onAsynchPlayerChat(AsyncPlayerChatEvent e)
	{
		if (e.isCancelled() || !plugin.getSettings().CONTROL_CHAT.value)
		{
			return;
		}
		plugin.getChat().onPlayerchat(e);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(null);

		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
		else
		{
			plugin.getTeams().addPlayer(e.getPlayer());
		}
		
		Team team = plugin.getTeams().getTeamOf(e.getPlayer());
		GameUtil.broadcast(ChatManager.leftborder + TeamUtil.getTeamColor(team) + e.getPlayer().getName() + ChatColor.WHITE + " has " + ChatColor.GREEN + "joined" + ChatColor.WHITE + " the game.", Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		GameUtil.broadcast(ChatColor.YELLOW + e.getPlayer().getName() + " has joined the game", Team.NONE);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent e)
	{
		e.setLeaveMessage(null);
		
		Team team = plugin.getTeams().getTeamOf(e.getPlayer());
		GameUtil.broadcast(ChatManager.leftborder + TeamUtil.getTeamColor(team) + e.getPlayer().getName() + ChatColor.WHITE + " has " + ChatColor.RED + "left" + ChatColor.WHITE + " the game.", Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		GameUtil.broadcast(ChatColor.YELLOW + e.getPlayer().getName() + " has left the game", Team.NONE);
		
		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else
		{
			plugin.getTeams().deletePlayer(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		e.setQuitMessage(null);
		
		Team team = plugin.getTeams().getTeamOf(e.getPlayer());
		GameUtil.broadcast(ChatManager.leftborder + TeamUtil.getTeamColor(team) + e.getPlayer().getName() + ChatColor.WHITE + " has " + ChatColor.RED + "left" + ChatColor.WHITE + " the game.", Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		GameUtil.broadcast(ChatColor.YELLOW + e.getPlayer().getName() + " has left the game", Team.NONE);
		
		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else
		{
			plugin.getTeams().deletePlayer(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if (plugin.gameIsRunning())
		{
			plugin.getGame().onPlayerRespawn(e.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		if (!plugin.gameIsRunning())
		{
			return;
		}
		
		Player p = e.getPlayer();
		Team team = plugin.getTeams().getTeamOf(p);
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			return;
		}
		
		if (team == Team.HUNTERS && plugin.getGame().freeze_hunters)
		{
			e.setCancelled(true);
			return;
		}
		if (team == Team.PREY && plugin.getGame().freeze_prey)
		{
			e.setCancelled(true);
			return;
		}
		
		ManhuntUtil.checkPlayerInBounds(p);
		
		plugin.getGame().finders.verifyFinder(p);
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e)
	{
		if (plugin.gameIsRunning())
		{
			plugin.getGame().finders.verifyFinder(e.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();

		if (p.getWorld() != plugin.getWorld())
		{
			return;
		}
		if (!plugin.gameIsRunning())
		{
			return;
		}
		if (plugin.locked && !p.isOp())
		{
			e.setCancelled(true);
			return;
		}
		
		Team team = plugin.getTeams().getTeamOf(p);
		
		if (team == Team.SPECTATORS)
		{
			e.setCancelled(true);
			return;
		}
		else if (team == Team.HUNTERS || team == Team.PREY)
		{
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK
					|| e.getAction() == Action.RIGHT_CLICK_AIR
					|| e.getAction() == Action.LEFT_CLICK_BLOCK
					|| e.getAction() == Action.LEFT_CLICK_AIR)
			{
				if (plugin.getSettings().PREY_FINDER.value && p.getItemInHand().getType() == Material.COMPASS)
				{
					plugin.getGame().finders.startFinder(p);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		if (e.getFrom().getWorld() == plugin.getWorld() && e.getTo().getWorld() != plugin.getWorld())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else if (e.getFrom().getWorld() != plugin.getWorld() && e.getTo().getWorld() == plugin.getWorld())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
		
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e)
	{

		if (e.getFrom().getWorld() == plugin.getWorld() && e.getTo().getWorld() != plugin.getWorld())
		{
			plugin.getGame().onPlayerLeave(e.getPlayer());
		}
		else if (e.getFrom().getWorld() != plugin.getWorld() && e.getTo().getWorld() == plugin.getWorld())
		{
			plugin.getGame().onPlayerJoin(e.getPlayer());
		}
	}

	/*
	 * public void onPlayerGameModeChange(PlayerGameModeChangeEvent e) {
	 * 
	 * Player p = e.getPlayer(); if (p.getWorld() !=
	 * plugin.getWorld()) { return; } if (!plugin.gameIsRunning())
	 * { return; }
	 * 
	 * if (plugin.getGame().isHunter(p) || plugin.getGame().isHunted(p)) { if (e.getNewGameMode() !=
	 * GameMode.SURVIVAL) { e.setCancelled(true); return; } } if
	 * (plugin.getGame().isSpectating(p)) { if (plugin.getSettings().flyingSpectators()) { if
	 * (e.getNewGameMode() != GameMode.CREATIVE) { e.setCancelled(true); } }
	 * else { if (e.getNewGameMode() != GameMode.SURVIVAL) {
	 * e.setCancelled(true); } } } }
	 */

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			if (plugin.getTeams().getTeamOf(e.getPlayer()) == Team.SPECTATORS)
			{
				e.setCancelled(true);
			}
			if (plugin.getGame().getStage() == GameStage.PREGAME)
			{
				e.setCancelled(true);
			}
		}
		else
		{
			if (plugin.locked && e.getPlayer().isOp())
			{
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			return;
		}
		
		if (plugin.gameIsRunning())
		{
			if (plugin.getTeams().getTeamOf(e.getPlayer()) == Team.SPECTATORS)
			{
				e.setCancelled(true);
			}
			if (plugin.getGame().getStage() == GameStage.PREGAME)
			{
				e.setCancelled(true);
			}
		}
		else
		{
			if (plugin.locked && e.getPlayer().isOp())
			{
				e.setCancelled(true);
				return;
			}
		}
	}

}