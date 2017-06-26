package me.superckl.biometweaker.util;

import com.google.gson.JsonObject;

import me.superckl.api.superscript.util.WarningHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityHelper {

	public static Class<? extends Entity> getEntityClass(final String identifier){
		Class<? extends Entity> clazz = null;
		try{
			clazz = WarningHelper.uncheckedCast(Class.forName(identifier));
			return clazz;
		}catch(final Exception e){
			return EntityList.getClassFromID(EntityList.getIDFromString(identifier));
		}

	}

	public static JsonObject populateObject(final Class<? extends Entity> entityClass){
		final JsonObject obj = new JsonObject();
		//obj.addProperty("Name", entity.getName());
		obj.addProperty("Registry ID", EntityList.getEntityStringFromClass(entityClass).toString());
		obj.addProperty("Class", entityClass.getCanonicalName());
		return obj;
	}

}
