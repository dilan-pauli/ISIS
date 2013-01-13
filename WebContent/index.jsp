<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.util.logging.Level, ServletPrograms.ISISServlet"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    	               "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GlassFish JSP Page</title>
<% 
	HttpServlet myServlet;
%>
</head>
<body>
	<h1>Hello ISIS World!</h1>
	<!-- The time is now <.%= new java.util.Date() %.> -->
	<br />
	<br />
	<button
		onclick='<%// This scriptlet simply logs a message when this button is clicked
						java.util.logging.Logger.getAnonymousLogger().log(
								Level.INFO, "Button was clicked at time: " + new java.util.Date());
    					// SCRAP
    					//System.out.println("Button was clicked at time: " + new java.util.Date());
    					// <jsp:include page="hello.jsp"/>
    					//ServerMain.ServerProcessMain.main(null);
    					//<%@ page import="WebSocketClasses.*"
    				%>'>Click
		Me!</button>
	<br />
	<br />
	<button
		onclick='<%// This scriptlet connects the client to the server by opening a new websocket
						myServlet = new ISISServlet();
						%>'>Connect</button>
	<br />
	<button
		onclick='<%// This scriptlet sends a message to the server
						//
						%>'>Send</button>
	<br />
	<button
		onclick='<%// This scriptlet disconnects the client from the server by terminating the websocket
					// connection
						//myServlet.
						%>'>Disconnect</button>
</body>
</html>
 