package com.deaboy.manhunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.commands.CommandHelper;
import com.deaboy.manhunt.finder.FinderManager;
import com.deaboy.manhunt.loadouts.LoadoutManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.HubLobby;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.ManhuntSettings;
import com.deaboy.manhunt.timeouts.TimeoutManager;

public class Manhunt
{
	
	//---------------- Constants ----------------//
	/** The extension to use for property files. */
	public static final String extension_properties = ".properties";
	/** The extension to use for loadout files. */
	public static final String extension_loadouts = ".dat";
	/** The extension to use for the world data files. */
	public static final String extension_worldprops = ".dat";
	
	/** The filename to use for the basic config property file. */
	public static final String filename_settings = "config";
	/** The filename to use for manhunt world data files. */
	public static final String filename_worldprops = "world";
	
	/** The directory name for the server's plugins. */
	public static final String dirname_plugins = "plugins";
	/** The directory name for this plugin, nested within <CODE>dirname_plugins</CODE>. */
	public static final String dirname_root = "Manhunt";
	/** The directory name for this plugin's loadouts, nested within <CODE>dirname_main</CODE>. */
	public static final String dirname_loadouts = "loadouts";
	/** The directory name for the world data files stored in each Minecraft world's folder. */
	public static final String dirname_world = "Manhunt";
	/** The directory name for the lobby data files. These files will hold each lobby's individual settings. */
	public static final String dirname_lobbies = "lobbies";
	
	/** The directory path for this plugin's root directory, relative to the server's running directory. */
	public static final String path_rootdir = dirname_plugins + "/" + dirname_root;
	/** The path for this plugin's main config file, relative to the server's running directory. */
	public static final String path_settings = path_rootdir + "/" + filename_settings + extension_properties;
	/** The directory path for the loadouts folder, relative to the server's running directory. */
	public static final String path_loadouts = path_rootdir + "/" + dirname_loadouts;
	/** The directory path for the lobbies folder, relative to the server's running directory. */
	public static final String path_lobbies = path_rootdir + "/" + dirname_lobbies;
	
	
	
	
	//---------------- Local variables -----------------//
	
	private static	Manhunt				instance;
	private 		ManhuntSettings		settings;
	private 		TimeoutManager		timeouts;
	private 		FinderManager		finders;
	private 		CommandHelper		command_helper;
	private 		LoadoutManager		loadouts;
	
	private 		HashMap<Long, Lobby>	lobbies;
	private			HashMap<String, Long>	players;
	private 		HashMap<String, World>	worlds;
	
	
	
	//---------------- Constructor ----------------//
	public Manhunt()
	{
		Manhunt.instance =		this;
		this.settings =			new ManhuntSettings(path_settings);
		this.timeouts =			new TimeoutManager();
		this.finders =			new FinderManager();
		this.command_helper =	new CommandHelper();
		this.loadouts =			new LoadoutManager();
		
		this.lobbies =			new HashMap<Long, Lobby>();
		this.worlds =			new HashMap<String, World>();
		this.players =			new HashMap<String, Long>();
	}
	
	
	//-------------------- Getters --------------------//
	public static Lobby getDefaultLobby()
	{
		return getLobby(Bukkit.getWorld(getSettings().DEFAULT_WORLD.getValue()));
	}
	
	public static ManhuntSettings getSettings()
	{
		return getInstance().settings;
	}
	
	public static Lobby getLobby(long id)
	{
		if (getInstance().lobbies.containsKey(id))
			return getInstance().lobbies.get(id);
		else
			return null;
		
	}
	
