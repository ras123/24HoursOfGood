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
    	
    } else {
    	response.sendRedirect("Login.jsp");
    }
%>

<!doctype html>
<html>
<link rel="stylesheet" type="text/css" href="css/Timeline.css" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.9.0/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="css/vader/jquery-ui-1.8.21.custom.css" />
<link rel="stylesheet" media="screen" type="text/css" href="css/colorpicker.css" />
<script type="text/javascript" src="js/colorpicker.js"></script>
<head>
    <title>Calendar</title>    
</head>
<div id="LightBox">
	<div id="CreateEventFormContainer" class="FormContainer">
		<div class="ExitButton"></div>
		<div class="FormTitle">Create a new event</div>
		
		<form id="CreateEventForm">
		<br />
			<div class="Label">Event Title</div><input class="Title" type="text" name="title" value="Event Title"><br/><br/>
			
			<div class="Label">Univeristy</div><input class="PostSecondaryName" type="text" name="postSecondaryName" value="UBC"><br/><br/>
			
			<div class="Label">Start Date</div><input class="StartDate" id="StartDate" type="text" name="startDate"><div class="Label">Start Time</div><input class="StartTime" id="StartTime" type="text" name="startTime" value="9:00"><br/>
			
			
			<div class="Label">End Date</div><input class="EndDate" id="EndDate" type="text" name="endDateTime"><div class="Label">End Time</div><input class="EndTime" id="EndTime" type="text" name="endDateTime" value="17:00"><br/>
			
			<div class="Label">Event Color</div><input class="ColorCode" id="ColorCode" type="text" name="colorCode"><br/><br/>
			
			<div class="Label">Notes</div><br/><textarea class="Notes">My Notes</textarea>
			<div id="CreateEventSubmitButton" class="SubmitButton">Create</div>
		</form>	
	</div>
</div>
<body>
    <div id="Header">
        <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>"><div id="UserButton">Logout</div></a>
        <div id="CreateEventButton" class="ActionButton">create event</div>
        <div id="DeleteEventButton" class="ActionButton">delete event</div>
        <div id="SwitchViewButton"></div>
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

$(".ExitButton").click(function(){
	$("#LightBox").fadeOut(400);
	$("#CreateEventFormContainer").fadeOut(400);
});

$(function() {
	$( "#StartDate" ).datepicker();
});

$(function() {
	$( "#EndDate" ).datepicker();
});

$('#ColorCode').ColorPicker({onSubmit: function(hsb, hex, rgb, el) {
		$(el).val(hex);
		$(el).ColorPickerHide();
	},
	onBeforeShow: function () {
		$(this).ColorPickerSetColor(this.value);
		
	}
})
.bind('keyup', function(){
	$(this).ColorPickerSetColor(this.value);
	
});

$("#CreateEventButton").click(function(){
	
	$("#LightBox").fadeIn(400);
	$("#CreateEventFormContainer").fadeIn(400);

	
});

$(".SubmitButton").click(function(){
	$("#LightBox").fadeOut(400);
	$("#CreateEventFormContainer").fadeOut(400);
	
	var form = $(this).parents("form");
	
	var title = $(form).find(".Title").val();
	var postSecondaryName = $(form).find(".PostSecondaryName").val();
	
	var startDate = $(form).find(".StartDate").val();
	var startTime = $(form).find(".StartTime").val();
	var startDateTime = startDate + " " + startTime + ":00";
	
	var endDate = $(form).find(".EndDate").val();
	var endTime = $(form).find(".EndTime").val();
	var endDateTime = endDate + " " + endTime +":00";
	
	var colorCode = $(form).find(".ColorCode").val();
	var notes = $(form).find(".Notes").val();
	
	var data = {title: title, postSecondaryName: postSecondaryName, startDate: startDateTime, endDate: endDateTime, colorCode: colorCode, notes: notes};
	console.log(data);
	
	$.ajax({
	  	url: '/timeline/createEvent',
	  	type: 'POST',
	  	data: data,
	  	dataType: "json",
	 	success: function(data) {
	 		console.log('success');
	    	console.log(data);
		}
	}); 

}); 

/*SWITCH */
$("#SwitchViewButton").toggle(function(){
	$("#EventList").fadeOut(400, function(){
		$("#Timeline").fadeIn(400);
	});	
	
}, function(){
	$("#Timeline").fadeOut(400, function(){
		$("#EventList").fadeIn(400);
	});
	
});

DisplayMessage("Welcome to the Achieve Timeline Application");

function DisplayMessage(message) {
	var notificationBar = $("#NotificationBar").text(message);
	$(notificationBar).fadeIn(1000, function(){
		setTimeout(function(){
			$(notificationBar).fadeOut(1000);
		}, 3000);

	});
}

</script>
</body

</html>