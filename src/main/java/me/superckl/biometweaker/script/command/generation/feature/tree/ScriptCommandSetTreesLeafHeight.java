package me.superckl.biometweaker.script.command.generation.feature.tree;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.biometweaker.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.script.object.decoration.TreesDecorationScriptObject;

@AutoRegister(classes = TreesDecorationScriptObject.class, name = "setLeafHeight")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="treeGenBuilder", parameterIndex=0), @ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1)})
public class ScriptCommandSetTreesLeafHeight implements IScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final int height;

	@Override
	public void perform() throws Exception {
		this.builder.setLeafHeight(this.height);
	}

}
