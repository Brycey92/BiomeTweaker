package me.superckl.biometweaker.script.command.generation.feature.cluster;

import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.AutoRegister;
import me.superckl.api.biometweaker.script.AutoRegister.ParameterOverride;
import me.superckl.api.superscript.script.command.ScriptCommand;
import me.superckl.biometweaker.common.world.gen.feature.WorldGenClusterBuilder;
import me.superckl.biometweaker.script.object.decoration.ClusterDecorationScriptObject;

@AutoRegister(classes = ClusterDecorationScriptObject.class, name = "setRadius")
@RequiredArgsConstructor(onConstructor_={@ParameterOverride(exceptionKey="clusterGenBuilder", parameterIndex=0), @ParameterOverride(exceptionKey="nonNegInt", parameterIndex=1)})
public class ScriptCommandSetClusterRadius extends ScriptCommand{

	private final WorldGenClusterBuilder builder;
	private final int radius;

	@Override
	public void perform() throws Exception {
		this.builder.setRadius(this.radius);
	}

}
