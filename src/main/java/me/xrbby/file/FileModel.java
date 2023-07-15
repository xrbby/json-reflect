package me.xrbby.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.utils.JsonDeserializer;
import me.xrbby.utils.JsonSerializer;
import me.xrbby.utils.SerializationMode;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface FileModel extends ReflectiveType {

	JsonFile getJsonFile();

	default void save(FileOperationType operationType) throws Exception {

		JsonFile jsonFile = getJsonFile();
		JsonObject jsonObject = jsonFile.jsonObject();

		JsonObject jsonModel = JsonSerializer.createBranch(this, true, SerializationMode.FILE);

		Set<Map.Entry<String, JsonElement>> entries = jsonModel.entrySet();
		Map.Entry<String, JsonElement> entry = entries.iterator().next();

		switch(operationType) {
			case SAVE -> jsonObject.add(entry.getKey(), entry.getValue());
			case REMOVE -> jsonObject.add(entry.getKey(), null);
		}

		jsonFile.save();
	}

	static FileModel[] reflect(Class<? extends FileModel> type, JsonObject jsonObject) throws Exception {

		ArrayList<FileModel> fileModels = new ArrayList<>();

		for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

			JsonObject elementObject = entry.getValue().getAsJsonObject();

			FileModel fileModel = (FileModel) JsonDeserializer.deserialize(type, entry.getKey(), elementObject, SerializationMode.FILE);

			fileModels.add(fileModel);
		}

		return fileModels.toArray(new FileModel[0]);
	}
}