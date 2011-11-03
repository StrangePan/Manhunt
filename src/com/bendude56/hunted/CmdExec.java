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

	@Override
	public boolean onCommand(CommandSender sender, Command c, String cmd,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "The /manhunt command cannot be used from the console!");
			return true;
		}
		Player p = (Player) sender;
		// DataFile f = (Game.isGameStarted()) ? Game.getActiveGame().getDataFile() : null;
		Game g = HuntedPlugin.getInstance().game;
		SettingsFile settings = HuntedPlugin.getInstance().settings;
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if (!settings.opPermission || sender.isOp()) {
				
				p.sendMessage(ChatColor.YELLOW + "/manhunt quit");
				p.sendMessage(ChatColor.YELLOW + "    Quits the current manhunt game and makes you a spectator.");
				
				p.sendMessage(ChatColor.YELLOW + "/manhunt join {hunter|hunted} <player>");
				p.sendMessage(ChatColor.YELLOW + "    Joins the selected manhunt team.");
				if (p.isOp()) p.sendMessage(ChatColor.YELLOW + "    Include an optional player name to assign them to that team.");
				
				p.sendMessage(ChatColor.YELLOW + "/manhunt spectate <player>");
				p.sendMessage(ChatColor.YELLOW + "    Lets you spectate the manhunt game.");
				if (p.isOp()) p.sendMessage(ChatColor.YELLOW + "     Include an optional player name to make them a spectator.");
				
				p.sendMessage(ChatColor.YELLOW + "/manhunt world <player>");
				p.sendMessage(ChatColor.YELLOW + "    Teleports you to the manhunt world");
				if (p.isOp()) p.sendMessage(ChatColor.YELLOW + "    Include an optional player name to teleport them to the world.");
				
				if (p.isOp()) {
					p.sendMessage(ChatColor.YELLOW + "/manhunt kick {player}");
					p.sendMessage(ChatColor.YELLOW + "    Kicks a player from the game and makes them a spectator.");
				}
				
			}
		} else if (args[0].equalsIgnoreCase("join")) {
			if (g.gameHasBegun()) {
				p.sendMessage(ChatColor.RED + "You cannot join a game already in progress!");
				return true;
			} else if (settings.opPermission && !p.isOp()) {
				p.sendMessage(ChatColor.RED + "Only OPs have permission to do that.");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED + "You must choose a team! Hunters or Hunted.");
				return true;
			} if (args[1].equalsIgnoreCase("hunter")) {
				if (args.length == 2) {
					g.addHunter(p);
					g.broadcastAll(ChatColor.RED + p.getName() + ChatColor.WHITE + " has joined team " + ChatColor.RED + "Hunters.");
				} else if (args.length > 2 && p.isOp()) {
					g.addHunter(args[2]);
					g.broadcastAll(ChatColor.RED + args[2] + ChatColor.WHITE + " has joined team " + ChatColor.RED + "Hunters.");
				}	
			} else if (args[1].equalsIgnoreCase("hunted") || args[1].equalsIgnoreCase("prey")) {
				if (args.length == 2) {
					g.addHunted(p);
					g.broadcastAll(ChatColor.BLUE + p.getName() + ChatColor.WHITE + " has joined team " + ChatColor.BLUE + "Hunted.");
				} else if (args.length > 2 && p.isOp()) {
					g.addHunted(args[2]);
					g.broadcastAll(ChatColor.BLUE + args[2] + ChatColor.WHITE + " has joined team " + ChatColor.BLUE + "Hunted.");
				}
			} else if (args[1].equalsIgnoreCase("spectators") || args[1].equalsIgnoreCase("spectator")) {
				if (args.length == 2) {
					g.addSpectator(p);
					g.broadcastSpectators(ChatColor.YELLOW + p.getName() + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
				} else if (args.length > 2 && p.isOp()) {
					g.addHunted(args[2]);
					g.broadcastSpectators(ChatColor.YELLOW + args[2] + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
				}
			}
		} else if (args[0].equalsIgnoreCase("quit")) {
			if (g.isHunted(p) || g.isHunter(p)) {
				if (g.gameHasBegun()) {
					g.broadcastSpectators(ChatColor.YELLOW + p.getName() + ChatColor.WHITE + " has quit the game to become a " + ChatColor.YELLOW + "Spectator.");
					g.onDie(p);
					p.sendMessage(ChatColor.YELLOW + "You have quit the game and are now spectating.");
					return true;
				} else {
					g.addSpectator(p);
					g.broadcastSpectators(ChatColor.YELLOW + p.getName() + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
					return true;
				}
			}
		} else if (args[0].equalsIgnoreCase("kick")) {
			if (args.length >= 2) {
				Player p2 = Bukkit.getPlayerExact(args[1]);
				if (p2 == null) {
					p.sendMessage(ChatColor.RED + "No player named " + args[1] + " exists!");
					return true;
				}
				if (p.isOp() && !p2.isOp() && p != p2) {
					g.broadcastAll(g.getColor(p2) + p2.getName() + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
					g.onDie(p2);
					p2.sendMessage(ChatColor.RED + "You have been kicked from the game. You are now spectating.");
					return true;
				}
			}
		} else if (args[0].equalsIgnoreCase("spectate")) {
			if ((g.isHunted(p) || g.isHunter(p)) && g.gameHasBegun() && args.length == 1) {
				p.sendMessage(ChatColor.RED + "You can't spectate while you're playing!");
				p.sendMessage(ChatColor.RED + "Use \"/manhunt quit\" if you want to quit the game.");
				return true;
			} else if (p.isOp() || !settings.opPermission) {
				if (args.length==1) {
					g.addSpectator(p);
					g.broadcastSpectators(ChatColor.YELLOW + p.getName() + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
					return true;
				} else if (args.length >= 2 && p.isOp()) {
					Player p2 = Bukkit.getPlayerExact(args[1]);
					if (p2 == null) {
						p.sendMessage(ChatColor.RED + args[1] + " does not exist!");
						return true;
					}
					if ((g.isHunted(p2) || g.isHunter(p2)) && g.gameHasBegun()) {
						p.sendMessage(ChatColor.RED + args[1] + " is in the middle of a game!");
						p.sendMessage(ChatColor.RED + "Use \"/manhunt kick <" + args[1] + ">\" to properly kick them.");
						return true;
					} else {
						g.broadcastSpectators(g.getColor(p2) + p2.getName() + ChatColor.WHITE + " has become a " + ChatColor.YELLOW + "Spectator.");
						g.addSpectator(p2);
						return true;
					}
				}
			}
			
		}else if (args[0].equalsIgnoreCase("list")) {
			if (settings.opPermission && !p.isOp()) {
				p.sendMessage(ChatColor.RED + "You don'thave permission to do that!");
				return true;
			} else if (args.length == 1 || (args.length >= 2 && args[1].equalsIgnoreCase("all"))) {
				p.sendMessage(ChatColor.GREEN + "Manhunt players: (" + (g.HuntedAmount()+g.HuntersAmount()+g.SpectatorsAmount()) + ")");
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
				p.sendMessage(ChatColor.GREEN + "Team HUNTERS: (" + g.HuntersAmount() + ")");
				for (String s : g.getHunters()) {
					p.sendMessage(ChatColor.RED + "  " + s);
				}
			} else if (args.length >= 2 && args[1].equalsIgnoreCase("hunted")) {
				p.sendMessage(ChatColor.GREEN + "Team HUNTED: (" + g.HuntedAmount() + ")");
				for (String s : g.getHunted()) {
					p.sendMessage(ChatColor.BLUE + "  " + s);
				}
			} else if (args.length >= 2 && args[1].equalsIgnoreCase("spectators")) {
				p.sendMessage(ChatColor.GREEN + "Manhunt SPECTATORS: (" + g.SpectatorsAmount() + ")");
				for (String s : g.getSpectators()) {
					p.sendMessage(ChatColor.YELLOW + "  " + s);
				}
			} else {
				p.sendMessage(ChatColor.RED + "Invalid team. Available teams: Hunters, Hunted, Spectators, All.");
			}
			
		} else if (args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("spawn")) {
			
			if (p.isOp() || !settings.opPermission || g.isSpectating(p)) {
				if (args.length == 1) {
					if (g.isSpectating(p) || !g.gameHasBegun()) {
						p.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "You can't warp to the manhunt spawn once the game has started!");
						return true;
					}
				} else if (args.length >= 2 && p.isOp()) {
					Player p2 = Bukkit.getPlayerExact(args[1]);
					if (p2 == null) {
						p.sendMessage(ChatColor.RED + "Player " + args[1] + " does not exist!");
						return true;
					}
					if (g.isSpectating(p2) || !g.gameHasBegun()) {
						p2.teleport(HuntedPlugin.getInstance().getWorld().getSpawnLocation());
						p2.sendMessage(ChatColor.YELLOW + "You have been teleported to the manhunt world spawn.");
						p.sendMessage(ChatColor.YELLOW + args[1] + " has been teleported to the manhunt world spawn.");
						return true;
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("setspawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You can't do that from the console!");
				return true;
			}
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "Only ops can start the game!");
				return true;
			} else {
				if (p.getWorld().equals(HuntedPlugin.getInstance().getWorld())) {
					HuntedPlugin.getInstance().getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
					p.sendMessage(ChatColor.GREEN + "World spawn set!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "You must be in the manhunt world to set it's spawn. Use \"/manhunt world\" to teleport to the manhunt world.");
					return true;
				}
			}
		} else if (args[0].equalsIgnoreCase("startgame")) {
			if (!p.isOp() && (sender instanceof Player)) {
				p.sendMessage(ChatColor.RED + "Only ops can start the manhunt game!");
				return true;
			}
			if (g.HuntersAmount() == 0 || g.HuntedAmount() == 0) {
				p.sendMessage(ChatColor.RED + "There must be at least one Hunter and Hunted to start the game!");
				return true;
			}
			g.start();
			p.sendMessage(ChatColor.GRAY + "You have successfully started the manhunt game!");
			return true;
		} else if (args[0].equalsIgnoreCase("stopgame")) {
			if (!p.isOp() && (sender instanceof Player)) {
				p.sendMessage(ChatColor.RED + "Only ops can quit the manhunt game!");
				return true;
			}
			g.stop();
			g.broadcastAll(g.getColor(p) + p.getName() + ChatColor.WHITE + " has stopped the game!");
		} else if (args[0].equalsIgnoreCase("setting") || args[0].equalsIgnoreCase("settings")
				|| args[0].equalsIgnoreCase("preferences") || args[0].equalsIgnoreCase("properties")) {
			if (!p.isOp() && (sender instanceof Player)) {
				p.sendMessage(ChatColor.RED + "Only ops can change manhunt game settings!");
			}
			if (args.length == 1 || (args.length ==  2 && args[1].equalsIgnoreCase("1"))) {
				p.sendMessage(ChatColor.YELLOW + "Available manhunt settings: (1/3)");
				p.sendMessage(ChatColor.YELLOW + "    opPermission [true]  Only ops have access to all mahunt commands.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Anyone can choose their team and warp to world spawn.");
				p.sendMessage(ChatColor.YELLOW + "    allTalk      [true]  All players can text chat.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Hunters, hunted, and spectators can't chat with eachother.");
				p.sendMessage(ChatColor.YELLOW + "    spawnPassive [true]  Passive mobs will spawn.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Passive mobs will not spawn.");
				p.sendMessage(ChatColor.YELLOW + "    spawnHostile [true]  Hostile mobs will spawn.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Hostile mobs will not spawn.");
			} else if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("2"))) {
				p.sendMessage(ChatColor.YELLOW + "Available manhunt settings: (2/3)");
				p.sendMessage(ChatColor.YELLOW + "    envDeath     [true]  Players can die from mobs and enviromental hazards.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Players can be damaged, but never die by the cruel world.");
				p.sendMessage(ChatColor.YELLOW + "    envHunterRespawn [true]  Hunters respawn from enviromental death.");
				p.sendMessage(ChatColor.YELLOW + "                     [false] Hunters are eliminated by enviromental death.");
				p.sendMessage(ChatColor.YELLOW + "    envHuntedRespawn [true]  The Hunted respawn from enviromental death.");
				p.sendMessage(ChatColor.YELLOW + "                     [false] The Hunted are eliminated by enviromental death.");
				p.sendMessage(ChatColor.YELLOW + "    friendlyFire [true]  Players on same team can kill eachother.");
				p.sendMessage(ChatColor.YELLOW + "                 [false] Players on same team can't hurt eachother.");
			} else if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("3"))) {
				p.sendMessage(ChatColor.YELLOW + "Available manhunt settings: (3/3)");
				p.sendMessage(ChatColor.YELLOW + "    pvpInstantDeath  [true]  PvP damage causes instant DEATH");
				p.sendMessage(ChatColor.YELLOW + "                     [false] PvP damage is vanilla.");
				p.sendMessage(ChatColor.YELLOW + "    dayLimit       [integer]  How many days that the game lasts (MC days)");
				p.sendMessage(ChatColor.YELLOW + "    offlineTimeout [minutes]  How long absent players have till they're disqualified. (-1 = disable)");
				p.sendMessage(ChatColor.YELLOW + "    globalBoundry  [blocks ]  Blocks from spawn players are allowed to venture. (-1 = infinity)");
				p.sendMessage(ChatColor.YELLOW + "    hunterBoundry  [blocks ]  Blocks from spawn hunters are confined to. (pre-game) (-1 = infinity)");
			}
			
			else if (args.length >= 2) {
				if (args[1].equalsIgnoreCase("oppermission")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.opPermission = true;
							p.sendMessage(ChatColor.GREEN + "opPermission [true]. Only ops have access to the manhunt commands.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.opPermission = false;
							p.sendMessage(ChatColor.GREEN + "opPermission [false]. All players have access to a few commands.");
						}
					} else {
						if (settings.opPermission) {
							settings.opPermission = false;
							p.sendMessage(ChatColor.GREEN + "opPermission [false]. All players have access to a few commands.");
						} else {
							settings.opPermission = true;
							p.sendMessage(ChatColor.GREEN + "opPermission [true]. Only ops have access to the manhunt commands.");
						}
					}
				} else if (args[1].equalsIgnoreCase("alltalk")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.allTalk = true;
							p.sendMessage(ChatColor.GREEN + "allTalk [true]. All players can communicate.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.allTalk = false;
							p.sendMessage(ChatColor.GREEN + "allTalk [false]. Players can only communicate with their teams.");
						}
					} else {
						if (settings.allTalk) {
							settings.allTalk = false;
							p.sendMessage(ChatColor.GREEN + "allTalk [false]. Players can only communicate with their teams.");
						} else {
							settings.allTalk = true;
							p.sendMessage(ChatColor.GREEN + "allTalk [true]. All players can communicate.");
						}
					}
				} else if (args[1].equalsIgnoreCase("spawnpassive")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.spawnPassive = true;
							p.sendMessage(ChatColor.GREEN + "spawnPassive [true]. Passive mobs will NOW spawn.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.spawnPassive = false;
							p.sendMessage(ChatColor.GREEN + "spawnPassive [false]. Passive mobs will NOT spawn.");
						}
					} else {
						if (settings.spawnPassive) {
							settings.spawnPassive = false;
							p.sendMessage(ChatColor.GREEN + "spawnPassive [false]. Passive mobs will NOT spawn.");
						} else {
							settings.spawnPassive = true;
							p.sendMessage(ChatColor.GREEN + "spawnPassive [true]. Passive mobs will NOW spawn.");
						}
					}
				} else if (args[1].equalsIgnoreCase("spawnhostile")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.spawnHostile = true;
							p.sendMessage(ChatColor.GREEN + "spawnHostile [true]. Hostile mobs will NOW spawn.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.spawnHostile = false;
							p.sendMessage(ChatColor.GREEN + "spawnHostile [false]. Hostile mobs will NOT spawn.");
						}
					} else {
						if (settings.spawnHostile) {
							settings.spawnHostile = false;
							p.sendMessage(ChatColor.GREEN + "spawnHostile [false]. Hostile mobs will NOT spawn.");
						} else {
							settings.spawnHostile = true;
							p.sendMessage(ChatColor.GREEN + "spawnHostile [true]. Hostile mobs will NOW spawn.");
						}
					}
				} else if (args[1].equalsIgnoreCase("envdeath")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.envDeath = true;
							p.sendMessage(ChatColor.GREEN + "envDeath [true]. Players can now die by inviromental hazards.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.envDeath = false;
							p.sendMessage(ChatColor.GREEN + "envDeath [false]. Players can't die by inviromental hazards.");
						}
					} else {
						if (settings.envDeath) {
							settings.envDeath = false;
							p.sendMessage(ChatColor.GREEN + "envDeath [false]. Players can't die by inviromental hazards.");
						} else {
							settings.envDeath = true;
							p.sendMessage(ChatColor.GREEN + "envDeath [true]. Players can now die by inviromental hazards.");
						}
					}
				} else if (args[1].equalsIgnoreCase("envhunterrespawn")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.envHunterRespawn = true;
							p.sendMessage(ChatColor.GREEN + "envHunterRespawn [true]. Hunters respawn after dying from environmental hazards.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.envHunterRespawn = false;
							p.sendMessage(ChatColor.GREEN + "envHunterRespawn [false]. Hunters can be eliminated by enviromental hazards.");
						}
					} else {
						if (settings.envHunterRespawn) {
							settings.envHunterRespawn = false;
							p.sendMessage(ChatColor.GREEN + "envHunterRespawn [false]. Hunters can be eliminated by enviromental hazards.");
						} else {
							settings.envHunterRespawn = true;
							p.sendMessage(ChatColor.GREEN + "envHunterRespawn [true]. Hunters respawn after dying from environmental hazards.");
						}
					}
				} else if (args[1].equalsIgnoreCase("envhuntedRespawn")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.envHuntedRespawn = true;
							p.sendMessage(ChatColor.GREEN + "envHuntedRespawn [true]. The Hunted respawn after dying from environmental hazards.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.envHuntedRespawn = false;
							p.sendMessage(ChatColor.GREEN + "envHuntedRespawn [false]. The Hunted can be eliminated by environmental hazards.");
						}
					} else {
						if (settings.envHuntedRespawn) {
							settings.envHuntedRespawn = false;
							p.sendMessage(ChatColor.GREEN + "envHuntedRespawn [false]. The Hunted can be eliminated by environmental hazards.");
						} else {
							settings.envHuntedRespawn = true;
							p.sendMessage(ChatColor.GREEN + "envHuntedRespawn [true]. The Hunted respawn after dying from environmental hazards.");
						}
					}
				} else if (args[1].equalsIgnoreCase("friendlyfire")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.friendlyFire = true;
							p.sendMessage(ChatColor.GREEN + "friendlyFire [true]. Teammates can kill each other.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.friendlyFire = false;
							p.sendMessage(ChatColor.GREEN + "friendlyFire [false]. Teammates can't hurt each other.");
						}
					} else {
						if (settings.friendlyFire) {
							settings.friendlyFire = false;
							p.sendMessage(ChatColor.GREEN + "friendlyFire [false]. Teammates can't hurt each other.");
						} else {
							settings.friendlyFire = true;
							p.sendMessage(ChatColor.GREEN + "friendlyFire [true]. Teammates can kill each other.");
						}
					}
				} else if (args[1].equalsIgnoreCase("pvpinstantdeath")){ 
					if (args.length == 3) {
						if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("on")) {
							settings.pvpInstantDeath = true;
							p.sendMessage(ChatColor.GREEN + "pvpInstantDeath [true]. A single punch can fully kill the enemy.");
						} else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("off")) {
							settings.pvpInstantDeath = false;
							p.sendMessage(ChatColor.GREEN + "pvpInstantDeath [false]. PvP damage is now vanilla again.");
						}
					} else {
						if (settings.pvpInstantDeath) {
							settings.pvpInstantDeath = false;
							p.sendMessage(ChatColor.GREEN + "pvpInstantDeath [false]. PvP damage is now vanilla again.");
						} else {
							settings.pvpInstantDeath = true;
							p.sendMessage(ChatColor.GREEN + "pvpInstantDeath [true]. A single punch can fully kill the enemy.");
						}
					}
				} else if (args[1].equalsIgnoreCase("daylimit")){ 
					if (args.length == 3) {
						try {
							int value = Integer.parseInt(args[2]);
							if (value < 1) {
								p.sendMessage(ChatColor.RED + "You must enter an number greater than 0!");
							} else {
								settings.dayLimit = value;
								p.sendMessage(ChatColor.GREEN + "Day time limit set to " + value + " days.");
							}
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED + "You must enter an INTEGER. (ie 1, 3, 5...)");
						}
					}
				} else if (args[1].equalsIgnoreCase("offlinetimeout")){ 
					if (args.length == 3) {
						try {
							int value = Integer.parseInt(args[2]);
							if (value <= -1) {
								settings.offlineTimeout = -1;
								p.sendMessage(ChatColor.GREEN + "Offline timeout has been disabled. Players may come and go as they please.");
							} else {
								settings.offlineTimeout = value;
								p.sendMessage(ChatColor.GREEN + "Offline timeout has been set to " + value + " minutes.");
							}
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED + "You must enter an INTEGER. (ie -1, 0, 3...)");
						}
					}
				} else if (args[1].equalsIgnoreCase("globalboundry")){ 
					if (args.length == 3) {
						try {
							int value = Integer.parseInt(args[2]);
							if (value <= -1) {
								settings.globalBoundry = -1;
								p.sendMessage(ChatColor.GREEN + "The global boundry has been lifted. Players can venture out indefinately.");
							} else if (value < 256) {
								settings.globalBoundry = value;
								p.sendMessage(ChatColor.GREEN + "The global boundry has been set to 256 blocks. (minimum)");
							} else {
								settings.globalBoundry = value;
								p.sendMessage(ChatColor.GREEN + "The global boundry has been set to " + value + " blocks.");
							}
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED + "You must enter an INTEGER. (ie -1, 256, 1000...)");
						}
					}
				} else if (args[1].equalsIgnoreCase("hunterboundry")){ 
					if (args.length == 3) {
						try {
							int value = Integer.parseInt(args[2]);
							if (value <= -1) {
								settings.hunterBoundry = -1;
								p.sendMessage(ChatColor.GREEN + "The hunter's pre-game restriction has been lifted.");
							} else {
								settings.hunterBoundry = value;
								p.sendMessage(ChatColor.GREEN + "The hunter's pre-game restriction has been set to " + value + " blocks.");
							}
						} catch (NumberFormatException e) {
							p.sendMessage(ChatColor.RED + "You must enter an INTEGER. (ie -1, 3, 16...)");
						}
					}
				}
			}
		}
		return true;
			/*} else if (args[0].equalsIgnoreCase("create")) {
				if (!p.isOp()) {
					p.sendMessage(ChatColor.RED + "You're not allowed to do that!");
					return true;
				} else if (Game.isGameStarted()) {
					p.sendMessage(ChatColor.RED + "There is already a game running!");
					return true;
				}
			}
			if (args.length == 1) {
				if (!DataFile.exists("preferences")) {
					DataFile df;
					try {
						df = DataFile.newFile("preferences");
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while creating the new data file!");
						Bukkit.getLogger().severe("An attempt to create a data file (preferences.dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A new game was successfully created!");
				} else {
					DataFile df;
					try {
						df = new DataFile("preferences");
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while loading the data file!");
						Bukkit.getLogger().severe("An attempt to load a data file (preferences.dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A new game was successfully created!");
				}
			} else if (args.length > 1) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			}
		} else if (args[0].equalsIgnoreCase("load")) {
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "You're not allowed to do that!");
				return true;
			} else if (Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "There is already a game running!");
				return true;
			}
			if (args.length == 1) {
				if (!DataFile.exists("preferences")) {
					DataFile df;
					try {
						df = DataFile.newFile("preferences");
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while creating the new data file!");
						Bukkit.getLogger().severe("An attempt to create a data file (preferences.dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A new game was successfully created!");
				} else {
					DataFile df;
					try {
						df = new DataFile("preferences");
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while loading the data file!");
						Bukkit.getLogger().severe("An attempt to load a data file (preferences.dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A new game was successfully created!");
				}
			} else if (args.length > 2) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			} else {
				if (!DataFile.exists(args[1])) {
					p.sendMessage(ChatColor.RED + "No data file exists by the name '" + args[1] + ".dat'!");
				} else {
					DataFile df;
					try {
						df = new DataFile(args[1]);
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while loading the data file!");
						Bukkit.getLogger().severe("An attempt to load a data file (" + args[1] + ".dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A game was successfully created with that data file!");
				}
			}
		} else if (args[0].equalsIgnoreCase("new")) {
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "You're not allowed to do that!");
				return true;
			} else if (Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "There is already a game running!");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED + "Not enough arguments!");
			} else {
				if (!DataFile.exists(args[1])) {
					DataFile df;
					try {
						df = DataFile.newFile(args[1]);
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "An error occured while creating the new data file!");
						Bukkit.getLogger().severe("An attempt to create a data file (" + args[1] + ".dat) failed:");
						e.printStackTrace();
						return true;
					}
					Game.newActiveGame(df);
					p.sendMessage(ChatColor.GREEN + "A new game was successfully created with that data file.");
				}
			}
		} else {
			Bukkit.dispatchCommand(p, "manhunt help");
		}
		return true;*/
		/*if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments!");
			
		} else if (args[0].equalsIgnoreCase("help")
				|| args[0].equalsIgnoreCase("commands")) {
			
			sender.sendMessage(ChatColor.YELLOW + "/manhunt spawn");
			sender.sendMessage(ChatColor.WHITE + "   sends you to the manhunt world spawn.");
			
			if (sender.isOp()) {
				sender.sendMessage(ChatColor.YELLOW + "/manhunt hunter <name> <name> <name> ...");
				sender.sendMessage(ChatColor.WHITE + "   Adds the listed players to the 'hunters' team.");

				sender.sendMessage(ChatColor.YELLOW + "/manhunt prey <name> <name> <name> ...");
				sender.sendMessage(ChatColor.WHITE + "   Adds the listed players to the 'hunted' team.");

				sender.sendMessage(ChatColor.YELLOW + "/manhunt spectator <name> <name> <name> ...");
				sender.sendMessage(ChatColor.WHITE + "   Makes the listed players 'spectators'.");
				
				sender.sendMessage(ChatColor.YELLOW + "/manhunt moveall <hunters|prey|spectators>");
				sender.sendMessage(ChatColor.WHITE + "   Moves all current players to the specified team.");
				
				sender.sendMessage(ChatColor.YELLOW + "/manhunt timeout <minutes>");
				sender.sendMessage(ChatColor.WHITE + "   Adds a kick timer when someone loses connection. Use 'none' to disable it.");
				
				sender.sendMessage(ChatColor.YELLOW + "/manhunt timeout <minutes>");
				sender.sendMessage(ChatColor.WHITE + "   adds a kick timer when someone loses connection. Use 'none' to disable it");
			}
			
			
		} else if (args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("spawn")) {
			if (sender instanceof Player) {
				((Player) sender).teleport(Bukkit.getServer().getWorld("manhunt").getSpawnLocation());
			} else {
				sender.sendMessage(ChatColor.RED + "You can't do that from the console!");
			}
			
		} else if (args[0].equalsIgnoreCase("hunter")
				|| args[0].equalsIgnoreCase("hunters")
				|| args[0].equalsIgnoreCase("addhunters")
				|| args[0].equalsIgnoreCase("addhunters")
				|| args[0].equalsIgnoreCase("sethunters")
				|| args[0].equalsIgnoreCase("makehunter")
				|| args[0].equalsIgnoreCase("addhunter")
				|| args[0].equalsIgnoreCase("sethunter")
				|| args[0].equalsIgnoreCase("makehunters")) {
			
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You need to list some names!");
				return true;
			}
			
			for (int i=1 ; i < args.length ; i++) {
				game.makeHunter(args[i]);
			}
			
		} else if (args[0].equalsIgnoreCase("hunted")
				|| args[0].equalsIgnoreCase("addhunted")
				|| args[0].equalsIgnoreCase("sethunted")
				|| args[0].equalsIgnoreCase("makehunted")
				|| args[0].equalsIgnoreCase("prey")
				|| args[0].equalsIgnoreCase("addprey")
				|| args[0].equalsIgnoreCase("makeprey")
				|| args[0].equalsIgnoreCase("setprey")) {

			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You need to list some names!");
				return true;
			}
			
			for (int i=1 ; i < args.length ; i++) {
				game.makeHunted(args[i]);
			}
			
		} else if (args[0].equalsIgnoreCase("spectator")
				|| args[0].equalsIgnoreCase("spectate")
				|| args[0].equalsIgnoreCase("spectators")
				|| args[0].equalsIgnoreCase("makespectator")
				|| args[0].equalsIgnoreCase("makespectators")
				|| args[0].equalsIgnoreCase("setspectator")
				|| args[0].equalsIgnoreCase("setspectators")
				|| args[0].equalsIgnoreCase("addspectator")
				|| args[0].equalsIgnoreCase("addspectators")
				|| args[0].equalsIgnoreCase("addspectate")
				|| args[0].equalsIgnoreCase("makespectate")
				|| args[0].equalsIgnoreCase("setspectate")
				|| args[0].equalsIgnoreCase("watchers")
				|| args[0].equalsIgnoreCase("makewatcher")
				|| args[0].equalsIgnoreCase("forcewatch")
				|| args[0].equalsIgnoreCase("setwatcher")) {

			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You need to list some names!");
				return true;
			}
			
			for (int i=1 ; i < args.length ; i++) {
				game.makeSpectator(args[i]);
			}
			
		} else if (args[0].equals("moveall")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You need to specify a team to move everyone to!");
			}
			if (args[1].equalsIgnoreCase("hunters")
					|| args[1].equalsIgnoreCase("hunter")) {
				game.makeAllPlayersHunters();
			} else if (args[1].equalsIgnoreCase("hunted")
					|| args[1].equalsIgnoreCase("prey")) {
				game.makeAllPlayersHunted();
			} else if (args[1].equalsIgnoreCase("spectator")
					|| args[1].equalsIgnoreCase("spectators")
					|| args[1].equalsIgnoreCase("spectate")) {
				game.makeAllPlayersSpectators();
			}
			
		} else if (args[0].equalsIgnoreCase("clear")
				|| args[0].equalsIgnoreCase("clearall")) {
			if (args.length == 1) {
				game.removeAll();
			} else if (args[1].equalsIgnoreCase("hunters")) {
				game.removeAllHunters();
			} else if (args[1].equalsIgnoreCase("hunted")) {
				game.removeAllHunted();
			} else if (args[1].equalsIgnoreCase("spectators")) {
				game.removeAllSpectators();
			}
			
		} else if (args[0].equalsIgnoreCase("kickspectators")) {
			if (args.length == 1) {
				game.kickSpectators();
				sender.sendMessage(ChatColor.YELLOW + "All spectators have been kicked from the server!");
			} else if (args.length == 2) {
				if (game.kickSpectators(args[1])) {
					sender.sendMessage(ChatColor.YELLOW + "All spectators have been teleported to world " + args[1] + ".");
				} else {
					if (!args[1].equalsIgnoreCase("world")) {
						sender.sendMessage(ChatColor.RED + "That world does not exist! Try 'world'.");
					} else {
						sender.sendMessage(ChatColor.RED + "That world does not exist!");
					}
				}
			} else {
				sender.sendMessage(ChatColor.GREEN + "Too many arguments!");
			}
			
		} else if (args[0].equalsIgnoreCase("pvponly")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				if (plugin.pvpOnly == false) {
					plugin.pvpOnly = true;
					game.broadcastPlayers(ChatColor.GREEN + "PvP ONLY mode has been activated. You can only be killed by other players!");
				} else {
					plugin.pvpOnly = false;
					game.broadcastPlayers(ChatColor.GREEN + "PvP ONLY mode has been deactivated. Beware the creepers!");
				}
			} else if (args[1].equalsIgnoreCase("true")
					|| args[1].equalsIgnoreCase("yes")
					|| args[1].equalsIgnoreCase("1")) {
				plugin.pvpOnly = true;
				game.broadcastPlayers(ChatColor.GREEN + "PvP ONLY mode has been activated. You can only be killed by other players!");
			} else if (args[1].equalsIgnoreCase("false")
					|| args[1].equalsIgnoreCase("no")
					|| args[1].equalsIgnoreCase("0")) {
				plugin.pvpOnly = false;
				game.broadcastPlayers(ChatColor.GREEN + "PvP ONLY mode has been deactivated. Beware the creepers!");
			}
			
		} else if (args[0].equalsIgnoreCase("friendlyfire")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				if (plugin.friendlyFire == false) {
					plugin.friendlyFire = true;
					game.broadcastPlayers(ChatColor.GREEN + "Friendly fire is enabled! You can be killed by your own team!");
				} else {
					plugin.friendlyFire = false;
					game.broadcastPlayers(ChatColor.GREEN + "Friendly fire is disabled! You can only be killed by the enemy!");
				}
			} else if (args[1].equalsIgnoreCase("true")
					|| args[1].equalsIgnoreCase("yes")
					|| args[1].equalsIgnoreCase("1")) {
				plugin.friendlyFire = true;
				game.broadcastPlayers(ChatColor.GREEN + "Friendly fire is enabled! You can be killed by your own team!");
			} else if (args[1].equalsIgnoreCase("false")
					|| args[1].equalsIgnoreCase("no")
					|| args[1].equalsIgnoreCase("0")) {
				plugin.friendlyFire = false;
				game.broadcastPlayers(ChatColor.GREEN + "Friendly fire is disabled! You can only be killed by the enemy!");
			}
			
		} else if (args[0].equalsIgnoreCase("passivemobs")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				if (plugin.passiveMobs == false) {
					plugin.passiveMobs = true;
					game.broadcastPlayers(ChatColor.GREEN + "Passive mobs can now spawn!");
				} else {
					plugin.passiveMobs = false;
					game.broadcastPlayers(ChatColor.GREEN + "Passive mobs can no longer spawn!");
				}
			} else if (args[1].equalsIgnoreCase("true")
					|| args[1].equalsIgnoreCase("yes")
					|| args[1].equalsIgnoreCase("1")) {
				plugin.passiveMobs = true;
				game.broadcastPlayers(ChatColor.GREEN + "Passive mobs can now spawn!");
			} else if (args[1].equalsIgnoreCase("false")
					|| args[1].equalsIgnoreCase("no")
					|| args[1].equalsIgnoreCase("0")) {
				plugin.passiveMobs = false;
				game.broadcastPlayers(ChatColor.GREEN + "Passive mobs can no longer spawn!");
			}
			
		} else if (args[0].equalsIgnoreCase("allowspectators")
				|| args[0].equalsIgnoreCase("allowspectating")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				if (plugin.allowSpectators == false) {
					plugin.allowSpectators = true;
					game.broadcastPlayers(ChatColor.GREEN + "Spectating is now allowed!");
				} else {
					plugin.allowSpectators = false;
					game.broadcastPlayers(ChatColor.GREEN + "Spectating is no longer allowd!");
				}
			} else if (args[1].equalsIgnoreCase("true")
					|| args[1].equalsIgnoreCase("yes")
					|| args[1].equalsIgnoreCase("1")) {
				plugin.allowSpectators = true;
				game.broadcastPlayers(ChatColor.GREEN + "Spectating is now allowed!");
			} else if (args[1].equalsIgnoreCase("false")
					|| args[1].equalsIgnoreCase("no")
					|| args[1].equalsIgnoreCase("0")) {
				plugin.allowSpectators = false;
				game.broadcastPlayers(ChatColor.GREEN + "Spectating is no longer allowd!");
			}
			
		
		} else if (args[0].equalsIgnoreCase("offlinetimeout")
				|| args[0].equalsIgnoreCase("timeout")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You must specify a number of minutes!");
				return true;
			} else try {
				if (args[1].equalsIgnoreCase("none")
						|| args[1].equalsIgnoreCase("off")) {
					args[1] = "-1";
				}
				plugin.offlineTimeout = Integer.parseInt(args[1]);
				if (plugin.offlineTimeout < 0) {
					plugin.offlineTimeout = -1;
				}
				if (plugin.offlineTimeout > 1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to " + plugin.offlineTimeout + " minutes! If you disconnect, you will only have");
					game.broadcastPlayers(ChatColor.GREEN + "" + plugin.offlineTimeout + " minutes to reconnect before you are booted from the game!");
				} else if (plugin.offlineTimeout == 1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to 1 minute! If you disconnect, you will only have");
					game.broadcastPlayers(ChatColor.GREEN + "1 minute to reconnect before you are booted from the game!");
				} else if (plugin.offlineTimeout == 0) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to 0 minutes! If you disconnect, you will be immediately");
					game.broadcastPlayers(ChatColor.GREEN + "booted from the game!");
				} else if (plugin.offlineTimeout == -1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout has been removed! If you disconnect, you will never be");
					game.broadcastPlayers(ChatColor.GREEN + "booted from the game!");
				} 
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Invalid number of minutes! You must use an integer!");
				return true;
			}
			
		} else if (args[0].equalsIgnoreCase("offlinetimeout")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			if (args.length == 1) {
				sender.sendMessage(ChatColor.RED + "You must specify a number of minutes!");
				return true;
			} else try {
				if (args[1].equalsIgnoreCase("none")
						|| args[1].equalsIgnoreCase("off")) {
					args[1] = "-1";
				}
				plugin.offlineTimeout = Integer.parseInt(args[1]);
				if (plugin.offlineTimeout < 0) {
					plugin.offlineTimeout = -1;
				}
				if (plugin.offlineTimeout > 1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to " + plugin.offlineTimeout + " minutes! If you disconnect, you will only have");
					game.broadcastPlayers(ChatColor.GREEN + "" + plugin.offlineTimeout + " minutes to reconnect before you are booted from the game!");
				} else if (plugin.offlineTimeout == 1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to 1 minute! If you disconnect, you will only have");
					game.broadcastPlayers(ChatColor.GREEN + "1 minute to reconnect before you are booted from the game!");
				} else if (plugin.offlineTimeout == 0) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout set to 0 minutes! If you disconnect, you will be immediately");
					game.broadcastPlayers(ChatColor.GREEN + "booted from the game!");
				} else if (plugin.offlineTimeout == -1) {
					game.broadcastPlayers(ChatColor.GREEN + "Timeout has been removed! If you disconnect, you will never be");
					game.broadcastPlayers(ChatColor.GREEN + "booted from the game!");
				} 
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Invalid number of minutes! You must use an integer!");
				return true;
			}
			
		} else if (args[0].equalsIgnoreCase("setspawn")) {
			if (!sender.isOp()) {
				sender.sendMessage(ChatColor.RED + "You need to be an operator to do that!");
				return true;
			}
			if (game.gameStarted()) {
				sender.sendMessage(ChatColor.RED + "You can't do that while the game is in progress!");
				return true;
			}
			Player player = (Player) sender;
			if (((Player) sender).getWorld() == plugin.getWorld()) {
				plugin.getWorld().setSpawnLocation(player.getLocation().getBlockX(),
													player.getLocation().getBlockY(),
													player.getLocation().getBlockZ());
				game.broadcastPlayers(ChatColor.GREEN + "New spawn location has been set!");
			}
		}
		return true;*/
	}
}
