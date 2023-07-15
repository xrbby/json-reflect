package me.xrbby.utils;

import me.xrbby.database.DatabaseSerialization;
import me.xrbby.database.DatabaseSerializationKey;
import me.xrbby.file.FileSerialization;
import me.xrbby.file.FileSerializationKey;

import java.lang.annotation.Annotation;

public enum SerializationMode {

	DATABASE(DatabaseSerializationKey.class, DatabaseSerialization.class),
	FILE(FileSerializationKey.class, FileSerialization.class);

	private final Class<? extends Annotation> keyAnnotation;
	private final Class<? extends Annotation> fieldAnnotation;

	SerializationMode(Class<? extends Annotation> keyAnnotation, Class<? extends Annotation> fieldAnnotation) {

		this.keyAnnotation = keyAnnotation;
		this.fieldAnnotation = fieldAnnotation;
	}

	public Class<? extends Annotation> getKeyAnnotation() { return keyAnnotation; }
	public Class<? extends Annotation> getFieldAnnotation() { return fieldAnnotation; }
}