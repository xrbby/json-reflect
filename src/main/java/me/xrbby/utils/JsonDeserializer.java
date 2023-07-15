package me.xrbby.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.xrbby.reflection.*;
import me.xrbby.reflection.components.ReflectionField;

import java.lang.reflect.Constructor;
import java.util.Map;

public class JsonDeserializer {

	public static ReflectiveType readBranch(Class<?> clazz, JsonObject jsonObject, boolean keyCheck, SerializationMode serializationMode) throws Exception {

		Constructor<?>[] constructors = clazz.getDeclaredConstructors();

		ReflectionMapper reflectionMapper = new ReflectionMapper(clazz, serializationMode, keyCheck);

		Constructor<?> constructor = reflectionMapper.getReflectiveConstructor(constructors);

		for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			ReflectiveType instance = (ReflectiveType) constructor.newInstance();

			if(keyCheck) {
				ReflectionField reflectionKey = reflectionMapper.getReflectionKey();

				reflectionKey.setInstance(instance);

				ReflectionUtils.isKeyValid(reflectionKey, entry.getKey());
			}

			JsonObject jsonElement = entry.getValue().getAsJsonObject();

			for(ReflectiveField reflectiveField : reflectionMapper.getReflectiveFields()) {
				reflectiveField.setInstance(instance);

				JsonElement jsonValue = jsonElement.get(reflectiveField.getField().getName());

				if(jsonValue == null)
					throw new SerializationException("File format doesn't match with the format of the class");

				reflectiveField.resolveValue(jsonValue);
			}

			instance.init();

			return instance;
		}

		return null;
	}

	public static ReflectiveType deserialize(Class<?> clazz, String key, JsonElement jsonElement, SerializationMode serializationMode) throws Exception {

		JsonObject jsonObject = new JsonObject();

		jsonObject.add(key, jsonElement);

		return readBranch(clazz, jsonObject, true, serializationMode);
	}
}