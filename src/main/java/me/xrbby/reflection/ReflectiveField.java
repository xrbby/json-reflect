package me.xrbby.reflection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.xrbby.handlers.HandlerRegistry;
import me.xrbby.handlers.TypeHandler;

import java.lang.reflect.Field;

public interface ReflectiveField extends ReflectiveComponent {

	void setInstance(ReflectiveType instance);
	ReflectiveType getInstance();

	default JsonElement serializeValue(JsonObject jsonObject) throws Exception {

		ReflectiveType instance = getInstance();
		Field field = getField();
		Class<?> type = getType();

		if(instance == null)
			throw new ReflectionException("The instance is not set");

		TypeHandler handler = HandlerRegistry.getHandler(type);

		Object value = field.get(instance);

		JsonElement jsonElement = handler.serialize(this, value);

		jsonObject.add(field.getName(), jsonElement);

		return jsonElement;
	}

	default void resolveValue(JsonElement jsonElement) throws Exception {

		ReflectiveType instance = getInstance();
		Field field = getField();
		Class<?> type = getType();

		if(instance == null)
			throw new ReflectionException("The instance is not set");

		TypeHandler handler = HandlerRegistry.getHandler(type);

		Object value = handler.deserialize(this, jsonElement);

		field.set(instance, value);
	}
}