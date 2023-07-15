package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectiveComponent;

import java.util.UUID;

public class UUIDHandler implements TypeHandler {

	@Override
	public JsonPrimitive serialize(ReflectiveComponent reflectiveComponent, Object value) {

		return new JsonPrimitive(value.toString());
	}

	@Override
	public UUID deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws HandlerException {

		if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString())
			return UUID.fromString(jsonElement.getAsString());
		else throw new HandlerException("Invalid JSON element for UUID: " + jsonElement);
	}
}