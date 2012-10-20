package com.timeline.project.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@SuppressWarnings("serial")
public class DeleteEventServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
        
        String eventKeyStr = req.getParameter("key");
        
        if (eventKeyStr != null) {
        	System.out.println("Deleting id " + eventKeyStr);
        	Key eventKey = KeyFactory.stringToKey(eventKeyStr);
        	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        	
        	Entity event = null;
        	try {
				event = datastore.get(eventKey);
			} catch (EntityNotFoundException e) {
				System.out.println("Event does not exist. Not deleted.");
				return;
			}
        	
        	String uId = (String) event.getProperty("userId");
        	if (uId == userId) {
        		datastore.delete(eventKey);        		
        	} else {
        		System.out.println("Current user does not match requested user event. Not deleted.");
        	}
        }
        else
        {
        	System.out.println("Id " + eventKeyStr + " could not be found");
        	eventKeyStr = "-1";
        }
        
        resp.setContentType("application/json");
    	Gson gson = new Gson();
		String json = gson.toJson(eventKeyStr);
		resp.getWriter().println(json);
	}
	
}
