package me.superckl.biometweaker.script.object.decoration;

import java.util.Map;

import com.google.common.collect.Lists;

import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.ScriptCommandListing;
import me.superckl.api.superscript.util.ParameterTypes;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetTreesGrowVines;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetTreesHeight;
import me.superckl.biometweaker.script.command.generation.feature.ScriptCommandSetTreesLeafBlock;

public class TreesDecorationScriptObject extends DecorationScriptObject<WorldGenTreesBuilder>{

	public TreesDecorationScriptObject() {
		super(new WorldGenTreesBuilder());
	}

	public static Map<String, ScriptCommandListing> populateCommands() throws Exception {
		final Map<String, ScriptCommandListing> validCommands = DecorationScriptObject.populateCommands();

		ScriptCommandListing listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.BOOLEAN.getSimpleWrapper()),
				ScriptCommandSetTreesGrowVines.class.getConstructor(WorldGenTreesBuilder.class, Boolean.TYPE));
		validCommands.put("setGrowVines", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetTreesHeight.class.getConstructor(WorldGenTreesBuilder.class, Integer.TYPE));
		validCommands.put("setHeight", listing);

		listing = new ScriptCommandListing();
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper(), ParameterTypes.NON_NEG_INTEGER.getSimpleWrapper()),
				ScriptCommandSetTreesLeafBlock.class.getConstructor(WorldGenTreesBuilder.class, String.class, Integer.TYPE));
		listing.addEntry(Lists.newArrayList(ParameterTypes.STRING.getSimpleWrapper()),
				ScriptCommandSetTreesLeafBlock.class.getConstructor(WorldGenTreesBuilder.class, String.class));
		validCommands.put("setLeafBlock", listing);

		return validCommands;
	}

}
