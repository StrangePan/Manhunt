package com.deaboy.manhunt.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.loadouts.Loadout;
import com.deaboy.manhunt.lobby.Lobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.Map;
import com.deaboy.manhunt.map.Spawn;
import com.deaboy.manhunt.map.SpawnType;
import com.deaboy.manhunt.map.Zone;
import com.deaboy.manhunt.map.ZoneFlag;

public class CommandUtil
{
	//////// STANDARD COMMAND ERROR MESSAGES ////////
	public static final String NO_PERMISSION = ChatColor.RED + "You don't have permission to do that.";
	public static final String GAME_RUNNING = ChatColor.RED + "You can't do that while the game is running.";
	public static final String NO_GAME_RUNNING = ChatColor.RED + "There are no Manhunt games running.";
	public static final String IS_SERVER = ChatColor.RED + "The server cannot do that.";
	public static final String WRONG_WORLD = ChatColor.RED + "The player must be in the manhunt world first!";
	public static final String LOCKED = ChatColor.RED + "The teams are locked.";
	public static final String INVALID_USAGE = ChatColor.RED + "Invalid usage.";
	public static final String NO_WORLD_BY_NAME = ChatColor.RED + "There is no Manhunt world by that name!";
	public static final String MAP_NOT_SELECTED = ChatColor.RED + "You must select a map first.\n"
			+ ChatColor.GRAY + " Selecting a map: /mmap -s <mapname>\n"
			+ ChatColor.GRAY + " Seeing registered maps: /mmap -list [all] [-page <page>]";
	public static final String CORNER_PRIMARY_NOT_SELECTED = ChatColor.RED + "Select a corner using left-click with a wooden axe.";
	public static final String CORNER_SECONDARY_NOT_SELECTED = ChatColor.RED + "Select a corner using right-click with a wooden axe.";
	
	
	
	
	
