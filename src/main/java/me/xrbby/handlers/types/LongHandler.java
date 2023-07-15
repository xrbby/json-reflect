package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectiveComponent;

public class LongHandler implements TypeHandler {

	@Override
	public JsonPrimitive serialize(ReflectiveComponent reflectiveComponent, Object value) {

		return new JsonPrimitive(Long.parseLong(value.toString()));
	}

	@Override
	public Long deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws HandlerException {

		if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
			return jsonElement.getAsLong();
		else throw new HandlerException("Invalid JSON element for Long: " + jsonElement);
	}
}