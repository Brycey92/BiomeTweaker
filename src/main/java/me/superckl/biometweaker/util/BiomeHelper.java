package me.superckl.biometweaker.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import me.superckl.biometweaker.common.world.biome.property.BiomePropertyManager;
import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.integration.IntegrationManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;

public class BiomeHelper {

	private static Field biomeInfoMap;
	private static Field typeInfoList;
	private static Field typeList;
	private static Field biomes;
	private static Field isModded;

	public static JsonObject fillJsonObject(final Biome biome, final int ... coords){
		BiomeHelper.checkFields();
		final JsonObject obj = new JsonObject();
		obj.addProperty("ID", Biome.getIdForBiome(biome));
		obj.addProperty("Name", biome.getBiomeName());
		obj.addProperty("Resource Location", Biome.REGISTRY.getNameForObject(biome).toString());
		obj.addProperty("Class", biome.getClass().getName());
		obj.addProperty("Root Height", biome.getBaseHeight());
		obj.addProperty("Height Variation", biome.getHeightVariation());
		final boolean topNull = biome.topBlock == null || biome.topBlock.getBlock() == null || biome.topBlock.getBlock().delegate == null;
		final boolean bottomNull = biome.topBlock == null || biome.topBlock.getBlock() == null || biome.topBlock.getBlock().delegate == null;
		obj.addProperty("Top Block", topNull ? "ERROR":biome.topBlock.getBlock().delegate.name().toString());
		obj.addProperty("Filler Block", bottomNull ? "ERROR":biome.fillerBlock.getBlock().delegate.name().toString());
		try {
			int i = -1;
			//obj.addProperty("Actual Filler Block", ((Block) BiomeHelper.actualFillerBlock.get(gen)).delegate.name());
			//obj.addProperty("Liquid Filler Block", ((Block) BiomeHelper.liquidFillerBlock.get(gen)).delegate.name());
			final boolean hasCoords = (coords != null) && (coords.length == 3);
			int x = 0, y = 0, z = 0;
			if(hasCoords){
				x = coords[0];
				y = coords[1];
				z = coords[2];
			}
			obj.addProperty("Grass Color", ""+(hasCoords ? biome.getGrassColorAtPos(new BlockPos(x, y, z)):(i = BiomePropertyManager.GRASS_COLOR.get(biome)) == -1 ? "Not set. Check in-game.":i));
			obj.addProperty("Foliage Color", ""+(hasCoords ? biome.getFoliageColorAtPos(new BlockPos(x, y, z)):(i = BiomePropertyManager.FOLIAGE_COLOR.get(biome)) == -1 ? "Not set. Check in-game.":i));
			obj.addProperty("Water Color", ""+biome.getWaterColorMultiplier());
		} catch (final Exception e) {
			LogHelper.error("Failed to retrieve inserted fields!");
			e.printStackTrace();
		}
		obj.addProperty("Temperature", biome.getTemperature());
		obj.addProperty("Humidity", biome.getRainfall());
		obj.addProperty("Water Tint", biome.getWaterColorMultiplier());
		obj.addProperty("Enable Rain", biome.enableRain);
		obj.addProperty("Enable Snow", biome.enableSnow);
		JsonArray array = new JsonArray();
		for(final Type type: BiomeDictionary.getTypesForBiome(biome))
			array.add(new JsonPrimitive(type.toString()));
		obj.add("Dictionary Types", array);

		final JsonObject managerWeights = new JsonObject();
		for(final BiomeManager.BiomeType type:BiomeManager.BiomeType.values()){
			final JsonArray subArray = new JsonArray();
			final List<BiomeEntry> entries = BiomeManager.getBiomes(type);
			for(final BiomeEntry entry:entries)
				if(Biome.getIdForBiome(entry.biome) == Biome.getIdForBiome(biome))
					subArray.add(new JsonPrimitive(entry.itemWeight));
			if(subArray.size() > 0)
				managerWeights.add(type.name()+" Weights", subArray);
		}
		obj.add("BiomeManager Entries", managerWeights);

		array = new JsonArray();
		for(final Object entity:biome.spawnableCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Creatures", array);

		array = new JsonArray();
		for(final Object entity:biome.spawnableMonsterList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Monsters", array);

		array = new JsonArray();
		for(final Object entity:biome.spawnableWaterCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Water Creatures", array);

		array = new JsonArray();
		for(final Object entity:biome.spawnableCaveCreatureList){
			final SpawnListEntry entry = (SpawnListEntry) entity;
			final JsonObject object = new JsonObject();
			object.addProperty("Entity Class", entry.entityClass.getName());
			object.addProperty("Weight", entry.itemWeight);
			object.addProperty("Min Group Count", entry.minGroupCount);
			object.addProperty("Max Group Count", entry.maxGroupCount);
			array.add(object);
		}
		obj.add("Spawnable Cave Creatures", array);
		obj.add("Spawn Biome", new JsonPrimitive(BiomeProvider.allowedBiomes.contains(biome)));
		obj.addProperty("Tweaked", Config.INSTANCE.getTweakedBiomes().contains(-1) || Config.INSTANCE.getTweakedBiomes().contains(Biome.getIdForBiome(biome)));

		IntegrationManager.INSTANCE.addBiomeInfo(biome, obj);

		return obj;
	}

	private static void checkFields(){
		try{
			if(BiomeHelper.biomeInfoMap == null){
				BiomeHelper.biomeInfoMap = BiomeDictionary.class.getDeclaredField("biomeInfoMap");
				BiomeHelper.biomeInfoMap.setAccessible(true);
			}
			if(BiomeHelper.typeInfoList == null){
				BiomeHelper.typeInfoList = BiomeDictionary.class.getDeclaredField("typeInfoList");
				BiomeHelper.typeInfoList.setAccessible(true);
			}
			if(BiomeHelper.biomes == null){
				BiomeHelper.biomes = BiomeManager.class.getDeclaredField("biomes");
				BiomeHelper.biomes.setAccessible(true);
			}
		}catch(final Exception e){
			LogHelper.error("Failed to find inserted fields!");
			e.printStackTrace();
		}
	}

	public static int callGrassColorEvent(final int color, final Biome gen){
		final GetGrassColor e = new GetGrassColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.getNewColor();
	}

	public static int callFoliageColorEvent(final int color, final Biome gen){
		final GetFoliageColor e = new GetFoliageColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.getNewColor();
	}

	public static int callWaterColorEvent(final int color, final Biome gen){
		final GetWaterColor e = new GetWaterColor(gen, color);
		MinecraftForge.EVENT_BUS.post(e);
		return e.getNewColor();
	}

	public static void modifyBiomeDicType(final Biome gen, final BiomeDictionary.Type type, final boolean remove) throws Exception{
		BiomeHelper.checkFields();
		if(gen == null)
			return;
		final List<Biome>[] listArray = (List<Biome>[]) BiomeHelper.typeInfoList.get(null);
		if(listArray.length > type.ordinal()){
			List<Biome> list = listArray[type.ordinal()];
			if(list == null){
				list = Lists.newArrayList();
				listArray[type.ordinal()] = list;
			}
			if(remove)
				list.remove(gen);
			else if(!list.contains(gen))
				list.add(gen);
		}
		//Okay, here we go. REFLECTION OVERLOAD!!!1! (It's really not that bad.)
		final Map map = (Map) BiomeHelper.biomeInfoMap.get(null);
		final Object biomeInfo = map.get(Biome.REGISTRY.getNameForObject(gen));
		if(BiomeHelper.typeList == null){
			BiomeHelper.typeList = biomeInfo.getClass().getDeclaredField("typeList");
			BiomeHelper.typeList.setAccessible(true);
		}
		final EnumSet<BiomeDictionary.Type> set = (EnumSet<Type>) BiomeHelper.typeList.get(biomeInfo);
		if(remove)
			set.remove(type);
		else if(!set.contains(type))
			set.add(type);
	}

	public static void removeAllBiomeDicType(final Biome gen) throws Exception{
		BiomeHelper.checkFields();
		if(gen == null)
			return;
		final Object array = BiomeHelper.biomeInfoMap.get(null);
		final Object biomeInfo = Array.get(array, Biome.getIdForBiome(gen));
		if(BiomeHelper.typeList == null){
			BiomeHelper.typeList = biomeInfo.getClass().getDeclaredField("typeList");
			BiomeHelper.typeList.setAccessible(true);
		}
		final EnumSet<BiomeDictionary.Type> set = (EnumSet<Type>) BiomeHelper.typeList.get(biomeInfo);
		final List<Biome>[] listArray = (List<Biome>[]) BiomeHelper.typeInfoList.get(null);
		for(final BiomeDictionary.Type type : set)
			listArray[type.ordinal()].remove(gen);
		set.clear();
	}

	private static boolean hasModded;

	public static void modTypeLists() throws Exception{
		BiomeHelper.checkFields();
		if(BiomeHelper.hasModded)
			return;
		//LogHelper.info("Setting TrackedLists to modded...");
		final Object array = BiomeHelper.biomes.get(null);
		final int length = Array.getLength(array);
		for(int i = 0; i < length; i++){
			final Object list = Array.get(array, i);
			if(BiomeHelper.isModded == null){
				BiomeHelper.isModded = list.getClass().getDeclaredField("isModded");
				BiomeHelper.isModded.setAccessible(true);
			}
			BiomeHelper.isModded.setBoolean(list, true);
		}
		BiomeHelper.hasModded = true;
	}

}
