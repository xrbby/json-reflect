package me.xrbby.handlers.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectionUtils;
import me.xrbby.reflection.ReflectiveComponent;
import me.xrbby.reflection.components.ReflectionParameterizedField;
import me.xrbby.reflection.components.ReflectionParameterizedType;

import java.util.HashMap;
import java.util.Map;

public class HashMapHandler implements TypeHandler {

	@Override
	public JsonObject serialize(ReflectiveComponent reflectiveComponent, Object value) throws Exception {

		ReflectionParameterizedField parameterizedField = (ReflectionParameterizedField) reflectiveComponent;

		JsonObject hashMap = new JsonObject();

		ReflectionParameterizedType[] rpt = parameterizedField.getReflectionParameterizedTypes();

		ReflectionParameterizedType rptKey = rpt[0];
		ReflectionParameterizedType rptValue = rpt[1];

		for(Map.Entry<Object, Object> entry : ((HashMap<Object, Object>) value).entrySet()) {
			JsonElement keyValue = ReflectionUtils.isKeyValid(rptKey.serializeValue(entry.getKey()));
			JsonElement objectValue = rptValue.serializeValue(entry.getValue());

			hashMap.add(keyValue.getAsString(), objectValue);
		}

		return hashMap;
	}

	@Override
	public HashMap<Object, Object> deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws Exception {

		if(jsonElement.isJsonObject()) {
			ReflectionParameterizedField parameterizedField = (ReflectionParameterizedField) reflectiveComponent;

			HashMap<Object, Object> hashMap = new HashMap<>();

			ReflectionParameterizedType[] rpt = parameterizedField.getReflectionParameterizedTypes();

			ReflectionParameterizedType rptKey = rpt[0];
			ReflectionParameterizedType rptValue = rpt[1];

			JsonObject jsonObject = jsonElement.getAsJsonObject();

			for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				Object keyValue = ReflectionUtils.isKeyValid(rptKey, entry.getKey());
				Object objectValue = rptValue.resolveValue(entry.getValue());

				hashMap.put(keyValue, objectValue);
			}

			return hashMap;
		} else throw new HandlerException("Invalid JSON element for HashMap: " + jsonElement);
	}
}