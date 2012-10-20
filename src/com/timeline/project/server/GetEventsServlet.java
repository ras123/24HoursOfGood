package com.timeline.project.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GetEventsServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();		
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		
		String dateStr = req.getParameter("date");
		String monthBool = req.getParameter("getMonth");
		String eventId = req.getParameter("id");
		
		String json;
		if (eventId != null) {
			json = this.getEventById(eventId);
		} else if (dateStr != null) {
			json = this.getEventsByDate(dateStr, monthBool);
		} else {
			json = this.getAllEvents();
		}
		
		try {
			resp.setContentType("application/json");
			resp.getWriter().println(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getEventsByDate(String dateStr, String monthBool) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
        
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        
        List<Entity> events = new ArrayList<Entity>();
        
        try {
         	date = (Date) formatter.parse(dateStr);
         } catch (Exception e) {
         	System.out.println("Could not parse Date, "+date+", "+e);
         	return null;
         }
         
         Calendar fromDate = Calendar.getInstance();
         fromDate.setTime(date);
         
         DatastoreService datastore = 
         		DatastoreServiceFactory.getDatastoreService();
         
         Filter userFilter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
         Query q = new Query("Event").setFilter(userFilter);
         PreparedQuery pq = datastore.prepare(q);
         
        if (monthBool != null) {
        	
             for (Entity event : pq.asIterable()) {
             	Date startDate = (Date) event.getProperty("startDate");
             	Calendar toDate = Calendar.getInstance();
             	toDate.setTime(startDate);
             	
             	if (!toDate.before(fromDate)) {
         			events.add(event);
             	}
             }
        } else {
            int fromMonth = fromDate.get(Calendar.MONTH);
            int fromYear = fromDate.get(Calendar.YEAR);
            
            for (Entity event : pq.asIterable()) {
            	Date startDate = (Date) event.getProperty("startDate");
            	Calendar toDate = Calendar.getInstance();
            	toDate.setTime(startDate);
            	int toMonth = toDate.get(Calendar.MONTH);
            	int toYear = toDate.get(Calendar.YEAR);
            	
            	if (fromMonth == toMonth && fromYear == toYear) {
            		events.add(event);
            	}
            }
        }
        
        Gson gson = new Gson();
		String eventsJson = gson.toJson(events);
        return eventsJson;
	}
	
	private String getEventById(String eventId) {
        // Get event id from request
    	Key eventKey = KeyFactory.createKey("Event", eventId);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        try {
			Entity event = datastore.get(eventKey);
	        
			Gson gson = new Gson();
			String json = gson.toJson(event);
			System.out.println("Event Json: "+json);
	        return json;
		} catch (EntityNotFoundException e) {
			System.out.println("Event not found: "+e);
		}
        return null;
	}
	
	
	
	private String getAllEvents()
	{
		DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
        String userId = user.getUserId();
        
		Query q = new Query("Event");
		PreparedQuery pq = datastore.prepare(q);
		
		List<Entity> list = new ArrayList<Entity>();
		
		for (Entity result : pq.asIterable()) {
			String entry = (String) result.getProperty("userId");
			
			if(userId.equals(entry))
			{
				list.add(result);
			}			
		}
		
		Gson gson = new Gson();
		String eventsJson = gson.toJson(list.toString());
		return eventsJson;
	}
}
