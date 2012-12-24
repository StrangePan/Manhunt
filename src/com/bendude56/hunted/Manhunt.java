package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bendude56.hunted.finder.FinderManager;
import com.bendude56.hunted.lobby.GameLobby;
import com.bendude56.hunted.lobby.MainLobby;
import com.bendude56.hunted.lobby.ManhuntGameLobby;
import com.bendude56.hunted.settings.ManhuntSettings;
import com.bendude56.hunted.timeouts.TimeoutManager;

public class Manhunt
{
	
	//-------- Constants --------//
	
	public static final String extension_properties = ".properties";
	public static final String extension_loadout = ".dat";
	
	public static final String filename_settings = "config"; 
	
	public static final String dirname_plugins = "plugins";
	public static final String dirname_main = "Manhunt";
	public static final String dirname_loadouts = "loadouts";
	public static final String dirname_world = "Manhunt";
	
	public static final String path_maindir = dirname_plugins + "/" + dirname_main;
	public static final String path_settings = path_maindir + "/" + filename_settings + extension_properties;
	public static final String path_loadouts = path_maindir + "/" + dirname_loadouts;
	
	
	//-------- Local variables ---------//
	
	private static Manhunt instance;
	private MainLobby mainlobby;
	private List<GameLobby> lobbies;
	private ManhuntSettings settings;
	private TimeoutManager timeouts;
	private FinderManager finders;
	
	
	//-------- Constructor --------//
	public Manhunt()
	{
		instance = this;
		this.lobbies = new ArrayList<GameLobby>();
		this.settings = new ManhuntSettings(path_settings);
		this.timeouts = new TimeoutManager();
		this.finders = new FinderManager();
	}
	
	
	//---------------- Public static methods ----------------//
	//------------ Getters ------------//
	public static MainLobby getMainLobby()
	{
		return getInstance().mainlobby;
	}
	
	public static ManhuntSettings getSettings()
	{
		return getInstance().settings;
	}
	
	public static GameLobby getLobby(long id)
	{
		for (GameLobby lobby: getInstance().lobbies)
		{
			if (lobby.getId() == id)
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static GameLobby getLobby(String name)
	{
		for (GameLobby lobby : getInstance().lobbies)
		{
			if (lobby.getName().equalsIgnoreCase(name))
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static GameLobby getLobby(World world)
	{
		for (GameLobby lobby : getInstance().lobbies)
		{
			if (lobby.getWorlds().contains(world))
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static GameLobby getLobby(Player player)
	{
		for (GameLobby lobby : getInstance().lobbies)
		{
			if (lobby.getPlayers().contains(player))
			{
				return lobby;
			}
		}
		return null;
	}
	
	public static TimeoutManager getTimeoutManager()
	{
		return getInstance().timeouts;
	}
	
	public static FinderManager getFinders()
	{
		return getInstance().finders;
	}
	
	
	
	//-------- Private Methods (none) --------//
	private static Manhunt getInstance()
	{
		return instance;
	}
	
	
	
	//-------- Public Interface Methods --------//
	public static GameLobby createLobby(String name, World world)
	{
		GameLobby lobby = null;
		
		lobby = getLobby(name);
		if (lobby != null)
		{
			return null;
		}
		lobby = getLobby(world);
		if (lobby != null)
		{
			return null;
		}
		
		lobby = new ManhuntGameLobby(name, world);
		
		getInstance().lobbies.add(new ManhuntGameLobby(name, world));
		
		return lobby;
	}
	
	public static void destroyLobby(GameLobby lobby)
	{
		if (getInstance().lobbies.contains(lobby))
		{
			getInstance().lobbies.remove(lobby);
		}
		
	}

	/**
	 * Initializes a new Timeout object that will keep a timer
	 * and disqualify a player if they do not log in on time.
	 * @param player The player to start a timeout for.
	 * @param lobby The lobby that the player is in.
	 * @param time The the number of seconds until the player is disqualified.
	 */
	public static void startTimeout(Player player, GameLobby lobby, long time)
	{
		// TODO Instantiate a new Timeout object for the given player in the
		// given lobby, for the given time.
	}
	
	/**
	 * Initializes a new Finder object for the given player.
	 * This object will periodically check its validity
	 * and will cancel if it is no longer valid. Conditions for
	 * cancellation include
	 *    the player moving, or
	 *    the Player no longer holding a compass.
	 * 
	 * Once the charge time has passed, the Finder will cause
	 * the player's compass to point towards the nearest prey's
	 * last known location.
	 * 
	 * After a litle while longer, the finder will self-destruct,
	 * allowing a new finder to be created for the player.
	 * 
	 * 
	 * @param player The player to start a finder for.
	 */
	public static void startFinder(Player player)
	{
		// TODO Instantiate a new finder object for the given player.
	}
	
	
	
}
