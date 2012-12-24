package org.jnbt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_4_6.NBTBase;
import net.minecraft.server.v1_4_6.NBTTagByte;
import net.minecraft.server.v1_4_6.NBTTagByteArray;
import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagDouble;
import net.minecraft.server.v1_4_6.NBTTagEnd;
import net.minecraft.server.v1_4_6.NBTTagFloat;
import net.minecraft.server.v1_4_6.NBTTagInt;
import net.minecraft.server.v1_4_6.NBTTagList;
import net.minecraft.server.v1_4_6.NBTTagLong;
import net.minecraft.server.v1_4_6.NBTTagShort;
import net.minecraft.server.v1_4_6.NBTTagString;

import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable.ByteArray;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

/**
 * Represents a single NBT tag.
 * @author Graham Edgecombe
 *
 */
public abstract class Tag {
	
	/**
	 * The name of this tag.
	 */
	private final String name;
	/**
	 * The type of this tag
	 */
	private final TagType tag_type;
	/**
	 * The data type of this tag
	 */
	private final Type data_type;
	
	/**
	 * Creates the tag with the specified name.
	 * @param name The name.
	 */
	public Tag(String name, TagType type) {
		this.name = name;
		this.tag_type = type;
		switch (this.tag_type)
		{
		case BYTE_ARRAY:
			this.data_type = ByteArray.class;
			break;
		case BYTE:
			this.data_type = byte.class;
			break;
		case DOUBLE:
			this.data_type = double.class;
			break;
		case FLOAT:
			this.data_type = float.class;
			break;
		case INTEGER:
			this.data_type =  int.class;
			break;
		case LIST:
			this.data_type = List.class;
			break;
		case SHORT:
			this.data_type = short.class;
			break;
		case STRING:
			this.data_type = String.class;
			break;
		default:
			this.data_type = null;
		}
	}
	
	/**
	 * Gets the name of this tag.
	 * @return The name of this tag.
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Gets the value of this tag.
	 * @return The value of this tag.
	 */
	public abstract Object getValue();

	/**
	 * Gets the type of this tag.
	 * @return The type of this tag.
	 */
	public final TagType getTagType()
	{
		return tag_type;
	}
	
	/**
	 * Gets the data type of this tag.
	 * @return The data type of this tag.
	 */
	public final Type getDataType()
	{
		return data_type;
	}
	
	/**
	 * Creates a new specialized tag based on the given data type
	 */
	@SuppressWarnings("unchecked")
	public static final Tag fromValue(String name, Object o)
	{
		if (o.getClass() == byte[].class || o.getClass() == Byte[].class)
			return new ByteArrayTag(name, (byte[]) o);
		else if (o.getClass() == byte.class || o.getClass() == Byte.class)
			return new ByteTag(name, (byte) o);
		else if (o.getClass() == double.class || o.getClass() == Double.class)
			return new DoubleTag(name, (double) o);
		else if (o.getClass() == float.class || o.getClass() == Float.class)
			return new FloatTag(name, (float) o);
		else if (o.getClass() == int.class || o.getClass() == Integer.class)
			return new IntTag(name, (int) o);
		else if (o.getClass() == long.class || o.getClass() == Long.class)
			return new LongTag(name, (long) o);
		else if (o.getClass() == short.class || o.getClass() == Short.class)
			return new ShortTag(name, (short) o);
		else if (o.getClass() == String.class)
			return new StringTag(name, (String) o);
		else if (o.getClass() == List.class || o.getClass() == ArrayList.class)
		{
			List<Tag> l = new ArrayList<Tag>();
			Type t = null;
			for (Object obj : (List<? extends Tag>) o)
			{
				Tag tag = Tag.fromValue("", obj);
				l.add(tag);
				if (t == null)
					t = tag.getClass();
			}
			if (t == null)
				return new ListTag(name, ByteTag.class, new ArrayList<Tag>());
			else
				return new ListTag(name, (Class<? extends Tag>) t, l);
		}
		else // Compound Tag Tyme!
		{
			return NBTUtils.objectToCompoundTag(o, name);
		}
			
	}
	
	/**
	 * Converts the tag into it's NBTBase counterpart.
	 * @return
	 */
	public NBTBase toNBTTag()
	{
		return null;
	}
	
	/**
	 * Converts an NBTTag into its jnbt counterpart.
	 * @param base
	 * @return Tag
	 */
	public static Tag fromNBTTag(NBTBase base)
	{
		if (base instanceof NBTTagByte)
			return ByteTag.fromNBTTag((NBTTagByte) base);
		else if (base instanceof NBTTagByteArray)
			return ByteArrayTag.fromNBTTag((NBTTagByteArray) base);
		else if (base instanceof NBTTagShort)
			return ShortTag.fromNBTTag((NBTTagShort) base);
		else if (base instanceof NBTTagInt)
			return IntTag.fromNBTTag((NBTTagInt) base);
		else if (base instanceof NBTTagLong)
			return LongTag.fromNBTTag((NBTTagLong) base);
		else if (base instanceof NBTTagFloat)
			return FloatTag.fromNBTTag((NBTTagFloat) base);
		else if (base instanceof NBTTagDouble)
			return DoubleTag.fromNBTTag((NBTTagDouble) base);
		else if (base instanceof NBTTagString)
			return StringTag.fromNBTTag((NBTTagString) base);
		else if (base instanceof NBTTagList)
			return ListTag.fromNBTTag((NBTTagList) base);
		else if (base instanceof NBTTagCompound)
			return CompoundTag.fromNBTTag((NBTTagCompound) base);
		else if (base instanceof NBTTagEnd)
			return EndTag.fromNBTTag((NBTTagEnd) base);
		else
			return null;
	}
	
}
