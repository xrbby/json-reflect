package me.xrbby.handlers;

import me.xrbby.handlers.types.*;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class HandlerRegistry {

	private static final Map<TypeHandler, Predicate<Class<?>>> handlers = new HashMap<>();

	private static void registerHandlers() {

		handlers.put(new ByteHandler(), type -> type.equals(byte.class) || type.equals(Byte.class));
		handlers.put(new ShortHandler(), type -> type.equals(short.class) || type.equals(Short.class));
		handlers.put(new IntegerHandler(), type -> type.equals(int.class) || type.equals(Integer.class));
		handlers.put(new LongHandler(), type -> type.equals(long.class) || type.equals(Long.class));
		handlers.put(new FloatHandler(), type-> type.equals(float.class) || type.equals(Float.class));
		handlers.put(new DoubleHandler(), type -> type.equals(double.class) || type.equals(Double.class));
		handlers.put(new BooleanHandler(), type -> type.equals(boolean.class) || type.equals(Boolean.class));
		handlers.put(new StringHandler(), type -> type.equals(String.class));
		handlers.put(new ReflectiveTypeHandler(), ReflectiveType.class::isAssignableFrom);
		handlers.put(new ArrayListHandler(), type -> type.equals(ArrayList.class));
		handlers.put(new HashMapHandler(), type -> type.equals(HashMap.class));
		handlers.put(new EnumHandler(), Class::isEnum);
	}

	static { registerHandlers(); }

	public static void registerCustomHandler(TypeHandler typeHandler, Class<?> type) throws HandlerException {

		List<Predicate<Class<?>>> typeCheckers = ReflectionUtils.getTypeCheckers();

		boolean hasMatchingPredicate = typeCheckers.stream().anyMatch(checker -> checker.test(type));

		if(hasMatchingPredicate)
			throw new HandlerException("Type " + type.getName() + " has already been declared");

		Predicate<Class<?>> condition = checker -> checker.equals(type);

		typeCheckers.add(condition);
		handlers.put(typeHandler, condition);
	}

	public static void registerTypeCheckers() {

		List<Predicate<Class<?>>> typeCheckers = ReflectionUtils.getTypeCheckers();

		typeCheckers.addAll(handlers.values());
	}

	public static TypeHandler getHandler(Class<?> type) throws HandlerException {

		for(Map.Entry<TypeHandler, Predicate<Class<?>>> entry : handlers.entrySet())
			if(entry.getValue().test(type))
				return entry.getKey();

		throw new HandlerException("Could not resolve the handler for the type: " + type.getName());
	}
}