package me.xrbby.reflection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.HandlerRegistry;
import me.xrbby.handlers.types.UUIDHandler;
import me.xrbby.reflection.components.ReflectionParameterizedType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ReflectionUtils {

	private static final List<Predicate<Class<?>>> typeCheckers = new ArrayList<>();

	static {

		HandlerRegistry.registerTypeCheckers();
		try { HandlerRegistry.registerCustomHandler(new UUIDHandler(), UUID.class); }
		catch(HandlerException ignored) { }
	}

	public static List<Predicate<Class<?>>> getTypeCheckers() { return typeCheckers; }

	public static Class<?> isTypeValid(Class<?> type) throws ReflectionException {

		boolean doesTypeExist = typeCheckers.stream().anyMatch(checker -> checker.test(type));

		if(!doesTypeExist)
			throw new ReflectionException("Unsupported data type: " + type.getName());

		return type;
	}

	public static JsonPrimitive isKeyValid(JsonElement jsonElement) throws ReflectionException {

		if(!jsonElement.isJsonPrimitive())
			throw new ReflectionException("The key representation is not of primitive type: " + jsonElement);

		return (JsonPrimitive) jsonElement;
	}

	public static Object isKeyValid(ReflectiveComponent reflectiveComponent, String key) throws ReflectionException {

		JsonElement jsonElement = new JsonPrimitive(key);

		Object value = null;

		try {
			if(reflectiveComponent instanceof ReflectionParameterizedType reflectionParameterizedType)
				value = reflectionParameterizedType.resolveValue(jsonElement);
			else if(reflectiveComponent instanceof ReflectiveField reflectiveField)
				reflectiveField.resolveValue(jsonElement);
		} catch(Exception ignored) { throw new ReflectionException("The key representation is not of primitive type: " + reflectiveComponent.getType().getName()); }

		return value;
	}

	public static JsonObject wrapAsJsonObject(Field field, JsonElement jsonElement) {

		JsonObject jsonObject = new JsonObject();

		jsonObject.add(field.getName(), jsonElement);

		return jsonObject;
	}
}