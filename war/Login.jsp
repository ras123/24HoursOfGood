<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
   
    if (user != null) {
    	response.sendRedirect("Timeline.jsp");
    } else {
    
    }
%>

<html>
  <head>
    <link type="text/css" rel="stylesheet" href="../css/Login.css" />
  </head>

  <body>
  	<div id="WelcomeMessage">Welcome to Achieve<span>Timeline</span></div>
	<div id="LoginBox">
			<a href="<%= userService.createLoginURL(request.getRequestURI()) %>"><div id="LoginButton">Login</div></a>
	</div>
  </body>
</html>