package com.bendude56.hunted.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import com.bendude56.hunted.HuntedPlugin;
import com.bendude56.hunted.ManhuntUtil;
import com.bendude56.hunted.games.ManhuntGame;
import com.bendude56.hunted.loadouts.Loadout;
import com.bendude56.hunted.loadouts.LoadoutManager;
import com.bendude56.hunted.settings.Setting;
import com.bendude56.hunted.settings.SettingsManager;

public class Switchboard implements CommandExecutor {

	public final static String pre = ChatColor.GOLD + "| " + ChatColor.WHITE;
	
	public Switchboard() {
		Bukkit.getPluginCommand("manhunt").setExecutor(this);
		Bukkit.getPluginCommand("m").setExecutor(this);
	}

	ManhuntGame g = HuntedPlugin.getInstance().getGame();
	SettingsManager settings = HuntedPlugin.getInstance().getSettings();
	LoadoutManager loadouts = HuntedPlugin.getInstance().getLoadouts();

	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED
					+ "The /manhunt command cannot be used from the console!");
			return true;
		}
		Player p = (Player) sender;

		if (cmd.equalsIgnoreCase("manhunt") || cmd.equalsIgnoreCase("m")) {

			if (args.length == 0) {
				helpCommand(args, p);
				return true;
			}

			if (args[0].equalsIgnoreCase("help")) {
				args = shiftArgs(args);
				helpCommand(args, p);

			} else if (args[0].equalsIgnoreCase("1")
					|| args[0].equalsIgnoreCase("2")
					|| args[0].equalsIgnoreCase("3")) {
				helpCommand(args, p);

			} else if (args[0].equalsIgnoreCase("rules")
					|| args[0].equalsIgnoreCase("rule")) {
				args = shiftArgs(args);
				rulesCommand(args, p);

			} else if (args[0].equalsIgnoreCase("join")) {
				args = shiftArgs(args);
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED
							+ "Proper use is /m join <team>");
					return true;
				}
				joinCommand(args, p);

			} else if (args[0].equalsIgnoreCase("assign")) {
				args = shiftArgs(args);
				if (args.length < 2 || args.length == 0) {
					p.sendMessage(ChatColor.RED
							+ "Proper use is /m assign <player> <team>");
					return true;
				}
				if (!(args[0].equalsIgnoreCase("hunter")
						|| args[0].equalsIgnoreCase("hunters")
						|| args[0].equalsIgnoreCase("prey")
						|| args[0].equalsIgnoreCase("hunted")
						|| args[0].equalsIgnoreCase("spectators")
						|| args[0].equalsIgnoreCase("spectator") || args[0]
							.equals("spectate"))) {
					String a = args[0];
					args[0] = args[1];
					args[1] = a;
				}
				joinCommand(args, p);

			} else if (args[0].equalsIgnoreCase("hunter")
					|| args[0].equalsIgnoreCase("hunters")
					|| args[0].equalsIgnoreCase("prey")
					|| args[0].equalsIgnoreCase("hunted")) {
				joinCommand(args, p);

			} else if (args[0].equalsIgnoreCase("quit")) {
				args = shiftArgs(args);
				quitCommand(args, p);

			} else if (args[0].equalsIgnoreCase("kick")) {
				args = shiftArgs(args);
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED
							+ "Proper use is /kick <player>");
					return true;
				}
				kickCommand(args, p);

			} else if (args[0].equalsIgnoreCase("spectate")) {
				args = shiftArgs(args);
				spectateCommand(args, p);

			} else if (args[0].equalsIgnoreCase("makeall")
					|| args[0].equalsIgnoreCase("makeeveryone")) {
				args = shiftArgs(args);
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED
							+ "Proper use is /makeall <team>");
					return true;
				}
				makeallCommand(args, p);

			} else if (args[0].equalsIgnoreCase("list")) {
				args = shiftArgs(args);
				listCommand(args, p);

			} else if (args[0].equalsIgnoreCase("status")
					|| args[0].equalsIgnoreCase("info")) {
				args = shiftArgs(args);
				statusCommand(args, p);

			} else if (args[0].equalsIgnoreCase("world")
					|| args[0].equalsIgnoreCase("spawn")) {
				args = shiftArgs(args);
				spawnCommand(args, p);

			} else if (args[0].equalsIgnoreCase("setspawn")) {
				args = shiftArgs(args);
				setspawnCommand(args, p);

			} else if (args[0].equalsIgnoreCase("setworld")) {
				args = shiftArgs(args);
				setworldCommand(args, p);

			} else if (args[0].equalsIgnoreCase("startgame")) {
				args = shiftArgs(args);
				startCommand(args, p);

			} else if (args[0].equalsIgnoreCase("stopgame")) {
				args = shiftArgs(args);
				stopCommand(args, p);

			} else if (args[0].equalsIgnoreCase("restoredefaults")
					|| args[0].equalsIgnoreCase("defaultsettings")
					|| args[0].equalsIgnoreCase("loaddefaults")) {
				args = shiftArgs(args);
				defaultsCommand(args, p);

			} else if (args[0].equalsIgnoreCase("reload")
					|| args[0].equalsIgnoreCase("load")) {
				args = shiftArgs(args);
				loadCommand(args, p);

			} else if (args[0].equalsIgnoreCase("weather")) {
				args = shiftArgs(args);
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED
							+ "Proper use is /weather <clear|rainy|stormy>");
					return true;
				}
				weatherCommand(args, p);

			} else if (args[0].equalsIgnoreCase("loadout")
					|| args[0].equalsIgnoreCase("loadouts")
					|| args[0].equalsIgnoreCase("inventory")
					|| args[0].equalsIgnoreCase("inv")) {
				args = shiftArgs(args);
				loadoutCommand(args, p);

			} else if (args[0].equalsIgnoreCase("set")
					|| args[0].equalsIgnoreCase("setting")
					|| args[0].equalsIgnoreCase("settings")
					|| args[0].equalsIgnoreCase("preferences")
					|| args[0].equalsIgnoreCase("properties")) {
				args = shiftArgs(args);
				settingsCommand(args, p);

			} else {
				p.sendMessage(ChatColor.RED
						+ "Unknown Manhunt command! Type \"/manhunt help\" for help.");
			}
			return true;
		}

		return true;
	}

	private void helpCommand(String[] args, Player p) {
		String page = "1";

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("1")) {
				page = "1";
			}
			if (args[0].equalsIgnoreCase("2")) {
				page = "2";
			}
		}

		if (page.equalsIgnoreCase("1")) {
			p.sendMessage(ChatColor.GOLD + "---- Manhunt help: (1/2) ----");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m startgame"
					+ ChatColor.WHITE + " Starts the Manhunt game.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m stopgame"
					+ ChatColor.WHITE + " Stops the Manhunt game.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m hunter"
					+ ChatColor.WHITE + " Joins team " + ChatColor.DARK_RED
					+ "hunter.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m prey"
					+ ChatColor.WHITE + " Joins team " + ChatColor.BLUE
					+ "prey.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m spectate"
					+ ChatColor.WHITE + " Joins team " + ChatColor.WHITE
					+ "spectator.");
			p.sendMessage(pre + ChatColor.WHITE
					+ "Add a player's name to assign them to that team.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m list"
					+ ChatColor.WHITE + " Lists the Manhunt players.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m status"
					+ ChatColor.WHITE
					+ " Gives information on the current Manhunt game.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m quit"
					+ ChatColor.WHITE
					+ " Quits the Manhunt game and makes you a spectator.");
			if (args.length == 0)
				p.sendMessage(pre + ChatColor.DARK_GREEN + "Type \""
						+ ChatColor.RED + "/m help 2" + ChatColor.DARK_GREEN
						+ "\" to view page 2!");
		} else if (page.equalsIgnoreCase("2")) {
			p.sendMessage(ChatColor.GOLD + "---- Manhunt help: (2/2) ----");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m setting [page]"
					+ ChatColor.WHITE + " Lists current Manhunt settings.");
			p.sendMessage(pre + ChatColor.DARK_GREEN
					+ "/m setting [setting] [value]" + ChatColor.WHITE
					+ " Changes a Manhunt setting.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m spawn [team]"
					+ ChatColor.WHITE
					+ " Teleports you to the world/team spawn");
			p.sendMessage(pre + ChatColor.WHITE
					+ "Include a player's name to teleport them instead.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m setspawn [team]"
					+ ChatColor.WHITE + " Sets the world's or a team's spawn.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m setworld"
					+ ChatColor.WHITE
					+ " Sets the Manhunt world to the one you are in.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m makeall [team]"
					+ ChatColor.WHITE + " Assigns everyone to a specific team.");
			p.sendMessage(pre + ChatColor.DARK_GREEN + "/m kick [player]"
					+ ChatColor.WHITE + " Makes the palyer a spectator.");
		}
	}

	private void rulesCommand(String[] args, Player p) {
		if (args.length == 0 || args[0].equalsIgnoreCase("1")) {
			
			p.sendMessage(ChatColor.GOLD + "---- Manhunt General Rules (1/3) ----");
			p.sendMessage(pre + "Manhunt is an exciting game where two teams go");
			p.sendMessage(pre + "head-to-head: the " + ChatColor.DARK_RED + "Hunters" + ChatColor.WHITE + " and the " + ChatColor.BLUE + "Prey" + ChatColor.WHITE + ".");
			
			if (settings.SETUP_TIME.value != 0) {
				p.sendMessage(pre + "The " + ChatColor.BLUE + "Prey" + ChatColor.WHITE + " have " + settings.SETUP_TIME.value + " minutes to prepare before the hunt.");
			} else {
				p.sendMessage(pre + "The hunt starts immediately with no preparation time.");
			}
			
			p.sendMessage(pre + "When the game starts (at sundown), the " + ChatColor.DARK_RED + "Hunters");
			p.sendMessage(pre + "are released to hunt and kill the " + ChatColor.BLUE + "Prey" + ChatColor.WHITE + ".");
			p.sendMessage(pre + "The " + ChatColor.DARK_RED + "Hunters" + ChatColor.WHITE + " have " + settings.DAY_LIMIT.value + " days to kill all the " + ChatColor.BLUE + "Prey" + ChatColor.WHITE + ".");
			p.sendMessage(pre + "The first team to wipe out the other " + ChatColor.GOLD + "wins the game!");
			p.sendMessage(pre + "But if time runs out, the " + ChatColor.BLUE + "Prey" + ChatColor.WHITE + " will win.");
			
			if (args.length == 0)
				p.sendMessage(pre + ChatColor.DARK_GREEN + "Type \"" + ChatColor.RED + "/m rules 2" + ChatColor.DARK_GREEN + "\" to view page 2!");

		} else if (args[0].equalsIgnoreCase("2")) {
			p.sendMessage(ChatColor.GOLD + "---- Manhunt Detailed Rules (2/3) ----");
			
			if (settings.LOADOUTS.value) {
				p.sendMessage(pre + "Every player will get their own loadouts to start with.");
				
				if (settings.TEAM_HATS.value) {
					p.sendMessage(pre + "And each team gets identifying hats.");
				}
			}
			p.sendMessage(pre + "All players must remain above ground, and no spawn camping!");
			
			if (settings.INSTANT_DEATH.value)
				p.sendMessage(pre + "Insta-kill is ON, so everyone is a one-hit kill.");
			
			if (settings.ENVIRONMENT_DEATH.value) {
				if (settings.ENVIRONMENT_RESPAWN.value) {
					p.sendMessage(pre + "If someone dies from the environment, they will respawn.");
				} else {
					p.sendMessage(pre + "Players are elminiated when killed by the environment");
				}
			} else {
				p.sendMessage(pre + "You cannot die by the environment.");
			}
			
			if (settings.PREY_FINDER.value) {
				p.sendMessage(pre + ChatColor.DARK_RED + "Hunter" + ChatColor.WHITE + " compasses can be used as Prey Finders.");
				p.sendMessage(pre + ChatColor.GRAY + " (Requires at least " + g.locatorFood() / 2 + " food! Click while holding compass)");
			}
			
			if (settings.HOSTILE_MOBS.value) {
				p.sendMessage(pre + "Hostiles mobs are turned ON.");
			} else {
				p.sendMessage(pre + "Hostiles mobs are turned OFF.");
			}
			
			if (settings.ALL_TALK.value) {
				p.sendMessage(pre + "All talk is on, so all players can chat together.");
			} else {
				p.sendMessage(pre + "All talk is off, so only teammates can chat with eachother.");
			}
			
		} else if (args[0].equalsIgnoreCase("3")) {
			
			p.sendMessage(ChatColor.GOLD + "---- Manhunt Miscellaneous Rules (3/3) ---");
			p.sendMessage(pre + "Players may not build " + settings.SPAWN_PROTECTION.value + " blocks from the spawn.");
			p.sendMessage(pre + "Players have " + settings.OFFLINE_TIMEOUT.value + " minutes to return to the game.");
			
			if (settings.FLYING_SPECTATORS.value) {
				p.sendMessage(pre + "Spectators are invisible, and can fly freely.");
			} else {
				p.sendMessage(pre + "Spectators are invisible to other players");
			}
			
			if (settings.FRIENDLY_FIRE.value) {
				p.sendMessage(pre + "Friendly fire is ON, so be careful.");
			} else {
				p.sendMessage(pre + "Friendly fire is turned OFF.");
			}
		}
	}

	private void startCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only ops can start the manhunt game!");
			return;
		}
		if (g.HuntersAmount(true) == 0 || g.HuntedAmount(true) == 0) {
			p.sendMessage(ChatColor.RED
					+ "There must be at least one Hunter and Prey to start the game!");
			return;
		} else {
			// if (g.HuntersAmount(true) < (g.HuntedAmount(true)-1)*3) {
			// p.sendMessage(ChatColor.RED
			// + "There must be at least 3 hunters per prey!");
			// return;
			// }
		}
		HuntedPlugin.getInstance().log(Level.INFO,
				p.getName() + " has started the Manhunt game!");
		g.start();
		return;
	}

	private void stopCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED + "Only ops can stop the Manhunt game!");
			return;
		}
		if (!g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED + "There is no Manhunt game running!");
			return;
		}
		g.stop();
		g.broadcastAll(ChatColor.GOLD
				+ "-----------------------------------------------------");
		g.broadcastAll(ChatColor.GOLD + "" + g.getColor(p) + p.getName()
				+ ChatColor.GOLD + " has stopped the Manhunt game!");
		g.broadcastAll(ChatColor.GOLD
				+ "-----------------------------------------------------");
		HuntedPlugin.getInstance().log(Level.INFO,
				"-------------------------------------");
		HuntedPlugin.getInstance().log(Level.INFO,
				"" + p.getName() + " has stopped the Manhunt game!");
		HuntedPlugin.getInstance().log(Level.INFO,
				"-------------------------------------");
		return;
	}

	private void joinCommand(String[] args, Player p) {
		if (g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED
					+ "You cannot join a game already in progress!");
			return;
		} else if (settings.OP_CONTROL.value && !p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only OPs have permission to do that.");
			return;
		}
		if (args.length == 0) {
			p.sendMessage(ChatColor.RED
					+ "You must choose a team! Hunters or Prey.");
			return;
		}
		if (args[0].equalsIgnoreCase("hunter")
				|| args[0].equalsIgnoreCase("hunters")) {
			if (args.length == 1) {
				g.addHunter(p);
				g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE
						+ " has joined team " + ChatColor.DARK_RED + "Hunters.");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " has joined team Hunters.");
			} else if (args.length > 1 && p.isOp()) {
				if (Bukkit.getPlayerExact(args[1]) == null) {
					p.sendMessage(ChatColor.RED + "\"" + args[1]
							+ "\" does not exist!");
					return;
				}
				g.addHunter(Bukkit.getPlayerExact(args[1]).getName());
				g.broadcastAll(ChatColor.DARK_RED
						+ Bukkit.getPlayerExact(args[1]).getName()
						+ ChatColor.WHITE + " has joined team "
						+ ChatColor.DARK_RED + "Hunters.");
				HuntedPlugin.getInstance().log(
						Level.INFO,
						Bukkit.getPlayerExact(args[1]).getName()
								+ " has joined team Hunters.");
			}
		} else if (args[0].equalsIgnoreCase("hunted")
				|| args[0].equalsIgnoreCase("prey")) {
			if (args.length == 1) {
				g.addHunted(p);
				g.broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE
						+ " has joined team " + ChatColor.BLUE + "Prey.");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " has joined team Prey.");
			} else if (args.length > 1 && p.isOp()) {
				if (Bukkit.getPlayerExact(args[1]) == null) {
					p.sendMessage(ChatColor.RED + "\"" + args[1]
							+ "\" does not exist!");
					return;
				}
				g.addHunted(Bukkit.getPlayerExact(args[1]).getName());
				g.broadcastAll(ChatColor.BLUE
						+ Bukkit.getPlayerExact(args[1]).getName()
						+ ChatColor.WHITE + " has joined team "
						+ ChatColor.BLUE + "Prey.");
				HuntedPlugin.getInstance().log(
						Level.INFO,
						Bukkit.getPlayerExact(args[1]).getName()
								+ " has joined team Prey.");
			}
		} else if (args[0].equalsIgnoreCase("spectators")
				|| args[0].equalsIgnoreCase("spectator")) {
			if (args.length == 1) {
				g.addSpectator(p);
				g.broadcastAll(ChatColor.YELLOW + p.getName() + ChatColor.WHITE
						+ " has become a " + ChatColor.YELLOW + "Spectator.");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " has become a spectator.");
			} else if (args.length > 1 && p.isOp()) {
				if (Bukkit.getPlayerExact(args[1]) == null) {
					p.sendMessage(ChatColor.RED + "\"" + args[1]
							+ "\" does not exist!");
					return;
				}
				g.addHunted(Bukkit.getPlayerExact(args[1]).getName());
				g.broadcastAll(ChatColor.YELLOW
						+ Bukkit.getPlayerExact(args[1]).getName()
						+ ChatColor.WHITE + " has become a " + ChatColor.YELLOW
						+ "Spectator.");
				HuntedPlugin.getInstance().log(
						Level.INFO,
						Bukkit.getPlayerExact(args[1]).getName()
								+ " has become a spectator.");
			}
		} else {
			p.sendMessage(ChatColor.RED
					+ args[0]
					+ " is not a team! Choose either Hunters, Prey, or Spectators.");
		}
	}

	private void quitCommand(String[] args, Player p) {
		if (g.isHunted(p) || g.isHunter(p)) {
			if (g.gameHasBegun()) {
				g.broadcastAll(ChatColor.YELLOW + p.getName() + ChatColor.WHITE
						+ " has quit the game and become a " + ChatColor.YELLOW
						+ "Spectator.");
				g.onDie(p);
				HuntedPlugin.getInstance().log(
						Level.INFO,
						p.getName()
								+ " has quit the game and is now spectating.");
				return;
			} else {
				g.addSpectator(p);
				g.broadcastAll(ChatColor.YELLOW + p.getName() + ChatColor.WHITE
						+ " has become a " + ChatColor.YELLOW + "Spectator.");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " has become a spectator.");
				return;
			}
		}
	}

	private void kickCommand(String[] args, Player p) {
		if (args.length >= 1) {
			Player p2 = Bukkit.getPlayerExact(args[0]);
			if (p2 == null) {
				if (g.isHunted(args[0]) || g.isHunter(args[0])
						|| g.isSpectating(args[0])) {
					g.onDie(args[0]);
					HuntedPlugin.getInstance().log(
							Level.INFO,
							args[0] + " was removed from the Manhunt lists by "
									+ p.getName());
					g.deletePlayer(args[0]);
				} else {
					p.sendMessage(ChatColor.RED + "No player named " + args[0]
							+ " exists!");
				}
				return;
			}
			if (p.isOp() && !p2.isOp() && p != p2) {
				g.broadcastAll(g.getColor(p2) + p2.getName() + ChatColor.WHITE
						+ " has become a " + ChatColor.YELLOW + "Spectator.");
				g.onDie(p2);
				p2.sendMessage(ChatColor.RED
						+ "You have been kicked from the game. You are now spectating.");
				HuntedPlugin.getInstance()
						.log(Level.INFO,
								args[0] + " was kicked from the game by "
										+ p.getName());
				return;
			} else if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "Only ops can do that!");
			} else if (p2.isOp()) {
				p.sendMessage(ChatColor.RED + "You cannot kick a fellow op!");
			} else if (p == p2) {
				p.sendMessage(ChatColor.RED
						+ "You're trying to kick yourself? Use \"/m quit\" instead!");
			}
		} else {
			p.sendMessage(ChatColor.RED + "Who did you want to kick?");
			return;
		}
	}

	private void spectateCommand(String[] args, Player p) {
		if ((g.isHunted(p) || g.isHunter(p)) && g.gameHasBegun()
				&& args.length == 0) {
			p.sendMessage(ChatColor.RED
					+ "You can't spectate while you're playing!");
			p.sendMessage(ChatColor.RED
					+ "Use \"/manhunt quit\" if you want to quit the game.");
			return;
		} else if (p.isOp() || !settings.OP_CONTROL.value) {
			if (args.length == 0) {
				g.addSpectator(p);
				g.broadcastAll(ChatColor.YELLOW + p.getName() + ChatColor.WHITE
						+ " has become a " + ChatColor.YELLOW + "Spectator.");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " has become a Spectator");
				return;
			} else if (args.length >= 1 && p.isOp()) {
				Player p2 = Bukkit.getPlayerExact(args[0]);
				if (p2 == null) {
					p.sendMessage(ChatColor.RED + args[0] + " does not exist!");
					return;
				}
				if ((g.isHunted(p2) || g.isHunter(p2)) && g.gameHasBegun()) {
					p.sendMessage(ChatColor.RED + args[0]
							+ " is in the middle of a game!");
					p.sendMessage(ChatColor.RED + "Use \"/manhunt kick <"
							+ args[0] + ">\" to properly kick them.");
					return;
				} else {
					g.broadcastAll(g.getColor(p2) + p2.getName()
							+ ChatColor.WHITE + " has become a "
							+ ChatColor.YELLOW + "Spectator.");
					g.addSpectator(p2);
					HuntedPlugin.getInstance().log(Level.INFO,
							p2.getName() + " has become a Spectator");
					return;
				}
			}
		}
	}

	private void makeallCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
			return;
		}
		if (g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED
					+ "You can't do that while the game is running!");
		}
		if (args.length == 0) {
			p.sendMessage(ChatColor.RED
					+ "You must specify a team to move everyone to!");
			return;
		}
		if (args[0].equalsIgnoreCase("hunter")
				|| args[0].equalsIgnoreCase("hunters")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunted(s) || g.isSpectating(s)) {
					g.addHunter(s);
					s.sendMessage(ChatColor.WHITE
							+ "You have been moved to team " + ChatColor.RED
							+ "Hunters.");
				}
			}
			g.broadcastAll(ChatColor.WHITE
					+ "All players have been moved to team "
					+ ChatColor.DARK_RED + "Hunters.");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " moved everyone to team Hunters.");
			return;
		} else if (args[0].equalsIgnoreCase("hunted")
				|| args[0].equalsIgnoreCase("prey")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunter(s) || g.isSpectating(s)) {
					g.addHunted(s);
					s.sendMessage(ChatColor.WHITE
							+ "You have been moved to team " + ChatColor.BLUE
							+ "Prey.");
				}
			}
			g.broadcastAll(ChatColor.WHITE
					+ "All players have been moved to team " + ChatColor.BLUE
					+ "Prey.");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " moved everyone to team Prey.");
			return;
		} else if (args[0].equalsIgnoreCase("spectator")
				|| args[0].equalsIgnoreCase("spectators")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunter(s) || g.isHunted(s)) {
					g.addSpectator(s);
					s.sendMessage(ChatColor.WHITE + "You are now a "
							+ ChatColor.YELLOW + "Spectator.");
				}
			}
			g.broadcastAll(ChatColor.WHITE + "All players are now "
					+ ChatColor.YELLOW + "Spectating.");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " made everyone spectators.");
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "Invalid team. {Hunters|Prey|Spectator}");
		}
		return;
	}

	private void listCommand(String[] args, Player p) {
		if (args.length == 0
				|| (args.length >= 1 && args[0].equalsIgnoreCase("all"))) {

			p.sendMessage(ChatColor.GREEN
					+ "Manhunt players: ("
					+ (g.HuntedAmount(false) + g.HuntersAmount(false) + g
							.SpectatorsAmount(false)) + ")  "
					+ ChatColor.DARK_RED + "Hunters: " + g.HuntersAmount(false)
					+ ChatColor.BLUE + "  Prey: " + g.HuntedAmount(false)
					+ ChatColor.YELLOW + "  Spectators: "
					+ g.SpectatorsAmount(false));

			int counter = 0;
			String stringList = "";
			for (String s : g.getHunters()) {
				if (Bukkit.getPlayerExact(s) == null) {
					stringList += ChatColor.WHITE + "  -  ";
					stringList += ChatColor.DARK_RED + s + " " + ChatColor.GRAY
							+ "(Offline)";
					counter += 2;
				} else {
					stringList += ChatColor.WHITE + "  -  ";
					stringList += ChatColor.DARK_RED + s;
					counter++;
				}
				if (counter >= 3) {
					counter = 0;
					p.sendMessage(stringList);
					stringList = "";
				}
			}
			if (!stringList.equalsIgnoreCase("")) {
				p.sendMessage(stringList);
				stringList = "";
			}
			for (String s : g.getHunted()) {
				if (Bukkit.getPlayerExact(s) == null) {
					stringList += ChatColor.WHITE + "  -  ";
					stringList += ChatColor.BLUE + s + " " + ChatColor.GRAY
							+ "(Offline)";
					counter += 2;
				} else {
					stringList += ChatColor.WHITE + "  -  ";
					stringList += ChatColor.BLUE + s;
					counter++;
				}
				if (counter >= 3) {
					counter = 0;
					p.sendMessage(stringList);
					stringList = "";
				}
			}
			if (!stringList.equalsIgnoreCase("")) {
				p.sendMessage(stringList);
				stringList = "";
			}
			for (String s : g.getSpectators()) {
				stringList += ChatColor.WHITE + "  -  ";
				stringList += ChatColor.YELLOW + s;
				counter++;
				if (counter >= 3) {
					counter = 0;
					p.sendMessage(stringList);
					stringList = "";
				}
			}
			if (!stringList.equalsIgnoreCase("")) {
				p.sendMessage(stringList);
				stringList = "";
			}
		} else if (args.length >= 1 && args[0].equalsIgnoreCase("hunters")) {
			p.sendMessage(ChatColor.GREEN + "Team HUNTERS: ("
					+ g.HuntersAmount(false) + ")");
			for (String s : g.getHunters()) {
				p.sendMessage(ChatColor.DARK_RED + "  " + s);
			}
		} else if (args.length >= 1 && args[0].equalsIgnoreCase("prey")) {
			p.sendMessage(ChatColor.GREEN + "Team HUNTED: ("
					+ g.HuntedAmount(false) + ")");
			for (String s : g.getHunted()) {
				p.sendMessage(ChatColor.BLUE + "  " + s);
			}
		} else if (args.length >= 2 && args[0].equalsIgnoreCase("spectators")) {
			p.sendMessage(ChatColor.GREEN + "Manhunt SPECTATORS: ("
					+ g.SpectatorsAmount(false) + ")");
			for (String s : g.getSpectators()) {
				p.sendMessage(ChatColor.YELLOW + "  " + s);
			}
		} else {
			p.sendMessage(ChatColor.RED
					+ "Invalid team. Available teams: Hunters, Prey, Spectators, All.");
		}
	}

	private void statusCommand(String[] args, Player p) {
		if (!g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED + "No running Manhunt games.");
			return;
		}
		p.sendMessage(ChatColor.AQUA + "Status of this Manhunt game");
		String time = "";
		if (g.huntHasBegun()) {

			time = "Time till game end:  ";
			time += (int) Math.floor((g.getEndTick() - g.getTick()) / 72000)
					+ ":";
			if ((int) (Math.floor((g.getEndTick() - g.getTick()) / 1200) - Math
					.floor((g.getEndTick() - g.getTick()) / 72000) * 60) < 10) {
				time += "0";
			}
			time += (int) (Math.floor((g.getEndTick() - g.getTick()) / 1200) - Math
					.floor((g.getEndTick() - g.getTick()) / 72000) * 60) + ":";
			if ((int) (Math.floor((g.getEndTick() - g.getTick()) / 20) - (Math
					.floor((g.getEndTick() - g.getTick()) / 1200) - Math
					.floor((g.getEndTick() - g.getTick()) / 72000)) * 60) < 10) {
				time += "0";
			}
			time += (int) (Math.floor((g.getEndTick() - g.getTick()) / 20) - (Math
					.floor((g.getEndTick() - g.getTick()) / 1200) - Math
					.floor((g.getEndTick() - g.getTick()) / 72000)) * 60)
					+ "";

		} else {

			time = "Time till hunt starts:  ";
			time += (int) Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 72000)
					+ ":";
			if ((int) (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 1200) - Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 72000) * 60) < 10) {
				time += "0";
			}
			time += (int) (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 1200) - Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 72000) * 60)
					+ ":";
			if ((int) (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 20) - (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 1200) - Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 72000)) * 60) < 10) {
				time += "0";
			}
			time += (int) (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 20) - (Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 1200) - Math
					.floor((g.getHunterReleaseTick() - g.getTick()) / 72000)) * 60)
					+ "";

		}
		p.sendMessage(ChatColor.GOLD + time);
		p.sendMessage(ChatColor.DARK_RED + "Hunters: " + g.HuntersAmount(false)
				+ ChatColor.BLUE + "  Prey: " + g.HuntedAmount(false)
				+ ChatColor.YELLOW + "  Spectators: "
				+ g.SpectatorsAmount(false));
	}

	private void spawnCommand(String[] args, Player p) {
		if (p.isOp() || !settings.OP_CONTROL.value || g.isSpectating(p)) {
			if (args.length == 0) {
				if (g.isSpectating(p) || !g.gameHasBegun()) {
					p.teleport(ManhuntUtil.safeTeleport(HuntedPlugin.getInstance().getWorld()
							.getSpawnLocation()));
					p.sendMessage(ChatColor.GREEN
							+ "You have teleported to the " + ChatColor.GOLD
							+ "Manhunt" + ChatColor.GREEN + " world spawn.");
					return;
				} else {
					p.sendMessage(ChatColor.RED
							+ "You can't warp to the Manhunt spawn once the game has started!");
					return;
				}
			} else if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("prey")
						|| args[0].equalsIgnoreCase("hunted")) {
					if (g.gameHasBegun() && !g.isSpectating(p)) {
						p.sendMessage(ChatColor.RED
								+ "You can't teleport there while the game is running!");
						return;
					}
					if (args.length == 1) {
						p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_PREY.value));
						p.sendMessage(ChatColor.GREEN
								+ "You have teleported to the "
								+ ChatColor.BLUE + "Prey" + ChatColor.GREEN
								+ " spawn.");
						return;
					} else if (args.length > 1) {
						if (!p.isOp()) {
							p.sendMessage(ChatColor.RED
									+ "You don't have permission to teleport other players!");
							return;
						}
						Player p2 = Bukkit.getPlayerExact(args[1]);
						if (p2 == null) {
							p.sendMessage(ChatColor.RED + "Player " + args[0]
									+ " does not exist!");
							return;
						}
						if (g.isSpectating(p2) || !g.gameHasBegun()) {
							p2.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_PREY.value));
							p2.sendMessage(ChatColor.YELLOW
									+ "You have been teleported to the "
									+ ChatColor.BLUE + "Prey"
									+ ChatColor.YELLOW + " spawn.");
							p.sendMessage(g.getColor(p2) + p2.getName()
									+ ChatColor.YELLOW
									+ " has been teleported to the "
									+ ChatColor.BLUE + "Prey"
									+ ChatColor.YELLOW + " spawn.");
							return;
						}
					}
				} else if (args[0].equalsIgnoreCase("hunter")
						|| args[0].equalsIgnoreCase("hunters")) {
					if (g.gameHasBegun() && !g.isSpectating(p)) {
						p.sendMessage(ChatColor.RED
								+ "You can't teleport there while the game is running!");
						return;
					}
					if (args.length == 1) {
						p.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_HUNTER.value));
						p.sendMessage(ChatColor.GREEN
								+ "You have teleported to the "
								+ ChatColor.DARK_RED + "Hunter"
								+ ChatColor.GREEN + " spawn.");
						return;
					} else if (args.length > 1) {
						if (!p.isOp()) {
							p.sendMessage(ChatColor.RED
									+ "You don't have permission to teleport other players!");
							return;
						}
						Player p2 = Bukkit.getPlayerExact(args[1]);
						if (p2 == null) {
							p.sendMessage(ChatColor.RED + "Player " + args[0]
									+ " does not exist!");
							return;
						}
						if (g.isSpectating(p2) || !g.gameHasBegun()) {
							p2.teleport(ManhuntUtil.safeTeleport(settings.SPAWN_HUNTER.value));
							p2.sendMessage(ChatColor.YELLOW
									+ "You have been teleported to the "
									+ ChatColor.DARK_RED + "Hunter"
									+ ChatColor.YELLOW + " spawn.");
							p.sendMessage(g.getColor(p2) + p2.getName()
									+ ChatColor.YELLOW
									+ " has been teleported to the "
									+ ChatColor.DARK_RED + "Hunter"
									+ ChatColor.YELLOW + " spawn.");
							return;
						}
					}
				} else if (args[0].equalsIgnoreCase("pregame")
						|| args[0].equalsIgnoreCase("preparation")
						|| args[0].equalsIgnoreCase("waiting")) {
					if (g.gameHasBegun() && !g.isSpectating(p)) {
						p.sendMessage(ChatColor.RED
								+ "You can't teleport there while the game is running!");
						return;
					}
					if (args.length == 1) {
						p.teleport(settings.SPAWN_SETUP.value);
						p.sendMessage(ChatColor.GREEN
								+ "You have teleported to the "
								+ ChatColor.GOLD + "Pregame" + ChatColor.GREEN
								+ " spawn.");
						return;
					} else if (args.length > 1) {
						if (!p.isOp()) {
							p.sendMessage(ChatColor.RED
									+ "You don't have permission to teleport other players!");
							return;
						}
						Player p2 = Bukkit.getPlayerExact(args[1]);
						if (p2 == null) {
							p.sendMessage(ChatColor.RED + "Player " + args[0]
									+ " does not exist!");
							return;
						}
						if (g.isSpectating(p2) || !g.gameHasBegun()) {
							p2.teleport(settings.SPAWN_SETUP.value);
							p2.sendMessage(ChatColor.YELLOW
									+ "You have been teleported to the "
									+ ChatColor.GOLD + "Pregame"
									+ ChatColor.YELLOW + " spawn.");
							p.sendMessage(g.getColor(p2) + p2.getName()
									+ ChatColor.YELLOW
									+ " has been teleported to the "
									+ ChatColor.GOLD + "Pregame"
									+ ChatColor.YELLOW + " spawn.");
							return;
						}
					}
				} else {
					Player p2 = Bukkit.getPlayerExact(args[0]);
					if (p2 == null) {
						p.sendMessage(ChatColor.RED + "Player " + args[0]
								+ " does not exist!");
						return;
					}
					if (g.isSpectating(p2) || !g.gameHasBegun()) {
						p2.teleport(HuntedPlugin.getInstance().getWorld()
								.getSpawnLocation());
						p2.sendMessage(ChatColor.YELLOW
								+ "You have been teleported to the Manhunt world spawn.");
						p.sendMessage(g.getColor(p2)
								+ p2.getName()
								+ " has been teleported to the Manhunt world spawn.");
						return;
					}
				}
			}
		}
	}

	private void setspawnCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED + "Only ops can set spawns!");
			return;
		}
		if (g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED
					+ "You can't change spawn points while the game is running!");
			return;
		}
		if (p.getWorld() != HuntedPlugin.getInstance().getWorld()) {
			p.sendMessage(ChatColor.RED
					+ "You must be in the manhunt world to set it's spawn. Use \"/manhunt spawn\" to teleport to the manhunt world.");
			return;
		}
		if (args.length == 0) {
			if (ManhuntUtil.areNearby(settings.SPAWN_HUNTER.value, HuntedPlugin.getInstance()
					.getWorld().getSpawnLocation(), 1.0)) {
				settings.SPAWN_HUNTER.setValue(p.getLocation());
			}
			if (ManhuntUtil.areNearby(settings.SPAWN_PREY.value, HuntedPlugin.getInstance()
					.getWorld().getSpawnLocation(), 1.0)) {
				settings.SPAWN_PREY.setValue(p.getLocation());
			}
			if (ManhuntUtil.areNearby(settings.SPAWN_SETUP.value, HuntedPlugin
					.getInstance().getWorld().getSpawnLocation(), 1.0)) {
				settings.SPAWN_SETUP.setValue(p.getLocation());
			}
			HuntedPlugin
					.getInstance()
					.getWorld()
					.setSpawnLocation(p.getLocation().getBlockX(),
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ());
			p.sendMessage(ChatColor.GREEN + "World spawn set!");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " changed the Manhunt world spawn.");
			return;
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("hunter")
					|| args[0].equalsIgnoreCase("hunters")) {
				settings.SPAWN_HUNTER.setValue(p.getLocation());
				p.sendMessage(ChatColor.DARK_RED + "Hunter" + ChatColor.GREEN
						+ " spawn set!");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " changed the Hunter spawn.");
				return;
			}
			if (args[0].equalsIgnoreCase("hunted")
					|| args[0].equalsIgnoreCase("prey")) {
				settings.SPAWN_PREY.setValue(p.getLocation());
				p.sendMessage(ChatColor.BLUE + "Prey" + ChatColor.GREEN
						+ " spawn set!");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " changed the Prey spawn.");
				return;
			}
			if (args[0].equalsIgnoreCase("pregame")
					|| args[0].equalsIgnoreCase("waiting")
					|| args[0].equalsIgnoreCase("preparation")) {
				settings.SPAWN_SETUP.setValue(p.getLocation());
				p.sendMessage(ChatColor.GOLD + "Pregame" + ChatColor.GREEN
						+ " spawn set!");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " changed the Pregame spawn.");
				return;
			}
			if (args[0].equalsIgnoreCase("all")) {
				HuntedPlugin
						.getInstance()
						.getWorld()
						.setSpawnLocation(p.getLocation().getBlockX(),
								p.getLocation().getBlockY(),
								p.getLocation().getBlockZ());
				settings.SPAWN_HUNTER.setValue(p.getLocation());
				settings.SPAWN_PREY.setValue(p.getLocation());
				settings.SPAWN_SETUP.setValue(p.getLocation());
				p.sendMessage(ChatColor.GREEN + "All spawn points set!");
				HuntedPlugin.getInstance().log(Level.INFO,
						p.getName() + " changed all spawn points.");
			}
		}
	}

	private void setworldCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only ops can change the Manhunt world!");
		}
		if (!g.gameHasBegun()) {
			if (p.getWorld() != HuntedPlugin.getInstance().getWorld()) {
				p.sendMessage(ChatColor.GREEN
						+ "Manhunt world has been set to \""
						+ p.getWorld().getName() + "\"");
				HuntedPlugin.getInstance().log(
						Level.INFO,
						p.getName() + " changed the Manhunt world to "
								+ p.getWorld().getName());
			} else {
				p.sendMessage(ChatColor.RED
						+ "You are already in the Manhunt world!");
			}
		} else {
			p.sendMessage(ChatColor.RED
					+ "You cannot change the Manhunt world while a game is running!");
		}
	}

	private void defaultsCommand(String[] args, Player p) {
		if (p.isOp()) {
			settings.loadDefaults();
			settings.saveAll();
			p.sendMessage(ChatColor.GREEN + "Default Manhunt settings loaded!");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " restored the default Manhunt settings!");
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
			return;
		}
	}

	private void loadCommand(String[] args, Player p) {
		if (p.isOp()) {
			if (g.gameHasBegun()) {
				p.sendMessage(ChatColor.RED
						+ "You can't do that while the plugin is running!");
				return;
			}
			settings.reloadAll();
			p.sendMessage(ChatColor.GREEN + "Settings file reloaded.");
			HuntedPlugin.getInstance().log(Level.INFO,
					p.getName() + " reloaded the Manhunt settings.");
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
			return;
		}
	}

	private void weatherCommand(String[] args, Player p) {
		if (p.isOp()) {
			if (args.length == 0) {
				p.sendMessage(ChatColor.RED
						+ "You must choose a weather: sunny, rain, or storm.");
				return;
			}
			if (args[0].equalsIgnoreCase("sunny")
					|| args[0].equalsIgnoreCase("norain")
					|| args[0].equalsIgnoreCase("clear")) {
				HuntedPlugin.getInstance().getWorld().setStorm(false);
				HuntedPlugin.getInstance().getWorld().setThundering(false);

			} else if (args[0].equalsIgnoreCase("rain")
					|| args[0].equalsIgnoreCase("rainy")) {
				HuntedPlugin.getInstance().getWorld().setStorm(true);
				HuntedPlugin.getInstance().getWorld().setThundering(false);

			} else if (args[0].equalsIgnoreCase("storm")
					|| args[0].equalsIgnoreCase("stormy")
					|| args[0].equalsIgnoreCase("lightning")
					|| args[0].equalsIgnoreCase("thunder")) {
				HuntedPlugin.getInstance().getWorld().setStorm(false);
				HuntedPlugin.getInstance().getWorld().setThundering(false);

			} else {
				p.sendMessage(ChatColor.RED
						+ "You must choose a weather: sunny, rainy, or stormy.");
				return;
			}
			p.sendMessage(ChatColor.GREEN + "Weather set to " + args[0]);
			HuntedPlugin.getInstance().log(
					Level.INFO,
					p.getName() + " set the Manhunt world weather to "
							+ args[0]);
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
		}
	}

	private void loadoutCommand(String[] args, Player p)
	{
		if (args.length == 0)
		{
			listLoadouts(1, p);
			return;
		}
		if (args.length >= 1)
		{
			if (args[0].equalsIgnoreCase("list"))
			{
				int page = 1;
				
				if (args.length >= 2)
				{
					try {
						page = Integer.parseInt(args[1]);
					}
					catch (NumberFormatException e)
					{
						page = 1;
					}
				}
				listLoadouts(page, p);
				return;
			}
			else if (args[0].equalsIgnoreCase("create")
					|| args[0].equalsIgnoreCase("add")
					|| args[0].equalsIgnoreCase("new")
					|| args[0].equalsIgnoreCase("save"))
			{
				if (args[1].equalsIgnoreCase("hunter")
						|| args[1].equalsIgnoreCase("prey"))
				{
					p.sendMessage(ChatColor.RED + "You may not use that name!");
					return;
				}
				if (loadouts.getLoadout(args[1]) == null)
				{
					loadouts.addLoadout(args[1], p.getInventory().getContents(), p.getInventory().getArmorContents());
					p.sendMessage(ChatColor.GREEN + "New loadout created with name " + args[1]);
					return;
				}
				else
				{
					loadouts.getLoadout(args[1]).setContents(p.getInventory().getContents(), p.getInventory().getArmorContents());
					p.sendMessage(ChatColor.GREEN + "The existing loadout " + loadouts.getLoadout(args[1]).name + " was overwritten.");
					return;
				}
			}
			else if (args.length == 1)
			{
				p.sendMessage(ChatColor.RED + "Incorrect syntax!");
				return;
			}
			else if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length >= 3)
				{
					if (args[1].equalsIgnoreCase("hunter"))
					{
						if (loadouts.getLoadout(args[2]) == null)
						{
							p.sendMessage(ChatColor.RED + "No loadout with that name exists.");
							return;
						}
						else
						{
							settings.HUNTER_LOADOUT_CURRENT.value = loadouts.getLoadout(args[2]).name;
							p.sendMessage(ChatColor.GREEN + "Hunter loadout set to \"" + settings.HUNTER_LOADOUT_CURRENT.value + "\".");
							return;
						}
					}
					else if (args[1].equalsIgnoreCase("prey"))
					{
						if (loadouts.getLoadout(args[2]) == null)
						{
							p.sendMessage(ChatColor.RED + "No loadout with that name exists.");
							return;
						}
						else
						{
							settings.PREY_LOADOUT_CURRENT.value = loadouts.getLoadout(args[2]).name;
							p.sendMessage(ChatColor.GREEN + "Prey loadout set to \"" + settings.PREY_LOADOUT_CURRENT.value + "\".");
							return;
						}
					}
				}
			}
			else if (args[0].equalsIgnoreCase("reset"))
			{
				if (args[1].equalsIgnoreCase("hunter"))
				{
					settings.HUNTER_LOADOUT_CURRENT.setValue(loadouts.HUNTER_LOADOUT.name);
					p.sendMessage(ChatColor.GREEN + "The Hunter has been reset to default.");
					return;
				}
				else if (args[1].equalsIgnoreCase("prey"))
				{
					settings.PREY_LOADOUT_CURRENT.setValue(loadouts.PREY_LOADOUT.name);
					p.sendMessage(ChatColor.GREEN + "The Prey has been reset to default.");
					return;
				}
			}
			else if (args[0].equalsIgnoreCase("load"))
			{
				if (args[1].equalsIgnoreCase("hunter"))
				{
					ManhuntUtil.clearInventory(p.getInventory());
					p.getInventory().setContents(loadouts.getHunterLoadout().getContents());
					p.getInventory().setArmorContents(loadouts.getHunterLoadout().getArmor());
					p.sendMessage(ChatColor.GREEN + "Current Hunter loadout has been loaded.");
					return;
				}
				else if (args[1].equalsIgnoreCase("prey"))
				{
					ManhuntUtil.clearInventory(p.getInventory());
					p.getInventory().setContents(loadouts.getPreyLoadout().getContents());
					p.getInventory().setArmorContents(loadouts.getPreyLoadout().getArmor());
					p.sendMessage(ChatColor.GREEN + "Current Prey loadout has been loaded.");
					return;
				}
				else if (loadouts.getLoadout(args[1]) != null)
				{
					ManhuntUtil.clearInventory(p.getInventory());
					p.getInventory().setContents(loadouts.getLoadout(args[1]).getContents());
					p.getInventory().setArmorContents(loadouts.getLoadout(args[1]).getArmor());
					p.sendMessage(ChatColor.GREEN + "Loadout \"" + loadouts.getLoadout(args[1]).name + "\" has been loaded.");
					return;
				}
				else
				{
					p.sendMessage(ChatColor.RED + "No loadout with that name exists.");
					return;
				}
			}
			else if (args[0].equalsIgnoreCase("delete")
					|| args[0].equalsIgnoreCase("remove"))
			{
				if (loadouts.getLoadout(args[1]) == null)
				{
					p.sendMessage(ChatColor.RED + "No loadout with that name exists.");
					return;
				}
				else
				{
					Loadout loadout = loadouts.getLoadout(args[1]);
					if (loadouts.deleteLoadout(args[1]))
						p.sendMessage(ChatColor.GREEN + "Loadout \"" + loadout.name + "\" was deleted.");
					else
						p.sendMessage(ChatColor.RED + "Loadout \"" + loadout.name + "\" was NOT deleted.");
					return;
				}
			}
		}
	}

	private void listLoadouts(int page, Player p)
	{
		List<Loadout> loads = loadouts.getAllLoadouts();
		
		if (loads.isEmpty())
		{
			p.sendMessage(ChatColor.RED + "There are no saved loadouts!");
			return;
		}
		
		int perPage = 6;
		
		p.sendMessage(ChatColor.GOLD + "--------[ " + ChatColor.GREEN + "Saved Loadouts (" + page + "/" + ((int) Math.floor(loads.size()/perPage)+1) + ")" + ChatColor.GOLD + " ]--------");
		
		for (int i = (page-1)*perPage; (i < (page*perPage) &&i < loads.size()); i++)
		{
			p.sendMessage(pre + ChatColor.GREEN + loads.get(i).name + (settings.HUNTER_LOADOUT_CURRENT.value.equals(loads.get(i).name) ? " (" + ChatColor.DARK_RED + "Hunter" + ChatColor.GREEN + ")" : "") + ChatColor.GREEN + (settings.PREY_LOADOUT_CURRENT.value.equals(loads.get(i).name) ? " (" + ChatColor.BLUE + "Prey" + ChatColor.GREEN + ")" : ""));
		}
		
		return;
	}

	private void settingsCommand(String[] args, Player p) {
		
		if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("1")))
		{
			listSettings(1, p);
			return;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("2"))
		{
			listSettings(2, p);
			return;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("3"))
		{
			listSettings(3, p);
			return;
		}
		
		else if (!p.isOp())
		{
			p.sendMessage(ChatColor.RED + "Only ops can change manhunt game settings!");
			return;
		}
		else if (g.gameHasBegun())
		{
			p.sendMessage(ChatColor.RED + "You can't change settings while the Manhunt game is in progress!");
			return;
		}

		else if (args.length >= 1) {
			
			Setting<?> setting = settings.getSetting(args[0]);
			
			if (setting == null)
			{
				p.sendMessage(ChatColor.RED + "Invalid Manhunt Setting!");
				return;
			}
			
			if (args.length == 1)
			{
				p.sendMessage(ChatColor.BLUE + setting.label + " " + setting.formattedValue() + " " + setting.message());
			}
			else if (args.length > 1)
			{
				if (setting.parseValue(args[1]))
					p.sendMessage(ChatColor.BLUE + setting.label + " " + setting.formattedValue() + " " + setting.message());
				else
					p.sendMessage(ChatColor.RED + args[1] + "is an invalid setting for \"" + setting.label + "\"");
			}
		}
	}

	private void listSettings(int page, Player p)
	{
		final int RESULTS_PER_PAGE = 9;
		
		p.sendMessage(ChatColor.GOLD + "Available manhunt settings: (" + page + "/3)");
		
		List<Setting<?>> list = settings.getAllSettings();
		
		int max = (RESULTS_PER_PAGE*page > list.size() ? list.size() : RESULTS_PER_PAGE*page);
		int index = RESULTS_PER_PAGE*(page-1);
		
		while (index < max)
		{
			Setting<?> setting = list.get(index);
			
			p.sendMessage(ChatColor.BLUE + setting.label
					+ " " + setting.formattedValue()
					+ ChatColor.WHITE + ": "
					+ setting.message());
			
			index++;
		}
	}

	private String[] shiftArgs(String[] args) {
		String[] a = new String[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			a[i - 1] = args[i];
		}
		return a;
	}

}