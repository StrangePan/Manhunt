package com.deaboy.manhunt;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.commands.CommandSwitchboard;
import com.deaboy.manhunt.commands.CommandUtil;
import com.deaboy.manhunt.finder.Finder;
import com.deaboy.manhunt.finder.FinderManager;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.game.GameType;
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.loadouts.LoadoutManager;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.ManhuntWorld;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Selection;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.ManhuntSettings;
import com.deaboy.manhunt.timeouts.TimeoutManager;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Manhunt implements Closeable, Listener
{
	//---------------- Constants ----------------//
	/** The extension to use for property files. */
	public static final String extension_properties = ".prop";
	/** The extension to use for lobby files. */
	public static final String extension_lobbies = ".mhl";
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
	private 		CommandUtil			command_util;
	private 		LoadoutManager		loadouts;
	private			long				default_lobby;
	
	private 		HashMap<Long, Lobby>	lobbies;
	private			HashMap<String, Long>	player_lobbies;
	private			HashMap<String, ManhuntMode> player_modes;
	private			HashMap<String, String> player_maps;
	private			HashMap<String, World>	worlds;
	private			HashMap<Long, GameType>	games;
	private			HashMap<String, Selection> selections;
	
	private static	WorldEdit			worldedit;
	private static	WorldEditPlugin		worldeditplugin;
	
	
	
	//---------------- Constructor ----------------//
	public Manhunt()
	{
		Manhunt.instance =		this;
		this.settings =			new ManhuntSettings(path_settings);
		settings.load();
		settings.save();
		
		if (Material.getMaterial(settings.FINDER_ITEM.getValue()) == null)
			settings.FINDER_ITEM.resetToDefault();
		
		this.timeouts =			new TimeoutManager();
		this.finders =			new FinderManager();
		this.command_util =		new CommandUtil();
		this.loadouts =			new LoadoutManager();
		
		this.lobbies =			new HashMap<Long, Lobby>();
		this.worlds =			new HashMap<String, World>();
		this.player_lobbies =	new HashMap<String, Long>();
		this.player_modes =		new HashMap<String, ManhuntMode>();
		this.player_maps =		new HashMap<String, String>();
		this.games =			new HashMap<Long, GameType>();
		this.selections =		new HashMap<String, Selection>();
		
		worldedit = null;
		worldeditplugin = null;
		
		for (Player p : Bukkit.getOnlinePlayers())
			selections.put(p.getName(), new Selection(p));
		
		//////// Set up worlds ////////
		for (org.bukkit.World world : Bukkit.getWorlds())
		{
			registerWorld(world);
		}
		
		if (settings.HANDLE_WORLDS.getValue())
		{
			for (String wname : settings.WORLDS.getValue())
			{
				if (worlds.containsKey(wname))
					continue;
				
				if (Bukkit.getWorld(wname) != null)
					continue;
				
				registerWorld(Bukkit.createWorld(WorldCreator.name(wname)));
			}
		}
		
		
		//////// Register game types
		registerGameType(ManhuntGame.class, "Manhunt");
		// registerGameType(JuggernautGame.class, "Juggerhunt");
		// registerGameType(GhostGame.class, "Manhaunt");
		
		
		
		///////// Set up the lobbies ////////
		loadLobbies();
		
		Lobby lobby = getLobby(settings.DEFAULT_LOBBY.getValue());
		
		if (lobby == null)
		{
			if (lobbies.isEmpty())
				lobby = null;
			else
			{
				for (Lobby l : getLobbies(Bukkit.getWorlds().get(0)))
				{
					if (l.getType() == LobbyType.HUB)
					{
						lobby = l;
						break;
					}
				}
				if (lobby == null)
				{
					for (Lobby l : lobbies.values())
					{
						if (l.getType() == LobbyType.HUB)
						{
							lobby = l;
							break;
						}
					}
				}
				if (lobby == null)
				{
					if (!getLobbies(Bukkit.getWorlds().get(0)).isEmpty())
						lobby = getLobbies(Bukkit.getWorlds().get(0)).get(0);
				}
				if (lobby == null)
				{
					lobby = lobbies.get(0);
				}
			}
		}
		
		if (lobby == null)
			lobby = createLobby("default", LobbyType.GAME, Bukkit.getWorlds().get(0).getSpawnLocation());
		
		setDefaultLobby(lobby);
		
		
		//////// Start up the command handlers ////////
		new CommandSwitchboard();
		
		
		//////// Register events ////////
		Bukkit.getPluginManager().registerEvents(this, ManhuntPlugin.getInstance());
		
		
		//////// Handle online players ////////
		for (Player p : Bukkit.getOnlinePlayers())
			playerJoinServer(p);
	}
	
	
	//-------------------- Getters --------------------//
	public static Lobby getDefaultLobby()
	{
		return getLobby(getInstance().default_lobby);
	}
	private static void verifyWorldEdit()
	{
		if (worldedit == null)
			if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
				if (Bukkit.getPluginManager().getPlugin("WorldEdit") instanceof WorldEditPlugin)
					worldedit = WorldEdit.getInstance();
		if (worldeditplugin == null)
			if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
				if (Bukkit.getPluginManager().getPlugin("WorldEdit") instanceof WorldEditPlugin)
					worldeditplugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
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
		for (Lobby l : getLobbies())
			if (l.getCurrentMap() != null && l.gameIsRunning() && l.getCurrentMap().getWorld() == world)
				return l;
		return null;
	}
	public static Lobby getLobby(World world)
	{
		if (world != null)
			return getLobby(world.getWorld());
		else
			return null;
	}
	public static List<Lobby> getLobbies(World world)
	{
		List<Lobby> lobbies = new ArrayList<Lobby>();
		
		for (Lobby lobby : getInstance().lobbies.values())
		{
			if (lobby.getWorlds().contains(world))
			{
				lobbies.add(lobby);
			}
		}
		return lobbies;
		
	}
	public static List<Lobby> getLobbies(org.bukkit.World world)
	{
		return getLobbies(Manhunt.getWorld(world));
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
		if (getInstance().player_lobbies.containsKey(name))
			return getLobby(getInstance().player_lobbies.get(name));
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
	
	public static ManhuntSettings getSettings()
	{
		return getInstance().settings;
	}
	public static TimeoutManager getTimeoutManager()
	{
		return getInstance().timeouts;
	}
	public static FinderManager getFinders()
	{
		return getInstance().finders;
	}
	public static CommandUtil getCommandUtil()
	{
		return getInstance().command_util;
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
		stopFinder(name, true);
		getPlayerLobby(name).removePlayer(name);
		player_lobbies.remove(name);
		player_maps.remove(name);
		player_modes.remove(name);
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
		p.setGameMode(GameMode.ADVENTURE);
	}
	private void registerGameType(Class<? extends Game> gameType, String name)
	{
		if (gameType.isInterface() || Modifier.isAbstract(gameType.getModifiers()))
			throw new IllegalArgumentException("Game class cannot be abstract or an interface.");
		
		for (GameType gc : getInstance().games.values())
			if (gc.getGameClass().equals(gameType))
				return;
		
		long i = 0;
		while (getInstance().games.containsKey(i))
			i++;
		
		getInstance().games.put(i, new GameType(gameType, i, name, ManhuntPlugin.getInstance()));
		
	}
	private void loadLobbies()
	{
		File file = new File(path_lobbies);
		if (!file.exists())
		{
			log("Lobby directory does not exist. Creating...");
			if (file.mkdir())
			{
				log("Lobby directory created successfully at " + path_lobbies + ".");
			}
			else
			{
				log(Level.SEVERE, "A unexpected problem occured when creating directory at " + path_lobbies + ".");
			}
			return;
		}
		if (!file.isDirectory())
		{
			 log(Level.SEVERE, "File at " + path_lobbies + " is not a directory!");
			 log(Level.SEVERE, "Please remedy the situation and restart Manhunt.");
			 return;
		}
		for (File f : file.listFiles())
		{
			if (!f.isFile() || !f.getName().endsWith(extension_lobbies))
				continue;
			
			Lobby lobby = createLobbyFromFile(f);
			
			if (lobby == null) // Loading from file failed
			{
				log("Failed to load lobby from file " + f.getName());
			}
		}
		
	}
	
	
	
	//---------------- Public Interface Methods ----------------//
	
	//////////////// LOBBIES ////////
	public static Lobby createLobby(String name, LobbyType type, Location location)
	{
		if (name == null || type == null || location == null)
			return null;
		
		Lobby lobby = null;
		
		World manhunt_world = getWorld(location.getWorld());
		
		if (manhunt_world == null)
			return null;
		
		long id = getInstance().getNextLobbyId();
		
		lobby = getLobby(name);
		if (lobby != null)
			return null;
		
		lobby = new Lobby(id, name, type, manhunt_world, location);
		
		getInstance().lobbies.put(lobby.getId(), lobby);
		
		return lobby;
	}
	public static boolean deleteLobby(long lobbyId)
	{
		Lobby lobby = getLobby(lobbyId);
		File lobby_settings;
		
		if (lobby == null)
		{
			return false;
		}
		
		closeLobby(lobbyId);
		lobby.close();
		getInstance().lobbies.remove(lobbyId);
		
		if (lobbyId == getInstance().default_lobby)
		{
			if (getInstance().lobbies.isEmpty())
			{
				getInstance().default_lobby = -1L;
			}
			else
			{
				for (long i = 0; true; i++)
				{
					if (getInstance().lobbies.containsKey(i))
					{
						getInstance().default_lobby = i;
						break;
					}
				}
			}
		}
		
		lobby_settings = lobby.getFile();
		if (lobby_settings != null && lobby_settings.exists())
		{
			lobby_settings.delete();
		}
		
		return true;
	}
	public static Lobby createLobbyFromFile(File f)
	{
		if (f == null || !f.exists())
		{
			return null;
		}
		
		Lobby lobby;
		int i;
		
		lobby = new Lobby(getInstance().getNextLobbyId(), f.getAbsolutePath());
		
		if (getLobby(lobby.getName()) != null)
		{
			i = 1;
			while (getLobby(lobby.getName() + i) != null)
				i++;
			lobby.setName(lobby.getName() + i);
		}
		
		getInstance().lobbies.put(lobby.getId(), lobby);
		
		return lobby;
	}
	public static void changePlayerLobby(Player p, long lobby_id)
	{
		
		Lobby old_lobby = getPlayerLobby(p);
		Lobby lobby = Manhunt.getLobby(lobby_id);
		
		if (lobby == null) 
			return;
		
		if (old_lobby == lobby)
			return;
		
		p.teleport(lobby.getRandomSpawnLocation());
		resetPlayer(p);
		
		getInstance().player_lobbies.put(p.getName(), lobby_id);
		
		if (lobby.addPlayer(p.getName()))
		{
			if (old_lobby != null)
			{
				old_lobby.broadcast(p.getName() + " has left the lobby.");
				old_lobby.removePlayer(p);
			}
			lobby.broadcast(p.getName() + " has joined the lobby.");
			log(p.getName() + " joined lobby " + lobby.getName());
		}
		
	}
	public static boolean setDefaultLobby(Lobby lobby)
	{
		if (lobby == null || !getInstance().lobbies.containsValue(lobby))
			return false;
		
		return setDefaultLobby(lobby.getId());
	}
	public static boolean setDefaultLobby(long lobbyId)
	{
		if (!getInstance().lobbies.containsKey(lobbyId))
			return false;
		else
		{
			getInstance().default_lobby = lobbyId;
			getSettings().DEFAULT_LOBBY.setValue(getLobby(lobbyId).getName());
			return true;
		}
	}
	public static void closeLobby(long lobbyId)
	{
		Lobby lobby = getLobby(lobbyId);
		
		if (lobby == null)
		{
			return;
		}
		
		if (!lobby.isEnabled())
		{
			return;
		}
		
		if (lobby.gameIsRunning())
		{
			lobby.stopGame();
		}
		
		// Lobby to close is NOT the default lobby
		if (lobbyId != getInstance().default_lobby)
		{
			for (Player p : lobby.getOnlinePlayers())
			{
				p.sendMessage(ChatColor.GOLD + ChatManager.leftborder + "The lobby you were in has been closed.");
				changePlayerLobby(p, getInstance().default_lobby);
			}
		}
		
	}
	public static void openLobby(long lobbyId)
	{
		Lobby lobby = getLobby(lobbyId);
		
		if (lobby == null)
		{
			return;
		}
		
		if (!lobby.isEnabled())
		{
			return;
		}
		
		lobby.enable();
	}
	
	
	
	//////////////// PLAYERS ////////
	public static void playerJoinServer(Player p)
	{
		if (timeoutExists(p))
			stopTimeout(p);
		
		// Reset the player's current lobby
		if (!getInstance().player_lobbies.containsKey(p.getName()))
		{
			changePlayerLobby(p, Manhunt.getDefaultLobby().getId());
		}
		
		// Set the player's current map
		if (!getInstance().player_maps.containsKey(p.getName()))
		{
			if (getWorlds().size() == 1 && getWorlds().get(0).getMaps().size() == 1)
				getInstance().player_maps.put(p.getName(), getWorlds().get(0).getMaps().get(0).getFullName());
			else
				getInstance().player_maps.put(p.getName(), null);
		}
		
		// Set the player's Manhunt mode
		if (!getInstance().player_modes.containsKey(p.getName()))
		{
			setPlayerManhuntMode(p, ManhuntMode.PLAY, false, true);
		}
		
	}
	public static void playerLeaveServer(Player p)
	{
		/*
		 * Protocol when a player leaves Manhunt
		 * 
		 * When a player leaves,...
		 * 		Starts a timeout,
		 * 		Or just plain kicks him
		 * 
		 * Completely removing a player involves...
			 * 	Stopping their timeout
			 * 	Stopping their finder
			 * 	Removing them from lobbies
			 * 	Forfeiting them from games
			 *  Deleting their stuff in the command util
		 */
		
		if (getPlayerLobby(p) != null && getPlayerLobby(p).gameIsRunning() && getSettings().OFFLINE_TIMEOUT.getValue() > 0)
		{
			startTimeout(p, getPlayerLobby(p), getSettings().OFFLINE_TIMEOUT.getValue() * 1000);
		}
		else
		{
			if (getPlayerLobby(p) != null && getPlayerLobby(p).gameIsRunning())
				getPlayerLobby(p).forfeitPlayer(p.getName());
			else if (getPlayerLobby(p) != null)
				getPlayerLobby(p).removePlayer(p);
			stopFinder(p.getName(), false);
			getInstance().removePlayer(p.getName());
		}
		
		getCommandUtil().deletePlayer(p);
	}
	public static void playerChat(Player p, String message)
	{
		Lobby l = getPlayerLobby(p);
		
		if (l == null)
		{
			p.sendMessage(ChatColor.RED + "You are not in a lobby!");
			p.sendMessage(ChatColor.GRAY + "Use /mjoin to join a lobby.");
			return;
		}
		
		message = ChatColor.WHITE + "<" + l.getPlayerTeam(p).getColor() + p.getName() + ChatColor.WHITE + "> " + message;
		
		if (!l.gameIsRunning() || l.getSettings().ALL_TALK.getValue())
		{
			l.broadcast(message);
		}
		else
		{
			switch (l.getPlayerTeam(p))
			{
			case HUNTERS:
			case PREY:
				l.broadcast(message, l.getPlayerTeam(p));
				break;
			default:
				l.broadcast(message, Team.SPECTATORS, Team.STANDBY, Team.NONE);
				break;
			}
			
			log(message);
			return;
		}
	}
	public static void setPlayerManhuntMode(Player p, ManhuntMode mode, boolean announce, boolean gamemode)
	{
		// Validate arguments
		if (p == null)
			return;
		
		if (mode == null)
			return;
		
		
		// Set the player's game mode
		if (announce && getInstance().player_modes.get(p.getName()) != mode)
			p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Manhunt Mode set to (" + mode.getId() + ") " + mode.getName());
		Manhunt.log(p.getName() + "'s Manhunt mode set to (" + mode.getId() + ") " + mode.getName());
		
		getInstance().player_modes.put(p.getName(), mode);
		
		verifyWorldEdit();
		
		// Enable/disable world edit's tools.
		switch (mode)
		{
		case PLAY:
			if (gamemode)
				p.setGameMode(GameMode.ADVENTURE);
			if (worldedit != null && worldedit.getSession(p.getName()) != null)
				worldedit.getSession(p.getName()).setToolControl(false);
			break;
		case EDIT:
			if (gamemode)
				p.setGameMode(GameMode.CREATIVE);
			if (worldedit != null && worldedit.getSession(p.getName()) != null)
				worldedit.getSession(p.getName()).setToolControl(true);
			break;
		}
		
		
	}
	public static ManhuntMode getPlayerMode(Player p)
	{
		if (getInstance().player_modes.containsKey(p.getName()))
			return getInstance().player_modes.get(p.getName());
		else
			return null;
	}
	public static void setPlayerSelectedMap(Player p, Map map)
	{
		CommandUtil.setSelectedMap(p, map);
	}
	public static Map getPlayerSelectedMap(Player p)
	{
		return CommandUtil.getSelectedMap(p);
	}
	
	
	
	
	
	//////////////// WORLDS /////////
	private static World registerWorld(org.bukkit.World world)
	{
		if (world == null || getInstance().worlds.containsKey(world.getName()) || getInstance().worlds.containsValue(world))
			return null;
		
		World mworld = new ManhuntWorld(world);
		getInstance().worlds.put(world.getName(), mworld);
		return mworld;
	}
	public static Map getMap(String fullmapname)
	{
		String[] strn;
		World world;
		Map map;
		strn = fullmapname.split("\\.");
		
		if (strn.length != 2)
			return null;
		
		world = getWorld(strn[0]);
		if (world == null)
			return null;
		
		map = world.getMap(strn[1]);
		
		return map;
	}
	
	
	
	//////////////// TIMEOUTS ////////
	public static void startTimeout(Player player, Lobby lobby)
	{
		if (getSettings().OFFLINE_TIMEOUT.getValue() >= 0)
			startTimeout(player, lobby, getSettings().OFFLINE_TIMEOUT.getValue() * 1000);
	}
	public static void startTimeout(Player player, Lobby lobby, long time)
	{
		if (getInstance().timeouts.hasTimeout(player))
			getInstance().timeouts.stopTimeout(player);
		
		getInstance().timeouts.startTimeout(player, lobby.getId(), time);
	}
	public static boolean timeoutExists(Player p)
	{
		return getInstance().timeouts.hasTimeout(p);
	}
	public static void stopTimeout(Player player)
	{
		getInstance().timeouts.stopTimeout(player);
	}
	
	
	
	//////////////// LOGGING ////////
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
	
	
	
	//////////////// REGISTERING GAME TYPES ////////
	public static boolean registerGameType(Class<? extends Game> gameType, String name, JavaPlugin plugin)
	{
		if (gameType.isInterface() || Modifier.isAbstract(gameType.getModifiers()))
			throw new IllegalArgumentException("Game class cannot be abstract or an interface.");
		
		for (GameType gc : getInstance().games.values())
			if (gc.getGameClass().equals(gameType))
				return false;
		
		if (plugin == ManhuntPlugin.getInstance())
			throw new IllegalArgumentException("\"plugin\" arugment cannot be the Manhunt plugin itself.");
		
		long i = 0;
		while (getInstance().games.containsKey(i))
			i++;
		
		getInstance().games.put(i, new GameType(gameType, i, name, plugin));
		
		return true;
		
	}
	public static List<GameType> getRegisteredGameTypes()
	{
		return new ArrayList<GameType>(getInstance().games.values());
	}
	public static GameType getGameType(long id)
	{
		if (getInstance().games.containsKey(id))
			return getInstance().games.get(id);
		else
			return null;
	}
	/**
	 * Used for lobby loading. Allows lobbies to get the GameType based
	 * off the Game class's canonical name.
	 * @param class_canonical_name
	 * @return
	 */
	public static GameType getGameTypeByClassCanonicalName(String class_canonical_name)
	{
		for (GameType gt : getRegisteredGameTypes())
		{
			if (gt.getGameClass().getCanonicalName().equals(class_canonical_name))
				return gt;
		}
		return null;
	}
	
	
	
	//////////////// FINDERS ////////
	public static void startFinder(Player p, long lobby_id)
	{
		getInstance().finders.startFinder(p, lobby_id);
	}
	public static void stopAllFinders(long lobby_id)
	{
		getInstance().finders.stopLobbyFinders(lobby_id);
	}
	public static void stopFinder(Player p, boolean ignoreUsed)
	{
		stopFinder(p.getName(), ignoreUsed);
	}
	public static void stopFinder(String name, boolean ignoreUsed)
	{
		getInstance().finders.stopFinder(name, ignoreUsed);
	}
	public static Finder getFinder(Player p)
	{
		return getFinders().getFinder(p);
	}
	public static Finder getFinder(String name)
	{
		return getFinders().getFinder(name);
	}
	public static boolean finderExists(Player p)
	{
		return getFinders().finderExists(p);
	}
	public static boolean finderExists(String name)
	{
		return getFinders().finderExists(name);
	}
	
	
	
	//////////////// SELECTIONS ////////
	private Selection getPlayerSelection(Player p)
	{
		if (p == null)
			return null;
		
		verifyWorldEdit();
		if (worldedit != null)
		{
			return null;
		}
		else if (selections.containsKey(p.getName()))
		{
			return selections.get(p.getName());
		}
		else
		{
			selections.put(p.getName(), new Selection(p));
			return selections.get(p.getName());
		}
	}
	public static boolean setPlayerSelectionPrimaryCorner(Player p, Location loc)
	{
		verifyWorldEdit();
		
		if (worldedit != null)
			return false;
		
		getInstance().getPlayerSelection(p).setPrimaryCorner(loc);
		return true;
	}
	public static boolean setPlayerSelectionSecondaryCorner(Player p, Location loc)
	{
		verifyWorldEdit();
		
		if (worldedit != null)
			return false;
		
		getInstance().getPlayerSelection(p).setSecondaryCorner(loc);
		return true;
	}
	public static Location getPlayerSelectionPrimaryCorner(Player p)
	{
		verifyWorldEdit();
		
		if (worldeditplugin == null)
			return getInstance().getPlayerSelection(p).getPrimaryCorner();
		else if (worldeditplugin.getSelection(p) == null)
			return null;
		else
			return worldeditplugin.getSelection(p).getMaximumPoint();
	}
	public static Location getPlayerSelectionSecondaryCorner(Player p)
	{
		verifyWorldEdit();
		
		if (worldeditplugin == null)
			return getInstance().getPlayerSelection(p).getSecondaryCorner();
		else if (worldeditplugin.getSelection(p) == null)
			return null;
		else
			return worldeditplugin.getSelection(p).getMinimumPoint();
	}
	public static boolean getPlayerSelectionValid(Player p)
	{
		return getInstance().getPlayerSelection(p).isValid();
	}
	
	
	
	//////////////// GLOBAL MANHUNT EVENTS ////////
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		playerJoinServer(e.getPlayer());
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		playerLeaveServer(e.getPlayer());
	}
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
	{
		playerChat(e.getPlayer(), e.getMessage());
		e.setCancelled(true);
	}
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e)
	{
		if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (e.getPlayer().getItemInHand().getTypeId() != getSettings().SELECTION_TOOL.getValue())
			return;
		
		if (getPlayerMode(e.getPlayer()) != ManhuntMode.EDIT)
			return;
		
		if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit"))
			return;
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if (!e.getClickedBlock().getLocation().equals(getPlayerSelectionPrimaryCorner(e.getPlayer())) && setPlayerSelectionPrimaryCorner(e.getPlayer(), e.getClickedBlock().getLocation()))
			{
				e.getPlayer().sendMessage(ChatManager.leftborder + "   First location set to " + ChatColor.GREEN + "("
						+ e.getClickedBlock().getLocation().getBlockX() + ", "
						+ e.getClickedBlock().getLocation().getBlockY() + ", "
						+ e.getClickedBlock().getLocation().getBlockZ() + ")"
						+ ChatColor.GOLD
						+ (getPlayerSelection(e.getPlayer()).isComplete() ? " [" + getPlayerSelection(e.getPlayer()).getArea() + "]" : "" )
						+ ".");
			}
			e.setCancelled(true);
		}
		else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (!e.getClickedBlock().getLocation().equals(getPlayerSelectionSecondaryCorner(e.getPlayer())) && setPlayerSelectionSecondaryCorner(e.getPlayer(), e.getClickedBlock().getLocation()))
			{

				e.getPlayer().sendMessage(ChatManager.leftborder + "Second location set to " + ChatColor.GREEN + "("
						+ e.getClickedBlock().getLocation().getBlockX() + ", "
						+ e.getClickedBlock().getLocation().getBlockY() + ", "
						+ e.getClickedBlock().getLocation().getBlockZ() + ")"
						+ ChatColor.GOLD
						+ (getPlayerSelection(e.getPlayer()).isComplete() ? " [" + getPlayerSelection(e.getPlayer()).getArea() + "]" : "" )
						+ ".");
			}
			e.setCancelled(true);
		}
		
	}
	
	public void close()
	{
		worldedit = null;
		worldeditplugin = null;
		
		for (GameType gc : games.values())
			gc.close();
		
		for (Lobby l : getInstance().lobbies.values())
			l.save();
		
		for (World w : getInstance().worlds.values())
			w.save();

		HandlerList.unregisterAll(this);
		
		settings.save();
	}
	
}
