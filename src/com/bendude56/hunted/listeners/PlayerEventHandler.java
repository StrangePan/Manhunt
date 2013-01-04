package com.bendude56.hunted.listeners;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.chat.ChatManager;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.Team;

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
		if (Manhunt.getSettings().HANDLE_CHAT.getValue())
			return;
		
		Set<Player> recipients = e.getRecipients();
		GameLobby lobby;
		recipients.clear();
		
		if (Manhunt.getMainLobby().getPlayers().contains(e.getPlayer()))
		{
			recipients.addAll(Manhunt.getMainLobby().getPlayers());
		}
		else
		{
			lobby = Manhunt.getLobby(e.getPlayer().getWorld());
			if (lobby == null)
			{
				recipients.addAll(e.getPlayer().getWorld().getPlayers());
			}
			else if (lobby.getSettings().ALL_TALK.getValue() || lobby.getGame().isRunning())
			{
				recipients.addAll(lobby.getPlayers());
			}
			else
			{
				recipients.addAll(lobby.getPlayers(lobby.getPlayerTeam(e.getPlayer())));
			}
		}
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
		
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		// Check if a player is meant to be in the lobby. If not, reset the player.
		if (lobby == null)
		{
			resetPlayer(e.getPlayer());
			e.getPlayer().teleport(Manhunt.getMainLobby().getSpawnLocation());
		}
		// Player is meant to be in the given lobby. Do nothing.
		else
		{
			Team team = lobby.getPlayerTeam(e.getPlayer());
			lobby.broadcast(ChatManager.leftborder + team.getColor() + e.getPlayer().getName() + ChatColor.YELLOW + " is back in the game.");
		}
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
		
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
		{
			for (Player p : e.getPlayer().getWorld().getPlayers())
			{
				p.sendMessage( ChatColor.YELLOW + e.getPlayer().getName() + " has left the game.");
			}
		}
		else
		{
			lobby.removePlayer(e.getPlayer().getName());
			resetPlayer(e.getPlayer());
		}
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
		
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
		{
			for (Player p : e.getPlayer().getWorld().getPlayers())
			{
				p.sendMessage( ChatColor.YELLOW + e.getPlayer().getName() + " has left the game.");
			} 
		}
		else
		{
			Manhunt.startTimeout(e.getPlayer(), lobby, lobby.getSettings().OFFLINE_TIMEOUT.getValue());
		}
	}
	
	/**
	 * Handles player deaths. Broadcasts messages and determines if
	 * the game should end.
	 * Updated 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		GameLobby lobby;
		Player p = e.getEntity();
		Player p2 = null;
		Team t = null;
		Team t2 = null;
		
		int preysize;
		int huntersize;
		
		
		lobby = Manhunt.getLobby(p);
		
		if (lobby == null)
		{
			return;
		}
		if (!lobby.getGame().isRunning())
		{
			return;
		}
		
		if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			if (((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof Player)
			{
				p2 = (Player) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager();
			}
			if (((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof Projectile && ((Projectile) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager()).getShooter() instanceof Player)
			{
				p2 = (Player) ((Projectile) ((EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager()).getShooter();
			}
		}
		
		t = lobby.getPlayerTeam(p);
		if (p2 != null) t2 = lobby.getPlayerTeam(p2);
		
		if (p2 == null) //Player died from the environment
		{
			lobby.broadcast(ChatManager.bracket1_ + t.getColor() + p.getName() + ChatColor.WHITE + " has died and is " + ChatColor.RED + "ELIMINATED" + ChatManager.bracket2_, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		}
		else //Player dies from another player
		{
			lobby.broadcast(ChatManager.bracket1_ + t.getColor() + p.getName() + ChatColor.WHITE + " was killed by " + t2.getColor() + p2.getName() + ChatColor.WHITE + " and is " + ChatColor.RED + "ELIMINATED" + ChatManager.bracket2_, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
		}
		
		e.setDeathMessage(null);
		
		//---------------
		lobby.setPlayerTeam(p.getName(), Team.SPECTATORS);
		
		preysize = lobby.getPlayers(Team.PREY).size();
		huntersize = lobby.getPlayers(Team.HUNTERS).size();
		
		if (preysize == 0 || huntersize == 0)
		{
			if (preysize == 0)
			{
				// Hunter win-specific action
				
				lobby.broadcast("The HUNTERS have won!");
			}
			else
			{
				// Prey win-specific action
				
				lobby.broadcast("The PREY have won!");
			}
			
			// Team-independent win action
			
			for (Player player : lobby.getPlayers())
			{
				player.teleport(lobby.getSpawnLocation());
				lobby.setPlayerTeam(player.getName(), Team.NONE);
			}
		}
		else
		{
			// Just-another-death action
			
			p.teleport(lobby.getSpawnLocation());
			resetPlayer(p);
			lobby.setPlayerTeam(p.getName(), Team.SPECTATORS);
		}
		
	}

	/**
	 * Handles player respawns. Teleports them to their lobby's spawn
	 * point.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		e.getPlayer().teleport(lobby.getSpawnLocation());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		
		GameLobby lobby;
		
		lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
			return;
		
		if (e.getPlayer().getWorld() != lobby.getWorld())
			return;
		
		
		
		
		// TODO Check if the player is not where they're supposed to be
		
		// TODO Check if the player's prey finder should be cancelled
	}

	/**
	 * Handles player interaction events. Cancels events not meant to
	 * occur because the player is a spectator or if other conditions
	 * are met.
	 * Updated 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
		{
			return;
		}
		else
		{
			Team team = lobby.getPlayerTeam(e.getPlayer());
			if (lobby.getGame().isRunning() && team != Team.HUNTERS && team != Team.PREY)
			{
				e.setCancelled(true);
			}
		}
			
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e)
	{
		
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent e)
	{
		
	}

	/**
	 * Handles players changing game modes. Cancels the event if a the
	 * player is a hunter or a prey while a game is running.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent e)
	{
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby.getGame().isRunning() &&
				(lobby.getPlayerTeam(e.getPlayer()) == Team.HUNTERS || lobby.getPlayerTeam(e.getPlayer()) == Team.PREY))
		{
			e.setCancelled(true);
		}
	}

	/**
	 * Handles player pickup item events. Stops spectators from picking
	 * up items when they shouldn't.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent e)
	{
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
		{
			return;
		}
		else
		{
			Team team = lobby.getPlayerTeam(e.getPlayer());
			if (lobby.getGame().isRunning() && team != Team.HUNTERS && team != Team.PREY)
			{
				e.setCancelled(true);
			}
		}
	}

	/**
	 * Handles player drop item events. Stops spectators from droping
	 * items onto the playing field.
	 * Updated 1.3
	 * @param e
	 */
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e)
	{
		GameLobby lobby = Manhunt.getLobby(e.getPlayer());
		
		if (lobby == null)
		{
			return;
		}
		else
		{
			Team team = lobby.getPlayerTeam(e.getPlayer());
			if (lobby.getGame().isRunning() && team != Team.HUNTERS && team != Team.PREY)
			{
				e.setCancelled(true);
			}
		}
	}

	/**
	 * Handles inventory click events. This prevents players from taking
	 * their team hats off during a game.
	 * Updated: 1.3
	 * @param e
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e)
	{
		GameLobby lobby;
		Team team;
		
		lobby = Manhunt.getLobby((Player) e.getWhoClicked());
		
		if (lobby == null)
		{
			return;
		}
		else
		{
			team = lobby.getPlayerTeam((Player) e.getWhoClicked());
			if (lobby.getGame().isRunning() && lobby.getSettings().TEAM_HATS.getValue() && (team == Team.HUNTERS || team == Team.PREY))
			{
				e.setCancelled(true);
			}
		}
	}
	
	
	/**
	 * Resets a player's inventory, stats, and location so that they spawn
	 * with a clean slate.
	 * Updated 1.3
	 * @param p
	 */
	private void resetPlayer(Player p)
	{
		p.getInventory().clear();
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(20f);
		p.setLevel(0);
		p.setExp(0f);
		p.setExhaustion(0f);
	}

}