	//////// COMMAND TEMPLATES ////////
	// Arguments
	public static final ArgumentTemplate arg_help	= new ArgumentTemplate("help", ArgumentType.FLAG).addAlias("?").finalize_();
	public static final ArgumentTemplate arg_args	= new ArgumentTemplate("arguments", ArgumentType.TEXT).addAlias("args").finalize_();
	public static final ArgumentTemplate arg_info	= new ArgumentTemplate("info", ArgumentType.TEXT).addAlias("i").finalize_();
	public static final ArgumentTemplate arg_player	= new ArgumentTemplate("player", ArgumentType.TEXT).addAlias("plr").addAlias("p").finalize_();
	public static final ArgumentTemplate arg_issues	= new ArgumentTemplate("issues", ArgumentType.FLAG).addAlias("issue").addAlias("is").addAlias("problem").addAlias("problems").finalize_();
	public static final ArgumentTemplate arg_select	= new ArgumentTemplate("select", ArgumentType.TEXT).addAlias("s").addAlias("sel").finalize_();
	public static final ArgumentTemplate arg_list	= new ArgumentTemplate("list", ArgumentType.TEXT).addAlias("ls").finalize_();
	public static final ArgumentTemplate arg_page	= new ArgumentTemplate("page", ArgumentType.TEXT).addAlias("pg").addAlias("p").finalize_();
	public static final ArgumentTemplate arg_join	= new ArgumentTemplate("join", ArgumentType.TEXT).addAlias("jn").finalize_();
	public static final ArgumentTemplate arg_leave	= new ArgumentTemplate("leave", ArgumentType.FLAG).addAlias("lv").finalize_();
	public static final ArgumentTemplate arg_create	= new ArgumentTemplate("create", ArgumentType.TEXT).addAlias("cr").addAlias("new").finalize_();
	public static final ArgumentTemplate arg_delete	= new ArgumentTemplate("delete", ArgumentType.FLAG).addAlias("del").addAlias("remove").addAlias("rm").finalize_();
	public static final ArgumentTemplate arg_reload	= new ArgumentTemplate("reload", ArgumentType.FLAG).addAlias("load").finalize_();
	public static final ArgumentTemplate arg_name	= new ArgumentTemplate("name", ArgumentType.TEXT).addAlias("n").addAlias("nm").finalize_();
	public static final ArgumentTemplate arg_world	= new ArgumentTemplate("world", ArgumentType.TEXT).addAlias("w").addAlias("worlds").finalize_();
	public static final ArgumentTemplate arg_close	= new ArgumentTemplate("close", ArgumentType.FLAG).finalize_();
	public static final ArgumentTemplate arg_open	= new ArgumentTemplate("open", ArgumentType.FLAG).finalize_();
	public static final ArgumentTemplate arg_lsmaps	= new ArgumentTemplate("listmaps", ArgumentType.TEXT).addAlias("listm").addAlias("lsmaps").addAlias("lsm").addAlias("maps").finalize_();
	public static final ArgumentTemplate arg_lspoints=new ArgumentTemplate("listpoints", ArgumentType.TEXT).addAlias("listp").addAlias("lspoints").addAlias("lsp").addAlias("points").finalize_();
	public static final ArgumentTemplate arg_lszones= new ArgumentTemplate("listzones", ArgumentType.TEXT).addAlias("listz").addAlias("lszones").addAlias("lsz").addAlias("zones").finalize_();
	public static final ArgumentTemplate arg_addmap	= new ArgumentTemplate("addmap", ArgumentType.TEXT).addAlias("addm").addAlias("map+").finalize_();
	public static final ArgumentTemplate arg_remmap	= new ArgumentTemplate("removemap", ArgumentType.TEXT).addAlias("remmap").addAlias("remm").addAlias("map-").finalize_();
	public static final ArgumentTemplate arg_addload= new ArgumentTemplate("addloadout", ArgumentType.TEXT).addAlias("addload").addAlias("addl").addAlias("loadout+").addAlias("load+").finalize_();
	public static final ArgumentTemplate arg_remload= new ArgumentTemplate("removeloadout", ArgumentType.TEXT).addAlias("remloadout").addAlias("removeload").addAlias("remload").addAlias("reml").addAlias("loadout-").addAlias("load-").finalize_();
	public static final ArgumentTemplate arg_lsload	= new ArgumentTemplate("listloadouts", ArgumentType.TEXT).addAlias("listload").addAlias("lsl").addAlias("lsloadouts").addAlias("lsloadout").addAlias("loadouts").addAlias("loads").addAlias("listkits").addAlias("lskits").addAlias("listk").addAlias("lsk").addAlias("kits").finalize_();
	public static final ArgumentTemplate arg_tp		= new ArgumentTemplate("tp", ArgumentType.TEXT).addAlias("teleport").addAlias("spawn").finalize_();
	public static final ArgumentTemplate arg_range	= new ArgumentTemplate("range", ArgumentType.TEXT).addAlias("r").finalize_();
	public static final ArgumentTemplate arg_set	= new ArgumentTemplate("set", ArgumentType.TEXT).addAlias("s").finalize_();
	public static final ArgumentTemplate arg_load	= new ArgumentTemplate("load", ArgumentType.FLAG).addAlias("ld").finalize_();
	public static final ArgumentTemplate arg_save	= new ArgumentTemplate("save", ArgumentType.FLAG).addAlias("sv").finalize_();
	public static final ArgumentTemplate arg_lspotions		= new ArgumentTemplate("listpotions", ArgumentType.TEXT).addAlias("listp").addAlias("lspotions").addAlias("lsp").addAlias("potions").finalize_();
	public static final ArgumentTemplate arg_addpotion		= new ArgumentTemplate("addpotion", ArgumentType.TEXT).addAlias("addpot").addAlias("addp").addAlias("potion+").addAlias("pot+").finalize_();
	public static final ArgumentTemplate arg_rempotion		= new ArgumentTemplate("removepotion", ArgumentType.TEXT).addAlias("rempotion").addAlias("rempot").addAlias("remp").addAlias("potion-").addAlias("pot-").finalize_();
	public static final ArgumentTemplate arg_duration		= new ArgumentTemplate("duration", ArgumentType.TEXT).addAlias("dur").addAlias("d").addAlias("time").addAlias("length").addAlias("l").finalize_();
	public static final ArgumentTemplate arg_redefine		= new ArgumentTemplate("redefine", ArgumentType.FLAG).addAlias("define").addAlias("move").addAlias("mv").finalize_();
	public static final ArgumentTemplate arg_setspawn		= new ArgumentTemplate("setspawn", ArgumentType.FLAG).addAlias("setsp").addAlias("move").addAlias("mv").finalize_();
	public static final ArgumentTemplate arg_zoneflags		= new ArgumentTemplate("flags", ArgumentType.CHECK).addAlias("flag").addAlias("fl");
	public static final ArgumentTemplate arg_lobbytype		= new ArgumentTemplate("type", ArgumentType.TEXT).addAlias("t");
	public static final ArgumentTemplate arg_pointtype		= new ArgumentTemplate("type", ArgumentType.RADIO).addAlias("t");
	public static final ArgumentTemplate arg_team			= new ArgumentTemplate("team", ArgumentType.CHECK).addAlias("t");
	public static final ArgumentTemplate arg_potiontype		= new ArgumentTemplate("type", ArgumentType.TEXT).addAlias("t").finalize_();
	static {	// Fill parameters in certain arguments and finalize.
		for (ZoneFlag flag : ZoneFlag.values())
			arg_zoneflags.addParameter(flag.getName().toLowerCase());
		arg_zoneflags.finalize_();
		
		/*for (LobbyType type : LobbyType.values())
			arg_lobbytype.addParameter(type.getName().toLowerCase());
		arg_lobbytype.finalize_();*/
		
		for (SpawnType type : SpawnType.values())
			arg_pointtype.addParameter(type.getName().toLowerCase());
		arg_pointtype.finalize_();
		
		for (PotionEffectType type : PotionEffectType.values())
			if (type != null)
				arg_potiontype.addParameter(type.getName().toLowerCase());
		arg_potiontype.finalize_();
		
		for (Team team : Team.values())
		{
			arg_team.addParameter(team.getName(false).toLowerCase());
		}
		arg_team.finalize_();
	}
	
	
	public static final SubcommandTemplate scmd_args		= new SubcommandTemplate(arg_args).finalize_();
	public static final SubcommandTemplate scmd_help		= new SubcommandTemplate(arg_help).finalize_();
	public static final SubcommandTemplate scmd_info		= new SubcommandTemplate(arg_info).finalize_();
	public static final SubcommandTemplate scmd_list		= new SubcommandTemplate(arg_list).addArgument(arg_page).finalize_();
	public static final SubcommandTemplate scmd_select		= new SubcommandTemplate(arg_select).finalize_();
	public static final SubcommandTemplate scmd_create		= new SubcommandTemplate(arg_create).addArgument(arg_name).finalize_();
	public static final SubcommandTemplate scmd_delete		= new SubcommandTemplate(arg_delete).finalize_();
	public static final SubcommandTemplate scmd_setspawn	= new SubcommandTemplate(arg_setspawn).finalize_();
	public static final SubcommandTemplate scmd_redefine	= new SubcommandTemplate(arg_redefine).finalize_();
	public static final SubcommandTemplate scmd_range		= new SubcommandTemplate(arg_range).finalize_();
	public static final SubcommandTemplate scmd_tp			= new SubcommandTemplate(arg_tp).addArgument(arg_player).finalize_();
	public static final SubcommandTemplate scmd_open		= new SubcommandTemplate(arg_open).finalize_();
	public static final SubcommandTemplate scmd_close		= new SubcommandTemplate(arg_close).finalize_();
	public static final SubcommandTemplate scmd_issues		= new SubcommandTemplate(arg_issues).finalize_();
	public static final SubcommandTemplate scmd_lspoints	= new SubcommandTemplate(arg_lspoints).addArgument(arg_page).finalize_();
	public static final SubcommandTemplate scmd_lszones		= new SubcommandTemplate(arg_lszones).addArgument(arg_page).finalize_();
	public static final SubcommandTemplate scmd_zoneflags	= new SubcommandTemplate(arg_zoneflags).finalize_();
	public static final SubcommandTemplate scmd_set			= new SubcommandTemplate(arg_set).finalize_();
	public static final SubcommandTemplate scmd_load		= new SubcommandTemplate(arg_load).finalize_();
	public static final SubcommandTemplate scmd_save		= new SubcommandTemplate(arg_save).finalize_();
	
