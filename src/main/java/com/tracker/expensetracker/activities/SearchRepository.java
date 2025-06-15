package com.tracker.expensetracker.activities;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository {

	public List<Activity> findByText(String text);
}
