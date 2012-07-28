package com.bendude56.hunted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ManhuntUtil {
	
	public static Location safeTeleport(Location loc) {
		Location location1 = loc.clone();
		location1.setY(location1.getY() - 1);
		Location location2 = loc.clone();
		location2.setY(location2.getY() - 1);

		while (!isTransparent(location1.getBlock())
				&& !isTransparent(location2.getBlock())) {
			location1.setY(location1.getY() + 1);
			location2.setY(location2.getY() + 1);
		}
		return location1;
	}
	
	public static void stepPlayer(Player player, Double distance, Location target) {
		double x1 = player.getLocation().getX();
		double z1 = player.getLocation().getZ();
		double x2 = target.getX();
		double z2 = target.getZ();
		Location destination = player.getLocation();

		destination
				.setX(x1 + distance * (x2 - x1) / getDistance(x1, 0, z1, x2, 0, z2, false));
		destination
				.setZ(z1 + distance * (z2 - z1) / getDistance(z1, 0, z1, z2, 0, z2, false));

		player.teleport(safeTeleport(destination));
	}
	
	public static Location randomLocation(Location origin, double radius)
	{
		Location randomLocation = origin.clone();
		int sign = (int) Math.floor(Math.random() * 2);
		if (sign == 0)
			sign = -1;
		else
			sign = 1;
		randomLocation.setX(randomLocation.getX() + (sign)
				* (Math.random() * radius)
				* (Math.cos(Math.toRadians(Math.random() * 180))));
		sign = (int) Math.floor(Math.random() * 2);
		if (sign == 0)
			sign = -1;
		else
			sign = 1;
		randomLocation.setZ(randomLocation.getZ() + (sign)
				* (Math.random() * radius)
				* (Math.cos(Math.toRadians(Math.random() * 180))));
		return randomLocation;
	}
	
	public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2, boolean ignoreY)
	{
		return Math.sqrt(Math.pow((x2 - x1), 2) + (ignoreY ? Math.pow((y2 - y1), 2) : 0) + Math.pow((z2 - z1), 2));
	}

	public static double getDistance(Location loc1, Location loc2, boolean ignoreY)
	{
		return getDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getZ(), loc2.getZ(), ignoreY);
	}

	public static double getDistance(Player p1, Player p2, boolean ignoreY)
	{
		return getDistance(p1.getLocation(), p2.getLocation(), ignoreY);
	}

	public static double getDirection(Location loc1, Location loc2)
	{
		double direction;
		direction = loc2.getZ() - loc1.getZ();
		direction = direction / ManhuntUtil.getDistance(loc1, loc2, true);
		direction = Math.acos(direction);
		direction = Math.toDegrees(direction);

		if (loc2.getX() < loc1.getX())
		{
			direction = 180 - direction + 180;
		}

		return direction;
	}

	public static double getDirectionFacing(Player p)
	{
		double direction = p.getLocation().getYaw();
		
		if (direction < 0)
		{
			direction += 360;
		}
		
		direction = 360 - direction;
		
		return direction;
	}

	public static double getDirectionDifference(double dir1, double dir2)
	{
		return dir2 - dir1;
	}

	public static boolean areEqual(Location loc1, Location loc2, double tolerance, boolean ignoreY) {
		if (loc1.getWorld() == loc2.getWorld()
				&& loc2.getX() >= loc1.getX() - tolerance
				&& loc2.getX() <= loc1.getX() + tolerance
				&& (ignoreY || loc2.getY() >= loc1.getY() - tolerance)
				&& (ignoreY || loc2.getY() <= loc1.getY() + tolerance)
				&& loc2.getZ() >= loc1.getZ() - tolerance
				&& loc2.getZ() <= loc1.getZ() + tolerance) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isTransparent(Block block) {
		List<Material> types = new ArrayList<Material>();
				types.add(Material.AIR);
				types.add(Material.BREWING_STAND);
				types.add(Material.BROWN_MUSHROOM);
				types.add(Material.CAKE);
				types.add(Material.CROPS);
				types.add(Material.DETECTOR_RAIL);
				types.add(Material.DIODE_BLOCK_ON);
				types.add(Material.DIODE_BLOCK_OFF);
				types.add(Material.DRAGON_EGG);
				types.add(Material.LEVER);
				types.add(Material.LONG_GRASS);
				types.add(Material.MELON_STEM);
				types.add(Material.NETHER_STALK);
				types.add(Material.PAINTING);
				types.add(Material.PORTAL);
				types.add(Material.POWERED_RAIL);
				types.add(Material.PUMPKIN_STEM);
				types.add(Material.RAILS);
				types.add(Material.RED_MUSHROOM);
				types.add(Material.RED_ROSE);
				types.add(Material.REDSTONE_TORCH_ON);
				types.add(Material.REDSTONE_TORCH_OFF);
				types.add(Material.REDSTONE_WIRE);
				types.add(Material.SAPLING);
				types.add(Material.SIGN_POST);
				types.add(Material.SNOW);
				types.add(Material.SUGAR_CANE_BLOCK);
				types.add(Material.TORCH);
				types.add(Material.VINE);
				types.add(Material.WALL_SIGN);
				types.add(Material.YELLOW_FLOWER);
		return (types.contains(block.getType()));
	}
	
	public static void clearInventory(Inventory inv)
	{
		inv.clear();
		inv.clear(36);
		inv.clear(37);
		inv.clear(38);
		inv.clear(39);
	}
	
	public static HashMap<Integer, ItemStack> defaultHunterLoadout(HashMap<Integer, ItemStack> inv)
	{
		inv.clear();
		inv.put(0, new ItemStack(Material.STONE_SWORD, 1));
		inv.put(1, new ItemStack(Material.BOW, 1));
		// inv.setItem(2, new ItemStack(Material.STONE_PICKAXE, 1));
		// inv.setItem(3, new ItemStack(Material.STONE_SPADE, 1));
		// inv.setItem(4, new ItemStack(Material.STONE_AXE, 1));
		inv.put(2, new ItemStack(Material.TORCH, 3));
		inv.put(3, new ItemStack(Material.COOKED_CHICKEN, 3));
		inv.put(4, new ItemStack(Material.ARROW, 64));
		inv.put(36, new ItemStack(Material.LEATHER_BOOTS, 1));
		inv.put(37, new ItemStack(Material.LEATHER_LEGGINGS, 1));
		inv.put(38, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		inv.put(39, new ItemStack(Material.LEATHER_HELMET, 1));
		
		return inv;
	}
	
	public static HashMap<Integer, ItemStack> defaultPreyLoadout(HashMap<Integer, ItemStack> inv)
	{
		inv.clear();
		inv.put(0, new ItemStack(Material.STONE_SWORD, 1));
		inv.put(1, new ItemStack(Material.BOW, 1));
		// inv.setItem(2, new ItemStack(Material.STONE_PICKAXE, 1));
		// inv.setItem(3, new ItemStack(Material.STONE_SPADE, 1));
		// inv.setItem(4, new ItemStack(Material.STONE_AXE, 1));
		inv.put(2, new ItemStack(Material.TORCH, 3));
		inv.put(3, new ItemStack(Material.COOKED_CHICKEN, 1));
		inv.put(4, new ItemStack(Material.ARROW, 64));
		// inv.setItem(36, new ItemStack(Material.LEATHER_BOOTS, 1));
		// inv.setItem(37, new ItemStack(Material.LEATHER_LEGGINGS, 1));
		// inv.setItem(38, new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		// inv.setItem(39, new ItemStack(Material.LEATHER_HELMET, 1));
		
		return inv;
	}
	
}
