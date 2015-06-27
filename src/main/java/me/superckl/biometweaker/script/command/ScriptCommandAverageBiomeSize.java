package me.superckl.biometweaker.script.command;

import me.superckl.biometweaker.common.handler.BiomeEventHandler;
import me.superckl.biometweaker.util.LogHelper;
import net.minecraft.world.WorldType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptCommandAverageBiomeSize implements IScriptCommand{
	
	private final String type;
	private final byte size;
	
	public ScriptCommandAverageBiomeSize(byte size) {
		this(null, size);
	}
	
	@Override
	public void perform() throws Exception {
		if(this.type == null){
			BiomeEventHandler.globalSize = this.size;
			return;
		}
		WorldType type = null;
		for(WorldType worldType:WorldType.worldTypes)
			if(worldType.getWorldTypeName().equals(this.type)){
				type = worldType;
				break;
			}
		if(type == null){
			LogHelper.warn("Failed to retrieve WorldType for '"+this.type+"'!");
			return;
		}
		BiomeEventHandler.sizes.put(type, this.size);
	}

}
