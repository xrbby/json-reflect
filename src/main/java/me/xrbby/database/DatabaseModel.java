package me.xrbby.database;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import me.xrbby.reflection.ReflectiveType;
import me.xrbby.utils.JsonDeserializer;
import me.xrbby.utils.JsonSerializer;
import me.xrbby.utils.SerializationException;
import me.xrbby.utils.SerializationMode;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;
import java.util.Set;

public interface DatabaseModel extends ReflectiveType {

	ModelKey getModelKey();
	MongoCollection<Document> getCollection();

	private Bson getFilter() { return Filters.eq(getModelKey().keyName(), getModelKey().keyValue()); }

	default void save(DatabaseOperationType operationType) throws Exception {

		ModelKey modelKey = getModelKey();
		MongoCollection<Document> collection = getCollection();

		JsonObject jsonObject = JsonSerializer.createBranch(this, true, SerializationMode.DATABASE);

		Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
		Map.Entry<String, JsonElement> entry = entries.iterator().next();

		JsonObject jsonModel = entry.getValue().getAsJsonObject();

		jsonModel.addProperty(modelKey.keyName(), modelKey.keyValue());

		String jsonString = jsonModel.toString();

		Document document = Document.parse(jsonString);
		Bson filter = getFilter();

		switch(operationType) {
			case CREATE -> collection.insertOne(document);
			case UPDATE -> collection.replaceOne(filter, document);
			case REMOVE -> collection.deleteOne(filter);
		}
	}

	static DatabaseModel reflect(Class<? extends DatabaseModel> type, MongoCollection<Document> collection, Bson filter) throws Exception {

		if(collection.countDocuments(filter) > 1)
			throw new SerializationException("You need to pass a filter that contains an unique key");

		Document document = collection.find(filter).first();

		if(document == null)
			return null;

		String jsonString = document.toJson();

		Gson gson = new Gson();

		BsonDocument bsonDocument = filter.toBsonDocument();
		Set<Map.Entry<String, BsonValue>> entries = bsonDocument.entrySet();
		Map.Entry<String, BsonValue> entry = entries.iterator().next();
		String key = entry.getValue().asString().getValue();

		JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

		return (DatabaseModel) JsonDeserializer.deserialize(type, key, jsonObject, SerializationMode.DATABASE);
	}
}