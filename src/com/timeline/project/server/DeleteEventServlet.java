package com.timeline.project.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
        
        String eventId = req.getParameter("id");
        
        Entity entity = new Entity("Event");
        if (eventId != null) {
        	System.out.println("Deleting id " + eventId);
        	Key eventKey = KeyFactory.createKey("Event", eventId);
        	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        	
        	datastore.delete(eventKey);
        }
        else
        {
        	System.out.println("Id " + eventId + " could not be found");
        	eventId = "-1";
        }
        
        resp.setContentType("application/json");
    	Gson gson = new Gson();
		String json = gson.toJson(eventId);
		resp.getWriter().println(json);
	}
	
}
