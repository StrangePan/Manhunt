package com.bendude56.hunted.settings;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.bendude56.hunted.ManhuntPlugin.ManhuntMode;


public class OldSettingsManager
{
	private List<OldSettingsFile> files = new ArrayList<OldSettingsFile>();
	
	public final OldSettingsFile FILE_MAIN;
	public final OldSettingsFile FILE_WORLD;

	private String plugin_path = "plugins/Manhunt";
	private String world_path;
	
	private List<OldSetting<?>> settings = new ArrayList<OldSetting<?>>();
	private List<OldSetting<?>> secretSettings = new ArrayList<OldSetting<?>>();

	public final OldSetting<ManhuntMode> MANHUNT_MODE;

	public final OldSetting<String> WORLD;
	public final OldSetting<String> HUNTER_LOADOUT_CURRENT;
	public final OldSetting<String> PREY_LOADOUT_CURRENT;

	public final OldSetting<Boolean> OP_CONTROL;
	public final OldSetting<Boolean> AUTO_JOIN;

	public final OldSetting<Boolean> ALL_TALK;
	public final OldSetting<Boolean> CONTROL_CHAT;
	public final OldSetting<Boolean> FRIENDLY_FIRE;
	public final OldSetting<Boolean> INSTANT_DEATH;
	public final OldSetting<Boolean> FLYING_SPECTATORS;
	public final OldSetting<Boolean> LOADOUTS;
	public final OldSetting<Boolean> TEAM_HATS;
	public final OldSetting<Boolean> NO_BUILD;
	
	public final OldSetting<Boolean> PASSIVE_MOBS;
	public final OldSetting<Boolean> HOSTILE_MOBS;
	public final OldSetting<Boolean> PREY_FINDER;
	//public final Setting<Boolean> NORTH_COMPASS;
	public final OldSetting<Integer> FINDER_COOLDOWN;
	
	public final OldSetting<Integer> OFFLINE_TIMEOUT;
	public final OldSetting<Integer> DAY_LIMIT;
	public final OldSetting<Integer> SETUP_TIME;
	public final OldSetting<Integer> INTERMISSION;
	public final OldSetting<Integer> MINIMUM_PLAYERS;
	
	public final OldSetting<Boolean> RESTORE_WORLD;
	public final OldSetting<Integer> BOUNDARY_WORLD;
	public final OldSetting<Integer> BOUNDARY_SETUP;
	public final OldSetting<Integer> SPAWN_PROTECTION;
	public final OldSetting<Boolean> BOUNDARY_BOXED;
	