	public static Lobby getLobby(String name)
	{
		for (Lobby lobby : getInstance().lobbies.values())
		{
			if (lobby.getName().equalsIgnoreCase(name))
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static Lobby getLobby(org.bukkit.World world)
	{
		for (Lobby lobby : getInstance().lobbies.values())
		{
			if (lobby.getWorlds().contains(world))
			{
				return lobby;
			}
		}
		return null;
		
	}
	
	public static List<Lobby> getLobbies()
	{
		return new ArrayList<Lobby>(getInstance().lobbies.values());
	}
	
	public static Lobby getPlayerLobby(Player player)
	{
		if (player == null)
			return null;
		else
			return getPlayerLobby(player.getName());
	}
	
	public static Lobby getPlayerLobby(String name)
	{
		if (getInstance().players.containsKey(name))
			return getLobby(getInstance().players.get(name));
		else
			return null;
	}
	
	public static List<World> getWorlds()
	{
		return new ArrayList<World>(getInstance().worlds.values());
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
		if (getInstance().worlds.containsKey(name))
			return getInstance().worlds.get(name);
		else
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
	
	
	
	//---------------- Private Methods ----------------//
	private static Manhunt getInstance()
	{
		return instance;
	}
	
	private void removePlayer(String name)
	{
		timeouts.stopTimeout(name);
		players.remove(name);
	}
	
	private long getNextLobbyId()
	{
		long i;
		for (i = 0; lobbies.containsKey(i); i++);
		return i;
	}
	
	/**
	 * Resets a player's food, hunger, inventory, game mode, etc.
	 * @param p
	 */
	private static void resetPlayer(Player p)
	{
		p.getInventory().clear();
		p.setHealth(p.getMaxHealth());
		p.setFoodLevel(20);
		p.setSaturation(20f);
		p.setExhaustion(20f);
		p.getEnderChest().clear();
		p.setBedSpawnLocation(null);
		p.setCompassTarget(p.getLocation());
		p.setExp(0f);
		p.setLevel(0);
		p.setFlying(false);
		p.setRemainingAir(10);
		p.setTotalExperience(0);
	}
	
	
	
	//---------------- Public Interface Methods ----------------//
	public static Lobby createLobby(String name, LobbyType type, org.bukkit.World world)
	{
		Lobby lobby = null;
		World manhunt_world = getWorld(world);
		long id = getInstance().getNextLobbyId();
		
		lobby = getLobby(name);
		if (lobby != null)
			return null;
		
		lobby = getLobby(world);
		if (lobby != null)
			return null;
		
		switch (type)
		{
		case HUB:
			lobby = new HubLobby(id, name, manhunt_world);
			break;
		case GAME:
			lobby = new GameLobby(id, name, manhunt_world);
			break;
		default:
			return null;
		}
		
		return lobby;
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
	
	public static void registerPlayer(Player p)
	{
		
		Lobby lobby = getDefaultLobby();
		
		p.teleport(lobby.getSpawn().getRandomLocation());
		resetPlayer(p);
		
		getInstance().players.put(p.getName(), lobby.getId());
		
		if (lobby.addPlayer(p.getName()))
		{
			lobby.broadcast(p.getName() + " has joined.");
		}
		
	}
	
	public static void changePlayerLobby(Player p, long lobby_id)
	{
		
		Lobby old_lobby = getPlayerLobby(p);
		Lobby lobby = Manhunt.getLobby(lobby_id);
		
		if (lobby == null) 
			throw new IllegalArgumentException("Invalid lobby id.");
		
		p.teleport(lobby.getSpawn().getRandomLocation());
		resetPlayer(p);
		
		getInstance().players.put(p.getName(), lobby.getId());
		
		if (lobby.addPlayer(p.getName()))
		{
			if (old_lobby != null)
				old_lobby.broadcast(p.getName() + " has left.");
			lobby.broadcast(p.getName() + " has joined.");
		}
		
	}
	
	public static void bootPlayer(Player p)
	{
		p.kickPlayer("You've been booted from the game.");
		
		bootPlayer(p.getName());
	}
	
	public static void bootPlayer(String name)
	{
		// TODO Kick the player from his lobby and from the main list
		Lobby lobby = getPlayerLobby(name);
		
		if (lobby != null)
			lobby.removePlayer(name);
		
		getInstance().removePlayer(name);
	}
	
	
	
	public static void log(String message)
	{
		log(Level.INFO, message);
	}
	
	public static void log(Level level, String message)
	{
		Bukkit.getLogger().log(level, "[Manhunt]  " + message);
	}
	
	public static void log(Exception e)
	{
		if (getSettings().DISPLAY_ERRORS.getValue())
		{
			log(Level.SEVERE, e.toString());
			log(Level.SEVERE, e.getMessage());
		}
		else
		{
			log(Level.SEVERE, e.toString());
		}
		
		
		// TODO Get some sort of separate logger set up.
	}
	
	
	
	
	
	
	
}
