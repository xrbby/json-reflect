package me.xrbby.reflection;

import me.xrbby.reflection.components.ReflectionField;
import me.xrbby.reflection.components.ReflectionParameterizedField;
import me.xrbby.utils.SerializationMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReflectionMapper {

	private static final HashMap<Class<?>, ReflectionMapper> mappers = new HashMap<>();

	private final Field[] fields;
	private final SerializationMode serializationMode;

	private ReflectionField reflectionKey;
	private final ReflectiveField[] reflectiveFields;

	public ReflectionMapper(Class<?> clazz,
	                        SerializationMode serializationMode,
	                        boolean keyCheck) throws ReflectionException {

		this.fields = clazz.getDeclaredFields();
		this.serializationMode = serializationMode;

		if(!mappers.containsKey(clazz)) {
			if(keyCheck)
				this.reflectionKey = generateReflectionKey();
			this.reflectiveFields = generateReflectiveFields();

			mappers.put(clazz, this);
		} else {
			ReflectionMapper mapper = mappers.get(clazz);

			this.reflectionKey = mapper.getReflectionKey();
			this.reflectiveFields = mapper.getReflectiveFields();
		}
	}

	public ReflectionField getReflectionKey() { return reflectionKey; }
	public ReflectiveField[] getReflectiveFields() { return reflectiveFields; }

	public Constructor<?> getReflectiveConstructor(Constructor<?>[] constructors) throws ReflectionException {

		Constructor<?> cnstr = null;

		for(Constructor<?> constructor : constructors) {
			constructor.setAccessible(true);

			if(constructor.isAnnotationPresent(ReflectiveConstructor.class))
				if(cnstr == null) cnstr = constructor;
				else throw new ReflectionException("There is more than one constructor with the ReflectionConstructor annotation: " + constructor);
		}

		if(cnstr == null)
			throw new ReflectionException("There is no constructor with the ReflectionConstructor annotation");

		if(cnstr.getParameterCount() != 0)
			throw new ReflectionException("The reflection constructor must not have parameters: " + cnstr);

		return cnstr;
	}

	private ReflectionField generateReflectionKey() throws ReflectionException {

		Field field = getKeyField();

		Type type = field.getGenericType();

		if(type instanceof ParameterizedType)
			throw new ReflectionException("A key cannot be represented as a parameterized field");

		return new ReflectionField(field, serializationMode);
	}

	private ReflectiveField[] generateReflectiveFields() throws ReflectionException {

		List<ReflectiveField> reflectiveFields = new ArrayList<>();

		for(Field field : getAnnotatedFields()) {
			Type type = field.getGenericType();

			if(type instanceof ParameterizedType)
				reflectiveFields.add(new ReflectionParameterizedField(field, serializationMode));
			else
				reflectiveFields.add(new ReflectionField(field, serializationMode));
		}

		return reflectiveFields.toArray(new ReflectiveField[0]);
	}

	private Field getKeyField() throws ReflectionException {

		Field key = null;

		for(Field field : fields) {
			field.setAccessible(true);

			if(field.isAnnotationPresent(serializationMode.getKeyAnnotation()))
				if(key == null) key = field;
				else
					throw new ReflectionException("Only one field can be annotated with the SerializationKey annotation");
		}

		if(key == null)
			throw new ReflectionException("The serialization process requires a serialization key");

		return key;
	}

	private Field[] getAnnotatedFields() throws ReflectionException {

		List<Field> annotatedFields = new ArrayList<>();

		for(Field field : fields) {
			field.setAccessible(true);

			if(field.isAnnotationPresent(serializationMode.getFieldAnnotation()))
				annotatedFields.add(field);
		}

		if(annotatedFields.isEmpty())
			throw new ReflectionException("No serialization annotation is present on any of the fields");

		return annotatedFields.toArray(new Field[0]);
	}
}