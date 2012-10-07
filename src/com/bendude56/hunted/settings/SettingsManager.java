package com.bendude56.hunted.settings;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.bendude56.hunted.ManhuntPlugin.ManhuntMode;


public class SettingsManager
{
	private List<SettingsFile> files = new ArrayList<SettingsFile>();
	
	public final SettingsFile FILE_MAIN;
	public final SettingsFile FILE_WORLD;

	private String plugin_path = "plugins/Manhunt";
	private String world_path;
	
	private List<Setting<?>> settings = new ArrayList<Setting<?>>();
	private List<Setting<?>> secretSettings = new ArrayList<Setting<?>>();

	public final Setting<ManhuntMode> MANHUNT_MODE;

	public final Setting<String> WORLD;
	public final Setting<String> HUNTER_LOADOUT_CURRENT;
	public final Setting<String> PREY_LOADOUT_CURRENT;

	public final Setting<Boolean> OP_CONTROL;
	public final Setting<Boolean> AUTO_JOIN;

	public final Setting<Boolean> ALL_TALK;
	public final Setting<Boolean> CONTROL_CHAT;
	public final Setting<Boolean> FRIENDLY_FIRE;
	public final Setting<Boolean> INSTANT_DEATH;
	public final Setting<Boolean> FLYING_SPECTATORS;
	public final Setting<Boolean> LOADOUTS;
	public final Setting<Boolean> TEAM_HATS;
	public final Setting<Boolean> NO_BUILD;
	
	public final Setting<Boolean> PASSIVE_MOBS;
	public final Setting<Boolean> HOSTILE_MOBS;
	public final Setting<Boolean> PREY_FINDER;
	//public final Setting<Boolean> NORTH_COMPASS;
	public final Setting<Integer> FINDER_COOLDOWN;
	
	public final Setting<Integer> OFFLINE_TIMEOUT;
	public final Setting<Integer> DAY_LIMIT;
	public final Setting<Integer> SETUP_TIME;
	public final Setting<Integer> INTERMISSION;
	public final Setting<Integer> MINIMUM_PLAYERS;
	
	public final Setting<Boolean> RESTORE_WORLD;
	public final Setting<Integer> BOUNDARY_WORLD;
	public final Setting<Integer> BOUNDARY_SETUP;
	public final Setting<Integer> SPAWN_PROTECTION;
	public final Setting<Boolean> BOUNDARY_BOXED;
	
