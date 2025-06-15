package com.tracker.expensetracker.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class SearchRepositoryImpl implements SearchRepository {

	@Autowired
	MongoClient client;

	@Autowired
	MongoConverter converter;

	@Override
	public List<Activity> findByText(String text) {
		List<Activity> lActivities = new ArrayList<>();

		MongoDatabase database = client.getDatabase("Tracker");
		MongoCollection<Document> collection = database.getCollection("activities");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
				new Document("$search",
						new Document("text",
								new Document("query", text).append("path",
										Arrays.asList("note", "name", "category","month")))),
				new Document("$sort", new Document("date", -1L))));
		result.forEach(doc -> lActivities.add(converter.read(Activity.class, doc)));
		return lActivities;
	}

}
