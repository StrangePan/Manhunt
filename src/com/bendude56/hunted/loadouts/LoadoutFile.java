package com.bendude56.hunted.loadouts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jnbt.CompoundTag;
import org.jnbt.DoubleTag;
import org.jnbt.ListTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;
import org.jnbt.StringTag;
import org.jnbt.Tag;
import org.jnbt.ByteTag;
import org.jnbt.TagType;

import com.bendude56.hunted.Manhunt;
import com.bendude56.hunted.NewManhuntPlugin;
import com.bendude56.hunted.loadouts.models.SimpleEffect;

public class LoadoutFile
{
	private static final String tag_name = "Name";
	private static final String tag_version = "Version";
	private static final String tag_contents = "Contents";
	private static final String tag_armor = "Armor";
	private static final String tag_effects = "Effects";
	private static final String tag_randoms = "Random";
	private static final String tag_chance = "Chance";
	
	
	public static void load(Loadout loadout)
	{
		// Allocate memory
		CompoundTag main_tag;
		ItemStack[] contents;
		ItemStack[] armor;
		List<PotionEffect> effects;
		List<RandomStack> randoms;
		@SuppressWarnings("unused")
		String version;
		
		
		// Read tag from disk
		main_tag = loadLoadoutFile(loadout.getFilename());
		
		if (main_tag == null)
			return;
		
		
		// Initialize arrays and lists
		contents = new ItemStack[36];
		armor = new ItemStack[4];
		effects = new ArrayList<PotionEffect>();
		randoms = new ArrayList<RandomStack>();
		
		
		// Read in important tags
		if (main_tag.getValue().containsKey(tag_version) && main_tag.getValue().get(tag_version).getTagType() == TagType.STRING)
		{
			version = (String) main_tag.getValue().get(tag_version).getValue();
		}
		if (main_tag.getValue().containsKey(tag_name) && main_tag.getValue().get(tag_name).getTagType() == TagType.STRING)
		{
			loadout.setName((String) main_tag.getValue().get(tag_name).getValue());
		}
		
		
		// Read in items and potion effects
		if (main_tag.getValue().containsKey(tag_contents)
				&& main_tag.getValue().get(tag_contents).getTagType() == TagType.LIST
				&& ((ListTag) main_tag.getValue().get(tag_contents)).getDataType() == CompoundTag.class)
		{
			for (Tag tag : ((ListTag) main_tag.getValue().get(tag_contents)).getValue())
			{
				if (tag.getTagType() == TagType.COMPOUND)
				{
					ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_4_6.ItemStack.a(((CompoundTag) tag).toNBTTag()));
					
					if (((CompoundTag) tag).getValue().containsKey("Slot") && ((CompoundTag) tag).getValue().get("Slot").getTagType() == TagType.BYTE)
					{
						contents[((ByteTag) ((CompoundTag) tag).getValue().get("Slot")).getValue()] = stack;
					}
				}
			}
		}

