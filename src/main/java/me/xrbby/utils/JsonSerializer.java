package me.xrbby.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.xrbby.reflection.ReflectionMapper;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveField;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.reflection.components.ReflectionField;

public class JsonSerializer {

	public static JsonObject createBranch(ReflectiveType instance, boolean keyCheck, SerializationMode serializationMode) throws Exception {

		Class<?> clazz = instance.getClass();

		ReflectionMapper reflectionMapper = new ReflectionMapper(clazz, serializationMode, keyCheck);

		JsonObject jsonObject = new JsonObject();

		JsonObject reflectiveType = new JsonObject();

		if(keyCheck) {
			ReflectionField reflectionKey = reflectionMapper.getReflectionKey();

			reflectionKey.setInstance(instance);

			JsonObject keyObject = new JsonObject();

			JsonPrimitive jsonPrimitive = ReflectionUtils.isKeyValid(reflectionKey.serializeValue(keyObject));

			jsonObject.add(jsonPrimitive.getAsString(), reflectiveType);
		}

		for(ReflectiveField reflectiveField : reflectionMapper.getReflectiveFields()) {
			reflectiveField.setInstance(instance);

			reflectiveField.serializeValue(reflectiveType);
		}

		if(keyCheck) return jsonObject;
		else return reflectiveType;
	}
}