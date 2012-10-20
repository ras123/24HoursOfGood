package com.timeline.project.server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.util.ajax.JSON;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@SuppressWarnings("serial")
public class StoreEvent extends HttpServlet {
	
	private static int eventIdCounter = 0;
	
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
        String colourCode = req.getParameter("colourCode");
        String notes = req.getParameter("notes");
        String postSecondaryName = req.getParameter("postSecondaryName");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
       
        try {
        	startDate = (Date) formatter.parse(startDateStr);
        	endDate = (Date) formatter.parse(endDateStr);
        } catch (Exception e) {
        	System.out.println("Could not parse Dates "+e);

        }
        
        // Create an entity to store event properties.
		Entity entity = new Entity("Event");
		entity.setProperty("user", user);
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

		// Put the entity in the data store.
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);

		// Notify the client of success.
		resp.setContentType("text/plain");
		Gson gson = new Gson();
		String json = gson.toJson(entity);
		resp.getWriter().println("Accepted POST " +json);
		
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // Get event id from request
        String eventKeyStr = req.getParameter("eventKey");
        
    	Key eventKey = KeyFactory.stringToKey(eventKeyStr);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        datastore.delete(eventKey);
        
        resp.setContentType("text/plain");
	}
	
	public String doGetEventById(HttpServletRequest req, HttpServletResponse resp) {
        // Get event id from request
        String eventKeyStr = req.getParameter("eventKey");
        
    	Key eventKey = KeyFactory.stringToKey(eventKeyStr);
        
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
	
	public String doGetEventsByDate(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        /*
        // Get event id from request
        String month = req.getParameter("month");
        String year = req.getParameter("year");
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        try {
			Entity event = datastore.get(eventKey);
			resp.setContentType("text/plain");
	        
			String json = JSON.toString(event);
			System.out.println("Event Json: "+json);
	        return json;
		} catch (EntityNotFoundException e) {
			System.out.println("Event not found: "+e);
		}*/
        return null;
	}
}
