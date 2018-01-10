package me.superckl.biometweaker.script.command.generation.feature.tree;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.block.BlockStateBuilder;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenTreesBuilder;
import me.superckl.biometweaker.script.object.decoration.TreesDecorationScriptObject;

@AutoRegister(classes = TreesDecorationScriptObject.class, name = "setLeafBlock")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="treeGenBuilder", parameterIndex=0)})
public class ScriptCommandSetTreesLeafBlock extends ScriptCommand{

	private final WorldGenTreesBuilder builder;
	private final BlockStateBuilder<?> block;

	@Override
	public void perform() throws Exception {
		this.builder.setLeafBlock(this.block.build());
	}

}
