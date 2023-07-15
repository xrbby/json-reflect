package me.xrbby.reflection.components;

import com.google.gson.JsonElement;
import me.xrbby.handlers.HandlerRegistry;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectionException;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveComponent;
import me.xrbby.utils.SerializationMode;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class ReflectionParameterizedType implements ReflectiveComponent {

	private final Field parentField;
	private final Class<?> type;
	private final SerializationMode serializationMode;

	public ReflectionParameterizedType(Field field, Type type, SerializationMode serializationMode) throws ReflectionException {

		this.parentField = field;

		if(!(type instanceof Class<?> classType))
			throw new ReflectionException("Invalid parameterized data type: " + type.getTypeName());

		this.type = ReflectionUtils.isTypeValid(classType);
		this.serializationMode = serializationMode;
	}

	@Override
	public Field getField() { return parentField; }

	@Override
	public Class<?> getType() { return type; }

	@Override
	public SerializationMode getSerializationMode() { return serializationMode; }

	public JsonElement serializeValue(Object value) throws Exception {

		TypeHandler handler = HandlerRegistry.getHandler(type);

		return handler.serialize(this, value);
	}

	public Object resolveValue(JsonElement jsonElement) throws Exception {

		TypeHandler handler = HandlerRegistry.getHandler(type);

		return handler.deserialize(this, jsonElement);
	}
}