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
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;


@SuppressWarnings("serial")
public class StoreEvent extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
        
        // Get event properties from event request
        String title = req.getParameter("title");
        String eventId = req.getParameter("id");
        String colourCode = req.getParameter("colourCode");
        String notes = req.getParameter("notes");
        String postSecondaryName = req.getParameter("postSecondaryName");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
       
        try {
        	System.out.println("start "+startDateStr+ " end "+endDateStr);
        	startDate = (Date) formatter.parse(startDateStr);
        	endDate = (Date) formatter.parse(endDateStr);
        } catch (Exception e) {
        	System.out.println("Could not parse Dates "+e);
        }

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity("Event");
        if (eventId != null) {
        	Key eventKey = KeyFactory.createKey("Event", eventId);
        	
        	try {
				entity = datastore.get(eventKey);
			} catch (EntityNotFoundException e) {
				System.out.println("Event not found: "+e);
				return;
			}
        }
        
        // Create an entity to store event properties.
		entity.setProperty("userId", userId);
		entity.setProperty("title", title);
		entity.setProperty("colourCode", colourCode);
		entity.setProperty("notes", notes);
		entity.setProperty("postSecondaryName", postSecondaryName);
		entity.setProperty("startDate", startDate);
		if (endDateStr == null) {
			entity.setProperty("eventType", "PointEvent");
		} else {
			entity.setProperty("eventType", "DurationEvent");
			entity.setProperty("endDate", endDate);
		}
		
		System.out.println("entity persisting: "+entity.getKey());

		// Put the entity in the data store.
		datastore.put(entity);
		
		System.out.println("entity persisted: "+entity.getKey());

		// Notify the client of success.
		resp.setContentType("application/json");
		Gson gson = new Gson();
		String json = gson.toJson(entity);
		resp.getWriter().println(json);
		
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // Get event id from request
        String eventId = req.getParameter("id");
        
    	Key eventKey = KeyFactory.createKey("Event", eventId);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        datastore.delete(eventKey);
        
        resp.setContentType("text/plain");
	}
	
	public String doGetEventById(HttpServletRequest req, HttpServletResponse resp) {
        // Get event id from request
        String eventId = req.getParameter("id");
        
    	Key eventKey = KeyFactory.createKey("Event", eventId);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        try {
			Entity event = datastore.get(eventKey);
			resp.setContentType("text/plain");
	        
			Gson gson = new Gson();
			String json = gson.toJson(event);
			System.out.println("Event Json: "+json);
	        return json;
		} catch (EntityNotFoundException e) {
			System.out.println("Event not found: "+e);
		}
        return null;
	}
	
	public List<String> doGetEventsByDate(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
        
     // Get event id from request
        String dateStr = req.getParameter("date");
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
       
        List<String> eventsJson = new ArrayList<String>();
        try {
        	date = (Date) formatter.parse(dateStr);
        } catch (Exception e) {
        	System.out.println("Could not parse Date, "+date+", "+e);
        	return eventsJson;
        }
        
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTime(date);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        
        Filter userFilter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
        Query q = new Query("Event").setFilter(userFilter);
        PreparedQuery pq = datastore.prepare(q);
        
        for (Entity event : pq.asIterable()) {
        	Date startDate = (Date) event.getProperty("startDate");
        	Calendar toDate = Calendar.getInstance();
        	toDate.setTime(startDate);
        	
        	if (!toDate.before(fromDate)) {
        		Gson gson = new Gson();
    			String json = gson.toJson(event);
    			eventsJson.add(json);
        	}
        }
        
        return eventsJson;
	}
	
	public void doGetAllEvents(HttpServletRequest req, HttpServletResponse resp)
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
		
		// Notify the client of success.
		resp.setContentType("text/plain");
		Gson gson = new Gson();
		String json = gson.toJson(list.toString());
		try {
			resp.getWriter().println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
