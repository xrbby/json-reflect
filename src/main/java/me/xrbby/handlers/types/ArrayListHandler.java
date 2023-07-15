package me.xrbby.handlers.types;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import me.xrbby.handlers.HandlerException;
import me.xrbby.handlers.TypeHandler;
import me.xrbby.reflection.ReflectiveComponent;
import me.xrbby.reflection.components.ReflectionParameterizedField;
import me.xrbby.reflection.components.ReflectionParameterizedType;

import java.util.ArrayList;

public class ArrayListHandler implements TypeHandler {

	@Override
	public JsonArray serialize(ReflectiveComponent reflectiveComponent, Object value) throws Exception {

		ReflectionParameterizedField parameterizedField = (ReflectionParameterizedField) reflectiveComponent;

		JsonArray arrayList = new JsonArray();

		ReflectionParameterizedType rpt = parameterizedField.getReflectionParameterizedTypes()[0];

		for(Object object : (ArrayList<Object>) value)
			arrayList.add(rpt.serializeValue(object));

		return arrayList;
	}

	@Override
	public ArrayList<Object> deserialize(ReflectiveComponent reflectiveComponent, JsonElement jsonElement) throws Exception {

		if(jsonElement.isJsonArray()) {
			ReflectionParameterizedField parameterizedField = (ReflectionParameterizedField) reflectiveComponent;

			ArrayList<Object> arrayList = new ArrayList<>();

			ReflectionParameterizedType rpt = parameterizedField.getReflectionParameterizedTypes()[0];

			JsonArray jsonArray = jsonElement.getAsJsonArray();

			for(JsonElement element : jsonArray)
				arrayList.add(rpt.resolveValue(element));

			return arrayList;
		} else throw new HandlerException("Invalid JSON element for ArrayList: " + jsonElement);
	}
}