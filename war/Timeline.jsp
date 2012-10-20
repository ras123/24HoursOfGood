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
			
			<input class="HiddenEventId" id="HiddenEventId" type="Hidden" name="key">
		</form>	
	</div>
</div>
<body>
    <div id="Header">
        <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>"><div id="UserButton">Logout</div></a>
        <div id="CreateEventButton" class="ActionButton">create event</div>
        <div id="DeleteEventButton" class="ActionButton">delete event</div>
        <div id="ModifyEventButton" class="ActionButton">modify event</div>
        <div id="SwitchViewButton"></div>
    </div>
    
    <div id="Content">
        <div id="Timeline"></div>
        <div id="EventList">
            
        </div>
    </div>
    <div id="NotificationBar"></div>
<script>
UpdateEventHandlers();

$(function() {
	$.ajax({
	  	url: '/timeline/getEvents',
	  	type: 'GET',
	  	dataType: "json",

	 	success: function(newData) {
			
			var index = 0;			
			while (index < newData.length) {				
				var data = newData[index];
				$("#EventList").prepend("<div data-eventId='"+data.key.id+"' class='Event' style='; border: 1px solid #"+data.propertyMap.colourCode+";'><div class='EventDate' style='background-color: #"+data.propertyMap.colourCode+";'>"+data.propertyMap.startDate+"</div><div class='EventTitle'>"+data.propertyMap.title+"</div><div class='EventHidden'><div class='EventPostSecondaryName'>Post Secondary Institute: "+data.propertyMap.postSecondaryName+"</div><div class='EventStartDate'><span>Start Date:</span> "+data.propertyMap.startDate+"</div><div class='EventEndDate'><span>End Date:</span> "+data.propertyMap.endDate+"</div><div class='EventNotes'>Notes: "+data.propertyMap.notes+"</div></div></div>");
				index++;
			}	
	 		
	 		UpdateEventHandlers();	 		
	 		DisplayMessage("Events successfully loaded!");
		}
	});
});

$("#ModifyEventButton").click(function(){
	var eventKey = $(this).attr("data-eventId");
	
	var event = $("#EventList").find(".Event[data-eventId='"+ eventKey +"']");
	
	var title = $(event).attr("data-title");
	var postSecondaryName = $(event).attr("data-postSecondaryName");
	var colorCode = $(event).attr("data-colorCode");
	console.log(colorCode);
	var notes = $(event).attr("data-notes");
	
	if(parseInt(eventKey) > 0) {
		$("#LightBox").fadeIn(400);
		$("#CreateEventFormContainer").fadeIn(400);
		$("#HiddenEventId").val(eventKey);
		$("#CreateEventForm .Title").val(title);
		$("#CreateEventForm .PostSecondaryName").val(postSecondaryName);
		$("#CreateEventForm .ColorCode").val(colorCode);
		$("#CreateEventForm .Notes").val(notes);
	}
	
	
	
	
});

$("#DeleteEventButton").click(function(){
	var eventKey = $(this).attr("data-eventId");
	
	if(eventKey != null || eventKey != "") {
		
		var data = {key: eventKey};
		
		$.ajax({
	  	url: '/timeline/deleteEvent',
	  	type: 'POST',
	  	data: data,
	  	dataType: "json",
	 	success: function(data) {
			
	 		$("#EventList").find(".Event[data-eventId='"+ data +"']").slideUp();
	 		$("#EventList").find(".Event[data-eventId='"+ data +"']").remove();
	 		DisplayMessage("Event was removed from your calendar!");
			}
		});		
	}
	
});

function UpdateEventHandlers(){
	$(".Event").click(function(){
		var eventId = $(this).attr("data-eventId");
		
		if(eventId != null) {
			$("#DeleteEventButton").attr("data-eventId", eventId);
			$("#DeleteEventButton").animate({opacity: 1});
			
			$("#ModifyEventButton").attr("data-eventId", eventId);
			$("#ModifyEventButton").animate({opacity: 1});
			
			$("#HiddenEventId").val(eventId);
		}
		
		$(this).siblings().each(function(){
			$(this).find(".EventHidden").slideUp();
			$(this).removeClass("ActiveEvent");
		});
		
		var isActive = $(this).hasClass("ActiveEvent");
		
		if(isActive == "false") {
			$(this).removeClass("ActiveEvent");
			$(this).find(".EventHidden").slideUp();		
		} else {
			$(this).addClass("ActiveEvent");
			$(this).find(".EventHidden").slideDown();	
		}	
		
	}); 
};




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
	$("#HiddenEventId").val("");
	$("#LightBox").fadeIn(400);
	$("#CreateEventFormContainer").fadeIn(400);

	$("#StartDate").val('');
	$("#EndDate").val('');
	
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
	
	var data = {title: title, postSecondaryName: postSecondaryName, startDate: startDateTime, endDate: endDateTime, colourCode: colorCode, notes: notes};
	
	
	$.ajax({
	  	url: '/timeline/createEvent',
	  	type: 'POST',
	  	data: data,
	  	dataType: "json",
	 	success: function(data) {
			console.log(data);
	 		$("#EventList").prepend("<div data-title='"+data.propertyMap.title+"' data-postSecondaryName='"+data.propertyMap.postSecondaryName+"' data-startDate='' data-endDate='' data-colorCode='"+data.propertyMap.colourCode+"' data-notes='"+data.propertyMap.notes+"' data-eventId='"+data.key.id+"' class='Event' style='display: none; border: 1px solid #"+data.propertyMap.colourCode+";'><div class='EventDate' style='background-color: #"+data.propertyMap.colourCode+";'>"+data.propertyMap.startDate+"</div><div class='EventTitle'>"+data.propertyMap.title+"</div><div class='EventHidden'><div class='EventPostSecondaryName'>Post Secondary Institute: "+data.propertyMap.postSecondaryName+"</div><div class='EventStartDate'><span>Start Date:</span> "+data.propertyMap.startDate+"</div><div class='EventEndDate'><span>End Date:</span> "+data.propertyMap.endDate+"</div><div class='EventNotes'>Notes: "+data.propertyMap.notes+"</div></div></div>");
	 		UpdateEventHandlers();
	 		$("#EventList").find(".Event:first-child").slideDown(1000);
	 		DisplayMessage("Event successfully added!");
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