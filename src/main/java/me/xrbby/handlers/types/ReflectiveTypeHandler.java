package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveComponent;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.utils.JsonDeserializer;
import me.xrbby.utils.JsonSerializer;

public class ReflectiveTypeHandler implements TypeHandler {

	@Override
	public JsonObject serialize(ReflectiveComponent reflectiveComponent, Object value) throws Exception {

		return JsonSerializer.createBranch((ReflectiveType) value, false, reflectiveComponent.getSerializationMode());
	}

	@Override
	public ReflectiveType deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws Exception {

		if(jsonElement.isJsonObject()) {
			JsonObject wrappedJsonObject = ReflectionUtils.wrapAsJsonObject(reflectiveComponent.getField(), jsonElement.getAsJsonObject());

			return JsonDeserializer.readBranch(reflectiveComponent.getType(), wrappedJsonObject, false, reflectiveComponent.getSerializationMode());
		} else throw new HandlerException("Invalid JSON element for ReflectiveType: " + jsonElement);
	}
}