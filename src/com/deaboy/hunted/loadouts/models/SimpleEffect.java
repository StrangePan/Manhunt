package com.deaboy.hunted.loadouts.models;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SimpleEffect
{
	//---------------- Properties ----------------//
	public Integer type;
	public Integer duration;
	public Integer amplifier;
	public Boolean ambient;
	
	
	
	//---------------- Public Methods ----------------//
	public PotionEffect toPotionEffect()
	{
		return new PotionEffect(PotionEffectType.getById(type), duration, amplifier, ambient);
	}
	
	
	
	//---------------- Public Static Methods ----------------//
	public static SimpleEffect fromPotionEffect(PotionEffect effect)
	{
		SimpleEffect model = new SimpleEffect();
		
		model.type = effect.getType().getId();
		model.duration = effect.getDuration();
		model.amplifier = effect.getAmplifier();
		model.ambient = effect.isAmbient();
		
		return model;
	}
	
}
