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
		Game g = Game.getActiveGame();
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if (Game.isGameStarted()) {
				if (g.isSpectating(p) || g.isHunter(p) || g.isHunted(p)) {
					p.sendMessage(ChatColor.YELLOW + "/manhunt leave");
					p.sendMessage(ChatColor.YELLOW + "    Leaves the current manhunt game");
				} else {
					if (!g.hasGameBegun()) {
						p.sendMessage(ChatColor.YELLOW + "/manhunt join {hunter|hunted}");
						p.sendMessage(ChatColor.YELLOW + "    Joins an existing manhunt game");
					}
					p.sendMessage(ChatColor.YELLOW + "/manhunt spectate");
					p.sendMessage(ChatColor.YELLOW + "    Spectates an existing manhunt game");
				}
			} else if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "No manhunt game is currently available for joining...");
			}
			if (p.isOp()) {
				if (Game.isGameStarted()) {
					p.sendMessage(ChatColor.YELLOW + "/manhunt kick <player>");
					p.sendMessage(ChatColor.YELLOW + "    Kicks a player from a manhunt game");
				} else {
					p.sendMessage(ChatColor.YELLOW + "/manhunt create <datafile>");
					p.sendMessage(ChatColor.YELLOW + "    Creates a new manhunt game");
				}
				p.sendMessage(ChatColor.YELLOW + "/manhunt world");
				p.sendMessage(ChatColor.YELLOW + "    Teleports you to the manhunt world");
			}
		} else if (args[0].equalsIgnoreCase("join")) {
			if (!Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "No manhunt game is currently available for joining...");
				return true;
			} else if (g.hasGameBegun()) {
				p.sendMessage(ChatColor.RED + "You cannot join a game already in progress!");
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED + "Not enough arguments!");
			} else if (args.length > 2) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			} else {
				if (args[1].equalsIgnoreCase("hunter")) {
					g.addHunter(p);
				} else if (args[1].equalsIgnoreCase("hunted")) {
					g.addHunted(p);
				} else {
					p.sendMessage(ChatColor.RED + "Invalid argument: " + args[1]);
				}
			}
		} else if (args[0].equalsIgnoreCase("leave")) {
			if (!Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "No manhunt game is currently available for joining...");
				return true;
			} else if (!g.isHunted(p) && !g.isHunter(p) && !g.isSpectating(p)) {
				p.sendMessage(ChatColor.RED + "How can you leave a game you aren't a part of?");
				return true;
			}
			if (args.length > 1) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			} else {
				g.quit(p);
			}
		} else if (args[0].equalsIgnoreCase("spectate")) {
			if (!Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "No manhunt game is currently available for joining...");
				return true;
			} else if (!g.isHunted(p) && !g.isHunter(p) && !g.isSpectating(p)) {
				p.sendMessage(ChatColor.RED + "How can you spectate a game you're already a part of?");
				return true;
			}
			if (args.length > 1) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			} else {
				g.addSpectator(p);
			}
		} else if (args[0].equalsIgnoreCase("world")) {
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "You're not allowed to do that!");
				return true;
			} else if (Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "You can't do that while a game is in progress!");
				return true;
			}
			if (args.length > 1) {
				p.sendMessage(ChatColor.RED + "Too many arguments!");
			} else {
				p.teleport(HuntedPlugin.getInstance().manhuntWorld.getSpawnLocation());
			}
		} else if (args[0].equalsIgnoreCase("create")) {
			if (!p.isOp()) {
				p.sendMessage(ChatColor.RED + "You're not allowed to do that!");
				return true;
			} else if (Game.isGameStarted()) {
				p.sendMessage(ChatColor.RED + "You can't do that while a game is in progress!");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.RED + "Not enough arguments!");
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
					p.sendMessage(ChatColor.GREEN + "A game was successfully created with that data file...");
				}
			}
		} else {
			Bukkit.dispatchCommand(p, "manhunt help");
		}
		return true;
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
