package com.deaboy.manhunt.lobby;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntPlugin;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.settings.ManhuntGameLobbySettings;

public class ManhuntGameLobby extends GameLobby
{
	//////////////// PROPERTIES ////////////////
	private ManhuntGameLobbySettings settings;
	private long time_startgame;
	private int intermission_stage;
	private int intermission_setting;
	private final int schedule;
	
	
	//////////////// CONSTRUCTORS /////////////////
	public ManhuntGameLobby(long id, File file)
	{
		super(id, file);
		this.settings = new ManhuntGameLobbySettings();
		this.time_startgame = -1L;
		this.intermission_stage = -1;
		this.intermission_setting = getSettings().TIME_INTERMISSION.getValue();
		this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 10);
	}
	public ManhuntGameLobby(long id, File file, String name, Location loc)
	{
		super(id, file, name, loc);
		this.settings = new ManhuntGameLobbySettings();
		this.time_startgame = -1L;
		this.intermission_stage = -1;
		this.intermission_setting = getSettings().TIME_INTERMISSION.getValue();
		this.schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(ManhuntPlugin.getInstance(), new Runnable()
		{
			public void run()
			{
				onTick();
			}
		}, 0, 10);
	}
	
	//////////////// PUBLIC FUNCTIONS ////////////////
	//---------------- INTERFACE ----------------//
	@Override
	public boolean playerJoinLobby(Player player)
	{
		if (getSettings().MAX_PLAYERS.getValue() >= 0 && this.getPlayerNames().size() >= getMaxPlayers())
		{
			return false;
		}
		
		getGame().playerJoinLobby(player);
		
		if (!containsPlayer(player))
		{
			addPlayer(player, Team.STANDBY);
			player.teleport(ManhuntUtil.safeTeleport(this.getRandomSpawnLocation()));
			ManhuntUtil.resetPlayer(player);
		}
		
		if (gameIsRunning() && (getPlayerTeam(player) == Team.HUNTERS || getPlayerTeam(player) == Team.PREY))
		{
			broadcast(getPlayerTeam(player).getColor() + player.getName() + ChatManager.color + " is back in the game!");
			Manhunt.stopTimeout(player);
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
		getGame().playerLeaveLobby(name);
		
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
		getGame().playerLeaveServer(player);
		
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
		getGame().playerForfeit(name);
		
		if (gameIsRunning() && (getPlayerTeam(name) == Team.HUNTERS || getPlayerTeam(name) == Team.PREY))
		{
			broadcast(ChatManager.bracket1_ + getPlayerTeam(name).getColor() + name + ChatColor.DARK_RED + " has forfeit the game!" + ChatManager.bracket2_);
			if (Bukkit.getPlayerExact(name) != null)
			{
				Bukkit.getPlayerExact(name).teleport(ManhuntUtil.safeTeleport(getRandomSpawnLocation()));
				ManhuntUtil.resetPlayer(Bukkit.getPlayerExact(name));
			}
			else
			{
				this.removePlayer(name);
			}
			Manhunt.getTimeoutManager().cancelTimeout(name);
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
		if (!getGame().playerChangeTeam(name, team))
		{
			return false;
		}
		
		if (containsPlayer(name) && team != null && getPlayerTeam(name) != team)
		{
			this.setPlayerTeam(name, team);
			if (!gameIsRunning())
			{
				broadcast(team.getColor() + name + ChatManager.color + " joined team " + team.getColor() + team.getName(false));
			}
		}
		return true;
	}
	@Override
	public boolean registerMap(Map map)
	{
		if (!this.containsMap(map))
		{
			this.addMap(map);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public boolean unregisterMap(Map map)
	{
		if (map == null || gameIsRunning() && getCurrentMap() == map || !containsMap(map))
		{
			return false;
		}
		else
		{
			removeMap(map);
			return true;
		}
	}
	
	
	//---------------- GAMES ----------------//
	@Override
	public long getGameTicksRemaining()
	{
		if (this.getGame() != null)
		{
			return this.getGame().getGameTicksRemaining();
		}
		else
		{
			return -1L;
		}
	}
	public long getGameStartTime()
	{
		return this.time_startgame;
	}
	@Override
	public boolean startGame()
	{
		if (super.startGame())
		{
			time_startgame = -1L;
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	protected void stopGame()
	{
		super.stopGame();
		resetIntermissionTime();
	}
	
	
	//---------------- SETTINGS ----------------//
	@Override
	public ManhuntGameLobbySettings getSettings()
	{
		return this.settings;
	}
	
	
	//---------------- MISCELLANEOUS ----------------//
	@Override
	public void resetIntermissionTime()
	{
		this.intermission_setting = getSettings().TIME_INTERMISSION.getValue();
		if (getSettings().TIME_INTERMISSION.getValue() > 0)
		{
			this.time_startgame = new Date().getTime() + getSettings().TIME_INTERMISSION.getValue() * 60 * 1000;
			this.intermission_stage = getSettings().TIME_INTERMISSION.getValue();
		}
		else
		{
			this.time_startgame = -1L;
			this.intermission_stage = -1;
		}
	}
	private void onTick()
	{
		long time;
		
		if (gameIsRunning())
			return;
		
		// Deal with the intermission
		if (getSettings().TIME_INTERMISSION.getValue() > 0 && !gameIsRunning())
		{
			time = new Date().getTime();
			
			if (intermission_setting != getSettings().TIME_INTERMISSION.getValue() || time_startgame == -1L)
			{
				resetIntermissionTime();
				broadcast(ChatManager.leftborder + intermission_stage-- + " minutes until the game starts!");
			}
			else
			{
				if (time_startgame == -2L)
				{
					if (getOnlinePlayerNames().size() >= getSettings().MIN_PLAYERS.getValue())
					{
						time_startgame = time + 15000; // Start game in 15 seconds
						broadcast(ChatManager.leftborder + "The game will start in 15 seconds.");
					}
				}
				else if (time > time_startgame)
				{
					int hunters, prey, standby;
					
					hunters = getOnlinePlayerNames(Team.HUNTERS).size();
					prey = getOnlinePlayerNames(Team.PREY).size();
					standby = getOnlinePlayerNames(Team.STANDBY).size();
					
					int needed = getSettings().MIN_PLAYERS.getValue() - hunters - prey - standby;
					
					if (needed > 0)
					{
						// Delay the intermission if not enough players
						broadcast(ChatManager.leftborder + ChatColor.RED + "Unable to start game.");
						broadcast(ChatManager.leftborder + "  Not enough players to start. " + needed + " more player" + (needed > 1 ? "s" : "") + " are needed.");
						time_startgame = -2L;
					}
					else if (standby == 0 && (hunters == 0 || prey == 0))
					{
						broadcast(ChatManager.leftborder + ChatColor.RED + "Unable to start game.");
						broadcast(ChatManager.leftborder + "  Could not find a " + (hunters == 0 ? Team.HUNTERS.getColor() + Team.HUNTERS.getName(false) : Team.PREY.getColor() + Team.PREY.getName(false)) + ChatColor.GOLD + " to start the game!");
						time_startgame = -2L;
					}
					else
					{
						// Start the game
						if (!startGame())
						{
							Manhunt.log(Level.WARNING, "Error automatically starting game in lobby " + getName());
							broadcast(ChatManager.leftborder + ChatColor.RED + "Unable to start game.");
							broadcast(ChatManager.leftborder + "  There was an error attempting to start the game.");
							time_startgame = time + 60*1000; // Delay game by 1 minute if it fails to start
						}
						else
						{
							broadcast(ChatManager.leftborder + "The game has started!");
						}
					}
				}
				else if ((time_startgame - time) / (60*1000) < intermission_stage)
				{
					if (intermission_stage < 5 || intermission_stage % 5 == 0)
					{
						broadcast(ChatManager.leftborder + intermission_stage + " minutes until the game starts!");
					}
					intermission_stage--;
				}
			}
		}
		
		// If intermission is turned off
		else if (getSettings().TIME_INTERMISSION.getValue() <= 0 && time_startgame != -1L)
		{
			time_startgame = -1L;
		}
	}
	
	
	//---------------- CLOSE ----------------//
	@Override
	public void close()
	{
		Bukkit.getScheduler().cancelTask(schedule);
		super.close();
	}
	
	
}
