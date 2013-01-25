package com.deaboy.manhunt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.commands.CommandHelper;
import com.deaboy.manhunt.finder.FinderManager;
import com.deaboy.manhunt.loadouts.LoadoutManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.MainLobby;
import com.deaboy.manhunt.lobby.ManhuntGameLobby;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.ManhuntSettings;
import com.deaboy.manhunt.timeouts.TimeoutManager;

public class Manhunt
{
	
	//-------- Constants --------//
	/** The extension to use for property files */
	public static final String extension_properties = ".properties";
	/** The extension to use for loadout files */
	public static final String extension_loadouts = ".dat";
	/** The extension to use for the world data files */
	public static final String extension_worldprops = ".dat";
	
	/** The filename to use for the basic config property file */
	public static final String filename_settings = "config";
	/** The filename to use for manhunt world data files. */
	public static final String filename_worldprops = "world";
	
	/** The directory name for the server's plugins */
	public static final String dirname_plugins = "plugins";
	/** The directory name for this plugin, nested within <CODE>dirname_plugins</CODE> */
	public static final String dirname_root = "Manhunt";
	/** The directory name for this plugin's loadouts, nested within <CODE>dirname_main</CODE> */
	public static final String dirname_loadouts = "loadouts";
	/** The directory name for the world data files stored in each Minecraft world's folder. */
	public static final String dirname_world = "Manhunt";
	/** The directory name for the lobby data files. These files will hold each lobby's individual settings. */
	public static final String dirname_lobbies = "lobbies";
	
	/** The directory path for this plugin's root directory, relative to the server's running directory */
	public static final String path_rootdir = dirname_plugins + "/" + dirname_root;
	/** The path for this plugin's main config file, relative to the server's running directory */
	public static final String path_settings = path_rootdir + "/" + filename_settings + extension_properties;
	/** The directory path for the loadouts folder, relative to the server's running directory */
	public static final String path_loadouts = path_rootdir + "/" + dirname_loadouts;
	/** The directory path for the lobbies folder, relative to the server's running directory */
	public static final String path_lobbies = path_rootdir + "/" + dirname_lobbies;
	
	
	
	
	//-------- Local variables ---------//
	
	private static	Manhunt				instance;
	private 		MainLobby			mainlobby;
	private 		List<GameLobby>		lobbies;
	private 		List<World>			worlds;
	private 		ManhuntSettings		settings;
	private 		TimeoutManager		timeouts;
	private 		FinderManager		finders;
	private 		CommandHelper		command_helper;
	private 		LoadoutManager		loadouts;
	
	
	
	//-------- Constructor --------//
	public Manhunt()
	{
		instance = this;
		this.lobbies =			new ArrayList<GameLobby>();
		this.worlds =			new ArrayList<World>();
		this.settings =			new ManhuntSettings(path_settings);
		this.timeouts =			new TimeoutManager();
		this.finders =			new FinderManager();
		this.command_helper =	new CommandHelper();
		this.loadouts =			new LoadoutManager();
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
	
	public static GameLobby getLobby(org.bukkit.World world)
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
	
	public static List<World> getWorlds()
	{
		return getInstance().worlds;
	}
	
	public static World getWorld(org.bukkit.World world)
	{
		for (World w : getWorlds())
		{
			if (w.getWorld() == world)
				return w;
		}
		return null;
	}
	
	public static World getWorld(String name)
	{
		for (World w : getWorlds())
		{
			if (w.getWorld().getName().equals(name))
				return w;
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
	
	public static CommandHelper getCommandHelper()
	{
		return getInstance().command_helper;
	}
	
	public static LoadoutManager getLoadouts()
	{
		return getInstance().loadouts;
	}
	
	
	
	//-------- Private Methods (none) --------//
	private static Manhunt getInstance()
	{
		return instance;
	}
	
	
	
	//-------- Public Interface Methods --------//
	public static GameLobby createLobby(String name, org.bukkit.World world)
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
		// TODO Initiate a new Timeout object for the given player in the
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
	
	public static void log(String message)
	{
		log(Level.INFO, message);
	}
	
	public static void log(Level level, String message)
	{
		Bukkit.getLogger().log(level, "[Manhunt]  " + message);
	}
	
}
