package com.bendude56.hunted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdExec implements CommandExecutor {

	public CmdExec() {
		Bukkit.getPluginCommand("manhunt").setExecutor(this);
		Bukkit.getPluginCommand("mhunt").setExecutor(this);
	}
	
	Game g = HuntedPlugin.getInstance().game;
	SettingsFile settings = HuntedPlugin.getInstance().settings;
	
	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED
					+ "The /manhunt command cannot be used from the console!");
			return true;
		}
		Player p = (Player) sender;
		
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if (!settings.opPermission || sender.isOp()) {

				p.sendMessage(ChatColor.YELLOW + "/manhunt quit");
				p.sendMessage(ChatColor.YELLOW
						+ "    Quits the current manhunt game and makes you a spectator.");

				p.sendMessage(ChatColor.YELLOW
						+ "/manhunt join {hunter|prey} <player>");
				p.sendMessage(ChatColor.YELLOW
						+ "    Joins the selected manhunt team.");
				if (p.isOp())
					p.sendMessage(ChatColor.YELLOW
							+ "    Include an optional player name to assign them to that team.");

				p.sendMessage(ChatColor.YELLOW + "/manhunt spectate <player>");
				p.sendMessage(ChatColor.YELLOW
						+ "    Lets you spectate the manhunt game.");
				if (p.isOp())
					p.sendMessage(ChatColor.YELLOW
							+ "     Include an optional player name to make them a spectator.");

				p.sendMessage(ChatColor.YELLOW + "/manhunt world <player>");
				p.sendMessage(ChatColor.YELLOW
						+ "    Teleports you to the manhunt world");
				if (p.isOp())
					p.sendMessage(ChatColor.YELLOW
							+ "    Include an optional player name to teleport them to the world.");

				if (p.isOp()) {
					p.sendMessage(ChatColor.YELLOW + "/manhunt kick {player}");
					p.sendMessage(ChatColor.YELLOW
							+ "    Kicks a player from the game and makes them a spectator.");
				}

			}
		
		} else if (args[0].equalsIgnoreCase("join")) {
			joinCommand(args, p);
		
		} else if (args[0].equalsIgnoreCase("quit")) {
			quitCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("kick")) {
			kickCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("spectate")) {
			spectateCommand(args, p);

		} else if (args[0].equalsIgnoreCase("makeall")
				|| args[0].equalsIgnoreCase("makeeveryone")) {
			makeallCommand(args, p);

		} else if (args[0].equalsIgnoreCase("list")) {
			listCommand(args, p);

		} else if (args[0].equalsIgnoreCase("status")
				|| args[0].equalsIgnoreCase("info")) {
			statusCommand(args, p);

		} else if (args[0].equalsIgnoreCase("world")
				|| args[0].equalsIgnoreCase("spawn")) {
			spawnCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("setspawn")) {
			setspawnCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("startgame")) {
			startCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("stopgame")) {
			stopCommand(args, p);

		} else if (args[0].equalsIgnoreCase("restoredefaults")
				|| args[0].equalsIgnoreCase("defaultsettings")
				|| args[0].equalsIgnoreCase("loaddefaults")) {
			defaultsCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("reload")
				|| args[0].equalsIgnoreCase("load")) {
			loadCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("weather")) {
			weatherCommand(args, p);
			
		} else if (args[0].equalsIgnoreCase("setting")
				|| args[0].equalsIgnoreCase("settings")
				|| args[0].equalsIgnoreCase("preferences")
				|| args[0].equalsIgnoreCase("properties")) {
			settingCommand(args, p);
			
		} else {
			p.sendMessage(ChatColor.RED
					+ "Unknown Manhunt command! Type \"/manhunt help\" for help.");
		}
		return true;
	}
	
	private void joinCommand(String[] args, Player p) {
		
		if (g.gameHasBegun()) {
			p.sendMessage(ChatColor.RED
					+ "You cannot join a game already in progress!");
			return;
		} else if (settings.opPermission && !p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only OPs have permission to do that.");
			return;
		}
		if (args.length == 1) {
			p.sendMessage(ChatColor.RED
					+ "You must choose a team! Hunters or Prey.");
			return;
		}
		if (args[1].equalsIgnoreCase("hunter")) {
			if (args.length == 2) {
				g.addHunter(p);
				g.broadcastAll(ChatColor.RED + p.getName()
						+ ChatColor.WHITE + " has joined team "
						+ ChatColor.RED + "Hunters.");
			} else if (args.length > 2 && p.isOp()) {
				g.addHunter(args[2]);
				g.broadcastAll(ChatColor.RED + args[2] + ChatColor.WHITE
						+ " has joined team " + ChatColor.RED + "Hunters.");
			}
		} else if (args[1].equalsIgnoreCase("hunted")
				|| args[1].equalsIgnoreCase("prey")) {
			if (args.length == 2) {
				g.addHunted(p);
				g.broadcastAll(ChatColor.BLUE + p.getName()
						+ ChatColor.WHITE + " has joined team "
						+ ChatColor.BLUE + "Prey.");
			} else if (args.length > 2 && p.isOp()) {
				g.addHunted(args[2]);
				g.broadcastAll(ChatColor.BLUE + args[2] + ChatColor.WHITE
						+ " has joined team " + ChatColor.BLUE + "Prey.");
			}
		} else if (args[1].equalsIgnoreCase("spectators")
				|| args[1].equalsIgnoreCase("spectator")) {
			if (args.length == 2) {
				g.addSpectator(p);
				g.broadcastSpectators(ChatColor.YELLOW + p.getName()
						+ ChatColor.WHITE + " has become a "
						+ ChatColor.YELLOW + "Spectator.");
			} else if (args.length > 2 && p.isOp()) {
				g.addHunted(args[2]);
				g.broadcastSpectators(ChatColor.YELLOW + args[2]
						+ ChatColor.WHITE + " has become a "
						+ ChatColor.YELLOW + "Spectator.");
			}
		}
	}
	
	private void quitCommand(String[] args, Player p) {
		if (g.isHunted(p) || g.isHunter(p)) {
			if (g.gameHasBegun()) {
				g.broadcastSpectators(ChatColor.YELLOW + p.getName()
						+ ChatColor.WHITE
						+ " has quit the game to become a "
						+ ChatColor.YELLOW + "Spectator.");
				g.onDie(p);
				p.sendMessage(ChatColor.YELLOW
						+ "You have quit the game and are now spectating.");
				return;
			} else {
				g.addSpectator(p);
				g.broadcastSpectators(ChatColor.YELLOW + p.getName()
						+ ChatColor.WHITE + " has become a "
						+ ChatColor.YELLOW + "Spectator.");
				return;
			}
		}
	}
	
	private void kickCommand(String[] args, Player p) {
		if (args.length >= 2) {
			Player p2 = Bukkit.getPlayerExact(args[1]);
			if (p2 == null) {
				if (g.isHunted(args[1]) || g.isHunter(args[1])
						|| g.isSpectating(args[1])) {
					p.sendMessage(g.getColor(args[1]) + args[1]
							+ ChatColor.WHITE
							+ " was removed from the Manhunt list!");
					g.deletePlayer(args[1]);
				} else {
					p.sendMessage(ChatColor.RED + "No player named "
							+ args[1] + " exists!");
				}
				return;
			}
			if (p.isOp() && !p2.isOp() && p != p2) {
				g.broadcastAll(g.getColor(p2) + p2.getName()
						+ ChatColor.WHITE + " has become a "
						+ ChatColor.YELLOW + "Spectator.");
				g.onDie(p2);
				p2.sendMessage(ChatColor.RED
						+ "You have been kicked from the game. You are now spectating.");
				return;
			}
		} else {
			p.sendMessage(ChatColor.RED + "Who did you want to kick?");
			return;
		}
	}
	
	private void spectateCommand(String[] args, Player p) {
		if ((g.isHunted(p) || g.isHunter(p)) && g.gameHasBegun()
				&& args.length == 1) {
			p.sendMessage(ChatColor.RED
					+ "You can't spectate while you're playing!");
			p.sendMessage(ChatColor.RED
					+ "Use \"/manhunt quit\" if you want to quit the game.");
			return;
		} else if (p.isOp() || !settings.opPermission) {
			if (args.length == 1) {
				g.addSpectator(p);
				g.broadcastSpectators(ChatColor.YELLOW + p.getName()
						+ ChatColor.WHITE + " has become a "
						+ ChatColor.YELLOW + "Spectator.");
				return;
			} else if (args.length >= 2 && p.isOp()) {
				Player p2 = Bukkit.getPlayerExact(args[1]);
				if (p2 == null) {
					p.sendMessage(ChatColor.RED + args[1]
							+ " does not exist!");
					return;
				}
				if ((g.isHunted(p2) || g.isHunter(p2)) && g.gameHasBegun()) {
					p.sendMessage(ChatColor.RED + args[1]
							+ " is in the middle of a game!");
					p.sendMessage(ChatColor.RED + "Use \"/manhunt kick <"
							+ args[1] + ">\" to properly kick them.");
					return;
				} else {
					g.broadcastSpectators(g.getColor(p2) + p2.getName()
							+ ChatColor.WHITE + " has become a "
							+ ChatColor.YELLOW + "Spectator.");
					g.addSpectator(p2);
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
		if (args.length == 1) {
			p.sendMessage(ChatColor.RED
					+ "You must specify a team to move everyone to!");
			return;
		}
		if (args[1].equalsIgnoreCase("hunter")
				|| args[1].equalsIgnoreCase("hunters")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunted(s) || g.isSpectating(p)) {
					g.addHunted(s);
					s.sendMessage(ChatColor.WHITE
							+ "You have been moved to team "
							+ ChatColor.RED + "Hunters.");
				}
			}
			g.broadcastAll(ChatColor.WHITE
					+ "All players have been moved to team "
					+ ChatColor.RED + "Hunters.");
			return;
		} else if (args[1].equalsIgnoreCase("hunted")
				|| args[1].equalsIgnoreCase("prey")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunter(s) || g.isSpectating(p)) {
					g.addHunted(s);
					s.sendMessage(ChatColor.WHITE
							+ "You have been moved to team "
							+ ChatColor.BLUE + "Prey.");
				}
			}
			g.broadcastAll(ChatColor.WHITE
					+ "All players have been moved to team "
					+ ChatColor.BLUE + "Prey.");
			return;
		} else if (args[1].equalsIgnoreCase("spectator")
				|| args[1].equalsIgnoreCase("spectators")) {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (g.isHunter(s) || g.isHunted(p)) {
					g.addSpectator(s);
					s.sendMessage(ChatColor.WHITE + "You are now a "
							+ ChatColor.YELLOW + "Spectator.");
				}
			}
			g.broadcastAll(ChatColor.WHITE + "All players are now "
					+ ChatColor.YELLOW + "Spectating.");
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "Invalid team. {Hunters|Prey|Spectator}");
		}
		return;
	}
	
	private void listCommand(String[] args, Player p) {
		if (settings.opPermission && !p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "You don'thave permission to do that!");
			return;
		} else if (args.length == 1
				|| (args.length >= 2 && args[1].equalsIgnoreCase("all"))) {
			p.sendMessage(ChatColor.GREEN
					+ "Manhunt players: ("
					+ (g.HuntedAmount() + g.HuntersAmount() + g
							.SpectatorsAmount()) + ")");
			for (String s : g.getHunters()) {
				p.sendMessage(ChatColor.RED + "  " + s);
			}
			for (String s : g.getHunted()) {
				p.sendMessage(ChatColor.BLUE + "  " + s);
			}
			for (String s : g.getSpectators()) {
				p.sendMessage(ChatColor.YELLOW + "  " + s);
			}
		} else if (args.length >= 2 && args[1].equalsIgnoreCase("hunters")) {
			p.sendMessage(ChatColor.GREEN + "Team HUNTERS: ("
					+ g.HuntersAmount() + ")");
			for (String s : g.getHunters()) {
				p.sendMessage(ChatColor.RED + "  " + s);
			}
		} else if (args.length >= 2 && args[1].equalsIgnoreCase("prey")) {
			p.sendMessage(ChatColor.GREEN + "Team HUNTED: ("
					+ g.HuntedAmount() + ")");
			for (String s : g.getHunted()) {
				p.sendMessage(ChatColor.BLUE + "  " + s);
			}
		} else if (args.length >= 2
				&& args[1].equalsIgnoreCase("spectators")) {
			p.sendMessage(ChatColor.GREEN + "Manhunt SPECTATORS: ("
					+ g.SpectatorsAmount() + ")");
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
		if (g.huntHasBegun()) {
			p.sendMessage(ChatColor.GOLD
					+ "Time remaining:  "
					+ (int) Math.floor((g.getEndTick() - g.getTick()) / 72000)
					+ ":"
					+ (int) (Math.floor((g.getEndTick() - g.getTick()) / 1200) - Math
							.floor((g.getEndTick() - g.getTick()) / 72000) * 60)
					+ ":"
					+ (int) (Math.floor((g.getEndTick() - g.getTick()) / 20) - (Math
							.floor((g.getEndTick() - g.getTick()) / 1200) - Math
							.floor((g.getEndTick() - g.getTick()) / 72000)) * 60));
		} else {
			p.sendMessage(ChatColor.GOLD
					+ "Time remaining:  "
					+ (int) Math.floor((g.getHunterReleaseTick() - g
							.getTick()) / 72000)
					+ ":"
					+ (int) (Math.floor((g.getHunterReleaseTick() - g
							.getTick()) / 1200) - Math.floor((g
							.getHunterReleaseTick() - g.getTick()) / 72000) * 60)
					+ ":"
					+ (int) (Math.floor((g.getHunterReleaseTick() - g
							.getTick()) / 20) - (Math.floor((g
							.getHunterReleaseTick() - g.getTick()) / 1200) - Math
							.floor((g.getHunterReleaseTick() - g.getTick()) / 72000)) * 60));
		}
		p.sendMessage(ChatColor.RED + "Hunters: " + g.HuntersAmount()
				+ ChatColor.BLUE + "  Prey: " + g.HuntedAmount()
				+ ChatColor.YELLOW + "  Spectators: "
				+ g.SpectatorsAmount());
	}
	
	private void spawnCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED + "Only ops can change spawn points!");
			return;
		}
		if (p.isOp() || !settings.opPermission || g.isSpectating(p)) {
			if (args.length == 1) {
				if (g.isSpectating(p) || !g.gameHasBegun()) {
					p.teleport(HuntedPlugin.getInstance().getWorld()
							.getSpawnLocation());
					return;
				} else {
					p.sendMessage(ChatColor.RED
							+ "You can't warp to the manhunt spawn once the game has started!");
					return;
				}
			} else if (args.length >= 2 && p.isOp()) {
				if (args[1].equalsIgnoreCase("prey")) {
					if (g.gameHasBegun()) {
						p.sendMessage(ChatColor.RED
								+ "You can't teleport there while the game is running!");
						return;
					}
					if (args.length == 2) {
						p.teleport(settings.preySpawn);
						p.sendMessage(ChatColor.GREEN
								+ "Teleported to the " + ChatColor.BLUE
								+ "Prey" + ChatColor.GREEN + " spawn.");
						return;
					} else if (args.length > 3) {
						Player p2 = Bukkit.getPlayerExact(args[2]);
						if (p2 == null) {
							p.sendMessage(ChatColor.RED + "Player "
									+ args[1] + " does not exist!");
							return;
						}
						if (g.isSpectating(p2) || !g.gameHasBegun()) {
							p2.teleport(settings.preySpawn);
							p2.sendMessage(ChatColor.YELLOW
									+ "You have been teleported to the "
									+ ChatColor.BLUE + "Prey"
									+ ChatColor.YELLOW + " spawn.");
							p.sendMessage(ChatColor.YELLOW + args[1]
									+ " has been teleported to the "
									+ ChatColor.BLUE + "Prey"
									+ ChatColor.YELLOW + " spawn.");
							return;
						}
					}
				} else if (args[1].equalsIgnoreCase("hunter")) {
					if (g.gameHasBegun()) {
						p.sendMessage(ChatColor.RED
								+ "You can't teleport there while the game is running!");
						return;
					}
					if (args.length == 2) {
						p.teleport(settings.preySpawn);
						p.sendMessage(ChatColor.GREEN
								+ "Teleported to the " + ChatColor.RED
								+ "Hunter" + ChatColor.GREEN + " spawn.");
						return;
					} else if (args.length > 2) {
						Player p2 = Bukkit.getPlayerExact(args[2]);
						if (p2 == null) {
							p.sendMessage(ChatColor.RED + "Player "
									+ args[1] + " does not exist!");
							return;
						}
						if (g.isSpectating(p2) || !g.gameHasBegun()) {
							p2.teleport(HuntedPlugin.getInstance()
									.getWorld().getSpawnLocation());
							p2.sendMessage(ChatColor.YELLOW
									+ "You have been teleported to the "
									+ ChatColor.RED + "Hunter"
									+ ChatColor.YELLOW + " spawn.");
							p.sendMessage(ChatColor.YELLOW + args[1]
									+ " has been teleported to the "
									+ ChatColor.RED + "Hunter"
									+ ChatColor.YELLOW + " spawn.");
							return;
						}
					}
				} else {
					Player p2 = Bukkit.getPlayerExact(args[1]);
					if (p2 == null) {
						p.sendMessage(ChatColor.RED + "Player " + args[1]
								+ " does not exist!");
						return;
					}
					if (g.isSpectating(p2) || !g.gameHasBegun()) {
						p2.teleport(HuntedPlugin.getInstance().getWorld()
								.getSpawnLocation());
						p2.sendMessage(ChatColor.YELLOW
								+ "You have been teleported to the manhunt world spawn.");
						p.sendMessage(ChatColor.YELLOW
								+ args[1]
								+ " has been teleported to the manhunt world spawn.");
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
		if (p.getWorld() != HuntedPlugin.getInstance().getWorld()) {
			p.sendMessage(ChatColor.RED
					+ "You must be in the manhunt world to set it's spawn. Use \"/manhunt world\" to teleport to the manhunt world.");
			return;
		}
		if (args.length == 1) {
			if (settings.hunterSpawn == HuntedPlugin.getInstance()
					.getWorld().getSpawnLocation()) {
				settings.changeSetting("hunterSpawn", p.getLocation());
			}
			if (settings.preySpawn == HuntedPlugin.getInstance().getWorld()
					.getSpawnLocation()) {
				settings.changeSetting("preySpawn", p.getLocation());
			}
			HuntedPlugin
					.getInstance()
					.getWorld()
					.setSpawnLocation(p.getLocation().getBlockX(),
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ());
			p.sendMessage(ChatColor.GREEN + "World spawn set!");
			return;
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("hunter")
					|| args[1].equalsIgnoreCase("hunters")) {
				settings.changeSetting("hunterSpawn", p.getLocation());
				p.sendMessage(ChatColor.RED + "Hunter" + ChatColor.GREEN
						+ " spawn set!");
				return;
			}
			if (args[1].equalsIgnoreCase("hunted")
					|| args[1].equalsIgnoreCase("prey")) {
				settings.changeSetting("preySpawn", p.getLocation());
				p.sendMessage(ChatColor.BLUE + "Prey" + ChatColor.GREEN
						+ " spawn set!");
				return;
			}
			if (args[1].equalsIgnoreCase("waiting")
					|| args[1].equalsIgnoreCase("pregame")) {
				if (settings.hunterSpawn == HuntedPlugin.getInstance()
						.getWorld().getSpawnLocation()) {
					settings.changeSetting("hunterSpawn", p.getLocation());
				}
				if (settings.preySpawn == HuntedPlugin.getInstance()
						.getWorld().getSpawnLocation()) {
					settings.changeSetting("preySpawn", p.getLocation());
				}
				HuntedPlugin
						.getInstance()
						.getWorld()
						.setSpawnLocation(p.getLocation().getBlockX(),
								p.getLocation().getBlockY(),
								p.getLocation().getBlockZ());
				p.sendMessage(ChatColor.GREEN + "Pregame" + ChatColor.GREEN
						+ " spawn set! (AKA World Spawn)");
				return;
			}
		}
	}
	
	private void startCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only ops can start the manhunt game!");
			return;
		}
		if (g.HuntersAmount() == 0 || g.HuntedAmount() == 0) {
			p.sendMessage(ChatColor.RED
					+ "There must be at least one Hunter and Prey to start the game!");
			return;
		} else {
			if (g.HuntersAmount() < (g.HuntedAmount()-1)*4) {
				p.sendMessage(ChatColor.RED
						+ "There must be at least 4 hunters per prey!");
				return;
			}
		}
		g.start();
		p.sendMessage(ChatColor.GRAY
				+ "You have successfully started the manhunt game!");
		return;
	}
	
	private void stopCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only ops can quit the manhunt game!");
			return;
		}
		g.stop();
		g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE
				+ " has stopped the game!");
		return;
	}
	
	private void defaultsCommand(String[] args, Player p) {
		if (p.isOp()) {
			settings.loadDefaults();
			settings.saveFile();
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
			return;
		}
	}
	
	private void loadCommand(String[] args, Player p) {
		if (p.isOp()) {
			settings.loadFile();
			p.sendMessage(ChatColor.GREEN + "Settings file loaded.");
			return;
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
			return;
		}
	}
	
	private void weatherCommand(String[] args, Player p) {
		if (p.isOp()) {
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED
						+ "You must choose a weather: sunny, rain, or storm.");
				return;
			}
			if (args[1].equalsIgnoreCase("sunny")
					|| args[1].equalsIgnoreCase("norain")
					|| args[1].equalsIgnoreCase("clear")) {
				HuntedPlugin.getInstance().getWorld().setStorm(false);
				HuntedPlugin.getInstance().getWorld().setThundering(false);
				p.sendMessage(ChatColor.GREEN + "Weather set to \"sunny\"");
				return;
			} else if (args[1].equalsIgnoreCase("rain")
					|| args[1].equalsIgnoreCase("rainy")) {
				HuntedPlugin.getInstance().getWorld().setStorm(true);
				HuntedPlugin.getInstance().getWorld().setThundering(false);
				p.sendMessage(ChatColor.GREEN + "Weather set to \"rainy\"");
				return;
			} else if (args[1].equalsIgnoreCase("storm")
					|| args[1].equalsIgnoreCase("stormy")
					|| args[1].equalsIgnoreCase("lightning")
					|| args[1].equalsIgnoreCase("thunder")) {
				HuntedPlugin.getInstance().getWorld().setStorm(false);
				HuntedPlugin.getInstance().getWorld().setThundering(false);
				p.sendMessage(ChatColor.GREEN + "Weather set to \"sunny\"");
				return;
			} else {
				p.sendMessage(ChatColor.RED
						+ "You must choose a weather: sunny, rain, or storm.");
				return;
			}
		} else {
			p.sendMessage(ChatColor.RED
					+ "You don't have permission to do that!");
		}
	}
	
	private void settingCommand(String[] args, Player p) {
		if (!p.isOp()) {
			p.sendMessage(ChatColor.RED
					+ "Only ops can change manhunt game settings!");
			return;
		}

		if (args.length == 1
				|| (args.length == 2 && args[1].equalsIgnoreCase("1"))) {
			p.sendMessage(ChatColor.GOLD
					+ "Available manhunt settings: (1/3)");
			if (settings.opPermission) {
				p.sendMessage(ChatColor.BLUE
						+ "opPermission "
						+ ChatColor.GREEN
						+ "[true]"
						+ ChatColor.WHITE
						+ " Players need op permissions to any manhunt commands.");
			} else {
				p.sendMessage(ChatColor.BLUE
						+ "opPermission "
						+ ChatColor.RED
						+ "[false]"
						+ ChatColor.WHITE
						+ " Players can choose their team and warp to world spawn.");
			}
			if (settings.allTalk) {
				p.sendMessage(ChatColor.BLUE + "allTalk " + ChatColor.GREEN
						+ "[true]" + ChatColor.WHITE
						+ " Teams can see eachother's chat.");
			} else {
				p.sendMessage(ChatColor.BLUE
						+ "allTalk "
						+ ChatColor.RED
						+ "[false]"
						+ ChatColor.WHITE
						+ " Hunters, prey, and spectators can't chat with eachother.");
			}
			if (settings.spawnPassive) {
				p.sendMessage(ChatColor.BLUE + "spawnPassive "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Passive mobs will spawn.");
			} else {
				p.sendMessage(ChatColor.BLUE + "spawnPassive "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " Passive mobs will not spawn.");
			}
			if (settings.spawnHostile) {
				p.sendMessage(ChatColor.BLUE + "spawnHostile "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Hostile mobs will spawn.");
			} else {
				p.sendMessage(ChatColor.BLUE + "spawnHostile "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " Hostile mobs will not spawn.");
			}
			if (settings.envDeath) {
				p.sendMessage(ChatColor.BLUE
						+ "envDeath "
						+ ChatColor.GREEN
						+ "[true]"
						+ ChatColor.WHITE
						+ " Players can die from mobs and enviromental hazards.");
			} else {
				p.sendMessage(ChatColor.BLUE
						+ "envDeath "
						+ ChatColor.RED
						+ "[false]"
						+ ChatColor.WHITE
						+ " Players can be damaged by environment, but never die.");
			}

		} else if (args.length == 2 && args[1].equalsIgnoreCase("2")) {
			p.sendMessage(ChatColor.GOLD
					+ "Available manhunt settings: (2/3)");
			if (settings.envHunterRespawn) {
				p.sendMessage(ChatColor.BLUE + "envHunterRespawn "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Hunters respawn from enviromental death.");
			} else {
				p.sendMessage(ChatColor.BLUE + "envHunterRespawn "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " Hunters are eliminated by enviromental death.");
			}
			if (settings.envPreyRespawn) {
				p.sendMessage(ChatColor.BLUE + "envPreyRespawn "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " The Prey respawn from enviromental death.");
			} else {
				p.sendMessage(ChatColor.BLUE + "envPreyRespawn "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " The Prey are eliminated by enviromental death.");
			}
			if (settings.envPreyRespawn) {
				p.sendMessage(ChatColor.BLUE
						+ "preyFinder "
						+ ChatColor.GREEN
						+ "[true]"
						+ ChatColor.WHITE
						+ " Hunters can use a compass to find the direction to the nearest Prey.");
			} else {
				p.sendMessage(ChatColor.BLUE
						+ "preyFinder "
						+ ChatColor.RED
						+ "[false]"
						+ ChatColor.WHITE
						+ " Hunters have to find the Prey the old-fashioned way.");
			}
			if (settings.friendlyFire) {
				p.sendMessage(ChatColor.BLUE + "friendlyFire "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Players can kill their teammates.");
			} else {
				p.sendMessage(ChatColor.BLUE + "friendlyFire "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " Players can't hurt their teammates.");
			}
			if (settings.pvpInstantDeath) {
				p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " PvP damage causes instant death.");
			} else {
				p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " PvP damage is vanilla.");
			}
			if (settings.loadouts) {
				p.sendMessage(ChatColor.BLUE + "loadouts "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Players will start with a pre-set inventory.");
			} else {
				p.sendMessage(ChatColor.BLUE + "loadouts " + ChatColor.RED
						+ "[false]" + ChatColor.WHITE
						+ " Players' inventorys will not be reset.");
			}
			if (settings.woolHats) {
				p.sendMessage(ChatColor.BLUE + "woolHats "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Teams get identifying headwear.");
			} else {
				p.sendMessage(ChatColor.BLUE + "woolHats " + ChatColor.RED
						+ "[false]" + ChatColor.WHITE
						+ " No fancy headwear for you!");
			}
			if (settings.northCompass) {
				p.sendMessage(ChatColor.BLUE + "northCompass "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " Hunters' compasses always point North.");
			} else {
				p.sendMessage(ChatColor.BLUE + "northCompass " + ChatColor.RED
						+ "[false]" + ChatColor.WHITE
						+ " Hunters' compasses point to the world spawn.");
			}

		} else if (args.length == 2 && args[1].equalsIgnoreCase("3")) {
			p.sendMessage(ChatColor.GOLD
					+ "Available manhunt settings: (3/3)");
			if (settings.autoHunter) {
				p.sendMessage(ChatColor.BLUE + "autoHunter "
						+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
						+ " New players are automatically Hunters.");
			} else {
				p.sendMessage(ChatColor.BLUE + "autoHunter "
						+ ChatColor.RED + "[false]" + ChatColor.WHITE
						+ " New players are only Spectators.");
			}
			p.sendMessage(ChatColor.BLUE + "dayLimit " + ChatColor.GREEN
					+ "[" + settings.dayLimit + "]" + ChatColor.WHITE
					+ " How many Minecraft days the game lasts.");
			if (settings.offlineTimeout >= 0) {
				p.sendMessage(ChatColor.BLUE
						+ "offlineTimeout "
						+ ChatColor.GREEN
						+ "["
						+ settings.offlineTimeout
						+ "]"
						+ ChatColor.WHITE
						+ " How long absent players have till they're disqualified.");
			} else {
				p.sendMessage(ChatColor.BLUE + "offlineTimeout "
						+ ChatColor.RED + "[off]" + ChatColor.WHITE
						+ " Players won't be kicked when logging off.");
			}
			if (settings.globalBoundry >= 0) {
				p.sendMessage(ChatColor.BLUE
						+ "globalBoundry "
						+ ChatColor.GREEN
						+ "["
						+ settings.globalBoundry
						+ "]"
						+ ChatColor.WHITE
						+ " Blocks from spawn players are allowed to venture.");
			} else {
				p.sendMessage(ChatColor.BLUE + "globalBoundry "
						+ ChatColor.RED + "[off]" + ChatColor.WHITE
						+ " Players can venture out indefinately.");
			}
			if (settings.hunterBoundry >= 0) {
				p.sendMessage(ChatColor.BLUE + "hunterBoundry "
						+ ChatColor.GREEN + "[" + settings.hunterBoundry
						+ "]" + ChatColor.WHITE
						+ " Blocks from spawn hunters are confined to.");
			} else {
				p.sendMessage(ChatColor.BLUE + "hunterBoundry "
						+ ChatColor.RED + "[off]" + ChatColor.WHITE
						+ " Hunters are not confined to spawn.");
			}
			if (settings.noBuildRange >= 0) {
				p.sendMessage(ChatColor.BLUE + "noBuildRange "
						+ ChatColor.GREEN + "[" + settings.noBuildRange
						+ "]" + ChatColor.WHITE
						+ " Hunter/Prey spawn points are protected.");
			} else {
				p.sendMessage(ChatColor.BLUE + "noBuildRange "
						+ ChatColor.RED + "[off]" + ChatColor.WHITE
						+ " Huners and Prey can build anywhere.");
			}
		}

		else if (args.length >= 2) {

			if (args[1].equalsIgnoreCase("oppermission")) {
				if (args.length == 2) {
					if (settings.opPermission) {
						p.sendMessage(ChatColor.BLUE
								+ "opPermission "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Players need op permissions to any manhunt commands.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "opPermission "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Players can choose their team and warp to world spawn.");
					}
				}
				if ((args.length == 2 && settings.opPermission == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("opPermission", "true");
					p.sendMessage(ChatColor.BLUE
							+ "opPermission "
							+ ChatColor.GREEN
							+ "[true]"
							+ ChatColor.WHITE
							+ " Players need op permissions to any manhunt commands.");

				} else if ((args.length == 2 && settings.opPermission == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("opPermission", "false");
					p.sendMessage(ChatColor.BLUE
							+ "opPermission "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Players can choose their team and warp to world spawn.");
				}

			} else if (args[1].equalsIgnoreCase("alltalk")) {
				if (args.length == 2) {
					if (settings.allTalk) {
						p.sendMessage(ChatColor.BLUE + "allTalk "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " Teams can see eachother's chat.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "allTalk "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Hunters, prey, and spectators can't chat with eachother.");
					}
				}
				if ((args.length == 2 && settings.allTalk == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("allTalk", "true");
					p.sendMessage(ChatColor.BLUE + "allTalk "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Teams can see eachother's chat.");

				} else if ((args.length == 2 && settings.allTalk == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("allTalk", "false");
					p.sendMessage(ChatColor.BLUE
							+ "allTalk "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Hunters, prey, and spectators can't chat with eachother.");
				}

			} else if (args[1].equalsIgnoreCase("spawnpassive")) {
				if (args.length == 2) {
					if (settings.spawnPassive) {
						p.sendMessage(ChatColor.BLUE + "spawnPassive "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " Passive mobs will spawn.");
					} else {
						p.sendMessage(ChatColor.BLUE + "spawnPassive "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " Passive mobs will not spawn.");
					}
					return;
				}
				if ((args.length == 2 && settings.spawnPassive == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("spawnPassive", "true");
					p.sendMessage(ChatColor.BLUE + "spawnPassive "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Passive mobs will spawn.");

				} else if ((args.length == 2 && settings.spawnPassive == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("spawnPassive", "false");
					p.sendMessage(ChatColor.BLUE + "spawnPassive "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " Passive mobs will not spawn.");
				}

			} else if (args[1].equalsIgnoreCase("spawnhostile")) {
				if (args.length == 2) {
					if (settings.spawnHostile) {
						p.sendMessage(ChatColor.BLUE + "spawnHostile "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " Hostile mobs will spawn.");
					} else {
						p.sendMessage(ChatColor.BLUE + "spawnHostile "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " Hostile mobs will not spawn.");
					}
					return;
				}
				if ((args.length == 2 && settings.spawnHostile == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("spawnHostile", "true");
					p.sendMessage(ChatColor.BLUE + "spawnHostile "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Hostile mobs will spawn.");

				} else if ((args.length == 2 && settings.spawnHostile == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("spawnHostile", "false");
					p.sendMessage(ChatColor.BLUE + "spawnHostile "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " Hostile mobs will not spawn.");
				}

			} else if (args[1].equalsIgnoreCase("envdeath")) {
				if (args.length == 2) {
					if (settings.envDeath) {
						p.sendMessage(ChatColor.BLUE
								+ "envDeath "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Players can die from mobs and enviromental hazards.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "envDeath "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Players can be damaged by environment, but never die.");
					}
					return;
				}
				if ((args.length == 2 && settings.envDeath == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("envDeath", "true");
					p.sendMessage(ChatColor.BLUE
							+ "envDeath "
							+ ChatColor.GREEN
							+ "[true]"
							+ ChatColor.WHITE
							+ " Players can die from mobs and enviromental hazards.");

				} else if ((args.length == 2 && settings.envDeath == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("envDeath", "false");
					p.sendMessage(ChatColor.BLUE
							+ "envDeath "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Players can be damaged by environment, but never die.");
				}

			} else if (args[1].equalsIgnoreCase("envhunterrespawn")) {
				if (args.length == 2) {
					if (settings.envHunterRespawn) {
						p.sendMessage(ChatColor.BLUE
								+ "envHunterRespawn "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Hunters respawn from enviromental death.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "envHunterRespawn "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Hunters are eliminated by enviromental death.");
					}
				}
				if ((args.length == 2 && settings.envHunterRespawn == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("envHunterRespawn", "true");
					p.sendMessage(ChatColor.BLUE + "envHunterRespawn "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Hunters respawn from enviromental death.");

				} else if ((args.length == 2 && settings.envHunterRespawn == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("envHunterRespawn", "false");
					p.sendMessage(ChatColor.BLUE
							+ "envHunterRespawn "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Hunters are eliminated by enviromental death.");
				}

			} else if (args[1].equalsIgnoreCase("envpreyrespawn")) {
				if (args.length == 2) {
					if (settings.envPreyRespawn) {
						p.sendMessage(ChatColor.BLUE
								+ "envPreyRespawn "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " The Prey respawn from enviromental death.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "envPreyRespawn "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " The Prey are eliminated by enviromental death.");
					}
				}
				if ((args.length == 2 && settings.envPreyRespawn == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("envPreyRespawn", "true");
					p.sendMessage(ChatColor.BLUE + "envPreyRespawn "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " The Prey respawn from enviromental death.");

				} else if ((args.length == 2 && settings.envPreyRespawn == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("envPreyRespawn", "false");
					p.sendMessage(ChatColor.BLUE
							+ "envPreyRespawn "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " The Prey are eliminated by enviromental death.");
				}

			} else if (args[1].equalsIgnoreCase("preyfinder")) {
				if (args.length == 2) {
					if (settings.envPreyRespawn) {
						p.sendMessage(ChatColor.BLUE
								+ "preyFinder "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Hunters can use a compass to find the direction to the nearest Prey.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "preyFinder "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Hunters have to find the Prey the old-fashioned way.");
					}
				}
				if ((args.length == 2 && settings.preyFinder == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("preyFinder", "true");
					p.sendMessage(ChatColor.BLUE
							+ "preyFinder "
							+ ChatColor.GREEN
							+ "[true]"
							+ ChatColor.WHITE
							+ " Hunters can use a compass to find the direction to the nearest Prey.");

				} else if ((args.length == 2 && settings.preyFinder == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("preyFinder", "false");
					p.sendMessage(ChatColor.BLUE
							+ "preyFinder "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Hunters have to find the Prey the old-fashioned way.");
				}

			} else if (args[1].equalsIgnoreCase("northCompass")) {
				if (args.length == 2) {
					if (settings.northCompass) {
						p.sendMessage(ChatColor.BLUE
								+ "northCompass "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Hunters' compasses only point North.");
					} else {
						p.sendMessage(ChatColor.BLUE
								+ "northCompass "
								+ ChatColor.RED
								+ "[false]"
								+ ChatColor.WHITE
								+ " Hunters' compasses point to the world spawn.");
					}
				}
				if ((args.length == 2 && settings.northCompass == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("northCompass", "true");
					p.sendMessage(ChatColor.BLUE
							+ "northCompass "
							+ ChatColor.GREEN
							+ "[true]"
							+ ChatColor.WHITE
							+ " Hunters' compasses always point North.");

				} else if ((args.length == 2 && settings.northCompass == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("northCompass", "false");
					p.sendMessage(ChatColor.BLUE
							+ "northCompass "
							+ ChatColor.RED
							+ "[false]"
							+ ChatColor.WHITE
							+ " Hunters' compasses point to the world spawn.");
				}

			} else if (args[1].equalsIgnoreCase("friendlyfire")) {
				if (args.length == 2) {
					if (settings.friendlyFire) {
						p.sendMessage(ChatColor.BLUE + "friendlyFire "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " Players can kill their teammates.");
					} else {
						p.sendMessage(ChatColor.BLUE + "friendlyFire "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " Players can't hurt their teammates.");
					}
					return;
				}
				if ((args.length == 2 && settings.friendlyFire == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("friendlyFire", "true");
					p.sendMessage(ChatColor.BLUE + "friendlyFire "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Players can kill their teammates.");

				} else if ((args.length == 2 && settings.friendlyFire == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("friendlyFire", "false");
					p.sendMessage(ChatColor.BLUE + "friendlyFire "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " Players can't hurt their teammates.");
				}

			} else if (args[1].equalsIgnoreCase("pvpinstantdeath")) {
				if (args.length == 2) {
					if (settings.pvpInstantDeath) {
						p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " PvP damage causes instant death.");
					} else {
						p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " PvP damage is vanilla.");
					}
					return;
				}
				if ((args.length == 2 && settings.pvpInstantDeath == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("pvpInstantDeath", "true");
					p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " PvP damage causes instant death.");

				} else if ((args.length == 2 && settings.pvpInstantDeath == true)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("pvpInstantDeath", "false");
					p.sendMessage(ChatColor.BLUE + "pvpInstantDeath "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " PvP damage is vanilla.");
				}

			} else if (args[1].equalsIgnoreCase("autohunter")) {
				if (args.length == 2) {
					if (settings.autoHunter) {
						p.sendMessage(ChatColor.BLUE + "autoHunter "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " New players are automatically Hunters.");
					} else {
						p.sendMessage(ChatColor.BLUE + "autoHunter "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " New players are only Spectators.");
					}
					return;
				}
				if ((args.length == 2 && settings.autoHunter == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("autoHunter", "true");
					p.sendMessage(ChatColor.BLUE + "autoHunter "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " New players are automatically Hunters.");

				} else if ((args.length == 2 && settings.pvpInstantDeath == false)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("autoHunter", "false");
					p.sendMessage(ChatColor.BLUE + "autoHunter "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " New players are only Spectators.");
				}

			} else if (args[1].equalsIgnoreCase("loadouts")) {
				if (args.length == 2) {
					if (settings.loadouts) {
						p.sendMessage(ChatColor.BLUE
								+ "loadouts "
								+ ChatColor.GREEN
								+ "[true]"
								+ ChatColor.WHITE
								+ " Players will start with a pre-set inventory.");
					} else {
						p.sendMessage(ChatColor.BLUE + "loadouts "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " Players' inventorys will not be reset.");
					}
					return;
				}
				if ((args.length == 2 && settings.loadouts == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("loadouts", "true");
					p.sendMessage(ChatColor.BLUE
							+ "loadouts "
							+ ChatColor.GREEN
							+ "[true]"
							+ ChatColor.WHITE
							+ " Players will start with a pre-set inventory.");

				} else if ((args.length == 2 && settings.loadouts == false)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("loadouts", "false");
					p.sendMessage(ChatColor.BLUE + "loadouts "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " Players' inventorys will not be reset.");
				}

			} else if (args[1].equalsIgnoreCase("woolhats")) {
				if (args.length == 2) {
					if (settings.woolHats) {
						p.sendMessage(ChatColor.BLUE + "woolHats "
								+ ChatColor.GREEN + "[true]"
								+ ChatColor.WHITE
								+ " Teams get identifying wool hats.");
					} else {
						p.sendMessage(ChatColor.BLUE + "woolHats "
								+ ChatColor.RED + "[false]"
								+ ChatColor.WHITE
								+ " No identifying wool hats for you!");
					}
					return;
				}
				if ((args.length == 2 && settings.woolHats == false)
						|| args[2].equalsIgnoreCase("true")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("on")) {
					settings.changeSetting("woolHats", "true");
					p.sendMessage(ChatColor.BLUE + "woolHats "
							+ ChatColor.GREEN + "[true]" + ChatColor.WHITE
							+ " Teams get identifying wool hats.");

				} else if ((args.length == 2 && settings.woolHats == false)
						|| args[2].equalsIgnoreCase("false")
						|| args[2].equalsIgnoreCase("1")
						|| args[2].equalsIgnoreCase("off")) {
					settings.changeSetting("woolHats", "false");
					p.sendMessage(ChatColor.BLUE + "woolHats "
							+ ChatColor.RED + "[false]" + ChatColor.WHITE
							+ " No identifying wool hats for you!");
				}

			} else if (args[1].equalsIgnoreCase("daylimit")) {
				if (args.length >= 3) {
					try {
						int value = Integer.parseInt(args[2]);
						if (value < 1) {
							p.sendMessage(ChatColor.RED
									+ "You must enter an number greater than 0!");
						} else {
							settings.changeSetting("dayLimit",
									Integer.toString(value));
							p.sendMessage(ChatColor.BLUE
									+ "dayLimit "
									+ ChatColor.GREEN
									+ "["
									+ settings.dayLimit
									+ "]"
									+ ChatColor.WHITE
									+ " How many Minecraft days the game lasts.");
						}
					} catch (NumberFormatException e) {
						p.sendMessage(ChatColor.RED
								+ "You must enter an INTEGER. (ie 1, 3, 5...)");
					}
				} else {
					p.sendMessage(ChatColor.BLUE + "dayLimit "
							+ ChatColor.GREEN + "[" + settings.dayLimit
							+ "]" + ChatColor.WHITE
							+ " How many Minecraft days the game lasts.");
				}

			} else if (args[1].equalsIgnoreCase("offlinetimeout")) {
				if (args.length >= 3) {
					int value;
					if (args[2].equalsIgnoreCase("off")
							|| args[2].equalsIgnoreCase("disable")) {
						value = -1;
					} else {
						try {
							value = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED
									+ "Invalid value. You must enter an Integer or \"OFF\"");
							return;
						}
					}
					if (value <= -1) {
						settings.changeSetting("offlineTimeout", "-1");
						p.sendMessage(ChatColor.BLUE
								+ "offlineTimeout "
								+ ChatColor.RED
								+ "[off]"
								+ ChatColor.WHITE
								+ " Players won't be kicked when logging off.");
					} else {
						settings.changeSetting("offlineTimeout",
								Integer.toString(value));
						p.sendMessage(ChatColor.BLUE
								+ "offlineTimeout "
								+ ChatColor.GREEN
								+ "["
								+ settings.offlineTimeout
								+ "]"
								+ ChatColor.WHITE
								+ " How long absent players have till they're disqualified.");
					}
				} else {
					p.sendMessage(ChatColor.BLUE
							+ "offlineTimeout "
							+ ChatColor.GREEN
							+ "["
							+ settings.offlineTimeout
							+ "]"
							+ ChatColor.WHITE
							+ " How long absent players have till they're disqualified.");
				}

			} else if (args[1].equalsIgnoreCase("globalboundry")) {
				if (args.length >= 3) {
					int value;
					if (args[2].equalsIgnoreCase("off")
							|| args[2].equalsIgnoreCase("disable")) {
						value = -1;
					} else {
						try {
							value = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED
									+ "Invalid value. You must enter an Integer or \"OFF\"");
							return;
						}
					}
					if (value <= -1) {
						settings.changeSetting("globalBoundry", "-1");
						p.sendMessage(ChatColor.BLUE + "globalBoundry "
								+ ChatColor.RED + "[off]" + ChatColor.WHITE
								+ "Players can venture out indefinately.");
					} else if (value < 256) {
						settings.changeSetting("globalBoundry", "256");
						p.sendMessage(ChatColor.RED
								+ "256 blocks is the minimum setting for this!");
						p.sendMessage(ChatColor.BLUE
								+ "globalBoundry "
								+ ChatColor.GREEN
								+ "["
								+ settings.globalBoundry
								+ "]"
								+ ChatColor.WHITE
								+ " Blocks from spawn players are allowed to venture.");
					} else {
						settings.changeSetting("globalBoundry",
								Integer.toString(value));
						p.sendMessage(ChatColor.BLUE
								+ "globalBoundry "
								+ ChatColor.GREEN
								+ "["
								+ settings.globalBoundry
								+ "]"
								+ ChatColor.WHITE
								+ " Blocks from spawn players are allowed to venture.");
					}
				} else {
					p.sendMessage(ChatColor.BLUE
							+ "globalBoundry "
							+ ChatColor.GREEN
							+ "["
							+ settings.globalBoundry
							+ "]"
							+ ChatColor.WHITE
							+ " Blocks from spawn players are allowed to venture.");
				}
			} else if (args[1].equalsIgnoreCase("hunterboundry")) {
				if (args.length >= 3) {
					int value;
					if (args[2].equalsIgnoreCase("off")
							|| args[2].equalsIgnoreCase("disable")) {
						value = -1;
					} else {
						try {
							value = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED
									+ "Invalid value. You must enter an Integer or \"OFF\"");
							return;
						}
					}
					if (value <= -1) {
						settings.changeSetting("hunterBoundry", "-1");
						p.sendMessage(ChatColor.BLUE + "hunterBoundry "
								+ ChatColor.RED + "[off]" + ChatColor.WHITE
								+ "Hunters are not confined to spawn.");
					} else {
						settings.changeSetting("hunterBoundry",
								Integer.toString(value));
						p.sendMessage(ChatColor.BLUE
								+ "hunterBoundry "
								+ ChatColor.GREEN
								+ "["
								+ settings.hunterBoundry
								+ "]"
								+ ChatColor.WHITE
								+ " Blocks from spawn hunters are confined to.");
					}
				} else {
					p.sendMessage(ChatColor.BLUE + "hunterBoundry "
							+ ChatColor.GREEN + "["
							+ settings.hunterBoundry + "]"
							+ ChatColor.WHITE
							+ " Blocks from spawn hunters are confined to.");
				}
			} else if (args[1].equalsIgnoreCase("nobuildrange")) {
				if (args.length >= 3) {
					int value;
					if (args[2].equalsIgnoreCase("off")
							|| args[2].equalsIgnoreCase("disable")) {
						value = -1;
					} else {
						try {
							value = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED
									+ "Invalid value. You must enter an Integer or \"OFF\"");
							return;
						}
					}
					if (value <= -1) {
						settings.changeSetting("nobuildrange", "-1");
						p.sendMessage(ChatColor.BLUE + "noBuildRange "
								+ ChatColor.RED + "[off]" + ChatColor.WHITE
								+ " Huners and Prey can build anywhere.");
					} else {
						settings.changeSetting("nobuildrange",
								Integer.toString(value));
						p.sendMessage(ChatColor.BLUE
								+ "noBuildRange "
								+ ChatColor.GREEN
								+ "["
								+ settings.noBuildRange
								+ "]"
								+ ChatColor.WHITE
								+ " Hunter/Prey spawn points are protected.");
					}
				} else {
					p.sendMessage(ChatColor.BLUE + "noBuildRange "
							+ ChatColor.GREEN + "[" + settings.noBuildRange
							+ "]" + ChatColor.WHITE
							+ " Hunter/Prey spawn points are protected.");
				}
			} else {
				p.sendMessage(ChatColor.RED
						+ "Invalid setting. Type \"/manhunt settings <pg>\" for a complete list.");
			}
		}
	}
	
	
	
	
}
