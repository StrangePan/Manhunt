package com.bendude56.hunted.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import com.bendude56.hunted.HuntedPlugin;

public class SettingsFile extends Properties
{
	private static final long serialVersionUID = -1144343836355280845L;
	private final String directory;
	private final String filename;
	
	private List<Setting<?>> settings = new ArrayList<Setting<?>>();

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
	
	public final Setting<Boolean> PASSIVE_MOBS;
	public final Setting<Boolean> HOSTILE_MOBS;
	public final Setting<Boolean> ENVIRONMENT_RESPAWN;
	public final Setting<Boolean> ENVIRONMENT_DEATH;
	public final Setting<Boolean> PREY_FINDER;
	
	public final Setting<Integer> OFFLINE_TIMEOUT;
	public final Setting<Integer> DAY_LIMIT;
	public final Setting<Integer> FINDER_COOLDOWN;
	public final Setting<Integer> SETUP_TIME;

	
	public SettingsFile()
	{
		directory = "plugins/Manhunt/";
		filename = directory + "Manhunt.properties";
		
		settings.add(WORLD = new Setting<String>("world", "world", this, "The Manhunt world.", ""));

		settings.add(OP_CONTROL = new Setting<Boolean>("opControl", true, this, "Only ops have access to all commands.", "Non-ops have access to basic controls."));
		settings.add(PUBLIC_MODE = new Setting<Boolean>("publicMode", true, this, "The game is running in public mode.", "The game is running in private mode."));
		settings.add(AUTO_JOIN = new Setting<Boolean>("autoJoin", true, this, "New players automatically join team Hunters.", "New players will remain spectators."));

		settings.add(ALL_TALK = new Setting<Boolean>("allTalk", false, this, "Teams can communicate with each other.", "Teams cannot communicate with each other."));
		settings.add(FRIENDLY_FIRE = new Setting<Boolean>("friendlyFire", false, this, "Teammates can damage each other.", "Teammates cannot hurt each other."));
		settings.add(INSTANT_DEATH = new Setting<Boolean>("instantDeath", false, this, "Players will die in one hit.", "Players take damage like normal."));
		settings.add(FLYING_SPECTATORS = new Setting<Boolean>("flyingSpectators", true, this, "Spectators can fly in creative mode.", "Spectators are bound to the ground."));
		settings.add(LOADOUTS = new Setting<Boolean>("loadouts", true, this, "Players will recieve predefined loadouts.", "Players will start with empty inventories."));
		settings.add(NORTH_COMPASS = new Setting<Boolean>("northCompass", true, this, "Compasses will always point north.", "Compasses will always point towards spawn."));
		settings.add(TEAM_HATS = new Setting<Boolean>("teamHats", true, this, "Players get special, identifying hats.", "Players do not have special hats."));
		
		settings.add(PASSIVE_MOBS = new Setting<Boolean>("passiveMobs", true, this, "Passive mobs are enabled.", "Passive mobs are disabled."));
		settings.add(HOSTILE_MOBS = new Setting<Boolean>("hostileMobs", true, this, "Hostile mobs are enabled.", "Hostile mobs are disabled."));
		settings.add(ENVIRONMENT_RESPAWN = new Setting<Boolean>("envRespawn", true, this, "Players will respawn when dying from the environment.", "Players will not respawn when dying from the environment."));
		settings.add(ENVIRONMENT_DEATH = new Setting<Boolean>("envDeath", false, this, "Players can die from the environment.", "Players cannot die from the environment."));
		settings.add(PREY_FINDER = new Setting<Boolean>("preyFinder", true, this, "Hunters may use the Prey Finder.", "The compass is just a regular compass."));
		
		settings.add(OFFLINE_TIMEOUT = new Setting<Integer>("offlineTimeout", 60, this, "How many seconds until offline players are disqualified.", "Offline players will not be disqualified."));
		settings.add(DAY_LIMIT = new Setting<Integer>("dayLimit", 3, this, "How many dats the game will last.", "The manhunt game will never end."));
		settings.add(FINDER_COOLDOWN = new Setting<Integer>("finderCooldown", 3, this, "The Preyfinder cooldown delay in minutes.", "The PreyFinder has no cooldown delay."));
		settings.add(SETUP_TIME = new Setting<Integer>("setupTime", 10, this, "How many minutes the prey have to prepare.", "The game starts immediately with no setup."));
		
		saveFile();
	}
	
	public void loadFile() {
		File file = new File(filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Manhunt config file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			load(new FileInputStream(file));
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Manhunt config file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
		}
		saveFile();
	}
	
	public void saveFile() {
		File file = new File(filename);
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				HuntedPlugin.getInstance().log(Level.SEVERE,
						"Problem loading the Hunted config file!");
				HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try {
			store(new FileOutputStream(file),
					"- Manhunt Configurations and Settings -");
		} catch (IOException e) {
			HuntedPlugin.getInstance().log(Level.SEVERE,
					"Problem loading the Hunted config file!");
			HuntedPlugin.getInstance().log(Level.SEVERE, e.getMessage());
			return;
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

	public void reloadAll()
	{
		for (Setting<?> setting : settings)
		{
			setting.load();
		}
	}

	public void loadDefaults()
	{
		for (Setting<?> setting : settings)
		{
			setting.reset();
		}
	}
}
