package com.deaboy.manhunt.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.settings.GameLobbySettings;

public class ManhuntGameLobby extends GameLobby
{
	//////////////// PROPERTIES ////////////////
	private GameLobbySettings settings;
	
	
	//////////////// CONSTRUCTORS /////////////////
	public ManhuntGameLobby(String name, long id, Location loc)
	{
		super(id, name, loc);
		this.settings = new GameLobbySettings();
	}
	
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- INTERFACE ----------------//
	@Override
	public boolean playerJoinLobby(Player player)
	{
		if (!containsPlayer(player))
		{
			addPlayer(player, Team.STANDBY);
			player.teleport(this.getRandomSpawnLocation());
			ManhuntUtil.resetPlayer(player);
		}
		
		if (gameIsRunning() && (getPlayerTeam(player) == Team.HUNTERS || getPlayerTeam(player) == Team.PREY))
		{
			broadcast(getPlayerTeam(player).getColor() + player.getName() + ChatManager.color + " is back in the game!");
		}
		else if (gameIsRunning())
		{
			broadcast(Team.STANDBY.getColor() + player.getName() + " has joined the lobby", Team.SPECTATORS, Team.STANDBY);
		}
		else
		{
			broadcast(Team.STANDBY.getColor() + player.getName() + " has joined the lobby");
		}
		return true;
	}
	@Override
	public boolean playerLeaveLobby(String name)
	{
		if (gameIsRunning() && (getPlayerTeam(name) == Team.HUNTERS || getPlayerTeam(name) == Team.PREY))
		{
			playerForfeit(name);
			return true;
		}
		else if (Bukkit.getOfflinePlayer(name).isOnline())
		{
			if (gameIsRunning())
			{
				broadcast((getPlayerTeam(name) == null ? ChatColor.YELLOW : getPlayerTeam(name).getColor()) + name + " has left the lobby.", Team.STANDBY, Team.SPECTATORS);
			}
			else
			{
				broadcast((getPlayerTeam(name) == null ? ChatColor.YELLOW : getPlayerTeam(name).getColor()) + name + " has left the lobby.");
			}
		}
		
		removePlayer(name);
		return true;
	}
	@Override
	public boolean playerLeaveServer(Player player)
	{
		if (gameIsRunning() && (getPlayerTeam(player.getName()) == Team.HUNTERS || getPlayerTeam(player.getName()) == Team.PREY))
		{
			broadcast(ChatManager.bracket1_ + getPlayerTeam(player).getColor() + player.getName() + ChatManager.color + " has disconnected" + (getSettings().OFFLINE_TIMEOUT.getValue() > 0 ? ". Forfeit in " + getSettings().OFFLINE_TIMEOUT.getValue() + " seconds." : "") + ChatManager.bracket2_);
			if (getSettings().OFFLINE_TIMEOUT.getValue() == 0)
			{
				playerForfeit(player.getName());
			}
			else if (getSettings().OFFLINE_TIMEOUT.getValue() > 0)
			{
				Manhunt.startTimeout(player);
			}
		}
		else if (gameIsRunning())
		{
			broadcast(getPlayerTeam(player).getColor() + player.getName() + ChatManager.color + " left the lobby.", Team.SPECTATORS, Team.STANDBY);
		}
		else
		{
			broadcast(getPlayerTeam(player).getColor() + player.getName() + ChatManager.color + " left the lobby.");
		}
		return true;
	}
	@Override
	public boolean playerForfeit(String name)
	{
		if (gameIsRunning() && (getPlayerTeam(name) == Team.HUNTERS || getPlayerTeam(name) == Team.PREY))
		{
			broadcast(ChatManager.bracket1_ + getPlayerTeam(name).getColor() + name + ChatColor.DARK_RED + " has forfeit the game!" + ChatManager.bracket2_);
			if (Bukkit.getPlayerExact(name) != null)
			{
				Bukkit.getPlayerExact(name).teleport(getRandomSpawnLocation());
				ManhuntUtil.resetPlayer(Bukkit.getPlayerExact(name));
			}
			else
			{
				this.removePlayer(name);
			}
			getGame().forfeitPlayer(name);
		}
		else if (containsPlayer(name))
		{
			if (gameIsRunning())
			{
				broadcast((getPlayerTeam(name) != null ? getPlayerTeam(name).getColor() : ChatManager.color) + name + ChatManager.color + " left the lobby", Team.SPECTATORS, Team.STANDBY);
			}
			else
			{
				broadcast((getPlayerTeam(name) != null ? getPlayerTeam(name).getColor() : ChatManager.color) + name + ChatManager.color + " left the lobby");
			}
		}
		return true;
	}
	@Override
	public boolean playerChangeTeam(String name, Team team)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean registerMap(Map map)
	{
		// TODO Write this
		return true;
	}
	@Override
	public boolean unregisterMap(Map map)
	{
		// TODO Write this
		return true;
	}
	
	
	//---------------- GAMES ----------------//
	@Override
	public boolean startGame()
	{
		
		return true;
	}
	@Override
	public boolean endGame()
	{
		
		stopGame();
		return true;
	}
	@Override
	public boolean cancelGame()
	{
		
		stopGame();
		return true;
	}
	private void stopGame()
	{
		
	}
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public GameLobbySettings getSettings()
	{
		return this.settings;
	}
	
	
	//---------------- FILES ----------------//
	protected void initializeSettings()
	{
		
	}
	
	
	//---------------- CLOSE ----------------//
	@Override
	public void close()
	{
		super.close();
	}
}
