package me.xrbby.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {

	private String url;
	private String databaseName;
	private DatabaseCredentials databaseCredentials;

	private MongoDatabase mongoDatabase;

	public DatabaseManager(String url, String databaseName) {

		this.url = url;
		this.databaseName = databaseName;
	}

	public DatabaseManager(DatabaseCredentials databaseCredentials) { this.databaseCredentials = databaseCredentials; }

	public void connect(Runnable runnable) {

		String url;

		if(this.url == null) {
			String username = databaseCredentials.username();
			String password = databaseCredentials.password();
			String server = databaseCredentials.server();

			url = String.format("mongodb+srv://%1$s:%2$s@%3$s/?retryWrites=true&w=majority", username, password, server);
		} else url = this.url;

		MongoClient mongoClient = MongoClients.create(url);

		String databaseName;

		if(this.databaseName == null)
			databaseName = databaseCredentials.database();
		else databaseName = this.databaseName;

		mongoDatabase = mongoClient.getDatabase(databaseName);

		runnable.run();
	}

	public MongoDatabase getMongoDatabase() { return mongoDatabase; }
}