package me.xrbby.reflection;

import me.xrbby.utils.SerializationMode;

import java.lang.reflect.Field;

public interface ReflectiveComponent {

	Field getField();
	Class<?> getType();
	SerializationMode getSerializationMode();
}