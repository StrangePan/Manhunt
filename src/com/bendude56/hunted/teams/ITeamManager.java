package com.bendude56.hunted.teams;

import java.util.List;

import org.bukkit.entity.Player;

import com.bendude56.hunted.teams.TeamManager.Team;


public interface ITeamManager {

	public boolean addPlayer(Player p, Team t);

	public boolean addPlayer(Player p);

	public boolean removePlayer(String s);

	public boolean setTeam(Player p, Team t);

	public boolean setTeam(String s, Team t);

	public Team getTeamOf(Player p);

	public Team getTeamOf(String s);

	public List<Player> getTeam(Team team);

}