	public final Setting<Location> SPAWN_HUNTER;
	public final Setting<Location> SPAWN_PREY;
	public final Setting<Location> SPAWN_SETUP;

	
	public SettingsManager()
	{
		files.add(FILE_MAIN = new SettingsFile("Main Config", plugin_path, "Manhunt"));
		
		secretSettings.add(WORLD = new Setting<String>("World", "world", FILE_MAIN, "The Manhunt world.", ""));
		if (Bukkit.getWorld(WORLD.value) == null)
		{
			WORLD.setValue(Bukkit.getWorlds().get(0).getName());
		}
		
		secretSettings.add(MANHUNT_MODE = new Setting<ManhuntMode>("GameMode", ManhuntMode.PRIVATE, FILE_MAIN, "The type of mode Manhunt is running in.", ""));
		
		settings.add(OP_CONTROL = new Setting<Boolean>("OpControl", true, FILE_MAIN, "Only ops have access to all commands.", "Non-ops have access to basic controls."));
		settings.add(AUTO_JOIN = new Setting<Boolean>("AutoJoin", true, FILE_MAIN, "New players automatically join team Hunters.", "New players will remain spectators."));

		settings.add(ALL_TALK = new Setting<Boolean>("AllTalk", false, FILE_MAIN, "Teams can communicate with each other.", "Teams cannot communicate with each other."));
		settings.add(CONTROL_CHAT = new Setting<Boolean>("ChatControl", true, FILE_MAIN, "The Manhunt plugin will control chat.", "Manhunt will not touch the chat."));
		settings.add(FRIENDLY_FIRE = new Setting<Boolean>("FriendlyFire", false, FILE_MAIN, "Teammates can damage each other.", "Teammates cannot hurt each other."));
		settings.add(INSTANT_DEATH = new Setting<Boolean>("InstantDeath", false, FILE_MAIN, "Players will die in one hit.", "Players take damage like normal."));
		settings.add(FLYING_SPECTATORS = new Setting<Boolean>("FlyingSpectators", true, FILE_MAIN, "Spectators can fly in creative mode.", "Spectators are bound to the ground."));
		settings.add(LOADOUTS = new Setting<Boolean>("Loadouts", true, FILE_MAIN, "Players will recieve predefined loadouts.", "Players will start with empty inventories."));
		settings.add(TEAM_HATS = new Setting<Boolean>("TeamHats", true, FILE_MAIN, "Players get special, identifying hats.", "Players do not have special hats."));
		settings.add(NO_BUILD = new Setting<Boolean>("NoBuild", true, FILE_MAIN, "Players cannot build unless the game is running.", "Players can edit the world when the game is not running."));
		
		settings.add(PASSIVE_MOBS = new Setting<Boolean>("PassiveMobs", true, FILE_MAIN, "Passive mobs are enabled.", "Passive mobs are disabled."));
		settings.add(HOSTILE_MOBS = new Setting<Boolean>("HostileMobs", true, FILE_MAIN, "Hostile mobs are enabled.", "Hostile mobs are disabled."));
		
		settings.add(OFFLINE_TIMEOUT = new Setting<Integer>("OfflineTimeout", 60, FILE_MAIN, "Seconds until offline players disqualify.", "Offline players will not disqualify."));
		settings.add(DAY_LIMIT = new Setting<Integer>("DayLimit", 3, FILE_MAIN, "How many dats the game will last.", "The manhunt game will never end."));
		settings.add(SETUP_TIME = new Setting<Integer>("SetupTime", 10, FILE_MAIN, "How many minutes the prey have to prepare.", "The game starts immediately with no setup."));
		settings.add(INTERMISSION = new Setting<Integer>("Intermission", 10, FILE_MAIN, "Minutes between Manhunt games.", "One minute intermission between games. (minimum)"));
		settings.add(MINIMUM_PLAYERS = new Setting<Integer>("MinimumPlayers", 4, FILE_MAIN, "Players needed to start a public game.", "No limit to number of players."));
		
		settings.add(PREY_FINDER = new Setting<Boolean>("PreyFinder", true, FILE_MAIN, "Hunters may use the Prey Finder.", "The compass is just a regular compass."));
		settings.add(FINDER_COOLDOWN = new Setting<Integer>("FinderCooldown", 180, FILE_MAIN, "Seconds until the PreyFinder is ready.", "The PreyFinder has no cooldown delay."));
		//settings.add(NORTH_COMPASS = new Setting<Boolean>("NorthCompass", true, FILE_MAIN, "Compasses will always point north.", "Compasses will always point towards spawn."));
		
		this.world_path = (Bukkit.getWorld(WORLD.value) == null ? Bukkit.getWorlds().get(0).getName() : WORLD.value) + "/Manhunt";
		files.add(FILE_WORLD = new SettingsFile("World Config", world_path, "Config"));
		
		settings.add(RESTORE_WORLD = new Setting<Boolean>("RestoreWorld", true, FILE_WORLD, "Automatically restore the world after each game using Amber.", "Does not use the Amber plugin."));
		settings.add(BOUNDARY_WORLD = new Setting<Integer>("WorldBoundary", 128, FILE_WORLD, "Blocks players may roam during the hunt.", "There is no boundary around the world."));
		settings.add(BOUNDARY_SETUP = new Setting<Integer>("SetupBoundary", 16, FILE_WORLD, "Blocks Hunters are confined to.", "The hunters are not constrained during setup."));
		settings.add(SPAWN_PROTECTION = new Setting<Integer>("SpawnProtection", 24, FILE_WORLD, "The protected region around the spawns.", "The spawn points are not protected."));
		settings.add(BOUNDARY_BOXED = new Setting<Boolean>("BoxedBoundary", true, FILE_WORLD, "The world's boundary is rectangular.", "The world's shape is rounded."));
		
		secretSettings.add(HUNTER_LOADOUT_CURRENT = new Setting<String>("CurrentHunterLoadout", "default", FILE_WORLD, "The loadout the hunter get.", ""));
		secretSettings.add(PREY_LOADOUT_CURRENT = new Setting<String>("CurrentPreyLoadout", "default", FILE_WORLD, "The loadout the prey get.", ""));
		
		secretSettings.add(SPAWN_HUNTER = new Setting<Location>("HunterSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_PREY = new Setting<Location>("PreySpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_SETUP = new Setting<Location>("SetupSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		
		saveAll();
	}

	public void saveAll()
	{
		for (SettingsFile file : files)
			file.saveFile();
	}

	public void reloadAll()
	{
		for (Setting<?> setting : settings)
		{
			setting.load();
		}
		for (Setting<?> setting : secretSettings)
		{
			setting.load();
		}
	}

	public Setting<?> getSetting(String label)
	{
		for (Setting<?> setting : settings)
		{
			if (setting.label.equalsIgnoreCase(label))
				return setting;
		}
		return null;
	}

	public List<Setting<?>> getAllSettings()
	{
		return settings;
	}

	public void loadDefaults()
	{
		for (Setting<?> setting : settings)
		{
			setting.reset(false);
		}
		for (Setting<?> setting : secretSettings)
		{
			setting.reset(false);
		}
		saveAll();
	}
}
