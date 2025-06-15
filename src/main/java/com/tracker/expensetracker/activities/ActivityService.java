package com.tracker.expensetracker.activities;


import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ActivityService {

    List<Activity> getAllActivities();

    List<Activity> getActivitiesByUserId(String userId);

    Activity getActivityById(String id);

    Activity createActivity(Activity activity);

    Activity updateActivity(String id, Activity activity);

    void deleteActivity(String id);
}
