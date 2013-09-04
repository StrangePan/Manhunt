package com.deaboy.manhunt;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.commands.CommandSwitchboard;
import com.deaboy.manhunt.commands.CommandUtil;
import com.deaboy.manhunt.finder.FinderManager;
import com.deaboy.manhunt.game.Game;
import com.deaboy.manhunt.game.GameClass;
import com.deaboy.manhunt.game.ManhuntGame;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.loadouts.LoadoutManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.LobbyClass;
import com.deaboy.manhunt.lobby.LobbyType;
import com.deaboy.manhunt.lobby.ManhuntGameLobby;
import com.deaboy.manhunt.lobby.ManhuntHubLobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.ManhuntWorld;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Selection;
import com.deaboy.manhunt.map.World;
import com.deaboy.manhunt.settings.LobbySettings;
import com.deaboy.manhunt.settings.ManhuntSettings;
import com.deaboy.manhunt.settings.SettingsFile;
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
	private			SettingsFile		settingsfile;
	private 		TimeoutManager		timeouts;
	private 		FinderManager		finders;
	private 		CommandUtil			command_util;
	private 		LoadoutManager		loadouts;
	private			long				default_lobby;
	
	private 		HashMap<Long, Lobby>	lobbies;
	private			HashMap<String, Long>	player_lobbies;
	private			HashMap<String, ManhuntMode> player_modes;
	private			HashMap<String, String> player_maps;
	private			HashMap<String, Long> player_logoffs;
	private			HashMap<String, Location> locked_players;
	private			HashMap<String, World>	worlds;
	private			HashMap<Long, GameClass> gameclasses;
	private			HashMap<Long, LobbyClass> lobbyclasses;
	private			HashMap<String, Selection> selections;
	
	
	private static	WorldEdit			worldedit;
	private static	WorldEditPlugin		worldeditplugin;
	
	
	public static boolean free_mtac = false;
	
	
	
	//---------------- Constructor ----------------//
	public Manhunt()
	{
		Manhunt.instance =		this;
		this.settings =			new ManhuntSettings();
		this.settingsfile =		new SettingsFile(new File(path_settings));
		this.settingsfile		.addPack(this.settings);
		this.settingsfile.load();
		this.settingsfile.save();
		
		this.timeouts =			new TimeoutManager();
		this.finders =			new FinderManager();
		this.command_util =		new CommandUtil();
		this.loadouts =			new LoadoutManager();
		
		this.lobbies =			new HashMap<Long, Lobby>();
		this.worlds =			new HashMap<String, World>();
		this.player_lobbies =	new HashMap<String, Long>();
		this.player_modes =		new HashMap<String, ManhuntMode>();
		this.player_maps =		new HashMap<String, String>();
		this.player_logoffs =	new HashMap<String, Long>();
		this.locked_players =	new HashMap<String, Location>();
		this.gameclasses =		new HashMap<Long, GameClass>();
		this.lobbyclasses = 	new HashMap<Long, LobbyClass>();
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
		
		if (settings.LOAD_WORLDS.getValue())
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
		
		
		//////// Setup Loadouts ////////
		this.loadouts.loadLoadoutFiles();
		
		
		//////// Register game types
		registerGameClass(ManhuntGame.class, "Manhunt");
		// registerGameType(JuggernautGame.class, "Juggerhunt");
		// registerGameType(GhostGame.class, "Manhaunt");
		
		//////// Register lobby types
		registerLobbyClass(ManhuntGameLobby.class, LobbyType.GAME, "Manhunt");
		registerLobbyClass(ManhuntHubLobby.class, LobbyType.HUB, "Hub");
		
		
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
			lobby = createLobby("default", Manhunt.getLobbyClass(0), Bukkit.getWorlds().get(0).getSpawnLocation());
		
		setDefaultLobby(lobby);
		CommandUtil.setSelectedLobby(Bukkit.getConsoleSender(), getDefaultLobby());
		
		
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
	public static List<Lobby> getLobbies(World world)
	{
		List<Lobby> lobbies = new ArrayList<Lobby>();
		
		for (Lobby lobby : getInstance().lobbies.values())
		{
			if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).getWorlds().contains(world))
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
	public static Lobby getLobby(org.bukkit.World world)
	{
		for (Lobby lobby : getLobbies())
		{
			if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).gameIsRunning() && ((GameLobby) lobby).getCurrentMap().getWorld().getWorld() == world)
				return lobby;
		}
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
	
	
	
	//---------------- Private Methods ----------------//
	private static Manhunt getInstance()
	{
		return instance;
	}
	private void removePlayer(String name)
	{
		timeouts.cancelTimeout(name);
		cancelFinderFor(name);
		player_lobbies.remove(name);
		player_maps.remove(name);
		player_modes.remove(name);
		locked_players.remove(name);
	}
	private long getNextLobbyId()
	{
		long i;
		for (i = 0; lobbies.containsKey(i); i++);
		return i;
	}
	private void registerGameClass(Class<? extends Game> gameclass, String name)
	{
		if (gameclass.isInterface() || Modifier.isAbstract(gameclass.getModifiers()))
		{
			throw new IllegalArgumentException("Game class cannot be abstract or an interface.");
		}
		
		for (GameClass gc : getInstance().gameclasses.values())
		{
			if (gc.getGameClass().equals(gameclass))
			{
				return;
			}
		}
		
		long i = 0;
		while (getInstance().gameclasses.containsKey(i))
		{
			i++;
		}
		
		getInstance().gameclasses.put(i, new GameClass(i, name, gameclass, ManhuntPlugin.getInstance()));
		
	}
	private void registerLobbyClass(Class<? extends Lobby> lobbyclass, LobbyType type, String name)
	{
		if (lobbyclass.isInterface() || Modifier.isAbstract(lobbyclass.getModifiers()))
		{
			throw new IllegalArgumentException("Lobby class cannot be abstract or an interface.");
		}
		
		for (LobbyClass lc : getInstance().lobbyclasses.values())
		{
			if (lc.getLobbyClass().equals(lobbyclass))
			{
				return;
			}
		}
		
		long i = 0;
		while (getInstance().lobbyclasses.containsKey(i))
		{
			i++;
		}
		
		getInstance().lobbyclasses.put(i, new LobbyClass(i, name, type, lobbyclass, ManhuntPlugin.getInstance()));
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
	public static Lobby createLobby(String name, LobbyClass lobbyclass, Location location)
	{
		return createLobby(name, lobbyclass, location, Manhunt.getGameClass(0));
	}
	public static Lobby createLobby(String name, LobbyClass lobbyclass, Location location, GameClass gameclass)
	{
		if (name == null || lobbyclass == null || lobbyclass.getLobbyClass() == null || location == null)
			return null;
		
		Lobby lobby;
		File file;
		long id;
		
		id = 0;
		file = new File(path_lobbies + '/' + name + extension_lobbies);
		while (file.exists())
		{
			file = new File(path_lobbies + '/' + name + id++ + extension_lobbies);
		}
		
		id = getInstance().getNextLobbyId();
		lobby = getLobby(name);
		if (lobby != null)
			return null;
		
		lobby = lobbyclass.createInstance(id, file, name, location);
		if (lobby.getType() == LobbyType.GAME && gameclass != null && gameclass.getGameClass() != null)
			((GameLobby) lobby).setGameClass(gameclass);
		
		getInstance().lobbies.put(lobby.getId(), lobby);
		lobby.saveFiles();
		
		return lobby;
	}
	public static boolean deleteLobby(long lobbyId)
	{
		Lobby lobby = getLobby(lobbyId);
		
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

		lobby.deleteFiles();
		
		return true;
	}
	public static Lobby createLobbyFromFile(File f)
	{
		if (f == null || !f.exists())
		{
			return null;
		}
		
		Lobby lobby;
		SettingsFile file;
		LobbySettings settings;
		
		file = new SettingsFile(f);
		settings = new LobbySettings();
		file.addPack(settings);
		file.load();
		file.loadPacks();
		file.close();
		
		if (getLobbyClassByCanonicalName(settings.LOBBY_CLASS.getValue()) != null)
		{
			lobby = getLobbyClassByCanonicalName(settings.LOBBY_CLASS.getValue()).createInstance(getInstance().getNextLobbyId(), f);
		}
		else
		{
			return null;
		}
		
		
		if (lobby != null)
		{
			getInstance().lobbies.put(lobby.getId(), lobby);
			lobby.loadFiles();
		}
		
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
		
		getInstance().player_lobbies.put(p.getName(), lobby_id);
		
		if (lobby.playerJoinLobby(p))
		{
			if (old_lobby != null)
			{
				old_lobby.playerLeaveLobby(p.getName());
			}
			log(p.getName() + " joined lobby " + lobby.getName());
			
			CommandUtil.setSelectedLobby(p, lobby);
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
		
		if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).gameIsRunning())
		{
			((GameLobby) lobby).cancelGame();
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
		
		lobby.disable();
		
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
		if (timeoutExists(p) || getInstance().player_logoffs.containsKey(p.getName()) && new Date().getTime() < getInstance().player_logoffs.get(p.getName()) + getSettings().FORGET_PLAYER.getValue() * 1000)
		{
			if (getPlayerLobby(p) == null)
			{
				changePlayerLobby(p, getInstance().default_lobby);
			}
			else
			{
				getPlayerLobby(p).playerJoinLobby(p);
			}
		}
		else
		{
			getInstance().removePlayer(p.getName());
			getCommandUtil().deletePlayer(p);
		}
		
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
		getInstance().player_logoffs.put(p.getName(), new Date().getTime());
		if (getPlayerLobby(p) != null && getPlayerLobby(p).getType() == LobbyType.GAME && ((GameLobby) getPlayerLobby(p)).gameIsRunning())
		{
			((GameLobby) getPlayerLobby(p)).playerLeaveServer(p);
		}
		cancelFinderFor(p);
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
		
		message = ChatColor.WHITE + "<" + (l.getType() == LobbyType.GAME ? ((GameLobby) l).getPlayerTeam(p).getColor() : ChatColor.WHITE) + p.getName() + ChatColor.WHITE + "> " + message;
		
		if (l.getType() != LobbyType.GAME || !((GameLobby) l).gameIsRunning() || ((GameLobby) l).getSettings().ALL_TALK.getValue())
		{
			l.broadcast(message);
		}
		else
		{
			switch (((GameLobby) l).getPlayerTeam(p))
			{
			case HUNTERS:
			case PREY:
				((GameLobby) l).broadcast(message, ((GameLobby) l).getPlayerTeam(p));
				break;
			default:
				((GameLobby) l).broadcast(message, Team.SPECTATORS, Team.STANDBY, Team.NONE);
				break;
			}
			
			log('[' + l.getName() + "::" + l.getName() + "] " + p.getName() + ": " + message);
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
	public static void forgetPlayer(String playername)
	{
		Manhunt.getInstance().removePlayer(playername);
	}
	
	
	//////////////// LOCKED PLAYERS ////////
	public static void lockPlayer(Player player)
	{
		if (player != null && player.isOnline())
		{
			getInstance().locked_players.put(player.getName(), player.getLocation());
		}
	}
	public static void unlockPlayer(String playername)
	{
		if (getInstance().locked_players.containsKey(playername))
		{
			getInstance().locked_players.remove(playername);
		}
	}
	public static boolean playerIsLocked(String playername)
	{
		if (playername != null)
		{
			return getInstance().locked_players.containsKey(playername);
		}
		else
		{
			return false;
		}
	}
	public static Location getPlayerLockedPosition(String playername)
	{
		if (playername != null && getInstance().locked_players.containsKey(playername))
		{
			return getInstance().locked_players.get(playername);
		}
		else
		{
			return null;
		}
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
	public static boolean deleteMap(Map map)
	{
		if (map != null && map.getWorld().getMaps().contains(map))
		{
			for (Lobby lobby : getLobbies())
			{
				if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).containsMap(map) && ((GameLobby) lobby).gameIsRunning() && ((GameLobby) lobby).getCurrentMap() != map)
				{
					return false;
				}
			}
			for (Lobby lobby : getLobbies())
			{
				if (lobby.getType() == LobbyType.GAME && ((GameLobby) lobby).containsMap(map))
				{
					((GameLobby) lobby).unregisterMap(map);
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	//////////////// TIMEOUTS ////////
	public static void startTimeout(Player player)
	{
		getInstance().timeouts.startTimeout(player);
	}
	public static void startTimeout(String name)
	{
		getInstance().timeouts.startTimeout(name);
	}
	public static boolean timeoutExists(Player p)
	{
		return getInstance().timeouts.containsTimeout(p);
	}
	public static void stopTimeout(Player player)
	{
		getInstance().timeouts.cancelTimeout(player);
	}
	
	
	//////////////// LOGGING ////////
	public static void log(String message)
	{
		log(Level.INFO, message);
	}
	public static void log(Level level, String message)
	{
		Bukkit.getLogger().log(level, "[Manhunt]  " + ChatColor.stripColor(message));
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
	
	
	//////////////// REGISTERING GAME CLASSES ////////
	public static boolean registerGameClass(Class<? extends Game> gameclass, String name, JavaPlugin plugin)
	{
		if (gameclass.isInterface() || Modifier.isAbstract(gameclass.getModifiers()))
			throw new IllegalArgumentException("Game class cannot be abstract or an interface.");
		
		try
		{
			gameclass.getConstructor(GameLobby.class);
		}
		catch (NoSuchMethodException e)
		{
			return false;
		}
		
		for (GameClass gc : getInstance().gameclasses.values())
			if (gc.getGameClass().equals(gameclass))
				return false;
		
		if (plugin == ManhuntPlugin.getInstance())
			throw new IllegalArgumentException("plugin arugment cannot be the Manhunt plugin itself.");
		
		long i = 0;
		while (getInstance().gameclasses.containsKey(i))
			i++;
		
		getInstance().gameclasses.put(i, new GameClass(i, name, gameclass, plugin));
		
		return true;
		
	}
	public static List<GameClass> getRegisteredGameClasses()
	{
		return new ArrayList<GameClass>(getInstance().gameclasses.values());
	}
	public static GameClass getGameClass(long id)
	{
		if (getInstance().gameclasses.containsKey(id))
			return getInstance().gameclasses.get(id);
		else
			return null;
	}
	public static GameClass getGameClassByCanonicalName(String class_canonical_name)
	{
		for (GameClass gt : getRegisteredGameClasses())
		{
			if (gt.getGameClass().getCanonicalName().equals(class_canonical_name))
				return gt;
		}
		return null;
	}
	
	
	//////////////// REGISTERING LOBBY CLASSES ////////
	public static boolean registerLobbyClass(Class<? extends Lobby> lobbyclass, LobbyType lobbytype, String name, JavaPlugin plugin)
	{
		if (lobbyclass.isInterface() || Modifier.isAbstract(lobbyclass.getModifiers()))
			throw new IllegalArgumentException("Lobby class cannot be abstract nor an interface.");
		
		try
		{
			lobbyclass.getConstructor(long.class, File.class);
			lobbyclass.getConstructor(long.class, File.class, String.class, Location.class);
		}
		catch (NoSuchMethodException e)
		{
			return false;
		}
		
		for (LobbyClass lc : getInstance().lobbyclasses.values())
			if (lc.getLobbyClass().equals(lobbyclass))
				return false;
		
		if (plugin == ManhuntPlugin.getInstance())
			throw new IllegalArgumentException("plugin arugment cannot be the Manhunt plugin itself.");
		
		long i = 0;
		while (getInstance().lobbyclasses.containsKey(i))
			i++;
		
		getInstance().lobbyclasses.put(i, new LobbyClass(i, name, lobbytype, lobbyclass, plugin));
		
		return true;
		
	}
	public static List<LobbyClass> getRegisteredLobbyClasses()
	{
		return new ArrayList<LobbyClass>(getInstance().lobbyclasses.values());
	}
	public static LobbyClass getLobbyClass(long id)
	{
		if (getInstance().lobbyclasses.containsKey(id))
			return getInstance().lobbyclasses.get(id);
		else
			return null;
	}
	public static LobbyClass getLobbyClassByCanonicalName(String class_canonical_name)
	{
		for (LobbyClass lc : getRegisteredLobbyClasses())
		{
			if (lc.getLobbyClass().getCanonicalName().equals(class_canonical_name))
				return lc;
		}
		return null;
	}
	
	
	//////////////// LOADOUTS ////////
	public static Loadout getLoadout(String name)
	{
		return getInstance().loadouts.getLoadout(name);
	}
	public static boolean containsLoadout(String name)
	{
		return getInstance().loadouts.containsLoadout(name);
	}
	public static List<Loadout> getAllLoadouts()
	{
		return getInstance().loadouts.getAllLoadouts();
	}
	public static Loadout createLoadout(String name, ItemStack[] inventory, ItemStack[] armor)
	{
		Loadout loadout;
		String filename;
		int i;
		
		if (name == null || inventory == null || armor == null || getInstance().loadouts.containsLoadout(name))
			return null;
		
		filename = name;
		i = 0;
		if (new File(path_loadouts + '/' + filename + extension_loadouts).exists())
		{
			while (new File(path_loadouts + '/' + filename + i++ + extension_loadouts).exists());
			filename += i;
		}
		
		loadout = new Loadout(name, filename, inventory, armor);
		getInstance().loadouts.addLoadout(loadout);
		return loadout;
	}
	public static boolean deleteLoadout(String name)
	{
		if (name == null)
			return false;
		
		return getInstance().loadouts.deleteLoadout(name);
	}
	
	
	//////////////// FINDERS ////////
	public static boolean startFinderFor(Player p, long charge_ticks, long cooldown_ticks, Material finder_material, int food_cost, double health_cost, boolean use_xp)
	{
		return getInstance().finders.startFinderFor(p, charge_ticks, cooldown_ticks, finder_material, food_cost, health_cost, use_xp);
	}
	public static void cancelAllFinders(GameLobby lobby)
	{
		getInstance().finders.cancelAllFinders(lobby);
	}
	public static void cancelAllFinders(long lobby_id)
	{
		getInstance().finders.cancelAllFinders(lobby_id);
	}
	public static void cancelAllFinders()
	{
		getInstance().finders.cancelAllFinders();
	}
	public static void cancelFinderFor(Player p)
	{
		cancelFinderFor(p.getName());
	}
	public static void cancelFinderFor(String name)
	{
		getInstance().finders.cancelFinderFor(name);
	}
	public static boolean finderExistsFor(Player p)
	{
		return getFinders().finderExistsFor(p);
	}
	public static boolean finderExistsFor(String name)
	{
		return getFinders().finderExistsFor(name);
	}
	public static void sendFinderTimeRemaining(Player p)
	{
		if (p != null && finderExistsFor(p))
		{
			if (!getInstance().finders.finderIsUsed(p.getName()))
			{
				p.sendMessage(ChatManager.bracket1_ + ((getInstance().finders.getFinderChargeTicksRemainingFor(p) + 19) / 20) + " seconds until finder is charged." + ChatManager.bracket2_);
			}
			else
			{
				p.sendMessage(ChatManager.bracket1_ + ((getInstance().finders.getFinderCooldownTicksRemainingFor(p) + 19) / 20) + " seconds until finder has cooled down." + ChatManager.bracket2_);
			}
		}
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
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		playerJoinServer(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		playerLeaveServer(e.getPlayer());
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
	{
		playerChat(e.getPlayer(), e.getMessage());
		e.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerClick(PlayerInteractEvent e)
	{
		if (getPlayerMode(e.getPlayer()) != ManhuntMode.EDIT)
			return;
		
		if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if (e.getPlayer().getItemInHand().getTypeId() != getSettings().SELECTION_TOOL.getValue())
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
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent e)
	{
		if (e.isCancelled() || !(e.getTarget() instanceof Player))
			return;
		
		if (getPlayerLobby((Player) e.getTarget()).getType() != LobbyType.GAME || !((GameLobby) getPlayerLobby((Player) e.getTarget())).gameIsRunning())
		{
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if (e.isCancelled() || !(e.getEntity() instanceof Player))
			return;
		
		if (getPlayerLobby((Player) e.getEntity()).getType() != LobbyType.GAME || !((GameLobby) getPlayerLobby((Player) e.getEntity())).gameIsRunning())
		{
			e.setCancelled(true);
			return;
		}
		else
		{
			return;
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if (playerIsLocked(e.getPlayer().getName()) && !ManhuntUtil.areEqualLocations(getPlayerLockedPosition(e.getPlayer().getName()), e.getPlayer().getLocation(), 0, true))
		{
			e.setCancelled(true);
			Location point = getPlayerLockedPosition(e.getPlayer().getName()).clone();
			point.setYaw(e.getPlayer().getLocation().getYaw());
			point.setPitch(e.getPlayer().getLocation().getPitch());
			e.getPlayer().teleport(point);
		}
	}
	
	
	//////////////// CLOSING ////////
	public void saveSettings()
	{
		this.settingsfile.savePacks();
		this.settingsfile.save();
	}
	public void loadSettings()
	{
		this.settingsfile.load();
		this.settingsfile.loadPacks();
	}
	
	
	//////////////// CLOSING ////////
	public void close()
	{
		worldedit = null;
		worldeditplugin = null;
		
		cancelAllFinders();
		
		for (GameClass gc : gameclasses.values())
			gc.close();
		
		for (Lobby l : getInstance().lobbies.values())
			l.saveFiles();
		
		for (World w : getInstance().worlds.values())
			w.save();

		HandlerList.unregisterAll(this);
		
		settingsfile.save();
	}
	
	
}



