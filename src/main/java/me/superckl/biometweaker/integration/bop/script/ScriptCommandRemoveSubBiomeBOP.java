package me.superckl.biometweaker.integration.bop.script;

import biomesoplenty.common.init.ModBiomes;
import lombok.RequiredArgsConstructor;
import me.superckl.api.biometweaker.script.pack.IBiomePackage;
import me.superckl.api.superscript.command.IScriptCommand;
import me.superckl.biometweaker.config.Config;

@RequiredArgsConstructor
public class ScriptCommandRemoveSubBiomeBOP implements IScriptCommand{

	private final IBiomePackage pack;
	private final IBiomePackage toAdd;

	@Override
	public void perform() throws Exception {
		for(final int i:this.pack.getRawIds()){
			if(ModBiomes.subBiomesMap.containsKey(i))
				ModBiomes.subBiomesMap.get(i).removeAll(this.toAdd.getRawIds());
			Config.INSTANCE.onTweak(i);
		}
	}

}
