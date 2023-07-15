package me.xrbby.reflection.components;

import me.xrbby.reflection.ReflectionException;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveField;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.utils.SerializationMode;

import java.lang.reflect.Field;

public class ReflectionField implements ReflectiveField {

	private final Field field;
	private final Class<?> type;
	private final SerializationMode serializationMode;

	private ReflectiveType instance;

	public ReflectionField(Field field, SerializationMode serializationMode) throws ReflectionException {

		this.field = field;
		this.type = ReflectionUtils.isTypeValid(field.getType());
		this.serializationMode = serializationMode;
	}

	@Override
	public Field getField() { return field; }

	@Override
	public Class<?> getType() { return type; }

	@Override
	public SerializationMode getSerializationMode() { return serializationMode; }

	@Override
	public void setInstance(ReflectiveType instance) { this.instance = instance; }

	@Override
	public ReflectiveType getInstance() { return instance; }
}