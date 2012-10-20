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
<script src="http://code.jquery.com/ui/1.9.0/jquery-ui.js"></script>
<link rel="stylesheet" type="text/css" href="css/vader/jquery-ui-1.8.21.custom.css" />
<link rel="stylesheet" media="screen" type="text/css" href="css/colorpicker.css" />
<script type="text/javascript" src="js/colorpicker.js"></script>
<head>
    <title>Calendar</title>    
</head>
<div id="LightBox">
	<div class="FormContainer">
		<div class="FormTitle">Create a new event</div>
		<form>
		<br />
			<div class="Label">Event Title</div><input type="text" name="title"><br/><br/>
			<div class="Label">Start Date</div><input id="StartDate" type="text" name="startDate"><div class="Label">Start Time</div><input id="StartTime" type="text" name="startTime"><br/>
			
			
			<div class="Label">End Date</div><input id="EndDate" type="text" name="endDateTime"><div class="Label">End Time</div><input id="EndTime" type="text" name="endDateTime"><br/>
			
			<div class="Label">Event Color</div><input id="ColorCode" type="text" name="colorCode"><br/><br/>
			
			<div class="Label">Details</div><br/><textarea></textarea>
		</form>	
	</div>
</div>
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
	console.log('clicked');
	$.ajax({
	  	url: '/timeline/createEvent',
	  	type: 'POST',
	  	data: { title: "John" },
	  	dataType: "text",
	 	success: function(data) {
	    console.log(data);
		}
	});
});
</script>
</body

</html>