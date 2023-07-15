package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectiveComponent;

public class EnumHandler implements TypeHandler {

	@Override
	public JsonPrimitive serialize(ReflectiveComponent reflectiveComponent, Object value) {

		return new JsonPrimitive(value.toString());
	}

	@Override
	public Enum<?> deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws HandlerException {

		if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) {
			String value = jsonElement.getAsString();

			Class<?> enumClass = reflectiveComponent.getType();

			return Enum.valueOf(enumClass.asSubclass(Enum.class), value);
		} else throw new HandlerException("Invalid JSON element for Enum: " + jsonElement);
	}
}