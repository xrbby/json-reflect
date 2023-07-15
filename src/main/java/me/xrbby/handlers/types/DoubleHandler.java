package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectiveComponent;

public class DoubleHandler implements TypeHandler {

	@Override
	public JsonPrimitive serialize(ReflectiveComponent reflectiveComponent, Object value) {

		return new JsonPrimitive(Double.parseDouble(value.toString()));
	}

	@Override
	public Double deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws HandlerException {

		if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
			return jsonElement.getAsDouble();
		else throw new HandlerException("Invalid JSON element for Double: " + jsonElement);
	}
}