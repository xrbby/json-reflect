package me.xrbby.reflection.components;

import me.xrbby.reflection.ReflectionException;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveField;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.utils.SerializationMode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ReflectionParameterizedField implements ReflectiveField {

	private final Field field;
	private final Class<?> type;
	private final SerializationMode serializationMode;

	private ReflectiveType instance;

	private final ReflectionParameterizedType[] reflectionParameterizedTypes;

	public ReflectionParameterizedField(Field field, SerializationMode serializationMode) throws ReflectionException {

		this.field = field;
		this.type = ReflectionUtils.isTypeValid(field.getType());
		this.serializationMode = serializationMode;

		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();

		List<ReflectionParameterizedType> parameterizedTypeList = new ArrayList<>();

		Type[] parameterizedTypes = parameterizedType.getActualTypeArguments();

		for(Type type : parameterizedTypes)
			parameterizedTypeList.add(new ReflectionParameterizedType(field, type, serializationMode));

		this.reflectionParameterizedTypes = parameterizedTypeList.toArray(new ReflectionParameterizedType[0]);
	}

	public ReflectionParameterizedType[] getReflectionParameterizedTypes() { return reflectionParameterizedTypes; }

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