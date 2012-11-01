package com.bendude56.hunted.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.bendude56.hunted.ManhuntPlugin;
import com.bendude56.hunted.game.GameUtil;
import com.bendude56.hunted.game.Game.GameStage;
import com.bendude56.hunted.teams.TeamUtil;
import com.bendude56.hunted.teams.TeamManager.Team;

/**
 * Simple class full of static methods for messaging players and teams.
 * @author Deaboy
 *
 */
public class ChatManager {
	private ManhuntPlugin plugin;

	public final static ChatColor color = ChatColor.GOLD;
	public final static String bracket1 = color + "---[";
	public final static String bracket1_ = bracket1 + " ";
	public final static String bracket2 = color + "]---";
	public final static String bracket2_ = " " + bracket2;
	public final static String leftborder = color + "|  ";
	public final static String divider = color + "----------------------------------------";

	public void onPlayerchat(AsyncPlayerChatEvent e)
	{
		e.setCancelled(true);
		
		if (e.getPlayer().getWorld() != plugin.getWorld())
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.getWorld() != plugin.getWorld())
				{
					p.sendMessage("<" + e.getPlayer().getName() + "> " + e.getMessage());
				}
			}
		}
		else
		{
			Team t = plugin.getTeams().getTeamOf(e.getPlayer());
			String message = TeamUtil.getTeamColor(t) + e.getPlayer().getName() + ": " + ChatColor.WHITE + e.getMessage();
			
			if (!plugin.getSettings().ALL_TALK.value && plugin.gameIsRunning() && plugin.getGame().getStage() != GameStage.PREGAME)
			{
				GameUtil.broadcast(message, t);
			}
			else
			{
				GameUtil.broadcast(message, Team.HUNTERS, Team.PREY, Team.SPECTATORS);
			}
		}
		
	}

	public ChatManager(ManhuntPlugin plugin)
	{
		this.plugin = plugin;
	}
}
