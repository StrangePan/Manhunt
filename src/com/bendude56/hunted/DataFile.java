package com.bendude56.hunted;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataFile {
	// Mob spawn byte
	private static final int MOB_SP_PEACEFUL = 1 << 1;
	private static final int MOB_SP_HOSTILE = 1 << 2;
	private static final int MOB_SP_CREEPER = 1 << 3;
	
	// PvE damage control byte
	private static final int DMG_MOB_OFF = 1 << 1;
	private static final int DMG_FALL_OFF = 1 << 2;
	private static final int DMG_FIRE_OFF = 1 << 3;
	private static final int DMG_DROWN_OFF = 1 << 4;
	private static final int DMG_LAVA_OFF = 1 << 5;
	private static final int DMG_STARVE_OFF = 1 << 6;
	private static final int DMG_OTHER_OFF = 1 << 7;
	private static final int DMG_NAT_HALF = 1 << 8;
	
	// PvP damage control byte
	private static final int DMG_PVP_FF_OFF = 1 << 1;
	private static final int DMG_PVP_INSTAKILL = 1 << 2;
	private static final int DMG_PVP_HALF = 1 << 3;
	
	// Death control byte
	private static final int DIE_MOB_RESPAWN = 1 << 1;
	private static final int DIE_NAT_RESPAWN = 1 << 2;
	private static final int DIE_BAN = 1 << 3;
	private static final int DIE_HUNTER_RESPAWN = 1 << 4;
	
	/*// Join control byte
	private static final int JOIN_OP_ONLY = 1 << 1;
	private static final int JOIN_RANDOM = 1 << 2;
	private static final int JOIN_OP_SETPOS = 1 << 3;
	private static final int JOIN_SPEC_ALLOW = 1 << 4;
	private static final int JOIN_SPEC_OPONLY = 1 << 5;*/
	
	private String location;
	
	public boolean spawnAnimals;
	public boolean spawnHostile;
	public boolean spawnCreeper;
	
	public boolean hostileNoDamage;
	public boolean fallNoDamage;
	public boolean fireNoDamage;
	public boolean drownNoDamage;
	public boolean lavaNoDamage;
	public boolean starveNoDamage;
	public boolean otherNoDamage;
	public boolean natDamageHalf;
	
	public boolean noFriendlyFire;
	public boolean pvpDeathInstant;
	public boolean pvpDamageHalf;
	
	public boolean mobDeathRespawn;
	public boolean natDeathRespawn;
	public boolean banOnDeath;
	public boolean huntersRespawn;
	
	public int disTimeout;
	public short maxDays;
	
	public DataFile(String dat) {
		if (!exists(dat))
			throw new RuntimeException(new FileNotFoundException());
		if (new File("plugins/manhunt/" + dat + ".dat").isDirectory())
			throw new RuntimeException(new IOException());
		location = "plugins/manhunt/" + dat + ".dat";
		load();
	}
	
	private DataFile() {
		// Do nothing
	}
	
	public DataFile save(String l) {
		try {
			FileOutputStream s = new FileOutputStream(new File(l));
			
			int sp = 0;
			sp |= revBit(spawnAnimals, MOB_SP_PEACEFUL);
			sp |= revBit(spawnHostile, MOB_SP_HOSTILE);
			sp |= revBit(spawnCreeper, MOB_SP_CREEPER);
			s.write(sp);
			
			int edmg = 0;
			edmg |= revBit(hostileNoDamage, DMG_MOB_OFF);
			edmg |= revBit(fallNoDamage, DMG_FALL_OFF);
			edmg |= revBit(fireNoDamage, DMG_FIRE_OFF);
			edmg |= revBit(drownNoDamage, DMG_DROWN_OFF);
			edmg |= revBit(lavaNoDamage, DMG_LAVA_OFF);
			edmg |= revBit(starveNoDamage, DMG_STARVE_OFF);
			edmg |= revBit(otherNoDamage, DMG_OTHER_OFF);
			edmg |= revBit(natDamageHalf, DMG_NAT_HALF);
			s.write(edmg);
			
			int pdmg = 0;
			pdmg |= revBit(noFriendlyFire, DMG_PVP_FF_OFF);
			pdmg |= revBit(pvpDeathInstant, DMG_PVP_INSTAKILL);
			pdmg |= revBit(pvpDamageHalf, DMG_PVP_HALF);
			s.write(pdmg);
			
			int die = 0;
			die |= revBit(mobDeathRespawn, DIE_MOB_RESPAWN);
			die |= revBit(natDeathRespawn, DIE_NAT_RESPAWN);
			die |= revBit(banOnDeath, DIE_BAN);
			die |= revBit(huntersRespawn, DIE_HUNTER_RESPAWN);
			s.write(die);
			
			s.flush();
			s.close();
			
			if (l.equals(location)) {
				return null;
			} else {
				return new DataFile(l);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void save() {
		save(location);
	}
	
	public void loadDefaults() {
		spawnAnimals = true;
		spawnHostile = true;
		spawnCreeper = false;
		
		hostileNoDamage = false;
		fallNoDamage = false;
		fireNoDamage = false;
		lavaNoDamage = false;
		starveNoDamage = false;
		otherNoDamage = false;
		natDamageHalf = true;
		
		noFriendlyFire = true;
		pvpDeathInstant = false;
		pvpDamageHalf = false;
		
		mobDeathRespawn = true;
		natDeathRespawn = true;
		banOnDeath = false;
		huntersRespawn = false;
		
		disTimeout = 60000;
		maxDays = 3;
	}
	
	public void load() {
		try {
			FileInputStream s = new FileInputStream(new File(location));
			
			int sp = s.read();
			spawnAnimals = getBit(sp, MOB_SP_PEACEFUL);
			spawnHostile = getBit(sp, MOB_SP_HOSTILE);
			spawnCreeper = getBit(sp, MOB_SP_CREEPER);
			
			int edmg = s.read();
			hostileNoDamage = getBit(edmg, DMG_MOB_OFF);
			fallNoDamage = getBit(edmg, DMG_FALL_OFF);
			fireNoDamage = getBit(edmg, DMG_FIRE_OFF);
			drownNoDamage = getBit(edmg, DMG_DROWN_OFF);
			lavaNoDamage = getBit(edmg, DMG_LAVA_OFF);
			starveNoDamage = getBit(edmg, DMG_STARVE_OFF);
			otherNoDamage = getBit(edmg, DMG_OTHER_OFF);
			natDamageHalf = getBit(edmg, DMG_NAT_HALF);
			
			int pdmg = s.read();
			noFriendlyFire = getBit(pdmg, DMG_PVP_FF_OFF);
			pvpDeathInstant = getBit(pdmg, DMG_PVP_INSTAKILL);
			pvpDamageHalf = getBit(pdmg, DMG_PVP_HALF);
			
			int die = s.read();
			mobDeathRespawn = getBit(die, DIE_MOB_RESPAWN);
			natDeathRespawn = getBit(die, DIE_NAT_RESPAWN);
			banOnDeath = getBit(die, DIE_BAN);
			huntersRespawn = getBit(die, DIE_HUNTER_RESPAWN);
			
			disTimeout = s.read();
			disTimeout += s.read() * (Byte.MAX_VALUE - Byte.MIN_VALUE);
			disTimeout += s.read() * Math.pow((Byte.MAX_VALUE - Byte.MIN_VALUE), 2);
			disTimeout += s.read() * Math.pow((Byte.MAX_VALUE - Byte.MIN_VALUE), 3);
			disTimeout += s.read() * Math.pow((Byte.MAX_VALUE - Byte.MIN_VALUE), 4);
			
			maxDays = (short) s.read();
			maxDays += s.read() * (Byte.MAX_VALUE - Byte.MIN_VALUE);
			
			s.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getLocation() {
		return location;
	}
	
	public static boolean exists(String dat) {
		File f = new File("plugins/manhunt/" + dat + ".dat");
		return f.exists() && !f.isDirectory();
	}
	
	public static DataFile newFile(String dat) {
		File f = new File("plugins/manhunt/" + dat + ".dat");
		if (f.exists()) {
			return new DataFile(dat);
		} else {
			try {
				f.createNewFile();
				DataFile d = new DataFile();
				d.location = "plugins/manhunt/" + dat + ".dat";
				d.loadDefaults();
				return d;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static boolean getBit(int i, int bitMask) {
		return (i & bitMask) != 0;
	}
	
	private static int revBit(boolean bit, int bitMask) {
		return (bit) ? bitMask : 0;
	}
}
