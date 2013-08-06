package com.deaboy.manhunt.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.deaboy.manhunt.Manhunt;
import com.deaboy.manhunt.ManhuntUtil;
import com.deaboy.manhunt.chat.ChatManager;
import com.deaboy.manhunt.lobby.GameLobby;
import com.deaboy.manhunt.lobby.Team;
import com.deaboy.manhunt.map.SpawnType;
import com.deaboy.manhunt.settings.ManhuntGameSettings;
import com.deaboy.manhunt.timeline.*;

public class ManhuntGame extends Game
{
	//////////////// PROPERTIES ////////////////
	private Timeline timeline;
	private ManhuntGameSettings settings;
	private GameEventListener listener;
	
	
	//////////////// CONSTRUCTORS ////////////////
	public ManhuntGame(GameLobby lobby)
	{
		super(lobby);
		this.settings = new ManhuntGameSettings();
		this.listener = new ManhuntGameListener(this);
	}
	
	
	//////////////// PUBLIC METHODS ////////////////
	//---------------- Game Methods ----------------//
	@Override
	public void startGame()
	{
		this.setMap(getLobby().getCurrentMap());
		setStage(GameStage.PREGAME);
		// Initiate timeline
		
		timeline = generateTimeline();
		

		if (getWorld().getEnvironment() == Environment.NORMAL)
			ManhuntUtil.transitionWorldTime(getWorld(), 12000 - getSettings().TIME_SETUP.getValue() * 1200 - 320, new Runnable(){ public void run(){ timeline.run(); }});
		else
			timeline.run();
		
		listener.startListening();
	}
	@Override
	public void endGame()
	{
		if (!isRunning())
			return;
		
		int hunters = getLobby().getPlayerNames(Team.HUNTERS).size();
		int prey = getLobby().getPlayerNames(Team.PREY).size();
		
		getLobby().broadcast(ChatManager.bracket1_ + "The Game is OVER!" + ChatManager.bracket2_);
		// Neither team won.
		if (hunters == 0 && prey == 0)
		{
			getLobby().broadcast(ChatManager.leftborder + "Both teams died!");
			getLobby().broadcast(ChatManager.leftborder + ChatColor.RED + "You all FAIL! Booooo!");
		}
		// The hunters won
		else if (prey == 0)
		{
			getLobby().broadcast(ChatManager.leftborder + "The " + Team.PREY.getColor() + Team.PREY.getName(true) + ChatManager.color + " have all died!");
			getLobby().broadcast(ChatManager.leftborder + Team.HUNTERS.getColor() + "The " + Team.HUNTERS.getName(true) + " have won the game!");
		}
		// The prey won
		else if (hunters == 0)
		{
			getLobby().broadcast(ChatManager.leftborder + "The " + Team.HUNTERS.getColor() + Team.HUNTERS.getName(true) + ChatManager.color + " are all dead!");
			getLobby().broadcast(ChatManager.leftborder + Team.PREY.getColor() + "The " + Team.PREY.getName(true) + " have won the game!");
		}
		// The prey won from time limit
		else
		{
			getLobby().broadcast(ChatManager.leftborder + "The time limit has been reached!");
			getLobby().broadcast(ChatManager.leftborder + Team.PREY.getColor() + "The " + Team.PREY.getName(true) + " have won the game!");
		}
		
		stopGame();
	}
	@Override
	public void cancelGame()
	{
		if (!isRunning())
			return;
		
		stopGame();
	}
	private void stopGame()
	{
		ManhuntUtil.cancelWorldTimeTransition(getWorld());
		timeline.stop();
		listener.stopListening();
		setStage(GameStage.INTERMISSION);
	}
	public void testGame()
	{
 		if (getLobby().getPlayerNames(Team.HUNTERS).isEmpty() || getLobby().getPlayerNames(Team.PREY).isEmpty())
			getLobby().endGame();
	}
	
	
	//---------------- Players ----------------//
	@Override
	public boolean playerJoinLobby(Player player)
	{
		return true;
	}
	@Override
	public boolean playerLeaveLobby(String name)
	{
		Manhunt.cancelFinderFor(name);
		this.testGame();
		return true;
	}
	@Override
	public boolean playerLeaveServer(Player player)
	{
		Manhunt.cancelFinderFor(player);
		return true;
	}
	@Override
	public boolean playerForfeit(String name)
	{
		Manhunt.cancelFinderFor(name);
		this.testGame();
		return true;
	}
	public boolean playerChangeTeam(String name, Team team)
	{
		if (!getLobby().containsPlayer(name) || team == null || isRunning() && getLobby().getPlayerTeam(name) != team && (team == Team.HUNTERS || team == Team.PREY))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	//----------------  ----------------//
	public void distributeTeams()
	{
		List<String> hunters = new ArrayList<String>();
		List<String> prey = new ArrayList<String>();
		List<String> standby = getLobby().getPlayerNames(Team.STANDBY);
		double ratio = getSettings().TEAM_RATIO.getValue();

		String name;

		while (standby.size() > 0)
		{
			name = standby.get(new Random().nextInt(standby.size()));

			if (prey.size() == 0 || (double) hunters.size() / (double) prey.size() > ratio)
			{
				prey.add(name);
			}
			else
			{
				hunters.add(name);
			}

			standby.remove(name);
		}

		for (String p : prey)
		{
			getLobby().playerChangeTeam(p, Team.PREY);
		}
		for (String p : hunters)
		{
			getLobby().playerChangeTeam(p, Team.HUNTERS);
		}
	}
	@Override
	public GameLobby getLobby()
	{
		return (GameLobby) super.getLobby();
	}
	public ManhuntGameSettings getSettings()
	{
		return this.settings;
	}
	public void close()
	{
		listener.close();
		super.close();
	}
	
	
	//////////////// PRIVATE METHODS ////////////////
	private Timeline generateTimeline()
	{
		// Create the new timeline and placeholder event
		ManhuntTimeline timeline = new ManhuntTimeline(getWorld());
		Event event;
		long time = 0L;
		long lobby_id = getLobby().getId();
		
		
		//////////////// LENGTH OF PREGAME ////////////////
		//
		//
		//
		time = 320L; // 16 seconds until start time
		//
		//
		//
		///////////////////////////////////////////////////
		
		event = new ManhuntWorldEvent(getWorld(), time - 320);
		event.addAction(new BroadcastAction(lobby_id, "Prepare for Teleport!", Team.PREY, Team.HUNTERS));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time - 260);
		event.addAction(new RunnableAction(new Runnable()	// Forfeit offline players
		{
			public void run()
			{
				for (String playername : getLobby().getOfflinePlayerNames(Team.HUNTERS, Team.PREY))
				{
					getLobby().playerForfeit(playername);
				}
			}
		}));
		event.addAction(new TeleportTeamAction(lobby_id, Team.PREY, getMap().getPoints(SpawnType.PREY)));
		event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, getMap().getPoints(getSettings().TIME_SETUP.getValue() > 0 ? SpawnType.SETUP : SpawnType.HUNTER)));
		event.addAction(new RunnableAction(new Runnable()	// Give random loadouts to hunters
		{
			public void run()
			{
				if (getLobby().getHunterLoadouts().size() > 0)
				{
					for (Player player : getLobby().getOnlinePlayers(Team.HUNTERS))
					{
						getLobby().getRandomHunterLoadout().applyToPlayer(player);
					}
				}
			}
		}));
		event.addAction(new RunnableAction(new Runnable()	// Give random loadouts to prey
		{
			public void run()
			{
				if (getLobby().getPreyLoadouts().size() > 0)
				{
					for (Player player : getLobby().getOnlinePlayers(Team.PREY))
					{
						getLobby().getRandomPreyLoadout().applyToPlayer(player);
					}
				}
			}
		}));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time);
		event.addAction(new RunnableAction(new Runnable(){ public void run(){ for (Player player : getLobby().getOnlinePlayers(Team.HUNTERS, Team.PREY)) player.setGameMode(GameMode.SURVIVAL); }}));
		timeline.registerEvent(event);
		
		if (getSettings().TIME_SETUP.getValue() > 0)
		{
			event = new ManhuntWorldEvent(getWorld(), time - 200);
			event.addAction(new BroadcastAction(lobby_id, "Setup begins in 10 seconds."));
			timeline.registerEvent(event);
			
			event = new ManhuntWorldEvent(getWorld(), time - 100);
			event.addAction(new BroadcastAction(lobby_id, "5..."));
			timeline.registerEvent(event);
			
			event = new ManhuntWorldEvent(getWorld(), time - 80);
			event.addAction(new BroadcastAction(lobby_id, "4..."));
			timeline.registerEvent(event);
	
			event = new ManhuntWorldEvent(getWorld(), time - 60);
			event.addAction(new BroadcastAction(lobby_id, "3..."));
			timeline.registerEvent(event);
	
			event = new ManhuntWorldEvent(getWorld(), time - 40);
			event.addAction(new BroadcastAction(lobby_id, "2..."));
			timeline.registerEvent(event);
	
			event = new ManhuntWorldEvent(getWorld(), time - 20);
			event.addAction(new BroadcastAction(lobby_id, "1..."));
			timeline.registerEvent(event);
			
			event = new ManhuntWorldEvent(getWorld(), time);
			event.addAction(new RunnableAction(new Runnable(){ public void run(){ setStage(GameStage.SETUP); }}));
			event.addAction(new BroadcastAction(lobby_id, "GO! Use this time to prepare for the hunt!", Team.PREY));
			event.addAction(new BroadcastAction(lobby_id, "The prey are preparing for the hunt.", Team.HUNTERS));
			event.addAction(new BroadcastAction(lobby_id, (getWorld().getEnvironment() == Environment.NORMAL ? "The hunt will begin at sundown. (" + getSettings().TIME_SETUP.getValue() + " minutes)" : "The hunt will begin in " + getSettings().TIME_SETUP.getValue() + " minutes.")));
			timeline.registerEvent(event);
			
			//////////////// LENGTH OF SETUP ////////////////
			//
			//
			//
			time += getSettings().TIME_SETUP.getValue() * 1200;
			//
			//
			//
			/////////////////////////////////////////////////
			
			if (getSettings().TIME_SETUP.getValue() > 1)
			{
				event = new ManhuntWorldEvent(getWorld(), time - 1200);
				event.addAction(new BroadcastAction(lobby_id, "The hunt will begin in 1 minute!"));
				timeline.registerEvent(event);
			}
			
			event = new ManhuntWorldEvent(getWorld(), time - 320);
			event.addAction(new BroadcastAction(lobby_id, "Prepare for teleport!", Team.HUNTERS));
			timeline.registerEvent(event);
			
			event = new ManhuntWorldEvent(getWorld(), time - 260);
			event.addAction(new RunnableAction(new Runnable()	// Forfeit offline players
			{
				public void run()
				{
					for (String playername : getLobby().getOfflinePlayerNames(Team.HUNTERS))
					{
						getLobby().playerForfeit(playername);
					}
				}
			}));
			event.addAction(new TeleportTeamAction(lobby_id, Team.HUNTERS, getMap().getPoints(SpawnType.HUNTER)));
			timeline.registerEvent(event);
			
		}
		
		event = new ManhuntWorldEvent(getWorld(), time - 200);
		event.addAction(new BroadcastAction(lobby_id, "The hunt begins in 10 seconds."));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time - 100);
		event.addAction(new BroadcastAction(lobby_id, "5..."));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time - 80);
		event.addAction(new BroadcastAction(lobby_id, "4..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 60);
		event.addAction(new BroadcastAction(lobby_id, "3..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 40);
		event.addAction(new BroadcastAction(lobby_id, "2..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 20);
		event.addAction(new BroadcastAction(lobby_id, "1..."));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time);
		event.addAction(new RunnableAction(new Runnable(){ public void run(){ setStage(GameStage.HUNT); }}));
		event.addAction(new RunnableAction(new Runnable(){ public void run(){ for (Player player : getLobby().getOnlinePlayers(Team.HUNTERS, Team.PREY)) player.setExp(1f); }}));
		event.addAction(new BroadcastAction(lobby_id, "The hunt has begun! The game will end in " + getLobby().getSettings().TIME_LIMIT.getValue() + " minutes."));
		event.addAction(new BroadcastAction(lobby_id, "Beware! The hunters have been released!", Team.PREY));
		event.addAction(new BroadcastAction(lobby_id, "Now's your chance! Hunt the prey!", Team.HUNTERS));
		timeline.registerEvent(event);
		
		//////////////// LENGTH OF THE HUNT ////////////////
		//
		//
		//
		time += getLobby().getSettings().TIME_LIMIT.getValue() * 1200;
		//
		//
		//
		////////////////////////////////////////////////////
		
		if (getLobby().getSettings().TIME_LIMIT.getValue() > 20)
		{
			event = new ManhuntWorldEvent(getWorld(),  time - 20 * 1200);
			event.addAction(new BroadcastAction(lobby_id, "Only one day left in the hunt!"));
			timeline.registerEvent(event);
		}
		
		if (getLobby().getSettings().TIME_LIMIT.getValue() > 5)
		{
			event = new ManhuntWorldEvent(getWorld(), time - 5 * 1200);
			event.addAction(new BroadcastAction(lobby_id, "5 minutes left in the hunt!"));
			timeline.registerEvent(event);
		}
		
		if (getLobby().getSettings().TIME_LIMIT.getValue() > 1)
		{
			event = new ManhuntWorldEvent(getWorld(), time - 1200);
			event.addAction(new BroadcastAction(lobby_id, "1 minute left in the hunt!"));
			timeline.registerEvent(event);
		}
		
		event = new ManhuntWorldEvent(getWorld(), time - 600);
		event.addAction(new BroadcastAction(lobby_id, "30 seconds left in the hunt!"));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time - 300);
		event.addAction(new BroadcastAction(lobby_id, "15 remaining!"));
		
		event = new ManhuntWorldEvent(getWorld(), time - 200);
		event.addAction(new BroadcastAction(lobby_id, "10..."));
		timeline.registerEvent(event);
		
		event = new ManhuntWorldEvent(getWorld(), time - 180);
		event.addAction(new BroadcastAction(lobby_id, "9..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 160);
		event.addAction(new BroadcastAction(lobby_id, "8..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 140);
		event.addAction(new BroadcastAction(lobby_id, "7..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 120);
		event.addAction(new BroadcastAction(lobby_id, "6..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 100);
		event.addAction(new BroadcastAction(lobby_id, "5..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 80);
		event.addAction(new BroadcastAction(lobby_id, "4..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 60);
		event.addAction(new BroadcastAction(lobby_id, "3..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 40);
		event.addAction(new BroadcastAction(lobby_id, "2..."));
		timeline.registerEvent(event);

		event = new ManhuntWorldEvent(getWorld(), time - 20);
		event.addAction(new BroadcastAction(lobby_id, "1..."));
		timeline.registerEvent(event);
		
		
		//////////////// END GAME ////////////////
		event = new ManhuntWorldEvent(getWorld(), time);
		event.addAction(new RunnableAction(new Runnable()
		{
			public void run()
			{
				endGame();
			}
		}));
		timeline.registerEvent(event);
		
		
		return timeline;
	}
	
	
	
}



