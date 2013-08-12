package com.deaboy.manhunt.loadouts.models;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SimpleEffect
{
	//---------------- Properties ----------------//
	public int type;
	public int duration;
	public int amplifier;
	public byte ambient;
	
	
	
	//---------------- Public Methods ----------------//
	public PotionEffect toPotionEffect()
	{
		return new PotionEffect(PotionEffectType.getById(type), duration, amplifier, ambient == 0 ? false : true);
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleEffect fromPotionEffect(PotionEffect effect)
	{
		SimpleEffect model = new SimpleEffect();
		
		model.type = effect.getType().getId();
		model.duration = effect.getDuration();
		model.amplifier = effect.getAmplifier();
		model.ambient = (effect.isAmbient() ? (byte) 1 : (byte) 0);
		
		return model;
	}
	
}
