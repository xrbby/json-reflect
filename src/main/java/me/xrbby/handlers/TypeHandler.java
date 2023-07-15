package me.xrbby.handlers;

import com.google.gson.JsonElement;
import me.xrbby.reflection.ReflectiveComponent;

public interface TypeHandler {

	JsonElement serialize(ReflectiveComponent reflectiveComponent, Object value) throws Exception;
	Object deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws Exception;
}