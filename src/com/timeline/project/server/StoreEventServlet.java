package com.timeline.project.server;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;


@SuppressWarnings("serial")
public class StoreEventServlet extends HttpServlet {
	
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
        String eventKeyStr = req.getParameter("key");
        String colourCode = req.getParameter("colourCode");
        String notes = req.getParameter("notes");
        String postSecondaryName = req.getParameter("postSecondaryName");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        
        boolean completeForm = true;
        
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        
        if(title == null || title == ""){
        	System.out.println("Missing a title, please give this event a title.");
    		completeForm = false;
        }
        
        if (startDateStr == null && endDateStr == null) {
        	System.out.println("Event not created, missing both start and end date.");
        	completeForm = false;
        }
        
        //System.out.println("start "+startDateStr+ " end "+endDateStr);
        Date startDate = null;
        Date endDate = null;
       
        try {
        	if (startDateStr != null) startDate = (Date) formatter.parse(startDateStr);
        } catch (Exception e) {
        	System.out.println("Could not parse start date " + e);
        	startDate = null;
        	completeForm = false;
        }
        
        try {
        	if (endDateStr != null) endDate = (Date) formatter.parse(endDateStr);
        } catch (Exception e) {
        	System.out.println("Could not parse end date " + e);
        	endDate = null;
        	completeForm = false;
        }
        
        int dateDifference = (int)( (endDate.getTime() - startDate.getTime())/1000);
       
        if ((dateDifference < 0) )//|| (endDate.getTime()/(1000*60*60) > 24) || (startDate.getTime()/(1000*60*60) > 24))
        {
        	completeForm = false;
        	System.out.println(dateDifference+" date difference");
        }
        
        if ((endDate.getTime()/(1000*60*60) < 0) || (startDate.getTime()/(1000*60*60) < 0))
        {
        	completeForm = false;
        	System.out.println("neg value");
        }
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
     // Create an entity to store event properties.
        Entity entity = new Entity("Event");
        if (eventKeyStr != null) {
        	System.out.println(eventKeyStr);
        	
        	try {
				entity = datastore.get(KeyFactory.stringToKey(eventKeyStr));
			} catch (EntityNotFoundException e) {
				System.out.println("Event not updated, event "+eventKeyStr+" not found: "+e);
				return;
			}
        }
        if(completeForm){
	     // Set entity fields
			entity.setProperty("userId", userId);
			entity.setProperty("title", title);
			entity.setProperty("colourCode", colourCode);
			entity.setProperty("notes", notes);
			entity.setProperty("postSecondaryName", postSecondaryName);
			if (endDateStr == null) {
				entity.setProperty("eventType", "PointEvent");
				entity.setProperty("startDate", startDate);
				entity.setProperty("endDate", null);
			} else if (startDateStr == null) {
				entity.setProperty("eventType", "PointEvent");
				entity.setProperty("startDate", endDate);
				entity.setProperty("endDate", null);
			} else {
				entity.setProperty("eventType", "DurationEvent");
				entity.setProperty("startDate", startDate);
				entity.setProperty("endDate", endDate);
			}
			
			//System.out.println("entity persisting: "+entity.getKey());
	
			// Put the entity in the data store.
			datastore.put(entity);
			
			//System.out.println("entity persisted: "+entity.getKey());
	
			// Notify the client of success.
			resp.setContentType("application/json");
			Gson gson = new Gson();
			String json = gson.toJson(entity);
			resp.getWriter().println(json);
        }
	}
}
