package com.timeline.project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
public class GetPostSecondaryServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String postSecondaryName = req.getParameter("postSecondaryName");
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
		
        DatastoreService datastore = 
         		DatastoreServiceFactory.getDatastoreService();
         
         Filter userFilter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
         Filter postSecondaryFilter = new FilterPredicate("postSecondaryName", FilterOperator.EQUAL, postSecondaryName);
         Query q = new Query("Event");
         q.setFilter(userFilter);
         q.setFilter(postSecondaryFilter);
         PreparedQuery pq = datastore.prepare(q);
         
         List<Entity> events = new ArrayList<Entity>();
         
         for (Entity event : pq.asIterable()) {
        	 events.add(event);
         }
         
         Gson gson = new Gson();
         String eventsJson = gson.toJson(events);
         
         System.out.println("Events json, " + eventsJson);
         try {
        	 resp.setContentType("application/json");
        	 resp.getWriter().println(eventsJson);
         } catch (IOException e) {
        	 e.printStackTrace();
         }
	}
}