		if (main_tag.getValue().containsKey(tag_armor)
				&& main_tag.getValue().get(tag_armor).getTagType() == TagType.LIST
				&& ((ListTag) main_tag.getValue().get(tag_armor)).getDataType() == CompoundTag.class)
		{
			for (Tag tag : ((ListTag) main_tag.getValue().get(tag_armor)).getValue())
			{
				if (tag.getTagType() == TagType.COMPOUND)
				{
					ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_4_6.ItemStack.a(((CompoundTag) tag).toNBTTag()));
					
					if (((CompoundTag) tag).getValue().containsKey("Slot") && ((CompoundTag) tag).getValue().get("Slot").getTagType() == TagType.BYTE)
					{
						armor[((ByteTag) ((CompoundTag) tag).getValue().get("Slot")).getValue() - 100] = stack;
					}
				}
			}
		}

		if (main_tag.getValue().containsKey(tag_effects)
				&& main_tag.getValue().get(tag_effects).getTagType() == TagType.LIST
				&& ((ListTag) main_tag.getValue().get(tag_effects)).getDataType() == CompoundTag.class)
		{
			for (Tag tag : ((ListTag) main_tag.getValue().get(tag_effects)).getValue())
			{
				if (tag.getTagType() == TagType.COMPOUND)
				{
					SimpleEffect effect = new SimpleEffect();
					
					((CompoundTag) tag).toObject(effect);
					
					effects.add(effect.toPotionEffect());
				}
			}
		}
		
		//Read in random items
		if (main_tag.getValue().containsKey(tag_randoms)
				&& main_tag.getValue().get(tag_randoms).getTagType() == TagType.LIST
				&& ((ListTag) main_tag.getValue().get(tag_randoms)).getDataType() == CompoundTag.class)
		{
			for (Tag tag : ((ListTag) main_tag.getValue().get(tag_randoms)).getValue())
			{
				if (tag.getTagType() == TagType.COMPOUND)
				{
					ItemStack stack = CraftItemStack.asBukkitCopy(net.minecraft.server.v1_4_6.ItemStack.a(((CompoundTag) tag).toNBTTag()));
					
					if (((CompoundTag) tag).getValue().containsKey(tag_chance) && ((CompoundTag) tag).getValue().get(tag_chance).getTagType() == TagType.DOUBLE)
					{
						randoms.add(new RandomStack(stack, ((DoubleTag) ((CompoundTag) tag).getValue().get(tag_chance)).getValue()));
					}
				}
			}
		}
				
		
		loadout.setContents(contents, armor);
		loadout.setPotionEffects(effects);
		loadout.setRandomItemStack(randoms);
	}
	
	private static CompoundTag loadLoadoutFile(String filename)
	{
		File file = new File(Manhunt.dirname_loadouts + "/" + filename + Manhunt.extension_loadouts);
		File dir = new File(Manhunt.dirname_loadouts);
		
		if (!dir.exists())
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout file \"" + filename + Manhunt.extension_loadouts + "\"!");
			return null;
		}
		if (!file.exists())
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout file \"" + filename + Manhunt.extension_loadouts + "\"!");
			return null;
		}
		
		try
		{
			NBTInputStream stream = new NBTInputStream(new FileInputStream(file));
			Tag tag = stream.readTag();
			stream.close();
			
			if (tag.getTagType() == TagType.COMPOUND)
				return (CompoundTag) tag;
			else
				return null;
		}
		catch (IOException e)
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout file \"" + filename + Manhunt.extension_loadouts + "\"!");
			Manhunt.log(Level.SEVERE, e.getMessage());
			return null;
		}
	}
	
	public static void save(Loadout loadout)
	{
		// Allocate Memory
		CompoundTag main_tag;
		List<Tag> contents;
		List<Tag> armor;
		List<Tag> effects;
		List<Tag> randoms;
		
		
		// Initialize main tag
		main_tag = new CompoundTag("", new HashMap<String, Tag>());
		
		
		// Insert essential values
		main_tag.getValue().put(tag_version, new StringTag("version", NewManhuntPlugin.getInstance().getDescription().getVersion()));
		main_tag.getValue().put(tag_name, new StringTag("name", loadout.getName()));
		
		
		// Build item and effect lists
		contents = new ArrayList<Tag>();
		for (ItemStack stack : loadout.getContents())
		{
			contents.add(CompoundTag.fromNBTTag(CraftItemStack.asNMSCopy(stack).getTag()));
		}
		
		armor = new ArrayList<Tag>();
		for (ItemStack stack : loadout.getArmorContents())
		{
			armor.add(CompoundTag.fromNBTTag(CraftItemStack.asNMSCopy(stack).getTag()));
		}
		
		effects = new ArrayList<Tag>();
		for (PotionEffect effect : loadout.getEffects())
		{
			// Uses the "simple effect" object because there are no built-in
			//		craftbukkit methods for serializing potion effects into tags.
			effects.add(CompoundTag.fromObject(SimpleEffect.fromPotionEffect(effect)));
		}
		
		randoms = new ArrayList<Tag>();
		for (RandomStack random : loadout.getAllRandomItemStacks())
		{
			CompoundTag tag = CompoundTag.fromNBTTag(CraftItemStack.asNMSCopy(random.getItemStack()).getTag());
			tag.getValue().put(tag_chance, new DoubleTag(tag_chance, random.getChance()));
			
			randoms.add(tag);
		}
		
		
		// Insert lists into main tag
		main_tag.getValue().put(tag_contents, new ListTag(tag_contents, CompoundTag.class, contents));
		main_tag.getValue().put(tag_armor, new ListTag(tag_armor, CompoundTag.class, armor));
		main_tag.getValue().put(tag_effects, new ListTag(tag_effects, CompoundTag.class, effects));
		main_tag.getValue().put(tag_randoms, new ListTag(tag_randoms, CompoundTag.class, randoms));
		
		
		// Save the constructed tag to disk
		saveFile(loadout, main_tag);
	}
	
	private static void saveFile(Loadout loadout, CompoundTag tag)
	{
		File file = new File(Manhunt.dirname_loadouts + "/" + loadout.getFilename() + Manhunt.extension_loadouts);
		File dir = new File(Manhunt.dirname_loadouts);
		
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				Manhunt.log(Level.SEVERE,
						"Problem loading the Manhunt loadout \"" + loadout.getName() + "!\"");
				Manhunt.log(Level.SEVERE, e.getMessage());
				return;
			}
		}
		try
		{
			NBTOutputStream stream = new NBTOutputStream(new FileOutputStream(file));
			stream.writeTag(tag);
			stream.close();
		}
		catch (IOException e)
		{
			Manhunt.log(Level.SEVERE,
					"Problem loading the Manhunt loadout \"" + loadout.getName() + "!\"");
			Manhunt.log(Level.SEVERE, e.getMessage());
			return;
		}
	}
	
	public static boolean delete(Loadout loadout)
	{
		File file = new File(Manhunt.dirname_loadouts + "/" + loadout.getFilename() + Manhunt.extension_loadouts);
		if (file.exists())
		{
			return file.delete();
		}
		else
		{
			return true;
		}
	}
	
}
