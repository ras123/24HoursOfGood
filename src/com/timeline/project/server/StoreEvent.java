package com.timeline.project.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
import com.google.appengine.api.datastore.Text;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


@SuppressWarnings("serial")
public class StoreEvent extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("test");
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
				
        String event = req.getParameter("event");
        String userId = req.getParameter("userId");
        String title = req.getParameter("title");
        String colourCode = req.getParameter("colourCode");
        String notes = req.getParameter("notes");
        
        
        String userId = user.getUserId();
        
        if(userId.length() < 1) {
        	userId = "0";
        }
        
		// Create an entity to store the image.
		Entity entity = new Entity(KeyFactory.createKey(userId, event));
		entity.setProperty("user", user);
		entity.setProperty("date", new Date());
		//entity.setProperty("content", new Text(new String(image)));

		// Put the entity in the data store.
		DatastoreService datastore = 
				DatastoreServiceFactory.getDatastoreService();
		datastore.put(entity);

		// Notify the client of success.
		resp.setContentType("text/plain");
		resp.getWriter().println("Accepted POST");
	}
	
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        String eventId = req.getParameter("eventId");
        
        
    	Key eventKey = KeyFactory.createKey(user.getUserId(), eventId);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        datastore.delete(eventKey);
        
        resp.setContentType("text/plain");
        
        
	}
	
	public void doGetAllEvents(HttpServletRequest req, HttpServletResponse resp) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        String eventId = req.getParameter("userId");
        
        
    	Key eventKey = KeyFactory.createKey(user.getUserId(), eventId);
        
        DatastoreService datastore = 
        		DatastoreServiceFactory.getDatastoreService();
        datastore.delete(eventKey);
        
        resp.setContentType("text/plain");
        
        
	}
}
