package com.bendude56.hunted;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ManhuntUtil {
	
	public static Location safeTeleport(Location loc)
	{
		Location location1 = loc.clone();
		location1.setY(location1.getY() - 1);
		Location location2 = loc.clone();
		location2.setY(location2.getY() - 1);

		while (location1.getBlock().getType().isSolid()
				&& !location2.getBlock().getType().isSolid())
		{
			location1.setY(location1.getY() + 1);
			location2.setY(location2.getY() + 1);
		}
		return location1;
	}
	
	public static void stepPlayer(Player player, Double distance, Location target)
	{
		double x1 = player.getLocation().getX();
		double z1 = player.getLocation().getZ();
		double x2 = target.getX();
		double z2 = target.getZ();
		Location destination = player.getLocation();

		destination.setX(x1 + distance * (x2 - x1) / getDistance(x1, 0, z1, x2, 0, z2, false));
		destination.setZ(z1 + distance * (z2 - z1) / getDistance(z1, 0, z1, z2, 0, z2, false));

		player.teleport(safeTeleport(destination));
	}
	
	public static Location randomLocation(Location origin, double radius)
	{
		Location loc = origin.clone();
		
		double angle = Math.random() * 2 * Math.PI;
		double length = Math.random() * radius;
		
		loc.setX(loc.getX() + Math.cos(angle) * length);
		loc.setY(loc.getY() + Math.sin(angle) * length);
		
		return loc;
	}
	
	public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2, boolean ignoreY)
	{
		return Math.sqrt(Math.pow((x2 - x1), 2) + (ignoreY ? 0 : Math.pow((y2 - y1), 2)) + Math.pow((z2 - z1), 2));
	}

	public static double getDistance(Location loc1, Location loc2, boolean ignoreY)
	{
		return getDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getZ(), loc2.getZ(), ignoreY);
	}
	
	public static boolean isHostile(Entity entity)
	{
		switch (entity.getType())
		{
		case ZOMBIE:
		case SKELETON:
		case SPIDER:
		case SLIME:
		case ENDERMAN:
		case CAVE_SPIDER:
		case SILVERFISH:
		case PIG_ZOMBIE:
		case GHAST:
		case MAGMA_CUBE:
		case BLAZE:
			return true;
		default:
			return false;
		}
	}

	public static boolean isPassive(Entity entity)
	{
		switch (entity.getType())
		{
		case PIG:
		case COW:
		case SHEEP:
		case CHICKEN:
		case SQUID:
		case WOLF:
		case OCELOT:
		case MUSHROOM_COW:
		case VILLAGER:
		case BAT:
			return true;
		default:
			return false;
		}
	}
	
}
