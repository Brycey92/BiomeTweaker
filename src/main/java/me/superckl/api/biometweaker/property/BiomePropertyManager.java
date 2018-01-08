package me.superckl.api.biometweaker.property;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.APIInfo;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

public class BiomePropertyManager {

	public static Property<String> NAME;
	public static Property<Float> HEIGHT;
	public static Property<Float> HEIGHT_VARIATION;
	public static Property<IBlockState> TOP_BLOCK;
	public static Property<IBlockState> FILLER_BLOCK;
	public static Property<IBlockState> OCEAN_TOP_BLOCK;
	public static Property<IBlockState> OCEAN_FILLER_BLOCK;
	public static Property<Float> TEMPERATURE;
	public static Property<Float> HUMIDITY;
	public static Property<Integer> WATER_TINT;
	public static Property<Boolean> ENABLE_RAIN;
	public static Property<Boolean> ENABLE_SNOW;
	public static Property<Integer> GRASS_COLOR;
	public static Property<Integer> FOLIAGE_COLOR;
	public static Property<Integer> WATER_COLOR;
	public static Property<Integer> SKY_COLOR;
	public static Property<IBlockState[]> ACTUAL_FILLER_BLOCKS;
	public static Property<Integer> GEN_WEIGHT;
	public static Property<Boolean> GEN_VILLAGES;
	public static Property<Boolean> GEN_STRONGHOLDS;
	public static Property<Boolean> GEN_SCATTERED_FEATURES;
	public static Property<Boolean> IS_SPAWN_BIOME;
	public static Property<Boolean> GEN_TALL_PLANTS;
	public static Property<Integer> WATERLILY_PER_CHUNK;
	public static Property<Integer> TREES_PER_CHUNK;
	public static Property<Integer> FLOWERS_PER_CHUNK;
	public static Property<Integer> GRASS_PER_CHUNK;
	public static Property<Integer> DEAD_BUSH_PER_CHUNK;
	public static Property<Integer> MUSHROOMS_PER_CHUNK;
	public static Property<Integer> REEDS_PER_CHUNK;
	public static Property<Integer> CACTI_PER_CHUNK;
	public static Property<Integer> SAND_PER_CHUNK;
	public static Property<Integer> CLAY_PER_CHUNK;
	public static Property<Integer> BIG_MUSHROOMS_PER_CHUNK;
	public static Property<Integer> DESERT_WELLS_PER_CHUNK;
	public static Property<Integer> FOSSILS_PER_CHUNK;
	public static Property<Integer> ICE_PER_CHUNK;
	public static Property<Integer> LAKES_PER_CHUNK;
	public static Property<Integer> LAVA_LAKES_PER_CHUNK;
	public static Property<Integer> PUMPKINS_PER_CHUNK;
	public static Property<Integer> ROCK_PER_CHUNK;
	public static Property<Integer> SAND2_PER_CHUNK;

	public static final Map<String, Property<?>> propertyMap = Maps.newHashMap();

	public static void populatePropertyMap(){
		final Field[] fields = BiomePropertyManager.class.getDeclaredFields();
		for(final Field field:fields)
			try {
				if(!Property.class.isAssignableFrom(field.getType()) || field.get(null) == null)
					continue;
				BiomePropertyManager.propertyMap.put(field.getName().toLowerCase().replace("_", ""), (Property<?>) field.get(null));
			} catch (final Exception e) {
				APIInfo.log.error("Unable to add property to propertyMap!");
				e.printStackTrace();
			}
	}

	public static boolean setProperty(final Biome biome, final String property, final JsonElement value, final ScriptHandler handler) throws Exception{
		final Property<?> prop = BiomePropertyManager.propertyMap.get(property);
		if(prop == null)
			throw new IllegalArgumentException("No property found for "+property);
		if(!prop.isSettable())
			return false;
		final Class<?> type = prop.getTypeClass();
		try {
			if(type.getCanonicalName().equals(Integer.class.getCanonicalName()))
				WarningHelper.<Property<Integer>>uncheckedCast(prop).set(biome, value.getAsInt());
			else if(type.getCanonicalName().equals(Float.class.getCanonicalName()))
				WarningHelper.<Property<Float>>uncheckedCast(prop).set(biome, value.getAsFloat());
			else if(type.getCanonicalName().equals(Boolean.class.getCanonicalName()))
				WarningHelper.<Property<Boolean>>uncheckedCast(prop).set(biome, value.getAsBoolean());
			else if(type.getCanonicalName().equals(String.class.getCanonicalName()))
				WarningHelper.<Property<String>>uncheckedCast(prop).set(biome, ParameterTypes.STRING.tryParse(value.getAsString(), handler));
			else if(type.getCanonicalName().equals(IBlockState.class.getCanonicalName()))
				WarningHelper.<Property<IBlockState>>uncheckedCast(prop).set(biome, BTParameterTypes.BLOCKSTATE_BUILDER.tryParse(value.getAsString(), handler).build());
		} catch (final Exception e) {
			throw e;
		}
		return true;
	}

}
