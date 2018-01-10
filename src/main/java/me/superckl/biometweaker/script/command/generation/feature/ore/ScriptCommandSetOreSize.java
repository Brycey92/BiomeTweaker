package me.superckl.biometweaker.script.command.generation.feature.ore;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenMineableBuilder;
import me.superckl.biometweaker.script.object.decoration.OreDecorationScriptObject;

@AutoRegister(classes = OreDecorationScriptObject.class, name = "setSize")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="oreGenBuilder", parameterIndex=0), @ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1)})
public class ScriptCommandSetOreSize extends ScriptCommand{

	private final WorldGenMineableBuilder builder;
	private final int size;

	@Override
	public void perform() throws Exception {
		this.builder.setSize(this.size);
	}

}