	private static HashMap<String, CommandTemplate> command_templates;
	public static final CommandTemplate cmd_mlobby	= new CommandTemplate("mlobby")
			.addAlias("lobby")
			.addAlias("mhlobby")
			.addSubcommand(scmd_info)
			.addSubcommand(scmd_list)
			.addSubcommand(scmd_select)
			.addSubcommand(new SubcommandTemplate(arg_create).addArgument(arg_name).addArgument(arg_lobbytype).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_join).addArgument(arg_name).addArgument(arg_player).finalize_())
			.addSubcommand(scmd_delete)
			.addSubcommand(new SubcommandTemplate(arg_lsmaps).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_addmap).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_remmap).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_lsload).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_addload).addArgument(arg_team).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_remload).addArgument(arg_team).finalize_())
			.addSubcommand(scmd_setspawn)
			.addSubcommand(scmd_range)
			.addSubcommand(scmd_tp)
			.addSubcommand(scmd_open)
			.addSubcommand(scmd_close);
	public static final CommandTemplate cmd_mmap	= new CommandTemplate("mmap")
			.addAlias("map")
			.addAlias("mhmap")
			.addSubcommand(scmd_info)
			.addSubcommand(scmd_issues)
			.addSubcommand(scmd_select)
			.addSubcommand(new SubcommandTemplate(arg_list).addArgument(arg_world).finalize_())
			.addSubcommand(scmd_lspoints)
			.addSubcommand(scmd_lszones)
			.addSubcommand(scmd_create)
			.addSubcommand(scmd_delete)
			.addSubcommand(scmd_tp);
	public static final CommandTemplate cmd_mzone	= new CommandTemplate("mzone")
			.addAlias("zone")
			.addAlias("mhzone")
			.addSubcommand(scmd_info)
			.addSubcommand(scmd_select)
			.addSubcommand(scmd_list)
			.addSubcommand(scmd_create)
			.addSubcommand(scmd_delete)
			.addSubcommand(scmd_zoneflags)
			.addSubcommand(scmd_redefine);
	public static final CommandTemplate cmd_mpoint	= new CommandTemplate("mpoint")
			.addAlias("point")
			.addAlias("mhpoint")
			.addSubcommand(scmd_info)
			.addSubcommand(scmd_select)
			.addSubcommand(scmd_list)
			.addSubcommand(new SubcommandTemplate(arg_create).addArgument(arg_name).addArgument(arg_pointtype).finalize_())
			.addSubcommand(scmd_delete)
			.addSubcommand(scmd_redefine)
			.addSubcommand(scmd_tp)
			.addSubcommand(scmd_range);
	public static final CommandTemplate cmd_mworld	= new CommandTemplate("mworld")
			.addAlias("world")
			.addAlias("mhworld")
			.addSubcommand(new SubcommandTemplate(arg_info).addArgument(arg_world).finalize_())
			.addSubcommand(scmd_list)
			.addSubcommand(new SubcommandTemplate(arg_issues).addArgument(arg_world).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_lsmaps).addArgument(arg_world).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_tp).addArgument(arg_world).finalize_())
			.addSubcommand(scmd_setspawn);
	public static final CommandTemplate cmd_msettings = new CommandTemplate("msettings")
			.addAlias("settings")
			.addAlias("mhsettings")
			.addAlias("mset")
			.addAlias("set")
			.addAlias("mhset")
			.addSubcommand(scmd_list)
			.addSubcommand(scmd_set);
	public static final CommandTemplate cmd_mloadout= new CommandTemplate("mloadout")
			.addAlias("loadout")
			.addAlias("load")
			.addAlias("mhloadout")
			.addAlias("mloadouts")
			.addAlias("loadouts")
			.addAlias("mhloadouts")
			.addAlias("mload")
			.addAlias("mhload")
			.addSubcommand(scmd_info)
			.addSubcommand(scmd_select)
			.addSubcommand(scmd_list)
			.addSubcommand(scmd_create)
			.addSubcommand(scmd_delete)
			.addSubcommand(scmd_load)
			.addSubcommand(scmd_save)
			.addSubcommand(new SubcommandTemplate(arg_lspotions).addArgument(arg_page).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_addpotion).addArgument(arg_potiontype).addArgument(arg_duration).finalize_())
			.addSubcommand(new SubcommandTemplate(arg_rempotion).addArgument(arg_potiontype).finalize_());
	static {	// Store references to command templates in hashmap and add global arguments
		command_templates = new HashMap<String, CommandTemplate>();
		command_templates.put(cmd_mlobby.getName(), cmd_mlobby);
		command_templates.put(cmd_mmap.getName(), cmd_mmap);
		command_templates.put(cmd_mzone.getName(), cmd_mzone);
		command_templates.put(cmd_mpoint.getName(), cmd_mpoint);
		command_templates.put(cmd_mworld.getName(), cmd_mworld);
		command_templates.put(cmd_msettings.getName(), cmd_msettings);
		command_templates.put(cmd_mloadout.getName(), cmd_mloadout);
		
		for (CommandTemplate cmd : command_templates.values())
			cmd.addSubcommand(scmd_help).addSubcommand(scmd_args).finalize_();
	}
	
	
	//////// PROPERTIES ////////
	private HashMap<String, String> selected_maps;
	private HashMap<String, Long> selected_lobbies;
	private HashMap<String, String> selected_zones;
	private HashMap<String, String> selected_points;
	private HashMap<String, String> selected_loadouts;
	private HashMap<CommandSender, String> vcommands;
	private HashMap<CommandSender, Boolean> verified;
	
	
	//---------------- Constructors ----------------//
	public CommandUtil()
	{
		this.selected_maps = new HashMap<String, String>();
		this.selected_lobbies = new HashMap<String, Long>();
		this.selected_zones = new HashMap<String, String>();
		this.selected_points = new HashMap<String, String>();
		this.selected_loadouts = new HashMap<String, String>();
		this.vcommands = new HashMap<CommandSender, String>();
		this.verified = new HashMap<CommandSender, Boolean>();
	}
	
	
	//---------------- Command Stuff ----------------//
	public static Command parseCommand(org.bukkit.command.Command cmd, String label, String[] arguments)
	{
		Command command;
		
		if (command_templates.containsKey(cmd.getName()))
		{
			command = Command.fromTemplate(command_templates.get(cmd.getName()), label, arguments);
		}
		else
		{
			command = null;
		}
		
		return command;
	}
	public static CommandTemplate matchCommand(String command)
	{
		if (command_templates.containsKey(command))
		{
			return command_templates.get(command);
		}
		else
		{
			return null;
		}
	}
	public static boolean sendHelp(CommandSender sender, Subcommand cmd)
	{
		Bukkit.dispatchCommand(sender, "help " + cmd.getName());
		sender.sendMessage(ChatColor.GRAY + "To view arguments, use /" + cmd.getName() + " -" + CommandUtil.arg_args.getName() + " [page]");
		return true;
	}
	public static boolean sendArguments(CommandSender sender, Subcommand cmd)
	{
		final int perpage = 8;
		int page = 1;
		boolean all = false;
		List<ArgumentTemplate> args;
		
		// Get the page #
		if (cmd.containsArgument(CommandUtil.arg_page) && cmd.getArgument(CommandUtil.arg_page).getParameter() != null)
		{
			try
			{
				page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_page).getParameter());
			}
			catch (NumberFormatException e)
			{
				page = 1;
			}
		}
		else if (cmd.getArgument(CommandUtil.arg_args).getParameter() != null)
		{
			 if (cmd.getArgument(CommandUtil.arg_args).getParameter().equalsIgnoreCase("all"))
			 {
				 all = true;
				 page = 1;
			 }
			 else
			 {
				 try
				 {
					 page = Integer.parseInt(cmd.getArgument(CommandUtil.arg_args).getParameter());
				 }
				 catch (Exception e)
				 {
					 page = 1;
				 }
			 }
		}
		else
		{
			page = 1;
		}
		
		page--;
		
		// Assemble list of settings
		args = cmd.getCommandTemplate().getArguments();
		for (SubcommandTemplate sub : cmd.getCommandTemplate().getSubcommands())
		{
			args.add(sub.getRootArgument());
		}
 
		if (!all)
		{
			if (page * perpage > args.size() - 1 )
				page = (args.size()-1) / perpage;
			
			if (page < 0)
				page = 0;
			
			if (args.size() == 0)
			{
				sender.sendMessage("This command has no arguments.");
				return true;
			}
		}
		
		sender.sendMessage(ChatManager.bracket1_ + ChatColor.RED + "Registered Arguments for " + cmd.getName() + ChatManager.color + "(" + (all ? "All" : (page+1) + "/" + (int) Math.ceil((double) args.size()/perpage)) + ")" + ChatManager.bracket2_);
		if (!all)
		{
			sender.sendMessage(ChatColor.GRAY + "Use /mlobby list [n] to get page n of lobbies");
			args = args.subList(page * perpage, Math.min( (page + 1) * perpage, args.size() ));
		}
		
		for (ArgumentTemplate arg: args)
		{
			String message = ChatManager.color + "  -" + arg.getName() + " ";
			if (!arg.getAliases().isEmpty())
			{
				message += ChatManager.color + "(AKA ";
				for (int i = 0; i < arg.getAliases().size(); i++)
				{
					if (i > 0)
					{
						message += ", ";
					}
					message += arg.getAliases().get(i);
				}
				message += ") ";
			}
			message += ChatColor.GRAY + "(" + arg.getType().toString() + ") ";
			sender.sendMessage(message);
		}
		return true;
	}
	
	
	//---------------- Map Selection ----------------//
	public static void setSelectedMap(CommandSender sender, Map map)
	{
		Manhunt.getCommandUtil().selected_maps.put(sender.getName(), map.getFullName());
	}
	public static Map getSelectedMap(CommandSender sender)
	{
		if (Manhunt.getCommandUtil().selected_maps.containsKey(sender.getName()))
			return Manhunt.getMap(Manhunt.getCommandUtil().selected_maps.get(sender.getName()));
		else
			return null;
	}
	public static Map getSelectedMap(String name)
	{
		if (Manhunt.getCommandUtil().selected_maps.containsKey(name))
			return Manhunt.getMap(Manhunt.getCommandUtil().selected_maps.get(name));
		else
			return null;
	}
	
	
	//---------------- Lobby Selection ----------------//
	public static void setSelectedLobby(CommandSender sender, Lobby lobby)
	{
		Manhunt.getCommandUtil().selected_lobbies.put(sender.getName(), lobby.getId());
	}
	public static Lobby getSelectedLobby(CommandSender sender)
	{
		return getSelectedLobby(sender.getName());
	}
	public static Lobby getSelectedLobby(String name)
	{
		if (Manhunt.getCommandUtil().selected_lobbies.containsKey(name))
			return Manhunt.getLobby(Manhunt.getCommandUtil().selected_lobbies.get(name));
		else
			return null;
	}
	
	
	//---------------- Zone Selection -----------------//
	public static void setSelectedZone(CommandSender sender, Zone zone)
	{
		Manhunt.getCommandUtil().selected_zones.put(sender.getName(), zone.getName());
	}
	public static Zone getSelectedZone(CommandSender sender)
	{
		return getSelectedZone(sender.getName());
	}
	public static Zone getSelectedZone(String name)
	{
		if (Manhunt.getCommandUtil().selected_zones.containsKey(name))
			if (getSelectedMap(name) != null)
				return getSelectedMap(name).getZone(Manhunt.getCommandUtil().selected_zones.get(name));
			else
				return null;
		else
			return null;
	}
	
	
	//---------------- Spawn Selection ----------------//
	public static void setSelectedPoint(CommandSender sender, Spawn point)
	{
		if (sender != null && point != null)
		{
			Manhunt.getCommandUtil().selected_points.put(sender.getName(), point.getName());
		}
	}
	public static Spawn getSelectedPoint(CommandSender sender)
	{
		return getSelectedPoint(sender.getName());
	}
	public static Spawn getSelectedPoint(String name)
	{
		if (Manhunt.getCommandUtil().selected_points.containsKey(name))
			if (getSelectedMap(name) != null)
				return getSelectedMap(name).getPoint(Manhunt.getCommandUtil().selected_points.get(name));
			else
				return null;
		else
			return null;
	}
	
	
	//---------------- Loadout Selection ----------------//
	public static void setSelectedLoadout(CommandSender sender, Loadout loadout)
	{
		if (sender != null && loadout != null)
		{
			Manhunt.getCommandUtil().selected_loadouts.put(sender.getName(), loadout.getName());
		}
	}
	public static Loadout getSelectedLoadout(CommandSender sender)
	{
		if (sender != null)
		{
			return getSelectedLoadout(sender.getName());
		}
		else
		{
			return null;
		}
	}
	public static Loadout getSelectedLoadout(String name)
	{
		if (name != null && Manhunt.getCommandUtil().selected_loadouts.containsKey(name))
		{
			return Manhunt.getLoadout(Manhunt.getCommandUtil().selected_loadouts.get(name));
		}
		else
		{
			return null;
		}
	}
	
	
	//---------------- Verification ----------------//
	public static void addVerifyCommand(CommandSender sender, String command, String message)
	{
		addVerifyCommand(sender, command);
		sender.sendMessage(message);
		sender.sendMessage(ChatColor.GRAY + "To confirm, type \"/mverify\" or \"/cancel\"");
	}
	public static void addVerifyCommand(CommandSender sender, String command)
	{
		Manhunt.getCommandUtil().vcommands.put(sender, command);
		Manhunt.getCommandUtil().verified.put(sender, false);
	}
	public static boolean isVerified(CommandSender sender)
	{
		if (isVerifying(sender))
			return Manhunt.getCommandUtil().verified.get(sender);
		else
			return false;
	}
	public static boolean isVerifying(CommandSender sender)
	{
		return (Manhunt.getCommandUtil().verified.containsKey(sender)
				&& Manhunt.getCommandUtil().vcommands.containsKey(sender));
	}
	public static void executeCommand(CommandSender sender)
	{
		if (isVerifying(sender))
		{
			Manhunt.getCommandUtil().verified.put(sender,true);
			Bukkit.dispatchCommand(sender, Manhunt.getCommandUtil().vcommands.get(sender));
			cancelCommand(sender);
		}
	}
	public static void cancelCommand(CommandSender sender)
	{
		if (Manhunt.getCommandUtil().vcommands.containsKey(sender))
			Manhunt.getCommandUtil().vcommands.remove(sender);
		if (Manhunt.getCommandUtil().verified.containsKey(sender))
			Manhunt.getCommandUtil().verified.remove(sender);
	}
	public void deletePlayer(Player player)
	{
		if (selected_lobbies.containsKey(player.getName()))
			selected_lobbies.remove(player.getName());
		if (selected_maps.containsKey(player.getName()))
			selected_maps.remove(player.getName());
		if (selected_zones.containsKey(player.getName()))
			selected_zones.remove(player.getName());
		if (selected_points.containsKey(player.getName()))
			selected_points.remove(player.getName());
		if (vcommands.containsKey(player.getName()))
			vcommands.remove(player.getName());
		if (verified.containsKey(player.getName()))
			verified.remove(player.getName());
	}
	
	
	
}