	public final OldSetting<Location> SPAWN_HUNTER;
	public final OldSetting<Location> SPAWN_PREY;
	public final OldSetting<Location> SPAWN_SETUP;

	
	public OldSettingsManager()
	{
		files.add(FILE_MAIN = new OldSettingsFile("Main Config", plugin_path, "Manhunt"));
		
		secretSettings.add(WORLD = new OldSetting<String>("World", "world", FILE_MAIN, "The Manhunt world.", ""));
		if (Bukkit.getWorld(WORLD.value) == null)
		{
			WORLD.setValue(Bukkit.getWorlds().get(0).getName());
		}
		
		secretSettings.add(MANHUNT_MODE = new OldSetting<ManhuntMode>("GameMode", ManhuntMode.PRIVATE, FILE_MAIN, "The type of mode Manhunt is running in.", ""));
		
		settings.add(OP_CONTROL = new OldSetting<Boolean>("OpControl", true, FILE_MAIN, "Only ops have access to all commands.", "Non-ops have access to basic controls."));
		settings.add(AUTO_JOIN = new OldSetting<Boolean>("AutoJoin", true, FILE_MAIN, "New players automatically join team Hunters.", "New players will remain spectators."));

		settings.add(ALL_TALK = new OldSetting<Boolean>("AllTalk", false, FILE_MAIN, "Teams can communicate with each other.", "Teams cannot communicate with each other."));
		settings.add(CONTROL_CHAT = new OldSetting<Boolean>("ChatControl", true, FILE_MAIN, "The Manhunt plugin will control chat.", "Manhunt will not touch the chat."));
		settings.add(FRIENDLY_FIRE = new OldSetting<Boolean>("FriendlyFire", false, FILE_MAIN, "Teammates can damage each other.", "Teammates cannot hurt each other."));
		settings.add(INSTANT_DEATH = new OldSetting<Boolean>("InstantDeath", false, FILE_MAIN, "Players will die in one hit.", "Players take damage like normal."));
		settings.add(FLYING_SPECTATORS = new OldSetting<Boolean>("FlyingSpectators", true, FILE_MAIN, "Spectators can fly in creative mode.", "Spectators are bound to the ground."));
		settings.add(LOADOUTS = new OldSetting<Boolean>("Loadouts", true, FILE_MAIN, "Players will recieve predefined loadouts.", "Players will start with empty inventories."));
		settings.add(TEAM_HATS = new OldSetting<Boolean>("TeamHats", true, FILE_MAIN, "Players get special, identifying hats.", "Players do not have special hats."));
		settings.add(NO_BUILD = new OldSetting<Boolean>("NoBuild", true, FILE_MAIN, "Players cannot build unless the game is running.", "Players can edit the world when the game is not running."));
		
		settings.add(PASSIVE_MOBS = new OldSetting<Boolean>("PassiveMobs", true, FILE_MAIN, "Passive mobs are enabled.", "Passive mobs are disabled."));
		settings.add(HOSTILE_MOBS = new OldSetting<Boolean>("HostileMobs", true, FILE_MAIN, "Hostile mobs are enabled.", "Hostile mobs are disabled."));
		
		settings.add(OFFLINE_TIMEOUT = new OldSetting<Integer>("OfflineTimeout", 60, FILE_MAIN, "Seconds until offline players disqualify.", "Offline players will not disqualify."));
		settings.add(DAY_LIMIT = new OldSetting<Integer>("DayLimit", 3, FILE_MAIN, "How many dats the game will last.", "The manhunt game will never end."));
		settings.add(SETUP_TIME = new OldSetting<Integer>("SetupTime", 10, FILE_MAIN, "How many minutes the prey have to prepare.", "The game starts immediately with no setup."));
		settings.add(INTERMISSION = new OldSetting<Integer>("Intermission", 10, FILE_MAIN, "Minutes between Manhunt games.", "One minute intermission between games. (minimum)"));
		settings.add(MINIMUM_PLAYERS = new OldSetting<Integer>("MinimumPlayers", 4, FILE_MAIN, "Players needed to start a public game.", "No limit to number of players."));
		
		settings.add(PREY_FINDER = new OldSetting<Boolean>("PreyFinder", true, FILE_MAIN, "Hunters may use the Prey Finder.", "The compass is just a regular compass."));
		settings.add(FINDER_COOLDOWN = new OldSetting<Integer>("FinderCooldown", 180, FILE_MAIN, "Seconds until the PreyFinder is ready.", "The PreyFinder has no cooldown delay."));
		//settings.add(NORTH_COMPASS = new Setting<Boolean>("NorthCompass", true, FILE_MAIN, "Compasses will always point north.", "Compasses will always point towards spawn."));
		
		this.world_path = (Bukkit.getWorld(WORLD.value) == null ? Bukkit.getWorlds().get(0).getName() : WORLD.value) + "/Manhunt";
		files.add(FILE_WORLD = new OldSettingsFile("World Config", world_path, "Config"));
		
		settings.add(RESTORE_WORLD = new OldSetting<Boolean>("RestoreWorld", true, FILE_WORLD, "Automatically restore the world after each game using Amber.", "Does not use the Amber plugin."));
		settings.add(BOUNDARY_WORLD = new OldSetting<Integer>("WorldBoundary", 128, FILE_WORLD, "Blocks players may roam during the hunt.", "There is no boundary around the world."));
		settings.add(BOUNDARY_SETUP = new OldSetting<Integer>("SetupBoundary", 16, FILE_WORLD, "Blocks Hunters are confined to.", "The hunters are not constrained during setup."));
		settings.add(SPAWN_PROTECTION = new OldSetting<Integer>("SpawnProtection", 24, FILE_WORLD, "The protected region around the spawns.", "The spawn points are not protected."));
		settings.add(BOUNDARY_BOXED = new OldSetting<Boolean>("BoxedBoundary", true, FILE_WORLD, "The world's boundary is rectangular.", "The world's shape is rounded."));
		
		secretSettings.add(HUNTER_LOADOUT_CURRENT = new OldSetting<String>("CurrentHunterLoadout", "default", FILE_WORLD, "The loadout the hunter get.", ""));
		secretSettings.add(PREY_LOADOUT_CURRENT = new OldSetting<String>("CurrentPreyLoadout", "default", FILE_WORLD, "The loadout the prey get.", ""));
		
		secretSettings.add(SPAWN_HUNTER = new OldSetting<Location>("HunterSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_PREY = new OldSetting<Location>("PreySpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_SETUP = new OldSetting<Location>("SetupSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		
		saveAll();
	}

	public void saveAll()
	{
		for (OldSettingsFile file : files)
			file.saveFile();
	}

	public void reloadAll()
	{
		for (OldSetting<?> setting : settings)
		{
			setting.load();
		}
		for (OldSetting<?> setting : secretSettings)
		{
			setting.load();
		}
	}

	public OldSetting<?> getSetting(String label)
	{
		for (OldSetting<?> setting : settings)
		{
			if (setting.label.equalsIgnoreCase(label))
				return setting;
		}
		return null;
	}

	public List<OldSetting<?>> getAllSettings()
	{
		return settings;
	}

	public void loadDefaults()
	{
		for (OldSetting<?> setting : settings)
		{
			setting.reset(false);
		}
		for (OldSetting<?> setting : secretSettings)
		{
			setting.reset(false);
		}
		saveAll();
	}
}
