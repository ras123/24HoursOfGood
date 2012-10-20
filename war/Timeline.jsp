<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!doctype html>
<html>
<link rel="stylesheet" type="text/css" href="css/Timeline.css" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<head>
    <title>Calendar</title>    
</head>

<body>
    <div id="Header">
        <div id="UserButton">jason</div>
        <div id="CreateEventButton" class="ActionButton">create event</div>
        <div id="DeleteEventButton" class="ActionButton">delete event</div>
    </div>
    <div id="Content">
        <div id="Timeline"></div>
        <div id="EventList">
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            <div class="DateTitle">Friday Oct 12, 2012</div>
            <div class="EventInfo">Drive to google.</div>
            <div class="EventInfo">Google hackathon</div>
            
            
        </div>
    </div>
    <div id="NotificationBar"></div>
    <script>
$("#CreateEventButton").click(function(){
	alert('clicked');
});
</script>
</body

</html>