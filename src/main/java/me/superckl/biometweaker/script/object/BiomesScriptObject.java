package me.superckl.biometweaker.script.object;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;

import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.object.BiomePackScriptObject;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.biometweaker.script.pack.MergedBiomesPackage;
import me.superckl.api.biometweaker.script.wrapper.BTParameterTypes;
import me.superckl.api.biometweaker.util.SpawnListType;
import me.superckl.api.biometweaker.world.gen.feature.WorldGeneratorBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.script.ParameterTypes;
import me.superckl.api.superscript.script.ScriptHandler;
import me.superckl.biometweaker.BiomeTweaker;
import me.superckl.biometweaker.script.command.entity.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.entity.ScriptCommandMaxSpawnPackSize;
import me.superckl.biometweaker.script.command.entity.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddActualFillerBlock;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddDecoration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddDictionaryType;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.generation.ScriptCommandAddToGeneration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBiomeReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRegisterVillageBlockReplacement;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveAllDictionaryTypes;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDecoration;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveDictionaryType;
import me.superckl.biometweaker.script.command.generation.ScriptCommandRemoveFeature;
import me.superckl.biometweaker.script.command.misc.ScriptCommandDisableBonemealUse;
import me.superckl.biometweaker.script.command.misc.ScriptCommandInheritProperties;
import me.superckl.biometweaker.script.command.misc.ScriptCommandSetBiomeProperty;

public class BiomesScriptObject extends BiomePackScriptObject{

	@Override
	public Pair<Constructor<? extends IScriptCommand>, Object[]> modifyConstructorPair(
			final Pair<Constructor<? extends IScriptCommand>, Object[]> pair, final String[] args, final ScriptHandler handler) {
		final Object[] newArgs = new Object[pair.getValue().length+1];
		System.arraycopy(pair.getValue(), 0, newArgs, 1, pair.getValue().length);
		newArgs[0] = this.pack;
		return Pair.of(pair.getKey(), newArgs);
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = Maps.newLinkedHashMap();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(), ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("remove", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()
				, ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class, Integer.TYPE, Integer.TYPE, Integer.TYPE));
		validCommands.put("addSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandAddRemoveSpawn.class.getDeclaredConstructor(IBiomePackage.class, String.class, SpawnListType.class));
		validCommands.put("removeSpawn", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.SPAWN_TYPE.getSimpleWrapper())
				, ScriptCommandRemoveAllSpawns.class.getDeclaredConstructor(IBiomePackage.class, SpawnListType.class));
		validCommands.put("removeAllSpawns", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, BlockStateBuilder.class, Integer.TYPE));
		validCommands.put("addFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiomeFlower.class.getDeclaredConstructor(IBiomePackage.class, BlockStateBuilder.class));
		validCommands.put("removeFlower", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.JSON_ELEMENT.getSimpleWrapper())
				, ScriptCommandSetBiomeProperty.class.getDeclaredConstructor(IBiomePackage.class, String.class, JsonElement.class));
		validCommands.put("set", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandAddDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("addDicType", listing);

		if(BiomeTweaker.getInstance().isTweakEnabled("actualFillerBlocks")){
			listing = new ScriptCommandListing();
			listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper()), ScriptCommandAddActualFillerBlock.class.getDeclaredConstructor(IBiomePackage.class, BlockStateBuilder.class));
			validCommands.put("addActualFillerBlock", listing);
		}

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveDictionaryType.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeDicType", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(), ScriptCommandRemoveAllDictionaryTypes.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("removeAllDicTypes", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveDecoration.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeDecoration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING_ARRAY.getSpecialWrapper()), ScriptCommandRemoveFeature.class.getDeclaredConstructor(IBiomePackage.class, String[].class));
		validCommands.put("removeFeature", listing);

		//TODO: figure out why this command doesn't exist in the 1.11 branch at commit f49bc4bdc1ccc88b4da5bf2985449bd9c91b3c98
		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddRemoveBiome.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("create", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper(), BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, BlockStateBuilder.class, BlockStateBuilder.class));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper(), BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper(), BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper())
				, ScriptCommandRegisterBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE, BlockStateBuilder.class, BlockStateBuilder.class));
		validCommands.put("registerGenBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper(), BTParameterTypes.BLOCKSTATE_BUILDER.getSimpleWrapper())
				, ScriptCommandRegisterVillageBlockReplacement.class.getDeclaredConstructor(IBiomePackage.class, BlockStateBuilder.class, BlockStateBuilder.class));
		validCommands.put("registerGenVillageBlockRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandMaxSpawnPackSize.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("setMaxSpawnPackSize", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper())
				, ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class, String.class));
		listing.addEntry(Lists.newArrayList(), ScriptCommandDisableBonemealUse.class.getDeclaredConstructor(IBiomePackage.class));
		validCommands.put("disableBonemealUse", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandAddToGeneration.class.getDeclaredConstructor(IBiomePackage.class, String.class, Integer.TYPE));
		validCommands.put("addToGeneration", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper())
				, ScriptCommandRegisterBiomeReplacement.class.getDeclaredConstructor(IBiomePackage.class, Integer.TYPE));
		validCommands.put("registerGenBiomeRep", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper(), ParameterTypes.STRING_ARRAY.getSpecialWrapper())
				, ScriptCommandInheritProperties.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class, String[].class));
		listing.addEntry(Lists.newArrayList(BTParameterTypes.BASIC_BIOMES_PACKAGE.getSimpleWrapper())
				, ScriptCommandInheritProperties.class.getDeclaredConstructor(IBiomePackage.class, IBiomePackage.class));
		validCommands.put("inheritProperties", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(BTParameterTypes.WORLD_GENERATOR_BUILDER.getSimpleWrapper())
				, ScriptCommandAddDecoration.class.getDeclaredConstructor(IBiomePackage.class, WorldGeneratorBuilder.class));
		validCommands.put("addDecoration", listing);

		return validCommands;
	}

	@Override
	public void addCommand(final IScriptCommand command) {
		BiomeTweaker.getInstance().addCommand(command);
	}

	@Override
	public void readArgs(final Object... packs) throws Exception {
		final IBiomePackage[] bPacks = new IBiomePackage[packs.length];
		System.arraycopy(packs, 0, bPacks, 0, packs.length);
		if(bPacks.length == 1)
			this.pack = bPacks[0];
		else
			this.pack = new MergedBiomesPackage(bPacks);
	}



}
