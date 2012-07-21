package com.bendude56.hunted.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;


public class SettingsFile
{
	private List<ManhuntFile> files = new ArrayList<ManhuntFile>();
	
	public final ManhuntFile FILE_MAIN;
	public final ManhuntFile FILE_WORLD;
	
	
	private List<Setting<?>> settings = new ArrayList<Setting<?>>();
	private List<Setting<?>> secretSettings = new ArrayList<Setting<?>>();

	public final Setting<String> WORLD;

	public final Setting<Boolean> OP_CONTROL;
	public final Setting<Boolean> PUBLIC_MODE;
	public final Setting<Boolean> AUTO_JOIN;

	public final Setting<Boolean> ALL_TALK;
	public final Setting<Boolean> FRIENDLY_FIRE;
	public final Setting<Boolean> INSTANT_DEATH;
	public final Setting<Boolean> FLYING_SPECTATORS;
	public final Setting<Boolean> LOADOUTS;
	public final Setting<Boolean> NORTH_COMPASS;
	public final Setting<Boolean> TEAM_HATS;
	public final Setting<Boolean> NO_BUILD;
	
	public final Setting<Boolean> PASSIVE_MOBS;
	public final Setting<Boolean> HOSTILE_MOBS;
	public final Setting<Boolean> ENVIRONMENT_RESPAWN;
	public final Setting<Boolean> ENVIRONMENT_DEATH;
	public final Setting<Boolean> PREY_FINDER;
	
	public final Setting<Integer> OFFLINE_TIMEOUT;
	public final Setting<Integer> DAY_LIMIT;
	public final Setting<Integer> FINDER_COOLDOWN;
	public final Setting<Integer> SETUP_TIME;
	
	public final Setting<Integer> BOUNDARY_WORLD;
	public final Setting<Integer> BOUNDARY_SETUP;
	public final Setting<Integer> SPAWN_PROTECTION;
	public final Setting<Boolean> BOUNDARY_BOXED;
	
	public final Setting<Location> SPAWN_HUNTER;
	public final Setting<Location> SPAWN_PREY;
	public final Setting<Location> SPAWN_SETUP;

	
	public SettingsFile()
	{
		files.add(FILE_MAIN = new ManhuntFile("Main Config", "plugins/Manhunt", "Manhunt.properties"));
		
		secretSettings.add(WORLD = new Setting<String>("world", "world", FILE_MAIN, "The Manhunt world.", ""));
		settings.add(OP_CONTROL = new Setting<Boolean>("opControl", true, FILE_MAIN, "Only ops have access to all commands.", "Non-ops have access to basic controls."));
		settings.add(PUBLIC_MODE = new Setting<Boolean>("publicMode", true, FILE_MAIN, "The game is running in public mode.", "The game is running in private mode."));
		settings.add(AUTO_JOIN = new Setting<Boolean>("autoJoin", true, FILE_MAIN, "New players automatically join team Hunters.", "New players will remain spectators."));

		settings.add(ALL_TALK = new Setting<Boolean>("allTalk", false, FILE_MAIN, "Teams can communicate with each other.", "Teams cannot communicate with each other."));
		settings.add(FRIENDLY_FIRE = new Setting<Boolean>("friendlyFire", false, FILE_MAIN, "Teammates can damage each other.", "Teammates cannot hurt each other."));
		settings.add(INSTANT_DEATH = new Setting<Boolean>("instantDeath", false, FILE_MAIN, "Players will die in one hit.", "Players take damage like normal."));
		settings.add(FLYING_SPECTATORS = new Setting<Boolean>("flyingSpectators", true, FILE_MAIN, "Spectators can fly in creative mode.", "Spectators are bound to the ground."));
		settings.add(LOADOUTS = new Setting<Boolean>("loadouts", true, FILE_MAIN, "Players will recieve predefined loadouts.", "Players will start with empty inventories."));
		settings.add(NORTH_COMPASS = new Setting<Boolean>("northCompass", true, FILE_MAIN, "Compasses will always point north.", "Compasses will always point towards spawn."));
		settings.add(TEAM_HATS = new Setting<Boolean>("teamHats", true, FILE_MAIN, "Players get special, identifying hats.", "Players do not have special hats."));
		settings.add(NO_BUILD = new Setting<Boolean>("noBuild", true, FILE_MAIN, "Players cannot build unless the game is running.", "Players can edit the world when the game is not running."));
		
		settings.add(PASSIVE_MOBS = new Setting<Boolean>("passiveMobs", true, FILE_MAIN, "Passive mobs are enabled.", "Passive mobs are disabled."));
		settings.add(HOSTILE_MOBS = new Setting<Boolean>("hostileMobs", true, FILE_MAIN, "Hostile mobs are enabled.", "Hostile mobs are disabled."));
		settings.add(ENVIRONMENT_RESPAWN = new Setting<Boolean>("envRespawn", true, FILE_MAIN, "Players will respawn when dying from the environment.", "Players will not respawn when dying from the environment."));
		settings.add(ENVIRONMENT_DEATH = new Setting<Boolean>("envDeath", false, FILE_MAIN, "Players can die from the environment.", "Players cannot die from the environment."));
		settings.add(PREY_FINDER = new Setting<Boolean>("preyFinder", true, FILE_MAIN, "Hunters may use the Prey Finder.", "The compass is just a regular compass."));
		
		settings.add(OFFLINE_TIMEOUT = new Setting<Integer>("offlineTimeout", 60, FILE_MAIN, "How many seconds until offline players are disqualified.", "Offline players will not be disqualified."));
		settings.add(DAY_LIMIT = new Setting<Integer>("dayLimit", 3, FILE_MAIN, "How many dats the game will last.", "The manhunt game will never end."));
		settings.add(FINDER_COOLDOWN = new Setting<Integer>("finderCooldown", 3, FILE_MAIN, "The Preyfinder cooldown delay in minutes.", "The PreyFinder has no cooldown delay."));
		settings.add(SETUP_TIME = new Setting<Integer>("setupTime", 10, FILE_MAIN, "How many minutes the prey have to prepare.", "The game starts immediately with no setup."));


		files.add(FILE_WORLD = new ManhuntFile("World Config", (Bukkit.getWorld(WORLD.value) == null ? Bukkit.getWorlds().get(0).getName() : WORLD.value) + "/Manhunt", "World_Config.properties"));
		
		settings.add(BOUNDARY_WORLD = new Setting<Integer>("worldBoundary", 128, FILE_WORLD, "How far players may roam during the hunt.", "There is no boundary around the world."));
		settings.add(BOUNDARY_SETUP = new Setting<Integer>("setupBoundary", 16, FILE_WORLD, "The region in which the hunters must wait.", "The hunters are not constrained during setup."));
		settings.add(SPAWN_PROTECTION = new Setting<Integer>("spawnProtection", 24, FILE_WORLD, "The protected region around the spawn points.", "The spawn points are not protected."));
		settings.add(BOUNDARY_BOXED = new Setting<Boolean>("boxedBoundary", true, FILE_WORLD, "The world's boundary is rectangular.", "The world's shape is rounded."));
		
		secretSettings.add(SPAWN_HUNTER = new Setting<Location>("hunterSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_PREY = new Setting<Location>("preySpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
		secretSettings.add(SPAWN_SETUP = new Setting<Location>("setupSpawn", Bukkit.getWorld(WORLD.value).getSpawnLocation(), FILE_WORLD, "",""));
	}

	public void saveAll()
	{
		for (ManhuntFile file : files)
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
