package com.bendude56.hunted.games;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.loadouts.LoadoutUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * Contains various static methods for messages and other game-related things.
 * @author Deaboy
 *
 */
public class GameUtil {
	/**
	 * Broadcasts a string to the given Team(s)
	 * @param message The message to broadcast
	 * @param team The Team(s) to broadcast to
	 */
	public static void broadcast(String message, Team...team)
	{
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		
		for (Team t : team)
		{
			for (Player p : plugin.getTeams().getTeamPlayers(t))
			{
				p.sendMessage(message);
			}
		}
		
		if (!(team.length > 1 && team[0] == Team.NONE))
		{
			plugin.getLogger().log(Level.INFO, ChatColor.stripColor(message));
		}
	}

	/**
	 * Makes a player invisible to all other players on the server
	 * @param p
	 */
	public static void makeInvisible(Player p)
	{
		for (Player p2 : Bukkit.getOnlinePlayers())
		{
			p2.hidePlayer(p);
		}
	}

	/**
	 * Makes a player visible to all other players on the server
	 * @param p
	 */
	public static void makeVisible(Player p)
	{
		for (Player p2: Bukkit.getOnlinePlayers())
		{
			p2.showPlayer(p);
		}
	}

	public static void prepareForGame(Player p)
	{
		ManhuntPlugin plugin = ManhuntPlugin.getInstance();
		Team team = plugin.getTeams().getTeamOf(p);
		
		plugin.getTeams().saveOriginalPlayerState(p);
		
		if (team != Team.HUNTERS && team != Team.PREY)
		{
			if (team == Team.SPECTATORS)
			{
				p.setGameMode(GameMode.CREATIVE);
				p.setAllowFlight(plugin.getSettings().FLYING_SPECTATORS.value);
			}
			return;
		}
		else
		{
			p.setGameMode(GameMode.SURVIVAL);
		}
		
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setSaturation(10);
		p.setFireTicks(0);
		
		if (plugin.getSettings().LOADOUTS.value)
		{
			if (team == Team.HUNTERS)
			{
				LoadoutUtil.setPlayerInventory(p, plugin.getLoadouts().getHunterLoadout());
			}
			else if (team == Team.PREY)
			{
				LoadoutUtil.setPlayerInventory(p, plugin.getLoadouts().getPreyLoadout());
			}
		}
		else
		{
			LoadoutUtil.clearInventory(p.getInventory());
		}
		if (plugin.getSettings().TEAM_HATS.value)
		{
			if (team == Team.HUNTERS)
			{
				p.getInventory().setHelmet(new ItemStack(Material.WOOL, 0, (short) 14)); 
			}
			else if (team == Team.PREY)
			{
				p.getInventory().setHelmet(new ItemStack(Material.LEAVES, 0));
			}
		}
	}

}
