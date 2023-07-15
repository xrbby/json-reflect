package me.xrbby.file;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileManager {

	private static final ArrayList<File> registerFiles = new ArrayList<>();
	private static final ArrayList<JsonFile> jsonFiles = new ArrayList<>();

	public static void registerFile(File file) {

		try {
			if(!file.exists())
				file.createNewFile();

			registerFiles.add(file);
		} catch(Exception exception) { exception.printStackTrace(); }
	}

	public static void load() {

		for(File file : registerFiles)
			read(file);
	}

	public static JsonFile getJsonFile(File file) {

		for(JsonFile jsonFile : jsonFiles) {
			if(jsonFile.file().getName().equals(file.getName()))
				return jsonFile;
		}

		return null;
	}

	private static void read(File file) {

		FileReader fileReader = null;

		try {
			fileReader = new FileReader(file);

			Type type = new TypeToken<JsonObject>() {
			}.getType();

			JsonObject jsonObject;

			if (file.length() == 0)
				jsonObject = new Gson().fromJson("{}", type);
			else
				jsonObject = new Gson().fromJson(fileReader, type);

			jsonFiles.add(new JsonFile(file, jsonObject));

			fileReader.close();
		} catch(Exception exception) { exception.printStackTrace(); }
		finally {
			try { fileReader.close(); }
			catch(Exception exception) { exception.printStackTrace(); }
		}
	}

	protected static void save(JsonFile jsonFile) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(jsonFile.file());

			new Gson().toJson(jsonFile.jsonObject(), fileWriter);

			fileWriter.close();
		} catch(Exception exception) { exception.printStackTrace(); }
		finally {
			try { fileWriter.close(); }
			catch(Exception exception) { exception.printStackTrace(); }
		}
	}
}