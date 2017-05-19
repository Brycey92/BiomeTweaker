package me.superckl.biometweaker.util;

import com.google.gson.JsonObject;

import net.minecraft.world.DimensionType;

public class DimensionHelper {

	public static JsonObject populateObject(final DimensionType type){
		final JsonObject obj = new JsonObject();
		obj.addProperty("Name", type.getName());
		obj.addProperty("Suffix", type.getSuffix());
		obj.addProperty("ID", type.getId());
		return obj;
	}

}
